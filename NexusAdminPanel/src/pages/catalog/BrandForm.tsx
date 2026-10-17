import React, { useState, useEffect } from 'react';
import { Box, Card, CardContent, TextField, Button, Typography, Switch, FormControlLabel } from '@mui/material';
import { useNavigate, useParams, useSearchParams } from 'react-router';
import { brandCreateAPI, getBrandByIdAPI, brandUpdateByIdAPI } from '@/apis/catalog';
import { resolveImageUrl } from '@/utils/media';

const BrandForm: React.FC = () => {
  const navigate = useNavigate();
  const { id: paramId } = useParams<{ id: string }>();
  const [searchParams] = useSearchParams();
  const id = Number(paramId || searchParams.get('id'));
  const isEdit = Boolean(id);

  const [formData, setFormData] = useState({
    name: '',
    logo: '',
    isVisible: 1,
  });

  useEffect(() => {
    if (isEdit && id) {
      getBrandByIdAPI(id)
        .then((res) => {
          if (res) {
            setFormData({
              name: res.name || '',
              logo: res.logo || res.logoUrl || '',
              isVisible: res.isVisible ? 1 : 0,
            });
          }
        })
        .catch((err) => console.error('Failed to load brand', err));
    }
  }, [id, isEdit]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if (isEdit && id) {
        await brandUpdateByIdAPI(id, formData);
      } else {
        await brandCreateAPI(formData);
      }
      navigate('/catalog/brands');
    } catch (err) {
      console.error(isEdit ? 'Failed to update brand' : 'Failed to create brand', err);
    }
  };

  return (
    <Box sx={{ p: 3, maxWidth: 640, margin: '0 auto' }}>
      <Card>
        <CardContent>
          <Typography variant="h6" gutterBottom>{isEdit ? 'Update Brand' : 'Add Brand'}</Typography>
          <form onSubmit={handleSubmit}>
            <TextField
              fullWidth
              margin="normal"
              label="Brand Name"
              value={formData.name}
              onChange={(e) => setFormData({ ...formData, name: e.target.value })}
              required
            />
            <TextField
              fullWidth
              margin="normal"
              label="Logo URL"
              value={formData.logo}
              onChange={(e) => setFormData({ ...formData, logo: e.target.value })}
              required
            />
            {formData.logo && (
              <Box sx={{ mt: 1, mb: 1 }}>
                <img src={resolveImageUrl(formData.logo)} alt="Logo Preview" style={{ height: 48, objectFit: 'contain' }} />
              </Box>
            )}
            <Box sx={{ mt: 2, mb: 2 }}>
              <FormControlLabel
                control={
                  <Switch
                    checked={Boolean(formData.isVisible)}
                    onChange={(e) => setFormData({ ...formData, isVisible: e.target.checked ? 1 : 0 })}
                  />
                }
                label="Visible"
              />
            </Box>
            <Box sx={{ mt: 3, display: 'flex', gap: 2 }}>
              <Button type="submit" variant="contained" color="primary">Submit</Button>
              <Button variant="outlined" onClick={() => navigate('/catalog/brands')}>Cancel</Button>
            </Box>
          </form>
        </CardContent>
      </Card>
    </Box>
  );
};

export default BrandForm;
