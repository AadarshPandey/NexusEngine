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
    subTitle: '',
    description: '',
    price: '',
    originalPrice: '',
    stock: '',
    pic: '',
    productCategoryId: '',
    brandId: '',
    publishStatus: 1,
    newStatus: 1,
    recommendStatus: 1,
    verifyStatus: 1
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

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const cleanNumber = (val: string) => Number(String(val).replace(/,/g, ''));
      
      await productCreateAPI({
        ...formData,
        price: cleanNumber(formData.price),
        originalPrice: cleanNumber(formData.originalPrice),
        stock: cleanNumber(formData.stock)
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
              <TextField fullWidth label="Subtitle" value={formData.subTitle} onChange={e => setFormData({...formData, subTitle: e.target.value})} />
            </Grid>
            
            <Grid size={{ xs: 12, md: 6 }}>
              <FormControl fullWidth required>
                <InputLabel>Product Category</InputLabel>
                <Select label="Product Category" value={formData.productCategoryId} onChange={e => setFormData({...formData, productCategoryId: e.target.value as string})}>
                  {categories.map((parent: any) => [
                    <ListSubheader key={`header-${parent.id}`}>{parent.name}</ListSubheader>,
                    ...(parent.children || []).map((child: any) => (
                      <MenuItem key={child.id} value={child.id.toString()} sx={{ pl: 4 }}>
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
                  {brands.map((b: any) => (
                    <MenuItem key={b.id} value={b.id}>{b.name}</MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Grid>

            <Grid size={{ xs: 12, md: 6 }}>
              <TextField fullWidth label="Price (₹)" type="text" required value={formData.price} onChange={e => setFormData({...formData, price: e.target.value})} />
            </Grid>
            <Grid size={{ xs: 12, md: 6 }}>
              <TextField fullWidth label="Original Price (₹)" type="text" value={formData.originalPrice} onChange={e => setFormData({...formData, originalPrice: e.target.value})} />
            </Grid>
            <Grid size={{ xs: 12, md: 6 }}>
              <TextField fullWidth label="Stock" type="text" required value={formData.stock} onChange={e => setFormData({...formData, stock: e.target.value})} />
            </Grid>
            <Grid size={{ xs: 12, md: 6 }}>
              <TextField fullWidth label="Image URL" value={formData.pic} onChange={e => setFormData({...formData, pic: e.target.value})} />
            </Grid>
            <Grid size={{ xs: 12 }}>
              <TextField fullWidth label="Description" multiline rows={4} value={formData.description} onChange={e => setFormData({...formData, description: e.target.value})} />
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
