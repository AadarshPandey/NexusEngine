import React, { useState, useEffect } from 'react';
import { Box, Card, CardContent, TextField, Button, Typography, Switch, FormControlLabel, Select, MenuItem, InputLabel, FormControl } from '@mui/material';
import { useNavigate } from 'react-router';
import { productCategoryCreateAPI, getProductCategoryListWithChildrenAPI } from '@/apis/productCate';
import type { PmsProductCategory, PmsProductCategoryExt } from '@/types/productCate';

const ProductCategoryAdd: React.FC = () => {
  const navigate = useNavigate();
  const [categories, setCategories] = useState<PmsProductCategoryExt[]>([]);
  const [formData, setFormData] = useState<PmsProductCategory>({
    parentReviewId: 0,
    name: '',
    unitOfMeasure: '',
    isInNavigation: 0,
    isVisible: 1,
    displayOrder: 0,
    searchKeywords: '',
    customerComment: ''
  });

  useEffect(() => {
    fetchCategories();
  }, []);

  const fetchCategories = async () => {
    try {
      const res = await getProductCategoryListWithChildrenAPI();
      setCategories(res.data);
    } catch (e) {
      console.error(e);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await productCategoryCreateAPI(formData);
      navigate('/pms/productCate');
    } catch (e) {
      console.error('Failed to create category');
    }
  };

  return (
    <Box sx={{ p: 3, maxWidth: 800, margin: '0 auto' }}>
      <Card>
        <CardContent>
          <Typography variant="h6" gutterBottom>Add Product Category</Typography>
          <form onSubmit={handleSubmit}>
            <TextField fullWidth margin="normal" label="Category Name" value={formData.name} onChange={(e) => setFormData({...formData, name: e.target.value})} required />
            
            <FormControl fullWidth margin="normal">
              <InputLabel>Parent Category</InputLabel>
              <Select value={formData.parentReviewId} label="Parent Category" onChange={(e) => setFormData({...formData, parentReviewId: Number(e.target.value)})}>
                <MenuItem value={0}>None (Top Level)</MenuItem>
                {categories.map(c => (
                  <MenuItem key={c.id} value={c.id}>{c.name}</MenuItem>
                ))}
              </Select>
            </FormControl>

            <TextField fullWidth margin="normal" label="Product Unit" value={formData.unitOfMeasure} onChange={(e) => setFormData({...formData, unitOfMeasure: e.target.value})} />
            <TextField fullWidth margin="normal" type="number" label="Sort Order" value={formData.displayOrder} onChange={(e) => setFormData({...formData, displayOrder: Number(e.target.value)})} />
            
            <Box sx={{ display: 'flex', gap: 4, mt: 2, mb: 2 }}>
              <FormControlLabel control={<Switch checked={formData.isVisible === 1} onChange={(e) => setFormData({...formData, isVisible: e.target.checked ? 1 : 0})} />} label="Show Status" />
              <FormControlLabel control={<Switch checked={formData.isInNavigation === 1} onChange={(e) => setFormData({...formData, isInNavigation: e.target.checked ? 1 : 0})} />} label="Navigation Bar" />
            </Box>

            <TextField fullWidth margin="normal" label="Keywords" value={formData.searchKeywords} onChange={(e) => setFormData({...formData, searchKeywords: e.target.value})} />
            <TextField fullWidth margin="normal" label="Description" multiline rows={4} value={formData.customerComment} onChange={(e) => setFormData({...formData, customerComment: e.target.value})} />

            <Box sx={{ mt: 3, display: 'flex', gap: 2 }}>
              <Button type="submit" variant="contained" color="primary">Submit</Button>
              <Button variant="outlined" onClick={() => navigate('/pms/productCate')}>Cancel</Button>
            </Box>
          </form>
        </CardContent>
      </Card>
    </Box>
  );
};

export default ProductCategoryAdd;
