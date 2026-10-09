import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router';
import { Box, Typography, Button, Grid, Paper, CircularProgress, Divider, Stack, Chip } from '@mui/material';
import { useDispatch, useSelector } from 'react-redux';
import type { AppDispatch, RootState } from '../store';
import { fetchProductDetail } from '../api/product';
import type { PmsPortalProductDetail, PmsSkuStock } from '../api/product';
import { addItemToCart } from '../store/slices/cartSlice';
import ReactMarkdown from 'react-markdown';
import { ReviewSection } from '../components/ReviewSection';

const ProductDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const dispatch = useDispatch<AppDispatch>();
  const { isAuthenticated } = useSelector((state: RootState) => state.auth);
  const cartItems = useSelector((state: RootState) => state.cart.items);
  
  const [productDetail, setProductDetail] = useState<PmsPortalProductDetail | null>(null);
  const [loading, setLoading] = useState(true);
  const [addingToCart, setAddingToCart] = useState(false);

  // Gallery State
  const [activeImage, setActiveImage] = useState<string>('');
  
  // SKU Selection State
  const [selectedAttributes, setSelectedAttributes] = useState<Record<string, string>>({});
  const [activeSku, setActiveSku] = useState<PmsSkuStock | null>(null);

  useEffect(() => {
    if (id) {
      loadDetail(Number(id));
    }
  }, [id]);

  const loadDetail = async (orderLineId: number) => {
    try {
      setLoading(true);
      const res = await fetchProductDetail(orderLineId);
      setProductDetail(res.data);
      if (res.data?.product?.primaryImageUrl) {
        setActiveImage(res.data.product.primaryImageUrl);
      }
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  // Handle attribute selection
  const handleSelectAttribute = (attrName: string, attrValue: string) => {
    const newSelections = { ...selectedAttributes, [attrName]: attrValue };
    setSelectedAttributes(newSelections);

    if (productDetail?.skuStockList) {
      // Try to find a matching SKU
      const matchedSku = productDetail.skuStockList.find(sku => {
        try {
          const spData = JSON.parse(sku.spData || '[]');
          // Match if the SKU contains ALL of the attributes the user has currently selected
          return Object.keys(newSelections).every(key => 
            spData.some((sp: {key: string, value: string}) => sp.key === key && sp.value === newSelections[key])
          );
        } catch (e) {
          return false;
        }
      });

      if (matchedSku) {
        setActiveSku(matchedSku);
        if (matchedSku.primaryImageUrl) setActiveImage(matchedSku.primaryImageUrl);
      } else {
        setActiveSku(null);
        // Look for ANY sku that has the selected color to at least update the image
        const partialSku = productDetail.skuStockList.find(sku => {
           try {
             const sp = JSON.parse(sku.spData || '[]');
             return sp.some((s: {key: string, value: string}) => s.key === 'Color' && s.value === newSelections['Color']);
           } catch(e) { return false; }
        });
        if (partialSku && partialSku.primaryImageUrl) {
           setActiveImage(partialSku.primaryImageUrl);
        } else if (productDetail.product.primaryImageUrl) {
           setActiveImage(productDetail.product.primaryImageUrl);
        }
      }
    }
  };

  const currentCartQuantity = cartItems.find(item => item.orderLineId === productDetail?.product.id)?.quantity || 0;
  const isInCart = currentCartQuantity > 0;
  
  // Display Price & Stock based on SKU selection
  const isPromotionActive = productDetail?.product.promotionType === 1 || productDetail?.flashSaleActive;
  const displayPrice = activeSku 
    ? (isPromotionActive && activeSku.salePrice ? activeSku.salePrice : activeSku.listPrice) 
    : (isPromotionActive && productDetail?.product.salePrice ? productDetail.product.salePrice : productDetail?.product.listPrice);
  const displayStock = activeSku ? Math.max(0, activeSku.onHandQty - (activeSku.reservedQty || 0)) : (productDetail ? productDetail.product.onHandQty : 0);
  const isOutOfStock = !displayStock || displayStock <= 0;

  // Has user selected all required attributes?
  const requiredAttributes = productDetail?.productAttributeList?.filter(a => a.type === 0) || [];
  const isFullySelected = requiredAttributes.length === 0 || requiredAttributes.every(attr => selectedAttributes[attr.name]);

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
        orderLineId: productDetail.product.id,
        quantity: 1,
        listPrice: displayPrice || 0,
        productImageUrl: activeSku?.primaryImageUrl || productDetail.product.primaryImageUrl,
        productName: productDetail.product.name + (activeSku ? ` (${Object.values(selectedAttributes).join(', ')})` : ''),
        skuId: activeSku?.id,
        productSkuCode: activeSku?.skuCode,
        skuAttributes: JSON.stringify(Object.entries(selectedAttributes).map(([key, value]) => ({ key, value }))),
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

  const { product, productAttributeList, productAttributeValueList } = productDetail;
  const allImages = [product.primaryImageUrl, ...(product.galleryList ? [...product.galleryList].displayOrder((a,b)=>a.displayOrder-b.displayOrder).map(m=>m.url) : [])].filter(Boolean) as string[];

  // Group attribute values for rendering
  const attributesToRender = productAttributeList?.filter(attr => attr.type === 0).map(attr => {
    const valObj = productAttributeValueList?.find(val => val.attributeId === attr.id);
    const values = valObj?.value ? valObj.value.split(',') : [];
    return { ...attr, values };
  }) || [];

  return (
    <Box sx={{ mt: 4 }}>
      <Paper sx={{ p: 4 }}>
        <Grid container spacing={4}>
          {/* Image Gallery Column */}
          <Grid size={{ xs: 12, md: 6 }}>
            <Box
              component="img"
              src={activeImage?.includes('file') ? 'https://via.placeholder.com/400' : (activeImage || 'https://via.placeholder.com/400')}
              alt={product.name}
              sx={{ width: '100%', maxHeight: 500, objectFit: 'contain', borderRadius: 2 }}
            />
            {allImages.length > 1 && (
              <Stack direction="row" spacing={2} sx={{ mt: 2, overflowX: 'auto', pb: 1 }}>
                {allImages.map((img, idx) => (
                  <Box
                    key={idx}
                    component="img"
                    src={img}
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
            {attributesToRender.map(attr => (
              <Box key={attr.id} sx={{ mb: 3 }}>
                <Typography variant="subtitle2" sx={{ mb: 1, fontWeight: 'bold' }}>{attr.name}</Typography>
                <Stack direction="row" spacing={1} sx={{ flexWrap: "wrap" }} useFlexGap>
                  {attr.values.map(val => (
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
              <ReactMarkdown>{product.customerComment || 'No customerComment available.'}</ReactMarkdown>
            </Box>
          </Grid>
        </Grid>
      </Paper>
      <ReviewSection orderLineId={product.id} />
    </Box>
  );
};

export default ProductDetail;
