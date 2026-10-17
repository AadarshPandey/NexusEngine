import React, { useEffect, useState } from 'react';
import {
  Box,
  Card,
  CardContent,
  Typography,
  Button,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  IconButton,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Snackbar,
  Alert,
} from '@mui/material';
import { Add, Edit, Delete } from '@mui/icons-material';
import {
  resourceCategoryListAllAPI,
  resourceCategoryCreateAPI,
  resourceCategoryUpdateByIdAPI,
  resourceCategoryDeleteByIdAPI,
} from '@/apis/iam';
import type { PermissionGroup } from '@/types/iam';

const ResourceCategoryList: React.FC = () => {
  const [list, setList] = useState<PermissionGroup[]>([]);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [isEdit, setIsEdit] = useState(false);
  const [formData, setFormData] = useState<PermissionGroup>({ name: '', sort: 0 });
  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' as 'success' | 'error' });

  const fetchList = () => {
    resourceCategoryListAllAPI()
      .then((res) => setList(res || []))
      .catch(console.error);
  };

  useEffect(() => {
    fetchList();
  }, []);

  const handleOpenAdd = () => {
    setIsEdit(false);
    setFormData({ name: '', sort: 0 });
    setDialogOpen(true);
  };

  const handleOpenEdit = (row: PermissionGroup) => {
    setIsEdit(true);
    setFormData({ ...row, sort: row.sort ?? row.displayOrder ?? 0 });
    setDialogOpen(true);
  };

  const handleSubmit = async () => {
    if (!formData.name) return;
    try {
      if (isEdit) {
        await resourceCategoryUpdateByIdAPI(formData.id!, formData);
      } else {
        await resourceCategoryCreateAPI(formData);
      }
      setSnackbar({ open: true, message: 'Saved successfully', severity: 'success' });
      setDialogOpen(false);
      fetchList();
    } catch {
      setSnackbar({ open: true, message: 'Operation failed', severity: 'error' });
    }
  };

  const handleDelete = async (id: number) => {
    if (!window.confirm('Are you sure you want to delete this category?')) return;
    try {
      await resourceCategoryDeleteByIdAPI(id);
      setSnackbar({ open: true, message: 'Deleted successfully', severity: 'success' });
      fetchList();
    } catch {
      setSnackbar({ open: true, message: 'Delete failed', severity: 'error' });
    }
  };

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 3 }}>
        <Typography variant="h5" sx={{ fontWeight: 'bold' }}>
          Permission Groups
        </Typography>
        <Button variant="contained" startIcon={<Add />} onClick={handleOpenAdd}>
          Add Group
        </Button>
      </Box>
      <Card>
        <CardContent sx={{ p: 0 }}>
          <TableContainer>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>ID</TableCell>
                  <TableCell>Name</TableCell>
                  <TableCell>Created At</TableCell>
                  <TableCell>Sort</TableCell>
                  <TableCell align="center">Actions</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {list.map((row) => (
                  <TableRow key={row.id} hover>
                    <TableCell>{row.id}</TableCell>
                    <TableCell sx={{ fontWeight: 500 }}>{row.name}</TableCell>
                    <TableCell>{row.createTime ? new Date(row.createTime).toLocaleString() : '-'}</TableCell>
                    <TableCell>{row.sort ?? row.displayOrder ?? 0}</TableCell>
                    <TableCell align="center">
                      <IconButton size="small" onClick={() => handleOpenEdit(row)} sx={{ color: '#6366F1' }}>
                        <Edit fontSize="small" />
                      </IconButton>
                      <IconButton size="small" onClick={() => handleDelete(row.id!)} sx={{ color: '#EF4444' }}>
                        <Delete fontSize="small" />
                      </IconButton>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        </CardContent>
      </Card>

      <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)} maxWidth="xs" fullWidth>
        <DialogTitle>{isEdit ? 'Edit Group' : 'Add Group'}</DialogTitle>
        <DialogContent dividers sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
          <TextField
            label="Group Name"
            fullWidth
            required
            value={formData.name}
            onChange={(e) => setFormData({ ...formData, name: e.target.value })}
          />
          <TextField
            label="Sort Order"
            type="number"
            fullWidth
            value={formData.sort ?? 0}
            onChange={(e) => setFormData({ ...formData, sort: Number(e.target.value) })}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDialogOpen(false)}>Cancel</Button>
          <Button variant="contained" onClick={handleSubmit}>
            Save
          </Button>
        </DialogActions>
      </Dialog>

      <Snackbar open={snackbar.open} autoHideDuration={3000} onClose={() => setSnackbar((s) => ({ ...s, open: false }))}>
        <Alert severity={snackbar.severity}>{snackbar.message}</Alert>
      </Snackbar>
    </Box>
  );
};

export default ResourceCategoryList;
