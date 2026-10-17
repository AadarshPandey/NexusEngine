import React, { useEffect, useState } from 'react';
import { useNavigate, useParams, useSearchParams } from 'react-router';
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
  Autocomplete,
  Chip,
} from '@mui/material';
import { getCouponByIdAPI, couponCreateAPI, couponUpdateByIdAPI } from '@/apis/promotion';
import { getProductCategoryListWithChildrenAPI, getProductListAPI } from '@/apis/catalog';
import type { SmsCouponExt } from '@/types/promotion';
import dayjs from 'dayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { DateTimePicker } from '@mui/x-date-pickers/DateTimePicker';

interface OptionItem {
  id: number;
  label: string;
}

const CouponForm: React.FC = () => {
  const navigate = useNavigate();
  const { id: paramId } = useParams<{ id: string }>();
  const [searchParams] = useSearchParams();
  const id = paramId || searchParams.get('id');
  const isEdit = Boolean(id);

  const [loading, setLoading] = useState(false);
  const [saving, setSaving] = useState(false);
  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' as 'success' | 'error' });

  const [categories, setCategories] = useState<OptionItem[]>([]);
  const [products, setProducts] = useState<OptionItem[]>([]);

  const [formData, setFormData] = useState<SmsCouponExt>({
    type: 0,
    name: '',
    count: 100,
    amount: 0,
    perLimit: 1,
    minOrderAmount: 0,
    useType: 0,
    productCategoryRelationList: [],
    productRelationList: [],
  });

  useEffect(() => {
    fetchOptions();
    if (isEdit && id) {
      fetchCoupon(Number(id));
    }
  }, [id, isEdit]);

  const fetchOptions = async () => {
    try {
      const [cateRes, prodRes] = await Promise.all([
        getProductCategoryListWithChildrenAPI(),
        getProductListAPI({ pageNum: 1, pageSize: 100 }),
      ]);
      const flattenedCategories: OptionItem[] = [];
      if (Array.isArray(cateRes)) {
        cateRes.forEach((parent: any) => {
          flattenedCategories.push({ id: parent.id, label: parent.name });
          if (parent.children) {
            parent.children.forEach((child: any) => {
              flattenedCategories.push({ id: child.id, label: `${parent.name} > ${child.name}` });
            });
          }
        });
      }
      setCategories(flattenedCategories);

      const prodList: OptionItem[] = (prodRes?.content || []).map((p: any) => ({
        id: p.id,
        label: `${p.name} (ID: ${p.id})`,
      }));
      setProducts(prodList);
    } catch (e) {
      console.error('Failed to load category or product options', e);
    }
  };

  const fetchCoupon = async (couponId: number) => {
    setLoading(true);
    try {
      const data = await getCouponByIdAPI(couponId);
      if (data) {
        const clone = { ...data };
        if (clone.startTime) clone.startTime = new Date(clone.startTime).toISOString().slice(0, 16);
        if (clone.endTime) clone.endTime = new Date(clone.endTime).toISOString().slice(0, 16);
        setFormData(clone);
      }
    } catch (e) {
      console.error(e);
      setSnackbar({ open: true, message: 'Failed to fetch coupon details', severity: 'error' });
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async () => {
    setSaving(true);
    try {
      const payload: SmsCouponExt = {
        ...formData,
        startTime: formData.startTime ? new Date(formData.startTime).toISOString() : undefined,
        endTime: formData.endTime ? new Date(formData.endTime).toISOString() : undefined,
        productCategoryRelationList: formData.useType === 1 ? (formData.productCategoryRelationList || []) : [],
        productRelationList: formData.useType === 2 ? (formData.productRelationList || []) : [],
      };
      if (isEdit && id) {
        await couponUpdateByIdAPI(Number(id), payload);
        setSnackbar({ open: true, message: 'Coupon updated successfully', severity: 'success' });
      } else {
        await couponCreateAPI(payload);
        setSnackbar({ open: true, message: 'Coupon created successfully', severity: 'success' });
      }
      setTimeout(() => navigate('/promotion/coupons'), 1000);
    } catch {
      setSnackbar({ open: true, message: isEdit ? 'Failed to update coupon' : 'Failed to create coupon', severity: 'error' });
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return <Box sx={{ display: 'flex', justifyContent: 'center', p: 5 }}><CircularProgress /></Box>;
  }

  const selectedCategoryOptions = categories.filter((c) =>
    (formData.productCategoryRelationList || []).some((r) => r.productCategoryId === c.id)
  );

  const selectedProductOptions = products.filter((p) =>
    (formData.productRelationList || []).some((r) => r.productId === p.id)
  );

  return (
    <Box sx={{ maxWidth: 800, mx: 'auto', mt: 4 }}>
      <Card>
        <CardContent sx={{ p: 4 }}>
          <Typography variant="h6" gutterBottom>{isEdit ? 'Edit Coupon' : 'Add Coupon'}</Typography>

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
              <InputLabel>Usage Scope (promotion.coupon)</InputLabel>
              <Select
                value={formData.useType}
                label="Usage Scope (promotion.coupon)"
                onChange={(e) => setFormData({ ...formData, useType: Number(e.target.value) })}
              >
                <MenuItem value={0}>All Products (Universal)</MenuItem>
                <MenuItem value={1}>Specific Categories (promotion.coupon_category)</MenuItem>
                <MenuItem value={2}>Specific Products (promotion.coupon_product)</MenuItem>
              </Select>
            </FormControl>

            {/* Category Selector when useType === 1 */}
            {formData.useType === 1 && (
              <Autocomplete
                multiple
                options={categories}
                getOptionLabel={(option) => option.label}
                isOptionEqualToValue={(option, value) => option.id === value.id}
                value={selectedCategoryOptions}
                onChange={(_, selected) => {
                  setFormData({
                    ...formData,
                    productCategoryRelationList: selected.map((s) => ({
                      productCategoryId: s.id,
                      productCategoryName: s.label,
                    })),
                  });
                }}
                renderTags={(value, getTagProps) =>
                  value.map((option, index) => (
                    <Chip variant="outlined" label={option.label} {...getTagProps({ index })} key={option.id} />
                  ))
                }
                renderInput={(params) => (
                  <TextField {...params} label="Select Applicable Categories" placeholder="Categories" />
                )}
              />
            )}

            {/* Product Selector when useType === 2 */}
            {formData.useType === 2 && (
              <Autocomplete
                multiple
                options={products}
                getOptionLabel={(option) => option.label}
                isOptionEqualToValue={(option, value) => option.id === value.id}
                value={selectedProductOptions}
                onChange={(_, selected) => {
                  setFormData({
                    ...formData,
                    productRelationList: selected.map((s) => ({
                      productId: s.id,
                      productName: s.label,
                    })),
                  });
                }}
                renderTags={(value, getTagProps) =>
                  value.map((option, index) => (
                    <Chip variant="outlined" label={option.label} {...getTagProps({ index })} key={option.id} />
                  ))
                }
                renderInput={(params) => (
                  <TextField {...params} label="Select Applicable Products" placeholder="Products" />
                )}
              />
            )}

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
              <Button variant="outlined" onClick={() => navigate('/promotion/coupons')}>Cancel</Button>
              <Button variant="contained" onClick={handleSubmit} disabled={saving}>
                {saving ? (isEdit ? 'Saving...' : 'Creating...') : (isEdit ? 'Update Coupon' : 'Add Coupon')}
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

export default CouponForm;
