import React, { useEffect, useState } from 'react';
import {
  Box,
  Card,
  Typography,
  TextField,
  Button,
  InputAdornment,
  Grid,
  CircularProgress,
  Alert,
  Snackbar,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  IconButton,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  FormControlLabel,
  Checkbox,
  Chip,
  Divider,
} from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import AddIcon from '@mui/icons-material/Add';
import { getOrderSettingByIdAPI, orderSettingUpdateByIdAPI } from '@/apis/ordering';
import {
  getCompanyAddressListAllAPI,
  createCompanyAddressAPI,
  updateCompanyAddressAPI,
  deleteCompanyAddressAPI,
} from '@/apis/ordering/merchantAddress';
import type { OmsOrderSetting, MerchantAddress } from '@/types/ordering';

const OrderSetting: React.FC = () => {
  const [setting, setSetting] = useState<OmsOrderSetting | null>(null);
  const [loading, setLoading] = useState(false);
  const [saving, setSaving] = useState(false);
  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' as 'success' | 'error' });

  // Merchant warehouse addresses state
  const [addresses, setAddresses] = useState<MerchantAddress[]>([]);
  const [addressLoading, setAddressLoading] = useState(false);
  const [openAddressDialog, setOpenAddressDialog] = useState(false);
  const [editingAddressId, setEditingAddressId] = useState<number | null>(null);
  const [addressForm, setAddressForm] = useState({
    label: '',
    contactName: '',
    contactPhone: '',
    state: '',
    city: '',
    addressLine: '',
    isDefaultShipFrom: false,
    isDefaultReturnTo: false,
  });

  const SETTING_ID = 1;

  useEffect(() => {
    fetchSettings();
    fetchAddresses();
  }, []);

  const fetchSettings = async () => {
    setLoading(true);
    try {
      const res = await getOrderSettingByIdAPI(SETTING_ID);
      if (res) {
        setSetting(res);
      }
    } catch (e) {
      console.error(e);
      setSnackbar({ open: true, message: 'Failed to fetch order settings', severity: 'error' });
    } finally {
      setLoading(false);
    }
  };

  const fetchAddresses = async () => {
    setAddressLoading(true);
    try {
      const res = await getCompanyAddressListAllAPI();
      if (res) {
        setAddresses(res);
      }
    } catch (e) {
      console.error(e);
    } finally {
      setAddressLoading(false);
    }
  };

  const handleSave = async () => {
    if (!setting) return;
    setSaving(true);
    try {
      await orderSettingUpdateByIdAPI(SETTING_ID, setting);
      setSnackbar({ open: true, message: 'Order settings updated successfully!', severity: 'success' });
    } catch (e) {
      console.error(e);
      setSnackbar({ open: true, message: 'Failed to update order settings', severity: 'error' });
    } finally {
      setSaving(false);
    }
  };

  const handleChange = (field: keyof OmsOrderSetting, value: string) => {
    if (!setting) return;
    setSetting({ ...setting, [field]: Number(value) });
  };

  const handleOpenAddAddress = () => {
    setEditingAddressId(null);
    setAddressForm({
      label: '',
      contactName: '',
      contactPhone: '',
      state: '',
      city: '',
      addressLine: '',
      isDefaultShipFrom: false,
      isDefaultReturnTo: false,
    });
    setOpenAddressDialog(true);
  };

  const handleOpenEditAddress = (addr: any) => {
    setEditingAddressId(addr.id);
    setAddressForm({
      label: addr.label || addr.addressName || '',
      contactName: addr.contactName || addr.name || '',
      contactPhone: addr.contactPhone || addr.phone || '',
      state: addr.state || addr.province || '',
      city: addr.city || addr.region || '',
      addressLine: addr.addressLine || addr.detailAddress || '',
      isDefaultShipFrom: Boolean(addr.sendStatus === 1 || addr.isDefaultShipFrom),
      isDefaultReturnTo: Boolean(addr.receiveStatus === 1 || addr.isDefaultReturnTo),
    });
    setOpenAddressDialog(true);
  };

  const handleSaveAddress = async () => {
    if (!addressForm.label.trim() || !addressForm.addressLine.trim()) {
      alert('Please fill in Warehouse Label and Detailed Address.');
      return;
    }
    try {
      const payload: any = {
        addressName: addressForm.label,
        name: addressForm.contactName,
        phone: addressForm.contactPhone,
        state: addressForm.state,
        city: addressForm.city,
        detailAddress: addressForm.addressLine,
        sendStatus: addressForm.isDefaultShipFrom,
        receiveStatus: addressForm.isDefaultReturnTo,
      };
      if (editingAddressId) {
        await updateCompanyAddressAPI(editingAddressId, payload);
        setSnackbar({ open: true, message: 'Warehouse address updated', severity: 'success' });
      } else {
        await createCompanyAddressAPI(payload);
        setSnackbar({ open: true, message: 'Warehouse address added', severity: 'success' });
      }
      setOpenAddressDialog(false);
      fetchAddresses();
    } catch (e) {
      console.error(e);
      setSnackbar({ open: true, message: 'Failed to save address', severity: 'error' });
    }
  };

  const handleDeleteAddress = async (id: number) => {
    if (window.confirm('Are you sure you want to delete this warehouse address?')) {
      try {
        await deleteCompanyAddressAPI(id);
        setSnackbar({ open: true, message: 'Address deleted successfully', severity: 'success' });
        fetchAddresses();
      } catch (e) {
        console.error(e);
        setSnackbar({ open: true, message: 'Failed to delete address', severity: 'error' });
      }
    }
  };

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', p: 10 }}>
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Box sx={{ p: 3, display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 4 }}>
      {/* 1. Global Order Lifecycle Policies */}
      <Card sx={{ p: 4, width: '100%', maxWidth: 900 }}>
        <Typography variant="h5" sx={{ mb: 1 }}>Order Settings & Policies</Typography>
        <Typography variant="body2" color="text.secondary" sx={{ mb: 4 }}>
          Configure automated timeouts, confirmation limits, and lifecycle policies across all customer orders.
        </Typography>

        {setting && (
          <Grid container spacing={4}>
            {/* 1. Normal Order Overtime */}
            <Grid size={{ xs: 12 }}>
              <Box sx={{ display: 'flex', alignItems: 'center' }}>
                <Typography sx={{ width: 280, fontWeight: 500 }}>Unpaid Order Timeout:</Typography>
                <TextField
                  size="small"
                  type="number"
                  value={setting.paymentTimeoutMin || 0}
                  onChange={(e) => handleChange('paymentTimeoutMin', e.target.value)}
                  InputProps={{
                    endAdornment: <InputAdornment position="end">minutes</InputAdornment>,
                  }}
                  sx={{ width: 160, mr: 2 }}
                />
                <Typography variant="caption" color="text.secondary">
                  Duration before an unpaid order is automatically cancelled.
                </Typography>
              </Box>
            </Grid>

            {/* 2. Auto-Confirm Receipt */}
            <Grid size={{ xs: 12 }}>
              <Box sx={{ display: 'flex', alignItems: 'center' }}>
                <Typography sx={{ width: 280, fontWeight: 500 }}>Auto-Confirm Receipt:</Typography>
                <TextField
                  size="small"
                  type="number"
                  value={setting.autoConfirmReceiptDays || 0}
                  onChange={(e) => handleChange('autoConfirmReceiptDays', e.target.value)}
                  InputProps={{
                    endAdornment: <InputAdornment position="end">days</InputAdornment>,
                  }}
                  sx={{ width: 160, mr: 2 }}
                />
                <Typography variant="caption" color="text.secondary">
                  Days after shipment before order receipt is automatically confirmed.
                </Typography>
              </Box>
            </Grid>

            {/* 3. Return Request Window */}
            <Grid size={{ xs: 12 }}>
              <Box sx={{ display: 'flex', alignItems: 'center' }}>
                <Typography sx={{ width: 280, fontWeight: 500 }}>Return Request Window:</Typography>
                <TextField
                  size="small"
                  type="number"
                  value={setting.autoCompleteDays || 0}
                  onChange={(e) => handleChange('autoCompleteDays', e.target.value)}
                  InputProps={{
                    endAdornment: <InputAdornment position="end">days</InputAdornment>,
                  }}
                  sx={{ width: 160, mr: 2 }}
                />
                <Typography variant="caption" color="text.secondary">
                  Days after delivery confirmation during which customers can request returns/refunds.
                </Typography>
              </Box>
            </Grid>

            {/* 4. Review Window */}
            <Grid size={{ xs: 12 }}>
              <Box sx={{ display: 'flex', alignItems: 'center' }}>
                <Typography sx={{ width: 280, fontWeight: 500 }}>Review Window:</Typography>
                <TextField
                  size="small"
                  type="number"
                  value={setting.reviewWindowDays || 0}
                  onChange={(e) => handleChange('reviewWindowDays', e.target.value)}
                  InputProps={{
                    endAdornment: <InputAdornment position="end">days</InputAdornment>,
                  }}
                  sx={{ width: 160, mr: 2 }}
                />
                <Typography variant="caption" color="text.secondary">
                  Days after confirmation until customer review submission is closed.
                </Typography>
              </Box>
            </Grid>
          </Grid>
        )}

        <Box sx={{ mt: 5, display: 'flex', justifyContent: 'center' }}>
          <Button
            variant="contained"
            color="primary"
            size="large"
            onClick={handleSave}
            disabled={saving || !setting}
            sx={{ width: 200 }}
          >
            {saving ? 'Saving...' : 'Save Settings'}
          </Button>
        </Box>
      </Card>

      {/* 2. Warehouse & Return Addresses (ordering.merchant_address) */}
      <Card sx={{ p: 4, width: '100%', maxWidth: 900 }}>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
          <Box>
            <Typography variant="h5">Fulfillment & Return Warehouses</Typography>
            <Typography variant="body2" color="text.secondary">
              Manage physical distribution centers and return receipt addresses for shipping and reverse logistics.
            </Typography>
          </Box>
          <Button variant="contained" startIcon={<AddIcon />} onClick={handleOpenAddAddress}>
            Add Warehouse
          </Button>
        </Box>

        <TableContainer component={Paper} variant="outlined" sx={{ mt: 2 }}>
          <Table size="small">
            <TableHead>
              <TableRow sx={{ bgcolor: 'grey.50' }}>
                <TableCell>Warehouse Name</TableCell>
                <TableCell>Contact Person</TableCell>
                <TableCell>Location</TableCell>
                <TableCell>Fulfillment Role</TableCell>
                <TableCell align="right">Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {addresses.map((addr: any) => {
                const isShip = Boolean(addr.sendStatus === 1 || addr.isDefaultShipFrom);
                const isReturn = Boolean(addr.receiveStatus === 1 || addr.isDefaultReturnTo);
                return (
                  <TableRow key={addr.id}>
                    <TableCell sx={{ fontWeight: 600 }}>{addr.label || addr.addressName}</TableCell>
                    <TableCell>
                      {addr.contactName || addr.name}
                      <Typography variant="caption" color="text.secondary" sx={{ display: 'block' }}>
                        {addr.contactPhone || addr.phone}
                      </Typography>
                    </TableCell>
                    <TableCell>
                      {addr.addressLine || addr.detailAddress}, {addr.city}, {addr.state}
                    </TableCell>
                    <TableCell>
                      {isShip && <Chip label="Default Ship-From" color="primary" size="small" sx={{ mr: 0.5, mb: 0.5 }} />}
                      {isReturn && <Chip label="Default Return-To" color="secondary" size="small" />}
                      {!isShip && !isReturn && <Typography variant="caption" color="text.secondary">Standard Hub</Typography>}
                    </TableCell>
                    <TableCell align="right">
                      <IconButton size="small" color="primary" onClick={() => handleOpenEditAddress(addr)}>
                        <EditIcon fontSize="small" />
                      </IconButton>
                      <IconButton size="small" color="error" onClick={() => handleDeleteAddress(addr.id)}>
                        <DeleteIcon fontSize="small" />
                      </IconButton>
                    </TableCell>
                  </TableRow>
                );
              })}
              {addresses.length === 0 && !addressLoading && (
                <TableRow>
                  <TableCell colSpan={5} align="center" sx={{ py: 3, color: 'text.secondary' }}>
                    No warehouse addresses configured.
                  </TableCell>
                </TableRow>
              )}
            </TableBody>
          </Table>
        </TableContainer>
      </Card>

      {/* Warehouse Address Dialog */}
      <Dialog open={openAddressDialog} onClose={() => setOpenAddressDialog(false)} maxWidth="sm" fullWidth>
        <DialogTitle>{editingAddressId ? 'Edit Warehouse Address' : 'Add Warehouse Address'}</DialogTitle>
        <DialogContent>
          <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, mt: 1 }}>
            <TextField
              label="Warehouse Label / Name"
              fullWidth
              size="small"
              value={addressForm.label}
              onChange={(e) => setAddressForm({ ...addressForm, label: e.target.value })}
              placeholder="e.g. Main Warehouse Bengaluru"
            />
            <Grid container spacing={2}>
              <Grid size={{ xs: 6 }}>
                <TextField
                  label="Contact Name"
                  fullWidth
                  size="small"
                  value={addressForm.contactName}
                  onChange={(e) => setAddressForm({ ...addressForm, contactName: e.target.value })}
                />
              </Grid>
              <Grid size={{ xs: 6 }}>
                <TextField
                  label="Contact Phone"
                  fullWidth
                  size="small"
                  value={addressForm.contactPhone}
                  onChange={(e) => setAddressForm({ ...addressForm, contactPhone: e.target.value })}
                />
              </Grid>
              <Grid size={{ xs: 6 }}>
                <TextField
                  label="City"
                  fullWidth
                  size="small"
                  value={addressForm.city}
                  onChange={(e) => setAddressForm({ ...addressForm, city: e.target.value })}
                />
              </Grid>
              <Grid size={{ xs: 6 }}>
                <TextField
                  label="State / Province"
                  fullWidth
                  size="small"
                  value={addressForm.state}
                  onChange={(e) => setAddressForm({ ...addressForm, state: e.target.value })}
                />
              </Grid>
            </Grid>
            <TextField
              label="Street / Detailed Address Line"
              fullWidth
              size="small"
              multiline
              rows={2}
              value={addressForm.addressLine}
              onChange={(e) => setAddressForm({ ...addressForm, addressLine: e.target.value })}
            />
            <Box sx={{ display: 'flex', gap: 2 }}>
              <FormControlLabel
                control={
                  <Checkbox
                    checked={addressForm.isDefaultShipFrom}
                    onChange={(e) => setAddressForm({ ...addressForm, isDefaultShipFrom: e.target.checked })}
                  />
                }
                label="Default Ship-From Hub"
              />
              <FormControlLabel
                control={
                  <Checkbox
                    checked={addressForm.isDefaultReturnTo}
                    onChange={(e) => setAddressForm({ ...addressForm, isDefaultReturnTo: e.target.checked })}
                  />
                }
                label="Default Return-To Hub"
              />
            </Box>
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenAddressDialog(false)}>Cancel</Button>
          <Button variant="contained" onClick={handleSaveAddress}>
            {editingAddressId ? 'Update Address' : 'Create Address'}
          </Button>
        </DialogActions>
      </Dialog>

      <Snackbar
        open={snackbar.open}
        autoHideDuration={4000}
        onClose={() => setSnackbar({ ...snackbar, open: false })}
        anchorOrigin={{ vertical: 'top', horizontal: 'center' }}
      >
        <Alert severity={snackbar.severity} sx={{ width: '100%' }}>
          {snackbar.message}
        </Alert>
      </Snackbar>
    </Box>
  );
};

export default OrderSetting;

