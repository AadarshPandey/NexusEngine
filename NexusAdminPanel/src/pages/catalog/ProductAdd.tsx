import React, { useState, useEffect } from 'react';
import { Box, Typography, Paper, TextField, Button, Grid, FormControl, InputLabel, Select, MenuItem, ListSubheader } from '@mui/material';
import { productCreateAPI } from '@/apis/catalog';
import { getBrandListAPI } from '@/apis/catalog';
import { getProductCategoryListWithChildrenAPI } from '@/apis/catalog';
import { getProductAttributeCategoryListAPI } from '@/apis/catalog';
import { resolveImageUrl } from '@/utils/media';
import { useNavigate } from 'react-router';
import { useAppSelector } from '@/store/hooks';

const ProductAdd: React.FC = () => {
  const navigate = useNavigate();
  const userBrandId = useAppSelector((state) => state.user.userInfo.brandId);
  const [brands, setBrands] = useState<import('@/types/catalog').PmsBrand[]>([]);
  const [categories, setCategories] = useState<import('@/types/catalog').PmsProductCategory[]>([]);
  const [productTypes, setProductTypes] = useState<any[]>([]);
  
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    primaryImageUrl: '',
    productCategoryId: '',
    productAttributeCategoryId: '',
    brandId: userBrandId ? String(userBrandId) : '',
    keywords: '',
    isPublished: 1,
    isNewArrival: 1,
    isFeatured: 1,
    approvalStatus: 1,
    skuStockList: [{ skuCode: '', listPrice: '', onHandQty: '', promotionPrice: '', lowStock: '', pic: '', sale: '', lockStock: '', spDataList: [] as {key: string, value: string}[], version: '' }]
  });

  useEffect(() => {
    if (userBrandId) {
      setFormData(prev => ({ ...prev, brandId: String(userBrandId) }));
    }
  }, [userBrandId]);

  useEffect(() => {
    fetchDependencies();
  }, []);

  const fetchDependencies = async () => {
    try {
      const [brandRes, cateRes, typeRes] = await Promise.all([
        getBrandListAPI({ pageNum: 1, pageSize: 100 }),
        getProductCategoryListWithChildrenAPI(),
        getProductAttributeCategoryListAPI({ pageNum: 1, pageSize: 100 })
      ]);
      setBrands(brandRes?.content || []);
      setCategories(Array.isArray(cateRes) ? cateRes : []);
      setProductTypes(typeRes?.content || []);
    } catch (e) {
      console.error(e);
    }
  };

  const handleAddSku = () => {
    setFormData({
      ...formData,
      skuStockList: [...formData.skuStockList, { skuCode: '', listPrice: '', onHandQty: '', promotionPrice: '', lowStock: '', pic: '', sale: '', lockStock: '', spDataList: [] as {key: string, value: string}[], version: '' }]
    });
  };

  const handleRemoveSku = (index: number) => {
    const newList = [...formData.skuStockList];
    newList.splice(index, 1);
    setFormData({ ...formData, skuStockList: newList });
  };

  const handleSkuChange = (index: number, field: string, value: string) => {
    const newList = [...formData.skuStockList];
    newList[index] = { ...newList[index], [field]: value };
    setFormData({ ...formData, skuStockList: newList });
  };

  const handleAddSkuAttribute = (skuIndex: number) => {
    const newList = [...formData.skuStockList];
    const spDataList = newList[skuIndex].spDataList || [];
    newList[skuIndex] = { ...newList[skuIndex], spDataList: [...spDataList, { key: '', value: '' }] };
    setFormData({ ...formData, skuStockList: newList });
  };

  const handleRemoveSkuAttribute = (skuIndex: number, attrIndex: number) => {
    const newList = [...formData.skuStockList];
    const spDataList = [...(newList[skuIndex].spDataList || [])];
    spDataList.splice(attrIndex, 1);
    newList[skuIndex] = { ...newList[skuIndex], spDataList };
    setFormData({ ...formData, skuStockList: newList });
  };

  const handleSkuAttributeChange = (skuIndex: number, attrIndex: number, field: string, value: string) => {
    const newList = [...formData.skuStockList];
    const spDataList = [...(newList[skuIndex].spDataList || [])];
    spDataList[attrIndex] = { ...spDataList[attrIndex], [field]: value };
    newList[skuIndex] = { ...newList[skuIndex], spDataList };
    setFormData({ ...formData, skuStockList: newList });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (formData.skuStockList.length === 0) {
      alert("At least one SKU is required!");
      return;
    }
    for (let i = 0; i < formData.skuStockList.length; i++) {
      const sku = formData.skuStockList[i];
      if (!sku.skuCode || !sku.listPrice || !sku.onHandQty) {
        alert("Please fill in all SKU fields (Code, Price, Stock) for all SKUs.");
        return;
      }
    }
    
    try {
      const cleanNumber = (val: string) => Number(String(val).replace(/,/g, ''));
      
      const formattedSkus = formData.skuStockList.map((sku: any) => ({
        skuCode: sku.skuCode,
        listPrice: cleanNumber(sku.listPrice),
        onHandQty: cleanNumber(sku.onHandQty),
        salePrice: sku.promotionPrice ? cleanNumber(sku.promotionPrice) : undefined,
        lowStockThreshold: sku.lowStock ? cleanNumber(sku.lowStock) : undefined,
        imageUrl: sku.pic,
        unitsSold: sku.sale ? cleanNumber(sku.sale) : undefined,
        reservedQty: sku.lockStock ? cleanNumber(sku.lockStock) : undefined,
        skuAttributes: sku.spDataList && sku.spDataList.length > 0 ? JSON.stringify(sku.spDataList) : undefined,
        version: sku.version ? cleanNumber(sku.version) : undefined
      }));

      await productCreateAPI({
        ...formData,
        skuStockList: formattedSkus
      } as unknown as import('@/types/catalog').PmsProductParam);
      alert('Product created successfully!');
      navigate('/catalog/products');
    } catch (error) {
      alert('Failed to create product');
    }
  };

  return (
    <Box>
      <Typography variant="h5" sx={{ mb: 3 }}>Add New Product</Typography>
      <Paper sx={{ p: 4 }}>
        <form onSubmit={handleSubmit}>
          <Grid container spacing={3}>
            <Grid size={{ xs: 12 }}>
              <TextField fullWidth label="Product Name" required value={formData.name} onChange={e => setFormData({...formData, name: e.target.value})} />
            </Grid>
            <Grid size={{ xs: 12 }}>
            </Grid>
            
            <Grid size={{ xs: 12, md: 4 }}>
              <FormControl fullWidth required>
                <InputLabel>Product Category</InputLabel>
                <Select label="Product Category" value={formData.productCategoryId} onChange={e => setFormData({...formData, productCategoryId: e.target.value as string})}>
                  {categories.map((parent: import('@/types/catalog').PmsProductCategoryExt) => [
                    <ListSubheader key={`header-${parent.id}`}>{parent.name}</ListSubheader>,
                    ...(parent.children || []).map((child: import('@/types/catalog').PmsProductCategory) => (
                      <MenuItem key={child.id} value={child.id!.toString()} sx={{ pl: 4 }}>
                        {child.name}
                      </MenuItem>
                    ))
                  ])}
                </Select>
              </FormControl>
            </Grid>
            <Grid size={{ xs: 12, md: 4 }}>
              <FormControl fullWidth required>
                <InputLabel>Product Type</InputLabel>
                <Select label="Product Type" value={formData.productAttributeCategoryId} onChange={e => setFormData({...formData, productAttributeCategoryId: e.target.value as string})}>
                  {productTypes.map((type: any) => (
                    <MenuItem key={type.id} value={type.id?.toString() || ''}>{type.name}</MenuItem>
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
                  onChange={e => setFormData({...formData, brandId: e.target.value as string})}
                >
                  {brands.map((b: import('@/types/catalog').PmsBrand) => (
                    <MenuItem key={b.id} value={b.id?.toString() || ''}>{b.name}</MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Grid>

            <Grid size={{ xs: 12, md: 6 }}>
              <TextField fullWidth label="Image URL" value={formData.primaryImageUrl} onChange={e => setFormData({...formData, primaryImageUrl: e.target.value})} />
              {formData.primaryImageUrl && (
                <Box sx={{ mt: 1 }}>
                  <img src={resolveImageUrl(formData.primaryImageUrl)} alt="Preview" style={{ height: 60, objectFit: 'contain' }} />
                </Box>
              )}
            </Grid>
            <Grid size={{ xs: 12, md: 6 }}>
              <TextField fullWidth label="Search Keywords" value={formData.keywords} onChange={e => setFormData({...formData, keywords: e.target.value})} />
            </Grid>
            <Grid size={{ xs: 12 }}>
              <TextField fullWidth label="Description" multiline rows={4} value={formData.description} onChange={e => setFormData({...formData, description: e.target.value})} />
            </Grid>
            <Grid size={{ xs: 12 }}>
              <Box sx={{ mt: 2, mb: 2 }}>
                <Typography variant="h6" sx={{ mb: 2 }}>Product SKUs (Variants)</Typography>
                {formData.skuStockList.map((sku: any, index: number) => (
                  <Paper key={index} variant="outlined" sx={{ p: 2, mb: 2 }}>
                    <Grid container spacing={2} alignItems="center">
                      <Grid size={{ xs: 12, md: 3 }}>
                        <TextField fullWidth label="SKU Code" required size="small" value={sku.skuCode || ''} onChange={e => handleSkuChange(index, 'skuCode', e.target.value)} />
                      </Grid>
                      <Grid size={{ xs: 12, md: 3 }}>
                        <TextField fullWidth label="List Price (₹)" required size="small" value={sku.listPrice || ''} onChange={e => handleSkuChange(index, 'listPrice', e.target.value)} />
                      </Grid>
                      <Grid size={{ xs: 12, md: 3 }}>
                        <TextField fullWidth label="Sale Price (₹)" size="small" value={sku.promotionPrice || ''} onChange={e => handleSkuChange(index, 'promotionPrice', e.target.value)} />
                      </Grid>
                      <Grid size={{ xs: 12, md: 3 }}>
                        <TextField fullWidth label="Stock" required size="small" value={sku.onHandQty || ''} onChange={e => handleSkuChange(index, 'onHandQty', e.target.value)} />
                      </Grid>
                      <Grid size={{ xs: 12, md: 3 }}>
                        <TextField fullWidth label="Low Stock Threshold" size="small" value={sku.lowStock || ''} onChange={e => handleSkuChange(index, 'lowStock', e.target.value)} />
                      </Grid>
                      <Grid size={{ xs: 12, md: 3 }}>
                        <TextField fullWidth label="Image URL" size="small" value={sku.pic || ''} onChange={e => handleSkuChange(index, 'pic', e.target.value)} />
                      </Grid>
                      <Grid size={{ xs: 12, md: 3 }}>
                        <TextField fullWidth label="Units Sold" size="small" value={sku.sale || ''} onChange={e => handleSkuChange(index, 'sale', e.target.value)} />
                      </Grid>
                      <Grid size={{ xs: 12, md: 3 }}>
                        <TextField fullWidth label="Reserved Qty" size="small" value={sku.lockStock || ''} onChange={e => handleSkuChange(index, 'lockStock', e.target.value)} />
                      </Grid>
                      <Grid size={{ xs: 12 }}>
                        <Typography variant="subtitle2" sx={{ mb: 1 }}>Attributes</Typography>
                        {(sku.spDataList || []).map((attr: any, attrIndex: number) => (
                          <Box key={attrIndex} sx={{ display: 'flex', gap: 1, mb: 1 }}>
                            <TextField label="Key" size="small" value={attr.key || ''} onChange={e => handleSkuAttributeChange(index, attrIndex, 'key', e.target.value)} />
                            <TextField label="Value" size="small" value={attr.value || ''} onChange={e => handleSkuAttributeChange(index, attrIndex, 'value', e.target.value)} />
                            <Button color="error" size="small" onClick={() => handleRemoveSkuAttribute(index, attrIndex)}>X</Button>
                          </Box>
                        ))}
                        <Button size="small" variant="outlined" onClick={() => handleAddSkuAttribute(index)}>+ Add Attribute</Button>
                      </Grid>
                      <Grid size={{ xs: 12, md: 3 }}>
                        <TextField fullWidth label="Version" size="small" value={sku.version || ''} onChange={e => handleSkuChange(index, 'version', e.target.value)} />
                      </Grid>
                      <Grid size={{ xs: 12, md: 3 }}>
                        <Button color="error" variant="outlined" disabled={formData.skuStockList.length === 1} onClick={() => handleRemoveSku(index)}>Remove</Button>
                      </Grid>
                    </Grid>
                  </Paper>
                ))}
                <Button variant="contained" color="secondary" onClick={handleAddSku}>+ Add SKU</Button>
              </Box>
            </Grid>
            <Grid size={{ xs: 12 }}>
              <Box sx={{ display: 'flex', gap: 2 }}>
                <Button type="submit" variant="contained" color="primary" size="large">Submit Product</Button>
                <Button variant="outlined" size="large" onClick={() => navigate('/catalog/products')}>Cancel</Button>
              </Box>
            </Grid>
          </Grid>
        </form>
      </Paper>
    </Box>
  );
};

export default ProductAdd;
