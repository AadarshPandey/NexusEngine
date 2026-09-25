import React, { useEffect, useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router';
import {
  Box,
  Card,
  CardContent,
  Typography,
  TextField,
  Button,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  CircularProgress,
  Snackbar,
  Alert,
} from '@mui/material';
import { getCouponByIdAPI, couponUpdateByIdAPI } from '@/apis/coupon';
import type { SmsCouponExt } from '@/types/coupon';

const CouponUpdate: React.FC = () => {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const id = searchParams.get('id');

  const [loading, setLoading] = useState(false);
  const [saving, setSaving] = useState(false);
  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' as 'success' | 'error' });

  const [formData, setFormData] = useState<SmsCouponExt>({
    type: 0,
    name: '',
    platform: 0,
    amount: 0,
    perLimit: 1,
    minPoint: 0,
    useType: 0,
    publishCount: 100,
    note: '',
  });

  useEffect(() => {
    if (id) {
      fetchCoupon(Number(id));
    }
  }, [id]);

  const fetchCoupon = async (couponId: number) => {
    setLoading(true);
    try {
      const res = await getCouponByIdAPI(couponId);
      if (res.data) {
        const data = res.data;
        if (data.startTime) data.startTime = new Date(data.startTime).toISOString().slice(0, 16);
        if (data.endTime) data.endTime = new Date(data.endTime).toISOString().slice(0, 16);
        if (data.enableTime) data.enableTime = new Date(data.enableTime).toISOString().slice(0, 16);
        setFormData(data);
      }
    } catch (e) {
      console.error(e);
      setSnackbar({ open: true, message: 'Failed to fetch coupon details', severity: 'error' });
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async () => {
    if (!id) return;
    setSaving(true);
    try {
      const payload = {
        ...formData,
        startTime: formData.startTime ? new Date(formData.startTime).toISOString() : undefined,
        endTime: formData.endTime ? new Date(formData.endTime).toISOString() : undefined,
        enableTime: formData.enableTime ? new Date(formData.enableTime).toISOString() : undefined,
      };
      await couponUpdateByIdAPI(Number(id), payload);
      setSnackbar({ open: true, message: 'Coupon updated successfully', severity: 'success' });
      setTimeout(() => navigate('/sms/coupon'), 1000);
    } catch {
      setSnackbar({ open: true, message: 'Failed to update coupon', severity: 'error' });
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return <Box sx={{ display: 'flex', justifyContent: 'center', p: 5 }}><CircularProgress /></Box>;
  }

  return (
    <Box sx={{ maxWidth: 800, mx: 'auto', mt: 4 }}>
      <Card>
        <CardContent sx={{ p: 4 }}>
          <Typography variant="h6" gutterBottom>Edit Coupon</Typography>

          <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3, mt: 3 }}>
            <TextField
              label="Coupon Name"
              value={formData.name || ''}
              onChange={e => setFormData({ ...formData, name: e.target.value })}
              fullWidth
              required
            />

            <Box sx={{ display: 'flex', gap: 2 }}>
              <FormControl fullWidth>
                <InputLabel>Coupon Type</InputLabel>
                <Select
                  value={formData.type}
                  label="Coupon Type"
                  onChange={(e) => setFormData({ ...formData, type: Number(e.target.value) })}
                >
                  <MenuItem value={0}>Full Discount</MenuItem>
                  <MenuItem value={1}>Percentage Discount</MenuItem>
                  <MenuItem value={2}>Free Shipping</MenuItem>
                </Select>
              </FormControl>

              <FormControl fullWidth>
                <InputLabel>Platform</InputLabel>
                <Select
                  value={formData.platform}
                  label="Platform"
                  onChange={(e) => setFormData({ ...formData, platform: Number(e.target.value) })}
                >
                  <MenuItem value={0}>All Platforms</MenuItem>
                  <MenuItem value={1}>Mobile App</MenuItem>
                  <MenuItem value={2}>PC Web</MenuItem>
                </Select>
              </FormControl>
            </Box>

            <Box sx={{ display: 'flex', gap: 2 }}>
              <TextField
                label="Amount (Value / Percentage)"
                type="number"
                value={formData.amount || 0}
                onChange={e => setFormData({ ...formData, amount: Number(e.target.value) })}
                fullWidth
                required
              />
              <TextField
                label="Minimum Spend"
                type="number"
                value={formData.minPoint || 0}
                onChange={e => setFormData({ ...formData, minPoint: Number(e.target.value) })}
                fullWidth
              />
            </Box>

            <Box sx={{ display: 'flex', gap: 2 }}>
              <TextField
                label="Publish Count"
                type="number"
                value={formData.publishCount || 0}
                onChange={e => setFormData({ ...formData, publishCount: Number(e.target.value) })}
                fullWidth
              />
              <TextField
                label="Per User Limit"
                type="number"
                value={formData.perLimit || 1}
                onChange={e => setFormData({ ...formData, perLimit: Number(e.target.value) })}
                fullWidth
              />
            </Box>

            <FormControl fullWidth>
              <InputLabel>Usage Type</InputLabel>
              <Select
                value={formData.useType}
                label="Usage Type"
                onChange={(e) => setFormData({ ...formData, useType: Number(e.target.value) })}
              >
                <MenuItem value={0}>All Products</MenuItem>
                <MenuItem value={1}>Specific Categories</MenuItem>
                <MenuItem value={2}>Specific Products</MenuItem>
              </Select>
            </FormControl>

            <TextField
              label="Enable Time"
              type="datetime-local"
              InputLabelProps={{ shrink: true }}
              value={formData.enableTime || ''}
              onChange={e => setFormData({ ...formData, enableTime: e.target.value })}
              fullWidth
            />

            <Box sx={{ display: 'flex', gap: 2 }}>
              <TextField
                label="Start Time"
                type="datetime-local"
                InputLabelProps={{ shrink: true }}
                value={formData.startTime || ''}
                onChange={e => setFormData({ ...formData, startTime: e.target.value })}
                fullWidth
              />
              <TextField
                label="End Time"
                type="datetime-local"
                InputLabelProps={{ shrink: true }}
                value={formData.endTime || ''}
                onChange={e => setFormData({ ...formData, endTime: e.target.value })}
                fullWidth
              />
            </Box>

            <TextField
              label="Note"
              multiline
              rows={3}
              value={formData.note || ''}
              onChange={e => setFormData({ ...formData, note: e.target.value })}
              fullWidth
            />

            <Box sx={{ display: 'flex', justifyContent: 'center', gap: 2, mt: 2 }}>
              <Button variant="outlined" onClick={() => navigate('/sms/coupon')}>Cancel</Button>
              <Button variant="contained" onClick={handleSubmit} disabled={saving}>
                {saving ? 'Saving...' : 'Update Coupon'}
              </Button>
            </Box>
          </Box>
        </CardContent>
      </Card>

      <Snackbar open={snackbar.open} autoHideDuration={3000} onClose={() => setSnackbar(s => ({ ...s, open: false }))} anchorOrigin={{ vertical: 'top', horizontal: 'center' }}>
        <Alert severity={snackbar.severity} onClose={() => setSnackbar(s => ({ ...s, open: false }))}>{snackbar.message}</Alert>
      </Snackbar>
    </Box>
  );
};

export default CouponUpdate;
