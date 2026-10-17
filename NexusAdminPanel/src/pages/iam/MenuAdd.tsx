import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router';
import {
  Box,
  Card,
  CardContent,
  Typography,
  TextField,
  Button,
  Switch,
  FormControlLabel,
  MenuItem,
  Snackbar,
  Alert,
} from '@mui/material';
import { getMenuListByParentIdAPI, menuCreateAPI } from '@/apis/iam';
import type { ConsoleMenu } from '@/types/iam';

const MenuAdd: React.FC = () => {
  const navigate = useNavigate();
  const [rootMenus, setRootMenus] = useState<ConsoleMenu[]>([]);
  const [formData, setFormData] = useState<ConsoleMenu>({
    parentId: 0,
    title: '',
    name: '',
    icon: '',
    sort: 0,
    hidden: false,
  });
  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' as 'success' | 'error' });

  useEffect(() => {
    getMenuListByParentIdAPI(0, { pageNum: 1, pageSize: 100 })
      .then((res) => setRootMenus(res.content || []))
      .catch(console.error);
  }, []);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!formData.title) return;
    try {
      await menuCreateAPI(formData);
      setSnackbar({ open: true, message: 'Menu created successfully', severity: 'success' });
      setTimeout(() => navigate('/iam/menus'), 1000);
    } catch {
      setSnackbar({ open: true, message: 'Failed to create menu', severity: 'error' });
    }
  };

  return (
    <Box sx={{ maxWidth: 700, mx: 'auto', mt: 3 }}>
      <Card>
        <CardContent sx={{ p: 4 }}>
          <Typography variant="h6" sx={{ fontWeight: 700, mb: 3 }}>
            Add Menu
          </Typography>
          <Box component="form" onSubmit={handleSubmit} sx={{ display: 'flex', flexDirection: 'column', gap: 2.5 }}>
            <TextField
              label="Menu Title"
              required
              fullWidth
              value={formData.title}
              onChange={(e) => setFormData({ ...formData, title: e.target.value })}
            />
            <TextField
              select
              label="Parent Menu"
              fullWidth
              value={formData.parentId ?? 0}
              onChange={(e) => setFormData({ ...formData, parentId: Number(e.target.value) })}
            >
              <MenuItem value={0}>No Parent (Root Menu)</MenuItem>
              {rootMenus.map((m) => (
                <MenuItem key={m.id} value={m.id}>
                  {m.title}
                </MenuItem>
              ))}
            </TextField>
            <TextField
              label="Frontend Name"
              fullWidth
              value={formData.name || ''}
              onChange={(e) => setFormData({ ...formData, name: e.target.value })}
            />
            <TextField
              label="Icon Name"
              fullWidth
              value={formData.icon || ''}
              onChange={(e) => setFormData({ ...formData, icon: e.target.value })}
            />
            <TextField
              label="Sort Order"
              type="number"
              fullWidth
              value={formData.sort ?? 0}
              onChange={(e) => setFormData({ ...formData, sort: Number(e.target.value) })}
            />
            <FormControlLabel
              control={
                <Switch
                  checked={!Boolean(formData.hidden)}
                  onChange={(e) => setFormData({ ...formData, hidden: !e.target.checked })}
                />
              }
              label="Visible in Sidebar"
            />
            <Box sx={{ display: 'flex', gap: 2, justifyContent: 'flex-end', mt: 2 }}>
              <Button variant="outlined" onClick={() => navigate('/iam/menus')}>
                Cancel
              </Button>
              <Button type="submit" variant="contained">
                Create Menu
              </Button>
            </Box>
          </Box>
        </CardContent>
      </Card>
      <Snackbar open={snackbar.open} autoHideDuration={3000} onClose={() => setSnackbar((s) => ({ ...s, open: false }))}>
        <Alert severity={snackbar.severity}>{snackbar.message}</Alert>
      </Snackbar>
    </Box>
  );
};

export default MenuAdd;
