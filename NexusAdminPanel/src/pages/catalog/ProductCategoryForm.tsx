import React, { useState, useEffect } from 'react';
import { Box, Card, CardContent, TextField, Button, Typography, Switch, FormControlLabel, Select, MenuItem, InputLabel, FormControl } from '@mui/material';
import { useNavigate, useParams, useSearchParams } from 'react-router';
import { productCategoryCreateAPI, productCategoryUpdateByIdAPI, getProductCategoryByIdAPI, getProductCategoryListWithChildrenAPI } from '@/apis/catalog';
import type { PmsProductCategory, PmsProductCategoryExt } from '@/types/catalog';

const ProductCategoryForm: React.FC = () => {
  const navigate = useNavigate();
  const { id: paramId } = useParams<{ id: string }>();
  const [searchParams] = useSearchParams();
  const id = Number(paramId || searchParams.get('id'));
  const isEdit = Boolean(id);

  const [categories, setCategories] = useState<PmsProductCategoryExt[]>([]);
  const [formData, setFormData] = useState<PmsProductCategory>({
    parentId: 0,
    name: '',
    isInNavigation: 0,
    isVisible: 1,
  });

  useEffect(() => {
    if (isEdit && id) {
      getProductCategoryByIdAPI(id)
        .then((res) => {
          if (res) setFormData(res);
        })
        .catch((e) => console.error('Failed to load category', e));
    }
    fetchCategories();
  }, [id, isEdit]);

  const fetchCategories = async () => {
    try {
      const res = await getProductCategoryListWithChildrenAPI();
      setCategories(Array.isArray(res) ? res : []);
    } catch (e) {
      console.error(e);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if (isEdit && id) {
        await productCategoryUpdateByIdAPI(id, formData);
      } else {
        await productCategoryCreateAPI(formData);
      }
      navigate('/catalog/categories');
    } catch (e) {
      console.error(isEdit ? 'Failed to update category' : 'Failed to create category', e);
    }
  };

  return (
    <Box sx={{ p: 3, maxWidth: 800, margin: '0 auto' }}>
      <Card>
        <CardContent>
          <Typography variant="h6" gutterBottom>{isEdit ? 'Update Product Category' : 'Add Product Category'}</Typography>
          <form onSubmit={handleSubmit}>
            <TextField
              fullWidth
              margin="normal"
              label="Category Name"
              value={formData.name || ''}
              onChange={(e) => setFormData({ ...formData, name: e.target.value })}
              required
            />

            <FormControl fullWidth margin="normal">
              <InputLabel>Parent Category</InputLabel>
              <Select
                value={formData.parentId || 0}
                label="Parent Category"
                onChange={(e) => setFormData({ ...formData, parentId: Number(e.target.value) })}
              >
                <MenuItem value={0}>None (Top Level)</MenuItem>
                {categories.map((c) => (
                  <MenuItem key={c.id} value={c.id}>{c.name}</MenuItem>
                ))}
              </Select>
            </FormControl>

            <Box sx={{ display: 'flex', gap: 4, mt: 2, mb: 2 }}>
              <FormControlLabel
                control={<Switch checked={Boolean(formData.isVisible)} onChange={(e) => setFormData({ ...formData, isVisible: e.target.checked ? 1 : 0 })} />}
                label="Show Status"
              />
              <FormControlLabel
                control={<Switch checked={Boolean(formData.isInNavigation)} onChange={(e) => setFormData({ ...formData, isInNavigation: e.target.checked ? 1 : 0 })} />}
                label="Navigation Bar"
              />
            </Box>

            <Box sx={{ mt: 3, display: 'flex', gap: 2 }}>
              <Button type="submit" variant="contained" color="primary">Submit</Button>
              <Button variant="outlined" onClick={() => navigate('/catalog/categories')}>Cancel</Button>
            </Box>
          </form>
        </CardContent>
      </Card>
    </Box>
  );
};

export default ProductCategoryForm;
