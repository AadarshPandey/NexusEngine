import React, { useState, useEffect } from 'react';
import {
  Box,
  Typography,
  Paper,
  TextField,
  Button,
  Grid,
  CircularProgress,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  ListSubheader,
  Snackbar,
  Alert,
  IconButton,
  Divider,
} from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import AddPhotoAlternateIcon from '@mui/icons-material/AddPhotoAlternate';
import {
  productCreateAPI,
  productUpdateByIdAPI,
  getPruductUpdateInfoAPI,
  getBrandListAPI,
  getProductCategoryListWithChildrenAPI,
  getProductAttributeCategoryListAPI,
  getProductAttributeListAPI,
} from '@/apis/catalog';
import type { AttributeDefinition, ProductAttributeValue } from '@/types/catalog';
import { resolveImageUrl } from '@/utils/media';
import { useNavigate, useParams, useSearchParams } from 'react-router';
import { useAppSelector } from '@/store/hooks';

interface SkuFormState {
  id?: number;
  skuCode: string;
  listPrice: string;
  promotionPrice: string;
  onHandQty: string;
  lowStock: string;
  pic: string;
  spDataList: { key: string; value: string }[];
}

const emptySku = (): SkuFormState => ({
  skuCode: '',
  listPrice: '',
  promotionPrice: '',
  onHandQty: '',
  lowStock: '',
  pic: '',
  spDataList: [],
});

const ProductForm: React.FC = () => {
  const navigate = useNavigate();
  const userBrandId = useAppSelector((state) => state.user.userInfo.brandId);
  const { id: paramId } = useParams<{ id: string }>();
  const [searchParams] = useSearchParams();
  const id = paramId || searchParams.get('id');
  const isEdit = Boolean(id);

  const [brands, setBrands] = useState<import('@/types/catalog').PmsBrand[]>([]);
  const [categories, setCategories] = useState<import('@/types/catalog').PmsProductCategory[]>([]);
  const [productTypes, setProductTypes] = useState<any[]>([]);

  // Attribute definitions for selected product type
  const [paramDefinitions, setParamDefinitions] = useState<AttributeDefinition[]>([]);
  const [specDefinitions, setSpecDefinitions] = useState<AttributeDefinition[]>([]);

  const [loading, setLoading] = useState(isEdit);
  const [saving, setSaving] = useState(false);
  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' as 'success' | 'error' | 'warning' });

  const [formData, setFormData] = useState({
    name: '',
    description: '',
    primaryImageUrl: '',
    galleryList: [] as string[],
    productCategoryId: '',
    productAttributeCategoryId: '',
    productAttributeValueList: [] as ProductAttributeValue[],
    brandId: userBrandId ? String(userBrandId) : '',
    keywords: '',
    isPublished: 1,
    isNewArrival: 1,
    isFeatured: 1,
    approvalStatus: 1,
    skuStockList: [emptySku()],
  });

  useEffect(() => {
    if (userBrandId && !isEdit) {
      setFormData((prev) => ({ ...prev, brandId: String(userBrandId) }));
    }
  }, [userBrandId, isEdit]);

  useEffect(() => {
    fetchDependencies();
    if (isEdit && id) {
      fetchProduct(Number(id));
    }
  }, [id, isEdit]);

  // Load attribute definitions whenever productAttributeCategoryId changes
  useEffect(() => {
    if (formData.productAttributeCategoryId) {
      loadAttributeDefinitions(Number(formData.productAttributeCategoryId));
    } else {
      setParamDefinitions([]);
      setSpecDefinitions([]);
    }
  }, [formData.productAttributeCategoryId]);

  const loadAttributeDefinitions = async (typeId: number) => {
    try {
      const [specRes, paramRes] = await Promise.all([
        getProductAttributeListAPI(typeId, { type: 0, pageSize: 100 }),
        getProductAttributeListAPI(typeId, { type: 1, pageSize: 100 }),
      ]);
      setSpecDefinitions(specRes?.content || []);
      setParamDefinitions(paramRes?.content || []);
    } catch (e) {
      console.error('Failed to fetch attribute definitions for product type', e);
    }
  };

  const fetchDependencies = async () => {
    try {
      const [brandRes, cateRes, typeRes] = await Promise.all([
        getBrandListAPI({ pageNum: 1, pageSize: 100 }),
        getProductCategoryListWithChildrenAPI(),
        getProductAttributeCategoryListAPI({ pageNum: 1, pageSize: 100 }),
      ]);
      setBrands(brandRes?.content || []);
      setCategories(Array.isArray(cateRes) ? cateRes : []);
      setProductTypes(typeRes?.content || []);
    } catch (e) {
      console.error('Failed to fetch dependencies', e);
    }
  };

  const fetchProduct = async (productId: number) => {
    setLoading(true);
    try {
      const prodRes = await getPruductUpdateInfoAPI(productId);
      if (prodRes) {
        const prod = prodRes as any;
        const galleryUrls: string[] = (prod.galleryList || []).map((m: any) => m.url || m.mediaUrl || '').filter(Boolean);
        const attrValues: ProductAttributeValue[] = (prod.productAttributeValueList || []).map((a: any) => ({
          productAttributeId: a.productAttributeId || a.attributeId,
          value: a.value || '',
        }));

        setFormData({
          name: prod.name || '',
          description: prod.description || '',
          primaryImageUrl: prod.primaryImageUrl || prod.pic || '',
          galleryList: galleryUrls,
          productCategoryId: prod.productCategoryId?.toString() || prod.categoryId?.toString() || '',
          productAttributeCategoryId: prod.productAttributeCategoryId?.toString() || prod.productTypeId?.toString() || '',
          productAttributeValueList: attrValues,
          brandId: userBrandId ? String(userBrandId) : (prod.brandId?.toString() || ''),
          keywords: Array.isArray(prod.searchKeywords)
            ? prod.searchKeywords.join(',')
            : (prod.keywords || prod.searchKeywords || ''),
          isPublished: prod.isPublished !== undefined ? (prod.isPublished ? 1 : 0) : 1,
          isNewArrival: prod.isNewArrival !== undefined ? (prod.isNewArrival ? 1 : 0) : 1,
          isFeatured: prod.isFeatured !== undefined ? (prod.isFeatured ? 1 : 0) : 1,
          approvalStatus: prod.approvalStatus ?? 1,
          skuStockList: prod.skuStockList?.length
            ? prod.skuStockList.map((sku: any) => {
                const rawSp = sku.skuAttributes || sku.spData;
                let parsedSp: { key: string; value: string }[] = [];
                try {
                  const p = typeof rawSp === 'string' ? JSON.parse(rawSp) : rawSp;
                  if (Array.isArray(p)) parsedSp = p;
                  else if (typeof p === 'object' && p !== null) {
                    parsedSp = Object.entries(p).map(([key, value]) => ({ key, value: String(value) }));
                  }
                } catch {}
                return {
                  id: sku.id,
                  skuCode: sku.skuCode || '',
                  listPrice: sku.price?.toString() || sku.listPrice?.toString() || '',
                  promotionPrice: sku.promotionPrice?.toString() || sku.salePrice?.toString() || '',
                  onHandQty: sku.stock?.toString() || sku.onHandQty?.toString() || '',
                  lowStock: sku.lowStock?.toString() || sku.lowStockThreshold?.toString() || '',
                  pic: sku.imageUrl || sku.pic || sku.primaryImageUrl || '',
                  spDataList: parsedSp,
                };
              })
            : [emptySku()],
        });
      }
    } catch (error) {
      console.error('Failed to fetch product details', error);
      setSnackbar({ open: true, message: 'Failed to fetch product details', severity: 'error' });
    } finally {
      setLoading(false);
    }
  };

  const handleAddSku = () => {
    setFormData((prev) => ({
      ...prev,
      skuStockList: [...prev.skuStockList, emptySku()],
    }));
  };

  const handleRemoveSku = (index: number) => {
    setFormData((prev) => {
      const newList = [...prev.skuStockList];
      newList.splice(index, 1);
      return { ...prev, skuStockList: newList };
    });
  };

  const handleSkuChange = (index: number, field: keyof SkuFormState, value: string) => {
    setFormData((prev) => {
      const newList = [...prev.skuStockList];
      newList[index] = { ...newList[index], [field]: value };
      return { ...prev, skuStockList: newList };
    });
  };

  const handleAddSkuAttribute = (skuIndex: number) => {
    setFormData((prev) => {
      const newList = [...prev.skuStockList];
      const spDataList = newList[skuIndex].spDataList || [];
      newList[skuIndex] = { ...newList[skuIndex], spDataList: [...spDataList, { key: '', value: '' }] };
      return { ...prev, skuStockList: newList };
    });
  };

  const handleRemoveSkuAttribute = (skuIndex: number, attrIndex: number) => {
    setFormData((prev) => {
      const newList = [...prev.skuStockList];
      const spDataList = [...(newList[skuIndex].spDataList || [])];
      spDataList.splice(attrIndex, 1);
      newList[skuIndex] = { ...newList[skuIndex], spDataList };
      return { ...prev, skuStockList: newList };
    });
  };

  const handleSkuAttributeChange = (skuIndex: number, attrIndex: number, field: string, value: string) => {
    setFormData((prev) => {
      const newList = [...prev.skuStockList];
      const spDataList = [...(newList[skuIndex].spDataList || [])];
      spDataList[attrIndex] = { ...spDataList[attrIndex], [field]: value };
      newList[skuIndex] = { ...newList[skuIndex], spDataList };
      return { ...prev, skuStockList: newList };
    });
  };

  // Gallery Management
  const handleAddGalleryImage = () => {
    setFormData((prev) => ({
      ...prev,
      galleryList: [...prev.galleryList, ''],
    }));
  };

  const handleGalleryImageChange = (index: number, url: string) => {
    setFormData((prev) => {
      const updated = [...prev.galleryList];
      updated[index] = url;
      return { ...prev, galleryList: updated };
    });
  };

  const handleRemoveGalleryImage = (index: number) => {
    setFormData((prev) => {
      const updated = [...prev.galleryList];
      updated.splice(index, 1);
      return { ...prev, galleryList: updated };
    });
  };

  // Parameter Value Management
  const handleAttributeValueChange = (attributeId: number, value: string) => {
    setFormData((prev) => {
      const existing = [...prev.productAttributeValueList];
      const idx = existing.findIndex((a) => a.productAttributeId === attributeId);
      if (idx >= 0) {
        existing[idx] = { ...existing[idx], value };
      } else {
        existing.push({ productAttributeId: attributeId, value });
      }
      return { ...prev, productAttributeValueList: existing };
    });
  };

  const getAttributeCurrentValue = (attributeId: number): string => {
    const item = formData.productAttributeValueList.find((a) => a.productAttributeId === attributeId);
    return item?.value || '';
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (formData.skuStockList.length === 0) {
      setSnackbar({ open: true, message: 'At least one SKU is required', severity: 'warning' });
      return;
    }
    for (let i = 0; i < formData.skuStockList.length; i++) {
      const sku = formData.skuStockList[i];
      if (!sku.skuCode || !sku.listPrice || !sku.onHandQty) {
        setSnackbar({ open: true, message: 'Please fill in SKU Code, List Price, and Stock for all SKUs', severity: 'warning' });
        return;
      }
    }

    setSaving(true);
    try {
      const cleanNumber = (val: string | number) => Number(String(val).replace(/,/g, ''));

      const formattedSkus = formData.skuStockList.map((sku) => ({
        id: sku.id,
        skuCode: sku.skuCode,
        listPrice: cleanNumber(sku.listPrice),
        onHandQty: cleanNumber(sku.onHandQty),
        salePrice: sku.promotionPrice ? cleanNumber(sku.promotionPrice) : undefined,
        lowStockThreshold: sku.lowStock ? cleanNumber(sku.lowStock) : undefined,
        imageUrl: sku.pic,
        skuAttributes: sku.spDataList && sku.spDataList.length > 0 ? JSON.stringify(sku.spDataList) : undefined,
      }));

      const formattedGallery = formData.galleryList
        .filter((url) => Boolean(url && url.trim()))
        .map((url, idx) => ({
          mediaType: 'image',
          url: url.trim(),
          sortOrder: idx + 1,
        }));

      const formattedAttributeValues = formData.productAttributeValueList.filter((a) => Boolean(a.value && a.value.trim()));

      const payload = {
        ...formData,
        galleryList: formattedGallery,
        productAttributeValueList: formattedAttributeValues,
        skuStockList: formattedSkus,
      } as unknown as import('@/types/catalog').PmsProductParam;

      if (isEdit && id) {
        await productUpdateByIdAPI(Number(id), payload);
        setSnackbar({ open: true, message: 'Product updated successfully!', severity: 'success' });
      } else {
        await productCreateAPI(payload);
        setSnackbar({ open: true, message: 'Product created successfully!', severity: 'success' });
      }
      setTimeout(() => navigate('/catalog/products'), 1000);
    } catch (error) {
      console.error(error);
      setSnackbar({ open: true, message: isEdit ? 'Failed to update product' : 'Failed to create product', severity: 'error' });
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '60vh' }}>
        <CircularProgress sx={{ color: '#6366F1' }} />
      </Box>
    );
  }

  return (
    <Box>
      <Typography variant="h5" sx={{ mb: 3 }}>
        {isEdit ? 'Update Product' : 'Add New Product'}
      </Typography>
      <Paper sx={{ p: 4 }}>
        <form onSubmit={handleSubmit}>
          <Grid container spacing={3}>
            {/* Basic Info */}
            <Grid size={{ xs: 12 }}>
              <TextField
                fullWidth
                label="Product Name"
                required
                value={formData.name}
                onChange={(e) => setFormData({ ...formData, name: e.target.value })}
              />
            </Grid>

            <Grid size={{ xs: 12, md: 4 }}>
              <FormControl fullWidth required>
                <InputLabel>Product Category</InputLabel>
                <Select
                  label="Product Category"
                  value={formData.productCategoryId}
                  onChange={(e) => setFormData({ ...formData, productCategoryId: e.target.value as string })}
                >
                  {categories.map((parent: import('@/types/catalog').PmsProductCategoryExt) => [
                    <ListSubheader key={`header-${parent.id}`}>{parent.name}</ListSubheader>,
                    ...(parent.children || []).map((child: import('@/types/catalog').PmsProductCategory) => (
                      <MenuItem key={child.id} value={child.id!.toString()} sx={{ pl: 4 }}>
                        {child.name}
                      </MenuItem>
                    )),
                  ])}
                </Select>
              </FormControl>
            </Grid>

            <Grid size={{ xs: 12, md: 4 }}>
              <FormControl fullWidth required>
                <InputLabel>Product Type</InputLabel>
                <Select
                  label="Product Type"
                  value={formData.productAttributeCategoryId}
                  onChange={(e) => setFormData({ ...formData, productAttributeCategoryId: e.target.value as string })}
                >
                  {productTypes.map((type: any) => (
                    <MenuItem key={type.id} value={type.id?.toString() || ''}>
                      {type.name}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Grid>

            <Grid size={{ xs: 12, md: 4 }}>
              <FormControl fullWidth required disabled={Boolean(userBrandId)}>
                <InputLabel>Brand</InputLabel>
                <Select
                  label="Brand"
                  value={formData.brandId}
                  onChange={(e) => setFormData({ ...formData, brandId: e.target.value as string })}
                >
                  {brands.map((b: import('@/types/catalog').PmsBrand) => (
                    <MenuItem key={b.id} value={b.id?.toString() || ''}>
                      {b.name}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Grid>

            <Grid size={{ xs: 12, md: 6 }}>
              <TextField
                fullWidth
                label="Primary Image URL"
                value={formData.primaryImageUrl}
                onChange={(e) => setFormData({ ...formData, primaryImageUrl: e.target.value })}
              />
              {formData.primaryImageUrl && (
                <Box sx={{ mt: 1 }}>
                  <img
                    src={resolveImageUrl(formData.primaryImageUrl)}
                    alt="Preview"
                    style={{ height: 60, objectFit: 'contain' }}
                  />
                </Box>
              )}
            </Grid>

            <Grid size={{ xs: 12, md: 6 }}>
              <TextField
                fullWidth
                label="Search Keywords"
                value={formData.keywords}
                onChange={(e) => setFormData({ ...formData, keywords: e.target.value })}
              />
            </Grid>

            <Grid size={{ xs: 12 }}>
              <TextField
                fullWidth
                label="Description"
                multiline
                rows={4}
                value={formData.description}
                onChange={(e) => setFormData({ ...formData, description: e.target.value })}
              />
            </Grid>

            {/* Media Gallery Section */}
            <Grid size={{ xs: 12 }}>
              <Divider sx={{ my: 2 }} />
              <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
                <Typography variant="h6">Product Media Gallery (catalog.product_media)</Typography>
                <Button
                  size="small"
                  variant="outlined"
                  startIcon={<AddPhotoAlternateIcon />}
                  onClick={handleAddGalleryImage}
                >
                  + Add Gallery Image
                </Button>
              </Box>
              {formData.galleryList.length === 0 ? (
                <Typography variant="body2" color="text.secondary">
                  No additional gallery images added yet. Click "+ Add Gallery Image" to add extra product photos.
                </Typography>
              ) : (
                <Grid container spacing={2}>
                  {formData.galleryList.map((url, idx) => (
                    <Grid size={{ xs: 12, md: 6 }} key={idx}>
                      <Paper variant="outlined" sx={{ p: 2, display: 'flex', alignItems: 'center', gap: 2 }}>
                        {url ? (
                          <img
                            src={resolveImageUrl(url)}
                            alt={`Gallery ${idx + 1}`}
                            style={{ width: 60, height: 60, objectFit: 'contain', borderRadius: 4 }}
                          />
                        ) : (
                          <Box sx={{ width: 60, height: 60, bgcolor: '#f0f0f0', borderRadius: 1 }} />
                        )}
                        <TextField
                          fullWidth
                          size="small"
                          label={`Image #${idx + 1} URL`}
                          value={url}
                          onChange={(e) => handleGalleryImageChange(idx, e.target.value)}
                        />
                        <IconButton color="error" size="small" onClick={() => handleRemoveGalleryImage(idx)}>
                          <DeleteIcon />
                        </IconButton>
                      </Paper>
                    </Grid>
                  ))}
                </Grid>
              )}
            </Grid>

            {/* Specifications & Parameters Section */}
            {paramDefinitions.length > 0 && (
              <Grid size={{ xs: 12 }}>
                <Divider sx={{ my: 2 }} />
                <Typography variant="h6" sx={{ mb: 2 }}>
                  Product Parameters & Specs (catalog.product_attribute_value)
                </Typography>
                <Grid container spacing={2}>
                  {paramDefinitions.map((param) => {
                    const curVal = getAttributeCurrentValue(param.id!);
                    const allowed = param.allowedValues || param.inputList;
                    const options = allowed
                      ? (Array.isArray(allowed) ? allowed : String(allowed).split(',')).map((s) => s.trim()).filter(Boolean)
                      : [];

                    return (
                      <Grid size={{ xs: 12, md: 4 }} key={param.id}>
                        {options.length > 0 ? (
                          <FormControl fullWidth size="small">
                            <InputLabel>{param.name}</InputLabel>
                            <Select
                              label={param.name}
                              value={curVal}
                              onChange={(e) => handleAttributeValueChange(param.id!, e.target.value as string)}
                            >
                              <MenuItem value=""><em>None</em></MenuItem>
                              {options.map((opt) => (
                                <MenuItem key={opt} value={opt}>
                                  {opt}
                                </MenuItem>
                              ))}
                            </Select>
                          </FormControl>
                        ) : (
                          <TextField
                            fullWidth
                            size="small"
                            label={param.name}
                            value={curVal}
                            onChange={(e) => handleAttributeValueChange(param.id!, e.target.value)}
                          />
                        )}
                      </Grid>
                    );
                  })}
                </Grid>
              </Grid>
            )}

            {/* Product SKUs (Variants) */}
            <Grid size={{ xs: 12 }}>
              <Divider sx={{ my: 2 }} />
              <Box sx={{ mt: 1, mb: 2 }}>
                <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
                  <Typography variant="h6">
                    Product SKUs / Variants (catalog.product_sku)
                  </Typography>
                  <Button variant="outlined" size="small" onClick={handleAddSku}>
                    + Add SKU
                  </Button>
                </Box>
                {formData.skuStockList.map((sku, index: number) => (
                  <Paper key={index} variant="outlined" sx={{ p: 2, mb: 2 }}>
                    <Grid container spacing={2} alignItems="center">
                      <Grid size={{ xs: 12, md: 3 }}>
                        <TextField
                          fullWidth
                          label="SKU Code"
                          required
                          size="small"
                          value={sku.skuCode || ''}
                          onChange={(e) => handleSkuChange(index, 'skuCode', e.target.value)}
                        />
                      </Grid>
                      <Grid size={{ xs: 12, md: 3 }}>
                        <TextField
                          fullWidth
                          label="List Price (₹)"
                          required
                          size="small"
                          value={sku.listPrice || ''}
                          onChange={(e) => handleSkuChange(index, 'listPrice', e.target.value)}
                        />
                      </Grid>
                      <Grid size={{ xs: 12, md: 3 }}>
                        <TextField
                          fullWidth
                          label="Sale Price (₹)"
                          size="small"
                          value={sku.promotionPrice || ''}
                          onChange={(e) => handleSkuChange(index, 'promotionPrice', e.target.value)}
                        />
                      </Grid>
                      <Grid size={{ xs: 12, md: 3 }}>
                        <TextField
                          fullWidth
                          label="Stock (On Hand)"
                          required
                          size="small"
                          value={sku.onHandQty || ''}
                          onChange={(e) => handleSkuChange(index, 'onHandQty', e.target.value)}
                        />
                      </Grid>
                      <Grid size={{ xs: 12, md: 4 }}>
                        <TextField
                          fullWidth
                          label="Low Stock Threshold"
                          size="small"
                          value={sku.lowStock || ''}
                          onChange={(e) => handleSkuChange(index, 'lowStock', e.target.value)}
                        />
                      </Grid>
                      <Grid size={{ xs: 12, md: 6 }}>
                        <TextField
                          fullWidth
                          label="Image URL"
                          size="small"
                          value={sku.pic || ''}
                          onChange={(e) => handleSkuChange(index, 'pic', e.target.value)}
                        />
                      </Grid>
                      <Grid size={{ xs: 12, md: 2 }} sx={{ textAlign: 'right' }}>
                        {formData.skuStockList.length > 1 && (
                          <Button color="error" size="small" variant="outlined" onClick={() => handleRemoveSku(index)}>
                            Remove SKU
                          </Button>
                        )}
                      </Grid>

                      <Grid size={{ xs: 12 }}>
                        <Typography variant="subtitle2" sx={{ mb: 1 }}>
                          Variant Attributes (e.g. Color, Size, Storage)
                        </Typography>
                        {(sku.spDataList || []).map((attr, attrIndex: number) => (
                          <Box key={attrIndex} sx={{ display: 'flex', gap: 1, mb: 1 }}>
                            <TextField
                              label="Key"
                              size="small"
                              value={attr.key || ''}
                              onChange={(e) => handleSkuAttributeChange(index, attrIndex, 'key', e.target.value)}
                            />
                            <TextField
                              label="Value"
                              size="small"
                              value={attr.value || ''}
                              onChange={(e) => handleSkuAttributeChange(index, attrIndex, 'value', e.target.value)}
                            />
                            <Button color="error" size="small" onClick={() => handleRemoveSkuAttribute(index, attrIndex)}>
                              ✕
                            </Button>
                          </Box>
                        ))}
                        <Button size="small" variant="outlined" onClick={() => handleAddSkuAttribute(index)}>
                          + Add Attribute
                        </Button>
                      </Grid>
                    </Grid>
                  </Paper>
                ))}
              </Box>
            </Grid>

            {/* Actions */}
            <Grid size={{ xs: 12 }} sx={{ mt: 3, display: 'flex', gap: 2 }}>
              <Button type="submit" variant="contained" color="primary" disabled={saving}>
                {saving ? (isEdit ? 'Saving...' : 'Creating...') : (isEdit ? 'Update Product' : 'Create Product')}
              </Button>
              <Button variant="outlined" onClick={() => navigate('/catalog/products')}>
                Cancel
              </Button>
            </Grid>
          </Grid>
        </form>
      </Paper>

      <Snackbar
        open={snackbar.open}
        autoHideDuration={3000}
        onClose={() => setSnackbar((s) => ({ ...s, open: false }))}
        anchorOrigin={{ vertical: 'top', horizontal: 'center' }}
      >
        <Alert severity={snackbar.severity} onClose={() => setSnackbar((s) => ({ ...s, open: false }))}>
          {snackbar.message}
        </Alert>
      </Snackbar>
    </Box>
  );
};

export default ProductForm;
