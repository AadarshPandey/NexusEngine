import React, { useState, useEffect, useCallback } from 'react';
import { useNavigate } from 'react-router';
import {
  Box,
  Card,
  CardContent,
  Typography,
  Button,
  TextField,
  InputAdornment,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  TablePagination,
  Switch,
  IconButton,
  Snackbar,
  Alert,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Chip,
  Checkbox,
} from '@mui/material';
import {
  Search,
  Add,
  Delete,
  Edit,
  FilterList,
  ViewList,
  MenuOpen,
  Security,
} from '@mui/icons-material';
import { 
  getRoleListAPI, 
  roleCreateAPI, 
  roleUpdateByIdAPI, 
  roleUpdateStatusAPI, 
  roleDeleteByIdsAPI 
} from '@/apis/role';
import type { UmsRole } from '@/types/role';

const RoleList: React.FC = () => {
  const navigate = useNavigate();

  const [list, setList] = useState<UmsRole[]>([]);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(true);
  const [keyword, setKeyword] = useState('');
  const [pageNum, setPageNum] = useState(0);
  const [pageSize, setPageSize] = useState(10);
  const [selected, setSelected] = useState<number[]>([]);
  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' as 'success' | 'error' | 'warning' });

  // Add/Edit Dialog State
  const [formDialogOpen, setFormDialogOpen] = useState(false);
  const [isEdit, setIsEdit] = useState(false);
  const [formData, setFormData] = useState<UmsRole>({
    name: '',
    description: '',
    status: 1,
    sort: 0
  });

  const fetchList = useCallback(async () => {
    setLoading(true);
    try {
      const res = await getRoleListAPI({ 
        keyword: keyword || undefined, 
        pageNum: pageNum > 0 ? pageNum : 0, 
        pageSize 
      });
      setList(res.data.list);
      setTotal(res.data.total);
    } catch (error) {
      console.error('Failed to fetch role list:', error);
    } finally {
      setLoading(false);
    }
  }, [keyword, pageNum, pageSize]);

  useEffect(() => {
    fetchList();
  }, [fetchList]);

  const handleSearch = () => {
    setPageNum(0);
    fetchList();
  };

  const handleDelete = async (id: number) => {
    if (window.confirm('Are you sure you want to delete this role?')) {
      try {
        await roleDeleteByIdsAPI({ ids: String(id) });
        setSnackbar({ open: true, message: 'Deleted successfully', severity: 'success' });
        fetchList();
      } catch {
        setSnackbar({ open: true, message: 'Failed to delete', severity: 'error' });
      }
    }
  };

  const handleBatchDelete = async () => {
    if (selected.length === 0) {
      setSnackbar({ open: true, message: 'Please select at least one role', severity: 'warning' });
      return;
    }
    if (window.confirm('Are you sure you want to delete the selected roles?')) {
      try {
        await roleDeleteByIdsAPI({ ids: selected.join(',') });
        setSnackbar({ open: true, message: 'Batch deletion successful', severity: 'success' });
        setSelected([]);
        fetchList();
      } catch {
        setSnackbar({ open: true, message: 'Batch deletion failed', severity: 'error' });
      }
    }
  };

  const handleStatusChange = async (row: UmsRole) => {
    const newStatus = row.status === 1 ? 0 : 1;
    try {
      await roleUpdateStatusAPI(row.id!, { status: newStatus });
      setList((prev) => prev.map((item) => (item.id === row.id ? { ...item, status: newStatus } : item)));
      setSnackbar({ open: true, message: 'Status updated', severity: 'success' });
    } catch {
      setSnackbar({ open: true, message: 'Failed to update status', severity: 'error' });
    }
  };

  const handleSelectAll = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.checked) {
      setSelected(list.map((item) => item.id!));
    } else {
      setSelected([]);
    }
  };

  const handleSelectOne = (id: number) => {
    setSelected((prev) =>
      prev.includes(id) ? prev.filter((i) => i !== id) : [...prev, id],
    );
  };

  // Form handling
  const openAddDialog = () => {
    setIsEdit(false);
    setFormData({ name: '', description: '', status: 1, sort: 0 });
    setFormDialogOpen(true);
  };

  const openEditDialog = (row: UmsRole) => {
    setIsEdit(true);
    setFormData({ ...row });
    setFormDialogOpen(true);
  };

  const handleFormSubmit = async () => {
    if (!formData.name) {
      setSnackbar({ open: true, message: 'Role Name is required', severity: 'warning' });
      return;
    }
    try {
      if (isEdit) {
        await roleUpdateByIdAPI(formData.id!, formData);
        setSnackbar({ open: true, message: 'Role updated successfully', severity: 'success' });
      } else {
        await roleCreateAPI(formData);
        setSnackbar({ open: true, message: 'Role created successfully', severity: 'success' });
      }
      setFormDialogOpen(false);
      fetchList();
    } catch {
      setSnackbar({ open: true, message: 'Operation failed', severity: 'error' });
    }
  };

  const formatTime = (timeStr?: string) => {
    if (!timeStr) return 'N/A';
    return new Date(timeStr).toLocaleString();
  };

  return (
    <Box>
      <Card sx={{ mb: 2 }}>
        <CardContent sx={{ p: 2.5, '&:last-child': { pb: 2.5 } }}>
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 2 }}>
            <FilterList sx={{ fontSize: 18, color: '#6366F1' }} />
            <Typography sx={{ fontWeight: 600, fontSize: '0.875rem' }}>Filter & Search</Typography>
          </Box>
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, flexWrap: 'wrap' }}>
            <TextField
              size="small"
              placeholder="Role Name"
              value={keyword}
              onChange={(e) => setKeyword(e.target.value)}
              onKeyDown={(e) => e.key === 'Enter' && handleSearch()}
              sx={{ width: 250 }}
              InputProps={{
                startAdornment: (
                  <InputAdornment position="start">
                    <Search sx={{ fontSize: 18, color: 'text.secondary' }} />
                  </InputAdornment>
                ),
              }}
            />
            <Button variant="contained" onClick={handleSearch} size="small">
              Search
            </Button>
          </Box>
        </CardContent>
      </Card>

      <Card sx={{ mb: 2 }}>
        <CardContent sx={{ p: 2.5, '&:last-child': { pb: 2.5 }, display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
            <ViewList sx={{ fontSize: 18, color: '#6366F1' }} />
            <Typography sx={{ fontWeight: 600, fontSize: '0.875rem' }}>Data List</Typography>
            <Chip label={`${total} roles`} size="small" sx={{ ml: 1, height: 22, fontSize: '0.75rem' }} />
          </Box>
          <Button
            variant="contained"
            startIcon={<Add />}
            size="small"
            onClick={openAddDialog}
          >
            Add Role
          </Button>
        </CardContent>
      </Card>

      <Card>
        <TableContainer>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell padding="checkbox">
                  <Checkbox
                    indeterminate={selected.length > 0 && selected.length < list.length}
                    checked={list.length > 0 && selected.length === list.length}
                    onChange={handleSelectAll}
                    size="small"
                  />
                </TableCell>
                <TableCell>ID</TableCell>
                <TableCell>Role Name</TableCell>
                <TableCell>Description</TableCell>
                <TableCell>Admin Count</TableCell>
                <TableCell>Added Date</TableCell>
                <TableCell align="center">Enabled</TableCell>
                <TableCell align="center" width={180}>Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {loading ? (
                <TableRow>
                  <TableCell colSpan={8} sx={{ textAlign: 'center', py: 6 }}>
                    <Typography sx={{ color: 'text.secondary' }}>Loading...</Typography>
                  </TableCell>
                </TableRow>
              ) : list.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={8} sx={{ textAlign: 'center', py: 6 }}>
                    <Typography sx={{ color: 'text.secondary' }}>No roles found</Typography>
                  </TableCell>
                </TableRow>
              ) : (
                list.map((row) => (
                  <TableRow key={row.id} hover>
                    <TableCell padding="checkbox">
                      <Checkbox
                        checked={selected.includes(row.id!)}
                        onChange={() => handleSelectOne(row.id!)}
                        size="small"
                        disabled={row.name === 'SuperAdmin'} // Prevent deleting super admin role
                      />
                    </TableCell>
                    <TableCell>{row.id}</TableCell>
                    <TableCell sx={{ fontWeight: 500 }}>{row.name}</TableCell>
                    <TableCell>{row.description}</TableCell>
                    <TableCell>{row.adminCount}</TableCell>
                    <TableCell>{formatTime(row.createTime)}</TableCell>
                    <TableCell align="center">
                      <Switch
                        checked={row.status === 1}
                        onChange={() => handleStatusChange(row)}
                        size="small"
                        color="primary"
                        disabled={row.name === 'SuperAdmin'}
                      />
                    </TableCell>
                    <TableCell align="center">
                      <Box sx={{ display: 'flex', justifyContent: 'center', gap: 0.5 }}>
                        <IconButton
                          size="small"
                          onClick={() => navigate(`/ums/allocMenu?roleId=${row.id}`)}
                          sx={{ color: '#10B981' }}
                          title="Allocate Menus"
                        >
                          <MenuOpen sx={{ fontSize: 18 }} />
                        </IconButton>
                        <IconButton
                          size="small"
                          onClick={() => navigate(`/ums/allocResource?roleId=${row.id}`)}
                          sx={{ color: '#F59E0B' }}
                          title="Allocate Resources"
                        >
                          <Security sx={{ fontSize: 18 }} />
                        </IconButton>
                        <IconButton
                          size="small"
                          onClick={() => openEditDialog(row)}
                          sx={{ color: '#6366F1' }}
                          title="Edit"
                        >
                          <Edit sx={{ fontSize: 18 }} />
                        </IconButton>
                        <IconButton
                          size="small"
                          onClick={() => handleDelete(row.id!)}
                          sx={{ color: '#EF4444' }}
                          disabled={row.name === 'SuperAdmin'}
                          title="Delete"
                        >
                          <Delete sx={{ fontSize: 18 }} />
                        </IconButton>
                      </Box>
                    </TableCell>
                  </TableRow>
                ))
              )}
            </TableBody>
          </Table>
        </TableContainer>
        <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', px: 2, py: 1 }}>
          <Button variant="outlined" color="error" size="small" onClick={handleBatchDelete} disabled={selected.length === 0}>
            Batch Delete
          </Button>
          <TablePagination
            component="div"
            count={total}
            page={pageNum}
            onPageChange={(_, p) => setPageNum(p)}
            rowsPerPage={pageSize}
            onRowsPerPageChange={(e) => { setPageSize(parseInt(e.target.value, 10)); setPageNum(0); }}
            rowsPerPageOptions={[5, 10, 15]}
          />
        </Box>
      </Card>

      {/* Form Dialog */}
      <Dialog open={formDialogOpen} onClose={() => setFormDialogOpen(false)} maxWidth="sm" fullWidth>
        <DialogTitle>{isEdit ? 'Edit Role' : 'Add Role'}</DialogTitle>
        <DialogContent dividers>
          <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, pt: 1 }}>
            <TextField
              label="Role Name"
              value={formData.name}
              onChange={e => setFormData({ ...formData, name: e.target.value })}
              fullWidth
              required
              disabled={isEdit && formData.name === 'SuperAdmin'}
            />
            <TextField
              label="Description"
              multiline
              rows={3}
              value={formData.description || ''}
              onChange={e => setFormData({ ...formData, description: e.target.value })}
              fullWidth
            />
            {!isEdit && (
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                <Typography>Enabled</Typography>
                <Switch
                  checked={formData.status === 1}
                  onChange={e => setFormData({ ...formData, status: e.target.checked ? 1 : 0 })}
                  color="primary"
                />
              </Box>
            )}
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setFormDialogOpen(false)}>Cancel</Button>
          <Button variant="contained" onClick={handleFormSubmit}>Submit</Button>
        </DialogActions>
      </Dialog>

      <Snackbar open={snackbar.open} autoHideDuration={3000} onClose={() => setSnackbar((s) => ({ ...s, open: false }))} anchorOrigin={{ vertical: 'top', horizontal: 'center' }}>
        <Alert severity={snackbar.severity} onClose={() => setSnackbar((s) => ({ ...s, open: false }))}>{snackbar.message}</Alert>
      </Snackbar>
    </Box>
  );
};

export default RoleList;
