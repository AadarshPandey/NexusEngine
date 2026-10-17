import React, { useState, useEffect } from 'react';
import { useNavigate, useParams, useSearchParams } from 'react-router';
import {
  Box,
  Card,
  CardContent,
  Typography,
  TextField,
  Button,
  Switch,
  Snackbar,
  Alert,
} from '@mui/material';
import { homeAdvertiseCreateAPI, getHomeAdvertiseByIdAPI, homeAdvertiseUpdateAPI } from '@/apis/promotion';
import type { SmsHomeAdvertise } from '@/types/promotion';
import { resolveImageUrl } from '@/utils/media';

interface Props {
  isEdit?: boolean;
}

const AdvertiseDetail: React.FC<Props> = ({ isEdit = false }) => {
  const navigate = useNavigate();
  const { id: paramId } = useParams<{ id: string }>();
  const [searchParams] = useSearchParams();
  const id = paramId || searchParams.get('id');

  const [formData, setFormData] = useState<SmsHomeAdvertise>({
    name: '',
    type: 0,
    primaryImageUrl: '',
    startsAt: '',
    endsAt: '',
    status: 1,
    url: '',
    note: '',
    displayOrder: 0,
  });

  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' as 'success' | 'error' });

  useEffect(() => {
    if (isEdit && id) {
      getHomeAdvertiseByIdAPI(Number(id)).then(data => {
        if (!data) return;
        const clone = { ...data };
        if (!clone.primaryImageUrl && (clone.pic || clone.imageUrl)) {
          clone.primaryImageUrl = clone.pic || clone.imageUrl;
        }
        if (clone.startsAt) clone.startsAt = new Date(clone.startsAt).toISOString().slice(0, 16);
        if (clone.endsAt) clone.endsAt = new Date(clone.endsAt).toISOString().slice(0, 16);
        setFormData(clone);
      }).catch(console.error);
    }
  }, [isEdit, id]);

  const handleSubmit = async () => {
    try {
      const payload = { 
        ...formData,
        status: Boolean(formData.status),
        startsAt: formData.startsAt ? new Date(formData.startsAt).toISOString() : undefined,
        endsAt: formData.endsAt ? new Date(formData.endsAt).toISOString() : undefined,
      };

      if (isEdit && id) {
        await homeAdvertiseUpdateAPI(Number(id), payload);
        setSnackbar({ open: true, message: 'Updated successfully', severity: 'success' });
      } else {
        await homeAdvertiseCreateAPI(payload);
        setSnackbar({ open: true, message: 'Created successfully', severity: 'success' });
      }
      setTimeout(() => navigate('/promotion/banners'), 1000);
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

            <TextField
              label="Image URL"
              value={formData.primaryImageUrl || ''}
              onChange={e => setFormData({ ...formData, primaryImageUrl: e.target.value })}
              fullWidth
              required
            />
            {formData.primaryImageUrl && (
              <Box sx={{ mt: 1 }}>
                <img src={resolveImageUrl(formData.primaryImageUrl)} alt="Preview" style={{ height: 100, objectFit: 'contain' }} />
              </Box>
            )}
            
            <TextField
              label="Start Time"
              type="datetime-local"
              InputLabelProps={{ shrink: true }}
              value={formData.startsAt || ''}
              onChange={e => setFormData({ ...formData, startsAt: e.target.value })}
              fullWidth
            />
            
            <TextField
              label="End Time"
              type="datetime-local"
              InputLabelProps={{ shrink: true }}
              value={formData.endsAt || ''}
              onChange={e => setFormData({ ...formData, endsAt: e.target.value })}
              fullWidth
            />

            <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
              <Typography>Online Status</Typography>
              <Switch
                checked={Boolean(formData.status)}
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
              value={formData.displayOrder || 0}
              onChange={e => setFormData({ ...formData, displayOrder: Number(e.target.value) })}
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
              <Button variant="outlined" onClick={() => navigate('/promotion/banners')}>Cancel</Button>
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
