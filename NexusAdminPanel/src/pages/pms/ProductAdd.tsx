import React, { useState, useEffect } from 'react';
import { Box, Typography, Paper, TextField, Button, Grid, FormControl, InputLabel, Select, MenuItem, ListSubheader } from '@mui/material';
import { productCreateAPI } from '@/apis/product';
import { getBrandListAPI } from '@/apis/brand';
import { getProductCategoryListWithChildrenAPI } from '@/apis/productCate';
import { useNavigate } from 'react-router';

const ProductAdd: React.FC = () => {
  const navigate = useNavigate();
  const [brands, setBrands] = useState<import('@/types/brand').PmsBrand[]>([]);
  const [categories, setCategories] = useState<import('@/types/productCate').PmsProductCategory[]>([]);
  
  const [formData, setFormData] = useState({
    name: '',
    customerComment: '',
    listPrice: '',
    originalPrice: '',
    onHandQty: '',
    primaryImageUrl: '',
    permissionGroupId: '',
    brandId: '',
    isPublished: 1,
    isNewArrival: 1,
    isFeatured: 1,
    approvalStatus: 1,
    skuStockList: [{ skuCode: '', listPrice: '', onHandQty: '' }]
  });

  useEffect(() => {
    fetchDependencies();
  }, []);

  const fetchDependencies = async () => {
    try {
      const [brandRes, cateRes] = await Promise.all([
        getBrandListAPI({ pageNum: 1, pageSize: 100 }),
        getProductCategoryListWithChildrenAPI()
      ]);
      setBrands(brandRes.data?.list || []);
      setCategories(cateRes.data || []);
    } catch (e) {
      console.error(e);
    }
  };

  const handleAddSku = () => {
    setFormData({
      ...formData,
      skuStockList: [...formData.skuStockList, { skuCode: '', listPrice: '', onHandQty: '' }]
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
      
      const formattedSkus = formData.skuStockList.map(sku => ({
        skuCode: sku.skuCode,
        listPrice: cleanNumber(sku.listPrice),
        onHandQty: cleanNumber(sku.onHandQty)
      }));

      await productCreateAPI({
        ...formData,
        listPrice: cleanNumber(formData.listPrice),
        originalPrice: cleanNumber(formData.originalPrice),
        onHandQty: cleanNumber(formData.onHandQty),
        skuStockList: formattedSkus
      } as unknown as import('@/types/product').PmsProductParam);
      alert('Product created successfully!');
      navigate('/pms/product');
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
            
            <Grid size={{ xs: 12, md: 6 }}>
              <FormControl fullWidth required>
                <InputLabel>Product Category</InputLabel>
                <Select label="Product Category" value={formData.permissionGroupId} onChange={e => setFormData({...formData, permissionGroupId: e.target.value as string})}>
                  {categories.map((parent: import('@/types/productCate').PmsProductCategoryExt) => [
                    <ListSubheader key={`header-${parent.id}`}>{parent.name}</ListSubheader>,
                    ...(parent.children || []).map((child: import('@/types/productCate').PmsProductCategory) => (
                      <MenuItem key={child.id} value={child.id!.toString()} sx={{ pl: 4 }}>
                        {child.name}
                      </MenuItem>
                    ))
                  ])}
                </Select>
              </FormControl>
            </Grid>
            <Grid size={{ xs: 12, md: 6 }}>
              <FormControl fullWidth required>
                <InputLabel>Brand</InputLabel>
                <Select label="Brand" value={formData.brandId} onChange={e => setFormData({...formData, brandId: e.target.value as string})}>
                  {brands.map((b: import('@/types/brand').PmsBrand) => (
                    <MenuItem key={b.id} value={b.id}>{b.name}</MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Grid>

            <Grid size={{ xs: 12, md: 6 }}>
              <TextField fullWidth label="Price (₹)" type="text" required value={formData.listPrice} onChange={e => setFormData({...formData, listPrice: e.target.value})} />
            </Grid>
            <Grid size={{ xs: 12, md: 6 }}>
              <TextField fullWidth label="Original Price (₹)" type="text" value={formData.originalPrice} onChange={e => setFormData({...formData, originalPrice: e.target.value})} />
            </Grid>
            <Grid size={{ xs: 12, md: 6 }}>
              <TextField fullWidth label="Stock" type="text" required value={formData.onHandQty} onChange={e => setFormData({...formData, onHandQty: e.target.value})} />
            </Grid>
            <Grid size={{ xs: 12, md: 6 }}>
              <TextField fullWidth label="Image URL" value={formData.primaryImageUrl} onChange={e => setFormData({...formData, primaryImageUrl: e.target.value})} />
            </Grid>
            <Grid size={{ xs: 12 }}>
              <TextField fullWidth label="Description" multiline rows={4} value={formData.customerComment} onChange={e => setFormData({...formData, customerComment: e.target.value})} />
            </Grid>
            <Grid size={{ xs: 12 }}>
              <Box sx={{ mt: 2, mb: 2 }}>
                <Typography variant="h6" sx={{ mb: 2 }}>Product SKUs (Variants)</Typography>
                {formData.skuStockList.map((sku, index) => (
                  <Paper key={index} variant="outlined" sx={{ p: 2, mb: 2 }}>
                    <Grid container spacing={2} alignItems="center">
                      <Grid size={{ xs: 12, md: 3 }}>
                        <TextField fullWidth label="SKU Code" required size="small" value={sku.skuCode} onChange={e => handleSkuChange(index, 'skuCode', e.target.value)} />
                      </Grid>
                      <Grid size={{ xs: 12, md: 3 }}>
                        <TextField fullWidth label="Price (₹)" required size="small" value={sku.listPrice} onChange={e => handleSkuChange(index, 'listPrice', e.target.value)} />
                      </Grid>
                      <Grid size={{ xs: 12, md: 3 }}>
                        <TextField fullWidth label="Stock" required size="small" value={sku.onHandQty} onChange={e => handleSkuChange(index, 'onHandQty', e.target.value)} />
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
                <Button variant="outlined" size="large" onClick={() => navigate('/pms/product')}>Cancel</Button>
              </Box>
            </Grid>
          </Grid>
        </form>
      </Paper>
    </Box>
  );
};

export default ProductAdd;
