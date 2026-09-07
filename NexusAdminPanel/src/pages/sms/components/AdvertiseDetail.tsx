import React, { useState, useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router';
import {
  Box,
  Card,
  CardContent,
  Typography,
  TextField,
  Button,
  FormControl,
  Select,
  MenuItem,
  Switch,
  Snackbar,
  Alert,
} from '@mui/material';
import { homeAdvertiseCreateAPI, getHomeAdvertiseByIdAPI, homeAdvertiseUpdateAPI } from '@/apis/homeAdvertise';
import type { SmsHomeAdvertise } from '@/types/homeAdvertist';

interface Props {
  isEdit?: boolean;
}

const AdvertiseDetail: React.FC<Props> = ({ isEdit = false }) => {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const id = searchParams.get('id');

  const [formData, setFormData] = useState<SmsHomeAdvertise>({
    name: '',
    type: 0,
    pic: '',
    startTime: '',
    endTime: '',
    status: 1,
    url: '',
    note: '',
    sort: 0,
  });

  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' as 'success' | 'error' });

  useEffect(() => {
    if (isEdit && id) {
      getHomeAdvertiseByIdAPI(Number(id)).then(res => {
        // format dates for datetime-local input
        const data = res.data;
        if (data.startTime) data.startTime = new Date(data.startTime).toISOString().slice(0, 16);
        if (data.endTime) data.endTime = new Date(data.endTime).toISOString().slice(0, 16);
        setFormData(data);
      }).catch(console.error);
    }
  }, [isEdit, id]);

  const handleSubmit = async () => {
    try {
      // Convert dates back to ISO or appropriate format if needed, backend usually accepts ISO string
      const payload = { 
        ...formData,
        startTime: formData.startTime ? new Date(formData.startTime).toISOString() : undefined,
        endTime: formData.endTime ? new Date(formData.endTime).toISOString() : undefined,
      };

      if (isEdit && id) {
        await homeAdvertiseUpdateAPI(Number(id), payload);
        setSnackbar({ open: true, message: 'Updated successfully', severity: 'success' });
      } else {
        await homeAdvertiseCreateAPI(payload);
        setSnackbar({ open: true, message: 'Created successfully', severity: 'success' });
      }
      setTimeout(() => navigate('/sms/advertise'), 1000);
    } catch {
      setSnackbar({ open: true, message: 'Operation failed', severity: 'error' });
    }
  };

  return (
    <Box sx={{ maxWidth: 800, mx: 'auto', mt: 4 }}>
      <Card>
        <CardContent sx={{ p: 4 }}>
          <Typography variant="h6" gutterBottom>{isEdit ? 'Edit Advertisement' : 'Add Advertisement'}</Typography>
          
          <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3, mt: 3 }}>
            <TextField
              label="Advertisement Name"
              value={formData.name || ''}
              onChange={e => setFormData({ ...formData, name: e.target.value })}
              fullWidth
              required
            />
            
            <FormControl fullWidth>
              <Typography variant="caption" color="text.secondary" gutterBottom>Type</Typography>
              <Select
                value={formData.type ?? 0}
                onChange={e => setFormData({ ...formData, type: Number(e.target.value) })}
              >
                <MenuItem value={0}>PC Banner</MenuItem>
                <MenuItem value={1}>App Banner</MenuItem>
              </Select>
            </FormControl>

            <TextField
              label="Image URL"
              value={formData.pic || ''}
              onChange={e => setFormData({ ...formData, pic: e.target.value })}
              fullWidth
              required
            />
            
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

            <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
              <Typography>Online Status</Typography>
              <Switch
                checked={formData.status === 1}
                onChange={e => setFormData({ ...formData, status: e.target.checked ? 1 : 0 })}
                color="primary"
              />
            </Box>

            <TextField
              label="Target URL"
              value={formData.url || ''}
              onChange={e => setFormData({ ...formData, url: e.target.value })}
              fullWidth
            />
            
            <TextField
              label="Sort Order"
              type="number"
              value={formData.sort || 0}
              onChange={e => setFormData({ ...formData, sort: Number(e.target.value) })}
              fullWidth
            />
            
            <TextField
              label="Notes"
              multiline
              rows={3}
              value={formData.note || ''}
              onChange={e => setFormData({ ...formData, note: e.target.value })}
              fullWidth
            />

            <Box sx={{ display: 'flex', justifyContent: 'center', gap: 2, mt: 2 }}>
              <Button variant="outlined" onClick={() => navigate('/sms/advertise')}>Cancel</Button>
              <Button variant="contained" onClick={handleSubmit}>Submit</Button>
            </Box>
          </Box>
        </CardContent>
      </Card>

      <Snackbar open={snackbar.open} autoHideDuration={3000} onClose={() => setSnackbar((s) => ({ ...s, open: false }))} anchorOrigin={{ vertical: 'top', horizontal: 'center' }}>
        <Alert severity={snackbar.severity} onClose={() => setSnackbar((s) => ({ ...s, open: false }))}>{snackbar.message}</Alert>
      </Snackbar>
    </Box>
  );
};

export default AdvertiseDetail;
