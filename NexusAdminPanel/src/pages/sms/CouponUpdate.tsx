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
import dayjs from 'dayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { DateTimePicker } from '@mui/x-date-pickers/DateTimePicker';

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
        discountValue: 0,
    perMemberLimit: 1,
    minSpendAmount: 0,
    applicabilityScope: 0,
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
        if (data.startsAt) data.startsAt = new Date(data.startsAt).toISOString().slice(0, 16);
        if (data.endsAt) data.endsAt = new Date(data.endsAt).toISOString().slice(0, 16);
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
        startsAt: formData.startsAt ? new Date(formData.startsAt).toISOString() : undefined,
        endsAt: formData.endsAt ? new Date(formData.endsAt).toISOString() : undefined,
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
                  label="Platform"
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
                value={formData.discountValue || 0}
                onChange={e => setFormData({ ...formData, discountValue: Number(e.target.value) })}
                fullWidth
                required
              />
              <TextField
                label="Minimum Spend"
                type="number"
                value={formData.minSpendAmount || 0}
                onChange={e => setFormData({ ...formData, minSpendAmount: Number(e.target.value) })}
                fullWidth
              />
            </Box>

            <Box sx={{ display: 'flex', gap: 2 }}>
              
              <TextField
                label="Per User Limit"
                type="number"
                value={formData.perMemberLimit || 1}
                onChange={e => setFormData({ ...formData, perMemberLimit: Number(e.target.value) })}
                fullWidth
              />
            </Box>

            <FormControl fullWidth>
              <InputLabel>Usage Type</InputLabel>
              <Select
                value={formData.applicabilityScope}
                label="Usage Type"
                onChange={(e) => setFormData({ ...formData, applicabilityScope: Number(e.target.value) })}
              >
                <MenuItem value={0}>All Products</MenuItem>
                <MenuItem value={1}>Specific Categories</MenuItem>
                <MenuItem value={2}>Specific Products</MenuItem>
              </Select>
            </FormControl>

          <LocalizationProvider dateAdapter={AdapterDayjs}>
            

            <Box sx={{ display: 'flex', gap: 2, mb: 2 }}>
              <DateTimePicker
                label="Start Time"
                format="DD/MM/YYYY HH:mm"
                value={formData.startsAt ? dayjs(formData.startsAt) : null}
                onChange={(newValue) => setFormData({ ...formData, startsAt: newValue ? newValue.format('YYYY-MM-DDTHH:mm') : '' })}
                slotProps={{ textField: { fullWidth: true } }}
              />
              <DateTimePicker
                label="End Time"
                format="DD/MM/YYYY HH:mm"
                value={formData.endsAt ? dayjs(formData.endsAt) : null}
                onChange={(newValue) => setFormData({ ...formData, endsAt: newValue ? newValue.format('YYYY-MM-DDTHH:mm') : '' })}
                slotProps={{ textField: { fullWidth: true } }}
              />
            </Box>
          </LocalizationProvider>

            

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
