import React, { useState } from 'react';
import { useNavigate } from 'react-router';
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
  Snackbar,
  Alert,
} from '@mui/material';
import { couponCreateAPI } from '@/apis/coupon';
import type { SmsCouponExt } from '@/types/coupon';
import dayjs from 'dayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { DateTimePicker } from '@mui/x-date-pickers/DateTimePicker';

const CouponAdd: React.FC = () => {
  const navigate = useNavigate();
  const [saving, setSaving] = useState(false);
  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' as 'success' | 'error' });

  const [formData, setFormData] = useState<SmsCouponExt>({
    type: 0,
    name: '',
    count: 100,
    amount: 0,
    perLimit: 1,
    minOrderAmount: 0,
    useType: 0,
          });

  const handleSubmit = async () => {
    setSaving(true);
    try {
      const payload = {
        ...formData,
        startTime: formData.startTime ? new Date(formData.startTime).toISOString() : undefined,
        endTime: formData.endTime ? new Date(formData.endTime).toISOString() : undefined,
              };
      await couponCreateAPI(payload);
      setSnackbar({ open: true, message: 'Coupon created successfully', severity: 'success' });
      setTimeout(() => navigate('/sms/coupon'), 1000);
    } catch {
      setSnackbar({ open: true, message: 'Failed to create coupon', severity: 'error' });
    } finally {
      setSaving(false);
    }
  };

  return (
    <Box sx={{ maxWidth: 800, mx: 'auto', mt: 4 }}>
      <Card>
        <CardContent sx={{ p: 4 }}>
          <Typography variant="h6" gutterBottom>Add Coupon</Typography>

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

              <TextField
                label="Total Quantity"
                type="number"
                value={formData.count || 0}
                onChange={e => setFormData({ ...formData, count: Number(e.target.value) })}
                fullWidth
                required
              />
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
                value={formData.minOrderAmount || 0}
                onChange={e => setFormData({ ...formData, minOrderAmount: Number(e.target.value) })}
                fullWidth
              />
            </Box>

            <Box sx={{ display: 'flex', gap: 2 }}>
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

          <LocalizationProvider dateAdapter={AdapterDayjs}>
            

            <Box sx={{ display: 'flex', gap: 2, mb: 2 }}>
              <DateTimePicker
                label="Start Time"
                format="DD/MM/YYYY HH:mm"
                value={formData.startTime ? dayjs(formData.startTime) : null}
                onChange={(newValue) => setFormData({ ...formData, startTime: newValue ? newValue.format('YYYY-MM-DDTHH:mm') : '' })}
                slotProps={{ textField: { fullWidth: true } }}
              />
              <DateTimePicker
                label="End Time"
                format="DD/MM/YYYY HH:mm"
                value={formData.endTime ? dayjs(formData.endTime) : null}
                onChange={(newValue) => setFormData({ ...formData, endTime: newValue ? newValue.format('YYYY-MM-DDTHH:mm') : '' })}
                slotProps={{ textField: { fullWidth: true } }}
              />
            </Box>
          </LocalizationProvider>

            

            <Box sx={{ display: 'flex', justifyContent: 'center', gap: 2, mt: 2 }}>
              <Button variant="outlined" onClick={() => navigate('/sms/coupon')}>Cancel</Button>
              <Button variant="contained" onClick={handleSubmit} disabled={saving}>
                {saving ? 'Creating...' : 'Add Coupon'}
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

export default CouponAdd;
