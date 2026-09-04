import React, { useState } from 'react';
import { Box, Typography, Paper, TextField, Button, Grid } from '@mui/material';
import { productCreateAPI } from '@/apis/product';
import { useNavigate } from 'react-router';

const ProductAdd: React.FC = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    name: '',
    subTitle: '',
    description: '',
    price: '',
    originalPrice: '',
    stock: '',
    pic: '',
    productCategoryId: 2, // Hardcoded for simplified version
    brandId: 1, // Hardcoded for simplified version
    publishStatus: 1,
    newStatus: 1,
    recommandStatus: 1,
    verifyStatus: 1
  });

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await productCreateAPI({
        ...formData,
        price: Number(formData.price),
        originalPrice: Number(formData.originalPrice),
        stock: Number(formData.stock)
      } as any);
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
            <Grid item xs={12}>
              <TextField fullWidth label="Product Name" required value={formData.name} onChange={e => setFormData({...formData, name: e.target.value})} />
            </Grid>
            <Grid item xs={12}>
              <TextField fullWidth label="Subtitle" value={formData.subTitle} onChange={e => setFormData({...formData, subTitle: e.target.value})} />
            </Grid>
            <Grid item xs={12} md={6}>
              <TextField fullWidth label="Price (₹)" type="number" required value={formData.price} onChange={e => setFormData({...formData, price: e.target.value})} />
            </Grid>
            <Grid item xs={12} md={6}>
              <TextField fullWidth label="Original Price (₹)" type="number" value={formData.originalPrice} onChange={e => setFormData({...formData, originalPrice: e.target.value})} />
            </Grid>
            <Grid item xs={12} md={6}>
              <TextField fullWidth label="Stock" type="number" required value={formData.stock} onChange={e => setFormData({...formData, stock: e.target.value})} />
            </Grid>
            <Grid item xs={12} md={6}>
              <TextField fullWidth label="Image URL" value={formData.pic} onChange={e => setFormData({...formData, pic: e.target.value})} />
            </Grid>
            <Grid item xs={12}>
              <TextField fullWidth label="Description" multiline rows={4} value={formData.description} onChange={e => setFormData({...formData, description: e.target.value})} />
            </Grid>
            <Grid item xs={12}>
              <Button type="submit" variant="contained" color="primary" size="large">Submit Product</Button>
            </Grid>
          </Grid>
        </form>
      </Paper>
    </Box>
  );
};

export default ProductAdd;
