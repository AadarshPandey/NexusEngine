import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router';
import { Box, Typography, Button, Grid, Paper, CircularProgress, Divider, Stack, Chip } from '@mui/material';
import { useDispatch, useSelector } from 'react-redux';
import type { AppDispatch, RootState } from '../store';
import { getProductDetail } from '../api/catalog';
import type { ProductDetailResult } from '../api/catalog';
import type { ProductSku } from '../types/catalog';
import { addItemToCart } from '../store/slices/cartSlice';
import ReactMarkdown from 'react-markdown';
import { ReviewSection } from '../components/ReviewSection';
import { resolveImageUrl } from '../utils/media';

const ProductDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const dispatch = useDispatch<AppDispatch>();
  const { isAuthenticated } = useSelector((state: RootState) => state.auth);
  const cartItems = useSelector((state: RootState) => state.cart.items);
  
  const [productDetail, setProductDetail] = useState<ProductDetailResult | null>(null);
  const [loading, setLoading] = useState(true);
  const [addingToCart, setAddingToCart] = useState(false);

  // Gallery State
  const [activeImage, setActiveImage] = useState<string>('');
  
  // SKU Selection State
  const [selectedAttributes, setSelectedAttributes] = useState<Record<string, string>>({});
  const [activeSku, setActiveSku] = useState<ProductSku | null>(null);

  useEffect(() => {
    if (id) {
      loadDetail(Number(id));
    }
  }, [id]);

  const loadDetail = async (productId: number) => {
    try {
      setLoading(true);
      const res = await getProductDetail(productId);
      setProductDetail(res);
      if (res?.product?.primaryImageUrl) {
        setActiveImage(res.product.primaryImageUrl);
      }
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  // Helper to parse SKU attributes
  const parseSkuAttrs = (raw: unknown): { key: string; value: string }[] => {
    if (!raw) return [];
    if (typeof raw === 'string') {
      try {
        const parsed = JSON.parse(raw);
        if (Array.isArray(parsed)) return parsed;
        if (typeof parsed === 'object') return Object.entries(parsed).map(([key, value]) => ({ key, value: String(value) }));
      } catch {
        return [];
      }
    }
    if (Array.isArray(raw)) return raw;
    if (typeof raw === 'object') return Object.entries(raw).map(([key, value]) => ({ key, value: String(value) }));
    return [];
  };

  // Handle attribute selection
  const handleSelectAttribute = (attrName: string, attrValue: string) => {
    const newSelections = { ...selectedAttributes, [attrName]: attrValue };
    setSelectedAttributes(newSelections);

    if (productDetail?.skuStockList) {
      const matchedSku = productDetail.skuStockList.find(sku => {
        const attrs = parseSkuAttrs(sku.skuAttributes);
        return Object.keys(newSelections).every(key => 
          attrs.some(sp => sp.key === key && sp.value === newSelections[key])
        );
      });

      if (matchedSku) {
        setActiveSku(matchedSku);
        if (matchedSku.imageUrl) setActiveImage(matchedSku.imageUrl);
      } else {
        setActiveSku(null);
        const partialSku = productDetail.skuStockList.find(sku => {
          const attrs = parseSkuAttrs(sku.skuAttributes);
          return attrs.some(s => s.key === 'Color' && s.value === newSelections['Color']);
        });
        if (partialSku && partialSku.imageUrl) {
          setActiveImage(partialSku.imageUrl);
        } else if (productDetail.product.primaryImageUrl) {
          setActiveImage(productDetail.product.primaryImageUrl);
        }
      }
    }
  };

  const currentCartQuantity = cartItems.find(item => item.productId === productDetail?.product.id)?.quantity || 0;
  const isInCart = currentCartQuantity > 0;
  
  // Display Price & Stock based on SKU selection
  const displayPrice = activeSku?.salePrice ?? activeSku?.listPrice ?? productDetail?.product.salePrice ?? productDetail?.product.listPrice ?? 0;
  const displayStock = activeSku 
    ? (activeSku.availableQty ?? Math.max(0, activeSku.onHandQty - (activeSku.reservedQty || 0))) 
    : (productDetail?.skuStockList ? productDetail.skuStockList.reduce((sum, s) => sum + (s.availableQty ?? Math.max(0, s.onHandQty - (s.reservedQty || 0))), 0) : 0);
  const isOutOfStock = !displayStock || displayStock <= 0;

  // Build selectable SKU variant attributes from SKU stock combinations & attribute value rows
  const skuAttrMap = new Map<string, string[]>();
  (productDetail?.skuStockList || []).forEach(sku => {
    parseSkuAttrs(sku.skuAttributes).forEach(({ key, value }) => {
      if (!key || !value) return;
      const existing = skuAttrMap.get(key) || [];
      if (!existing.includes(value)) {
        existing.push(value);
        skuAttrMap.set(key, existing);
      }
    });
  });

  const attributesToRender = (() => {
    const list: { id: number | string; name: string; values: string[] }[] = [];
    const seenNames = new Set<string>();
    (productDetail?.productAttributeList || [])
      .filter((attr: any) => attr.type === 0)
      .forEach((attr: any) => {
        const fromSkus = skuAttrMap.get(attr.name) || [];
        const fromVals = (productDetail?.productAttributeValueList || [])
          .filter((val: any) => val.productAttributeId === attr.id && val.value)
          .flatMap((val: any) => String(val.value).split(',').map((s: string) => s.trim()).filter(Boolean));
        const merged = Array.from(new Set([...fromSkus, ...fromVals]));
        if (merged.length > 0) {
          seenNames.add(attr.name);
          list.push({ id: attr.id, name: attr.name, values: merged });
        }
      });
    skuAttrMap.forEach((values, name) => {
      if (!seenNames.has(name) && values.length > 0) {
        list.push({ id: name, name, values });
      }
    });
    return list;
  })();

  // Has user selected all required attributes?
  const requiredAttributes = attributesToRender;
  const isFullySelected = requiredAttributes.length === 0 || requiredAttributes.every((attr) => Boolean(selectedAttributes[attr.name]));

  const handleAddToCart = async () => {
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }
    if (!productDetail) return;
    if (!isFullySelected) {
      alert('Please select all product options before adding to cart.');
      return;
    }

    try {
      setAddingToCart(true);
      await dispatch(addItemToCart({
        productId: productDetail.product.id,
        quantity: 1,
        price: displayPrice || 0,
        skuId: activeSku?.id,
        productSkuId: activeSku?.id,
      })).unwrap();
      alert('Item added to cart!');
    } catch (error: unknown) {
      alert(error instanceof Error ? error.message : 'Failed to add to cart');
    } finally {
      setAddingToCart(false);
    }
  };

  if (loading) {
    return <Box sx={{ display: 'flex', justifyContent: 'center', mt: 10 }}><CircularProgress /></Box>;
  }

  if (!productDetail) {
    return <Box sx={{ mt: 10, textAlign: 'center' }}><Typography variant="h5">Product not found</Typography></Box>;
  }

  const { product } = productDetail;
  const allImages = Array.from(
    new Set(
      [
        product.primaryImageUrl,
        ...(product.galleryList
          ? [...product.galleryList]
              .sort((a, b) => ((a.displayOrder ?? a.sortOrder ?? 0) - (b.displayOrder ?? b.sortOrder ?? 0)))
              .map(m => m.url || m.mediaUrl)
          : []),
      ].filter(Boolean) as string[]
    )
  );

  return (
    <Box sx={{ mt: 4 }}>
      <Paper sx={{ p: 4 }}>
        <Grid container spacing={4}>
          {/* Image Gallery Column */}
          <Grid size={{ xs: 12, md: 6 }}>
            <Box
              component="img"
              src={resolveImageUrl(activeImage)}
              alt={product.name}
              sx={{ width: '100%', maxHeight: 500, objectFit: 'contain', borderRadius: 2 }}
            />
            {allImages.length > 1 && (
              <Stack direction="row" spacing={2} sx={{ mt: 2, overflowX: 'auto', pb: 1 }}>
                {allImages.map((img, idx) => (
                  <Box
                    key={idx}
                    component="img"
                    src={resolveImageUrl(img)}
                    onClick={() => setActiveImage(img)}
                    sx={{
                      width: 80,
                      height: 80,
                      objectFit: 'cover',
                      cursor: 'pointer',
                      border: activeImage === img ? '2px solid #1976d2' : '1px solid #ddd',
                      borderRadius: 1
                    }}
                  />
                ))}
              </Stack>
            )}
          </Grid>
          
          {/* Details Column */}
          <Grid size={{ xs: 12, md: 6 }}>
            <Typography variant="h4" gutterBottom sx={{ fontWeight: 'bold' }}>{product.name}</Typography>
            <Divider sx={{ my: 2 }} />
            
            <Typography variant="h3" color="primary" gutterBottom>₹{displayPrice?.toFixed(2)}</Typography>
            
            {/* SKU Variants */}
            {attributesToRender.map((attr: any) => (
              <Box key={attr.id} sx={{ mb: 3 }}>
                <Typography variant="subtitle2" sx={{ mb: 1, fontWeight: 'bold' }}>{attr.name}</Typography>
                <Stack direction="row" spacing={1} sx={{ flexWrap: "wrap" }} useFlexGap>
                  {attr.values.map((val: string) => (
                    <Chip
                      key={val}
                      label={val}
                      onClick={() => handleSelectAttribute(attr.name, val)}
                      color={selectedAttributes[attr.name] === val ? 'primary' : 'default'}
                      variant={selectedAttributes[attr.name] === val ? 'filled' : 'outlined'}
                      sx={{ cursor: 'pointer', borderRadius: 1 }}
                    />
                  ))}
                </Stack>
              </Box>
            ))}

            <Typography variant="subtitle1" color={displayStock && displayStock > 0 ? "text.secondary" : "error"} gutterBottom sx={{ mt: 2 }}>
              {displayStock && displayStock > 0 ? `Available Stock: ${displayStock} (In Cart: ${currentCartQuantity})` : 'Out of Stock'}
            </Typography>
            
            <Box sx={{ mt: 4 }}>
              <Button 
                variant="contained" 
                size="large" 
                color="primary" 
                fullWidth 
                onClick={handleAddToCart}
                disabled={addingToCart || isOutOfStock || isInCart || !isFullySelected}
              >
                {addingToCart ? 'Adding...' : isInCart ? 'Item already in cart' : !isFullySelected ? 'Select Options' : 'Add to Cart'}
              </Button>
            </Box>

            <Box sx={{ mt: 4 }}>
              <Typography variant="h6" gutterBottom>Product Description</Typography>
              <ReactMarkdown>{product.description || 'No description available.'}</ReactMarkdown>
            </Box>
          </Grid>
        </Grid>
      </Paper>
      <ReviewSection productId={product.id} />
    </Box>
  );
};

export default ProductDetail;
