import React, { useState, useEffect } from 'react';
import { Box, Typography, Paper, TextField, Button, Grid, CircularProgress } from '@mui/material';
import { productUpdateByIdAPI, getPruductUpdateInfoAPI } from '@/apis/product';
import { useNavigate, useSearchParams } from 'react-router';

const ProductUpdate: React.FC = () => {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const id = searchParams.get('id');

  const [loading, setLoading] = useState(true);
  const [formData, setFormData] = useState({
    name: '',
    subTitle: '',
    description: '',
    price: '',
    originalPrice: '',
    stock: '',
    pic: '',
    productCategoryId: 2,
    brandId: 1,
    publishStatus: 1,
    newStatus: 1,
    recommendStatus: 1,
    verifyStatus: 1
  });

  useEffect(() => {
    if (id) {
      fetchProductInfo(Number(id));
    } else {
      alert('Product ID is missing');
      navigate('/pms/product');
    }
  }, [id]);

  const fetchProductInfo = async (productId: number) => {
    try {
      setLoading(true);
      const res = await getPruductUpdateInfoAPI(productId);
      if (res.data) {
        setFormData({
          ...formData,
          ...res.data,
          // ensure price/stock are strings for the inputs if they come back as numbers
          price: res.data.price?.toString() || '',
          originalPrice: res.data.originalPrice?.toString() || '',
          stock: res.data.stock?.toString() || ''
        });
      }
    } catch (error) {
      alert('Failed to fetch product details');
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!id) return;
    try {
      const cleanNumber = (val: string | number) => Number(String(val).replace(/,/g, ''));
      
      await productUpdateByIdAPI(Number(id), {
        ...formData,
        price: cleanNumber(formData.price),
        originalPrice: cleanNumber(formData.originalPrice),
        stock: cleanNumber(formData.stock)
      } as unknown as import('@/types/product').PmsProductParam);
      alert('Product updated successfully!');
      navigate('/pms/product');
    } catch (error) {
      alert('Failed to update product');
    }
  };

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '50vh' }}>
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Box>
      <Typography variant="h5" sx={{ mb: 3 }}>Update Product</Typography>
      <Paper sx={{ p: 4 }}>
        <form onSubmit={handleSubmit}>
          <Grid container spacing={3}>
            <Grid size={{ xs: 12 }}>
              <TextField fullWidth label="Product Name" required value={formData.name || ''} onChange={e => setFormData({...formData, name: e.target.value})} />
            </Grid>
            <Grid size={{ xs: 12 }}>
              <TextField fullWidth label="Subtitle" value={formData.subTitle || ''} onChange={e => setFormData({...formData, subTitle: e.target.value})} />
            </Grid>
            <Grid size={{ xs: 12, md: 6 }}>
              <TextField fullWidth label="Price (₹)" type="text" required value={formData.price || ''} onChange={e => setFormData({...formData, price: e.target.value})} />
            </Grid>
            <Grid size={{ xs: 12, md: 6 }}>
              <TextField fullWidth label="Original Price (₹)" type="text" value={formData.originalPrice || ''} onChange={e => setFormData({...formData, originalPrice: e.target.value})} />
            </Grid>
            <Grid size={{ xs: 12, md: 6 }}>
              <TextField fullWidth label="Stock" type="text" required value={formData.stock || ''} onChange={e => setFormData({...formData, stock: e.target.value})} />
            </Grid>
            <Grid size={{ xs: 12, md: 6 }}>
              <TextField fullWidth label="Image URL" value={formData.pic || ''} onChange={e => setFormData({...formData, pic: e.target.value})} />
            </Grid>
            <Grid size={{ xs: 12 }}>
              <TextField fullWidth label="Description" multiline rows={4} value={formData.description || ''} onChange={e => setFormData({...formData, description: e.target.value})} />
            </Grid>
            <Grid size={{ xs: 12 }}>
              <Box sx={{ display: 'flex', gap: 2 }}>
                <Button type="submit" variant="contained" color="primary" size="large">Update Product</Button>
                <Button variant="outlined" size="large" onClick={() => navigate('/pms/product')}>Cancel</Button>
              </Box>
            </Grid>
          </Grid>
        </form>
      </Paper>
    </Box>
  );
};

export default ProductUpdate;
