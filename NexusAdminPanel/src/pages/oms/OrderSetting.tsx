import React, { useEffect, useState } from 'react';
import { Box, Card, Typography, TextField, Button, InputAdornment, Grid, CircularProgress, Alert, Snackbar } from '@mui/material';
import { getOrderSettingByIdAPI, orderSettingUpdateByIdAPI } from '@/apis/orderSetting';
import type { OmsOrderSetting } from '@/types/orderSetting';

const OrderSetting: React.FC = () => {
  const [setting, setSetting] = useState<OmsOrderSetting | null>(null);
  const [loading, setLoading] = useState(false);
  const [saving, setSaving] = useState(false);
  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' as 'success' | 'error' });

  // Typically order settings are a single global record with ID 1
  const SETTING_ID = 1;

  useEffect(() => {
    fetchSettings();
  }, []);

  const fetchSettings = async () => {
    setLoading(true);
    try {
      const res = await getOrderSettingByIdAPI(SETTING_ID);
      if (res.data) {
        setSetting(res.data);
      }
    } catch (e) {
      console.error(e);
      setSnackbar({ open: true, message: 'Failed to fetch order settings', severity: 'error' });
    } finally {
      setLoading(false);
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

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', p: 10 }}>
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Box sx={{ p: 3, display: 'flex', justifyContent: 'center' }}>
      <Card sx={{ p: 4, width: '100%', maxWidth: 800 }}>
        <Typography variant="h5" sx={{ mb: 1 }}>Order Settings</Typography>
        <Typography variant="body2" color="text.secondary" sx={{ mb: 4 }}>
          Configure the automated timeouts and lifecycle rules for all orders in the system.
        </Typography>

        {setting && (
          <Grid container spacing={4}>
            {/* 1. Normal Order Overtime */}
            <Grid item xs={12}>
              <Box sx={{ display: 'flex', alignItems: 'center' }}>
                <Typography sx={{ width: 300, fontWeight: 500 }}>Normal Order Timeout:</Typography>
                <TextField
                  size="small"
                  type="number"
                  value={setting.normalOrderOvertime || 0}
                  onChange={(e) => handleChange('normalOrderOvertime', e.target.value)}
                  InputProps={{
                    endAdornment: <InputAdornment position="end">minutes</InputAdornment>,
                  }}
                  sx={{ width: 150, mr: 2 }}
                />
                <Typography variant="caption" color="text.secondary">
                  Time until an unpaid standard order is auto-cancelled.
                </Typography>
              </Box>
            </Grid>

            {/* 2. Flash Order Overtime */}
            <Grid item xs={12}>
              <Box sx={{ display: 'flex', alignItems: 'center' }}>
                <Typography sx={{ width: 300, fontWeight: 500 }}>Flash Sale Order Timeout:</Typography>
                <TextField
                  size="small"
                  type="number"
                  value={setting.flashOrderOvertime || 0}
                  onChange={(e) => handleChange('flashOrderOvertime', e.target.value)}
                  InputProps={{
                    endAdornment: <InputAdornment position="end">minutes</InputAdornment>,
                  }}
                  sx={{ width: 150, mr: 2 }}
                />
                <Typography variant="caption" color="text.secondary">
                  Time until an unpaid flash sale order is auto-cancelled.
                </Typography>
              </Box>
            </Grid>

            {/* 3. Confirm Overtime */}
            <Grid item xs={12}>
              <Box sx={{ display: 'flex', alignItems: 'center' }}>
                <Typography sx={{ width: 300, fontWeight: 500 }}>Auto-Confirm Receipt:</Typography>
                <TextField
                  size="small"
                  type="number"
                  value={setting.confirmOvertime || 0}
                  onChange={(e) => handleChange('confirmOvertime', e.target.value)}
                  InputProps={{
                    endAdornment: <InputAdornment position="end">days</InputAdornment>,
                  }}
                  sx={{ width: 150, mr: 2 }}
                />
                <Typography variant="caption" color="text.secondary">
                  Days after shipping before order is automatically confirmed as received.
                </Typography>
              </Box>
            </Grid>

            {/* 4. Finish Overtime */}
            <Grid item xs={12}>
              <Box sx={{ display: 'flex', alignItems: 'center' }}>
                <Typography sx={{ width: 300, fontWeight: 500 }}>Return Request Window:</Typography>
                <TextField
                  size="small"
                  type="number"
                  value={setting.finishOvertime || 0}
                  onChange={(e) => handleChange('finishOvertime', e.target.value)}
                  InputProps={{
                    endAdornment: <InputAdornment position="end">days</InputAdornment>,
                  }}
                  sx={{ width: 150, mr: 2 }}
                />
                <Typography variant="caption" color="text.secondary">
                  Days after confirmation during which a user can request a return.
                </Typography>
              </Box>
            </Grid>

            {/* 5. Comment Overtime */}
            <Grid item xs={12}>
              <Box sx={{ display: 'flex', alignItems: 'center' }}>
                <Typography sx={{ width: 300, fontWeight: 500 }}>Auto-Comment Window:</Typography>
                <TextField
                  size="small"
                  type="number"
                  value={setting.commentOvertime || 0}
                  onChange={(e) => handleChange('commentOvertime', e.target.value)}
                  InputProps={{
                    endAdornment: <InputAdornment position="end">days</InputAdornment>,
                  }}
                  sx={{ width: 150, mr: 2 }}
                />
                <Typography variant="caption" color="text.secondary">
                  Days after confirmation until order is locked for reviews.
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
