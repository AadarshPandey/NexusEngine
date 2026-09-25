import React, { useState, useEffect, useCallback } from 'react';
import {
  Box,
  Card,
  CardContent,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Button,
  Typography,
  IconButton,
  Snackbar,
  Alert,
  TablePagination,
  TextField,
  MenuItem,
  Grid,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
} from '@mui/material';
import { Add, Delete, Edit, Search } from '@mui/icons-material';
import { getResourceListAPI, resourceDeleteByIdAPI, resourceCreateAPI, resourceUpdateAPI } from '@/apis/resource';
import { resourceCategoryListAllAPI } from '@/apis/resourceCategory';
import type { UmsResource, UmsResourceCategory } from '@/types/resource';

const ResourceList: React.FC = () => {
  const [list, setList] = useState<UmsResource[]>([]);
  const [categories, setCategories] = useState<UmsResourceCategory[]>([]);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);
  const [pageNum, setPageNum] = useState(0);
  const [pageSize, setPageSize] = useState(10);
  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' as 'success' | 'error' | 'warning' });

  // Filters
  const [searchName, setSearchName] = useState('');
  const [searchUrl, setSearchUrl] = useState('');
  const [searchCategoryId, setSearchCategoryId] = useState<number | ''>('');

  // Form Dialog
  const [dialogOpen, setDialogOpen] = useState(false);
  const [isEdit, setIsEdit] = useState(false);
  const [formData, setFormData] = useState<UmsResource>({ name: '', url: '', categoryId: 0, description: '' });

  const fetchCategories = async () => {
    try {
      const res = await resourceCategoryListAllAPI();
      // @ts-ignore
      setCategories(res.data);
    } catch (e) {
      console.error(e);
    }
  };

  const fetchList = useCallback(async (
    overridePageNum?: number,
    overrideSearchName?: string,
    overrideSearchUrl?: string,
    overrideCategoryId?: number | ''
  ) => {
    setLoading(true);
    
    const finalPageNum = overridePageNum !== undefined ? overridePageNum : pageNum;
    const finalName = overrideSearchName !== undefined ? overrideSearchName : searchName;
    const finalUrl = overrideSearchUrl !== undefined ? overrideSearchUrl : searchUrl;
    const finalCat = overrideCategoryId !== undefined ? overrideCategoryId : searchCategoryId;

    try {
      const res = await getResourceListAPI({ 
        nameKeyword: finalName || undefined,
        urlKeyword: finalUrl || undefined,
        categoryId: finalCat === '' ? undefined : finalCat,
        pageNum: finalPageNum + 1, 
        pageSize 
      });
      setList(res.data.list);
      setTotal(res.data.total);
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  }, [searchName, searchUrl, searchCategoryId, pageNum, pageSize]);

  useEffect(() => {
    fetchCategories();
  }, []);

  useEffect(() => {
    fetchList();
  }, [fetchList]);

  const handleSearch = () => {
    setPageNum(0);
    fetchList(0);
  };

  const handleClearSearch = () => {
    setSearchName('');
    setSearchUrl('');
    setSearchCategoryId('');
    setPageNum(0);
    fetchList(0, '', '', '');
  };

  const handleDelete = async (id: number) => {
    if (!window.confirm('Are you sure you want to delete this resource?')) return;
    try {
      await resourceDeleteByIdAPI(id);
      setSnackbar({ open: true, message: 'Deleted successfully', severity: 'success' });
      fetchList();
    } catch {
      setSnackbar({ open: true, message: 'Delete failed', severity: 'error' });
    }
  };

  const openAddDialog = () => {
    setIsEdit(false);
    setFormData({ name: '', url: '', categoryId: categories.length > 0 ? categories[0].id! : 0, description: '' });
    setDialogOpen(true);
  };

  const openEditDialog = (row: UmsResource) => {
    setIsEdit(true);
    setFormData({ ...row });
    setDialogOpen(true);
  };

  const handleFormSubmit = async () => {
    if (!formData.name || !formData.url || !formData.categoryId) {
      setSnackbar({ open: true, message: 'Name, URL, and Category are required', severity: 'warning' });
      return;
    }
    try {
      if (isEdit) {
        await resourceUpdateAPI(formData.id!, formData);
        setSnackbar({ open: true, message: 'Resource updated successfully', severity: 'success' });
      } else {
        await resourceCreateAPI(formData);
        setSnackbar({ open: true, message: 'Resource created successfully', severity: 'success' });
      }
      setDialogOpen(false);
      fetchList();
    } catch {
      setSnackbar({ open: true, message: 'Operation failed', severity: 'error' });
    }
  };

  const formatTime = (timeStr?: string) => {
    if (!timeStr) return 'N/A';
    return new Date(timeStr).toLocaleString();
  };

  const getCategoryName = (id: number) => {
    return categories.find(c => c.id === id)?.name || 'Unknown';
  };

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 3 }}>
        <Typography variant="h5" sx={{ fontWeight: 'bold', color: '#1E293B' }}>
          Backend API Resources
        </Typography>
        <Button
          variant="contained"
          startIcon={<Add />}
          onClick={openAddDialog}
          sx={{ bgcolor: '#6366F1', '&:hover': { bgcolor: '#4F46E5' }, textTransform: 'none' }}
        >
          Add Resource
        </Button>
      </Box>

      {/* Filter Card */}
      <Card sx={{ mb: 3, boxShadow: '0px 4px 20px rgba(0, 0, 0, 0.05)', borderRadius: 2 }}>
        <CardContent>
          <Grid container spacing={2} alignItems="center">
            <Grid size={{ xs: 12, sm: 3 }}>
              <TextField fullWidth label="Resource Name" size="small" value={searchName} onChange={e => setSearchName(e.target.value)} />
            </Grid>
            <Grid size={{ xs: 12, sm: 3 }}>
              <TextField fullWidth label="Resource URL" size="small" value={searchUrl} onChange={e => setSearchUrl(e.target.value)} />
            </Grid>
            <Grid size={{ xs: 12, sm: 3 }}>
              <TextField
                select
                fullWidth
                label="Category"
                size="small"
                value={searchCategoryId}
                onChange={e => setSearchCategoryId(e.target.value === '' ? '' : Number(e.target.value))}
              >
                <MenuItem value="">All Categories</MenuItem>
                {categories.map((c) => (
                  <MenuItem key={c.id} value={c.id}>{c.name}</MenuItem>
                ))}
              </TextField>
            </Grid>
            <Grid size={{ xs: 12, sm: 3 }} sx={{ display: 'flex', gap: 1 }}>
              <Button variant="contained" onClick={handleSearch} sx={{ textTransform: 'none' }} startIcon={<Search />}>Search</Button>
              <Button variant="outlined" onClick={handleClearSearch} sx={{ textTransform: 'none' }}>Clear</Button>
            </Grid>
          </Grid>
        </CardContent>
      </Card>

      {/* Data Table */}
      <Card sx={{ boxShadow: '0px 4px 20px rgba(0, 0, 0, 0.05)', borderRadius: 2 }}>
        <CardContent sx={{ p: 0 }}>
          <TableContainer component={Paper} elevation={0}>
            <Table>
              <TableHead sx={{ bgcolor: 'action.hover' }}>
                <TableRow>
                  <TableCell>ID</TableCell>
                  <TableCell>Resource Name</TableCell>
                  <TableCell>API URL</TableCell>
                  <TableCell>Description</TableCell>
                  <TableCell>Category</TableCell>
                  <TableCell>Added Date</TableCell>
                  <TableCell align="center">Actions</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {list.map((row) => (
                  <TableRow key={row.id} hover>
                    <TableCell>{row.id}</TableCell>
                    <TableCell sx={{ fontWeight: 500 }}>{row.name}</TableCell>
                    <TableCell sx={{ color: 'text.secondary', fontFamily: 'monospace' }}>{row.url}</TableCell>
                    <TableCell>{row.description || '-'}</TableCell>
                    <TableCell>{getCategoryName(row.categoryId)}</TableCell>
                    <TableCell>{formatTime(row.createTime)}</TableCell>
                    <TableCell align="center">
                      <IconButton size="small" onClick={() => openEditDialog(row)} sx={{ color: '#6366F1' }}>
                        <Edit fontSize="small" />
                      </IconButton>
                      <IconButton size="small" onClick={() => handleDelete(row.id!)} sx={{ color: '#EF4444' }}>
                        <Delete fontSize="small" />
                      </IconButton>
                    </TableCell>
                  </TableRow>
                ))}
                {list.length === 0 && !loading && (
                  <TableRow>
                    <TableCell colSpan={7} align="center" sx={{ py: 4, color: 'text.secondary' }}>
                      No resources found.
                    </TableCell>
                  </TableRow>
                )}
              </TableBody>
            </Table>
          </TableContainer>
          <TablePagination
            component="div"
            count={total}
            page={pageNum}
            onPageChange={(e, v) => setPageNum(v)}
            rowsPerPage={pageSize}
            onRowsPerPageChange={(e) => { setPageSize(parseInt(e.target.value, 10)); setPageNum(0); }}
            rowsPerPageOptions={[5, 10, 20]}
          />
        </CardContent>
      </Card>

      {/* Form Dialog */}
      <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)} maxWidth="sm" fullWidth>
        <DialogTitle>{isEdit ? 'Edit Resource' : 'Add Resource'}</DialogTitle>
        <DialogContent dividers sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
          <TextField
            label="Resource Name"
            fullWidth
            required
            value={formData.name}
            onChange={e => setFormData({ ...formData, name: e.target.value })}
          />
          <TextField
            label="Resource URL"
            fullWidth
            required
            placeholder="/api/example/**"
            value={formData.url}
            onChange={e => setFormData({ ...formData, url: e.target.value })}
          />
          <TextField
            select
            label="Category"
            fullWidth
            required
            value={formData.categoryId}
            onChange={e => setFormData({ ...formData, categoryId: Number(e.target.value) })}
          >
            {categories.map((c) => (
              <MenuItem key={c.id} value={c.id}>{c.name}</MenuItem>
            ))}
          </TextField>
          <TextField
            label="Description"
            fullWidth
            multiline
            rows={2}
            value={formData.description || ''}
            onChange={e => setFormData({ ...formData, description: e.target.value })}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDialogOpen(false)} sx={{ textTransform: 'none' }}>Cancel</Button>
          <Button variant="contained" onClick={handleFormSubmit} sx={{ textTransform: 'none' }}>Submit</Button>
        </DialogActions>
      </Dialog>

      <Snackbar open={snackbar.open} autoHideDuration={3000} onClose={() => setSnackbar(s => ({ ...s, open: false }))} anchorOrigin={{ vertical: 'top', horizontal: 'center' }}>
        <Alert severity={snackbar.severity} onClose={() => setSnackbar(s => ({ ...s, open: false }))}>{snackbar.message}</Alert>
      </Snackbar>
    </Box>
  );
};

export default ResourceList;
