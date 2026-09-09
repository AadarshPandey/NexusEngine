import React, { useState, useEffect, useCallback } from 'react';
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
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  OutlinedInput,
  Checkbox,
  ListItemText,
  Chip,
} from '@mui/material';
import {
  Search,
  Add,
  Delete,
  Edit,
  FilterList,
  ViewList,
  Security,
} from '@mui/icons-material';
import { 
  getAdminListAPI, 
  adminUpdateStatusByIdAPI, 
  adminDeleteByIdAPI, 
  adminRegisterAPI, 
  adminUpdateByIdAPI,
  getRoleByAdminIdAPI,
  adminRoleUpdateAPI
} from '@/apis/admin';
import { getRoleListAllAPI } from '@/apis/role';
import type { UmsAdmin } from '@/types/admin';
import type { UmsRole } from '@/types/role';

const AdminList: React.FC = () => {
  const [list, setList] = useState<UmsAdmin[]>([]);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(true);
  const [keyword, setKeyword] = useState('');
  const [pageNum, setPageNum] = useState(0);
  const [pageSize, setPageSize] = useState(10);
  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' as 'success' | 'error' });

  // Add/Edit Dialog State
  const [formDialogOpen, setFormDialogOpen] = useState(false);
  const [isEdit, setIsEdit] = useState(false);
  const [formData, setFormData] = useState<UmsAdmin>({
    username: '',
    password: '',
    nickName: '',
    email: '',
    status: 1,
    note: ''
  });

  // Role Allocation Dialog State
  const [roleDialogOpen, setRoleDialogOpen] = useState(false);
  const [allRoles, setAllRoles] = useState<UmsRole[]>([]);
  const [selectedRoles, setSelectedRoles] = useState<number[]>([]);
  const [activeAdminId, setActiveAdminId] = useState<number | null>(null);

  const fetchList = useCallback(async () => {
    setLoading(true);
    try {
      const res = await getAdminListAPI({ 
        keyword: keyword || undefined, 
        pageNum: pageNum + 1, 
        pageSize 
      });
      setList(res.data.list);
      setTotal(res.data.total);
    } catch (error) {
      console.error('Failed to fetch admin list:', error);
    } finally {
      setLoading(false);
    }
  }, [keyword, pageNum, pageSize]);

  useEffect(() => {
    fetchList();
  }, [fetchList]);

  useEffect(() => {
    getRoleListAllAPI().then(res => setAllRoles(res.data)).catch(console.error);
  }, []);

  const handleSearch = () => {
    setPageNum(0);
    fetchList();
  };

  const handleDelete = async (id: number) => {
    if (window.confirm('Are you sure you want to delete this user?')) {
      try {
        await adminDeleteByIdAPI(id);
        setSnackbar({ open: true, message: 'Deleted successfully', severity: 'success' });
        fetchList();
      } catch {
        setSnackbar({ open: true, message: 'Failed to delete', severity: 'error' });
      }
    }
  };

  const handleStatusChange = async (row: UmsAdmin) => {
    const newStatus = row.status === 1 ? 0 : 1;
    try {
      await adminUpdateStatusByIdAPI(row.id!, { status: newStatus });
      setList((prev) => prev.map((item) => (item.id === row.id ? { ...item, status: newStatus } : item)));
      setSnackbar({ open: true, message: 'Status updated', severity: 'success' });
    } catch {
      setSnackbar({ open: true, message: 'Failed to update status', severity: 'error' });
    }
  };

  // Form handling
  const openAddDialog = () => {
    setIsEdit(false);
    setFormData({ username: '', password: '', nickName: '', email: '', status: 1, note: '' });
    setFormDialogOpen(true);
  };

  const openEditDialog = (row: UmsAdmin) => {
    setIsEdit(true);
    setFormData({ ...row, password: '' }); // Don't pre-fill password for security
    setFormDialogOpen(true);
  };

  const handleFormSubmit = async () => {
    if (!formData.username) return;
    try {
      if (isEdit) {
        await adminUpdateByIdAPI(formData.id!, formData);
        setSnackbar({ open: true, message: 'Updated successfully', severity: 'success' });
      } else {
        await adminRegisterAPI(formData);
        setSnackbar({ open: true, message: 'Created successfully', severity: 'success' });
      }
      setFormDialogOpen(false);
      fetchList();
    } catch {
      setSnackbar({ open: true, message: 'Operation failed', severity: 'error' });
    }
  };

  // Role allocation handling
  const openRoleDialog = async (row: UmsAdmin) => {
    setActiveAdminId(row.id!);
    try {
      const res = await getRoleByAdminIdAPI(row.id!);
      setSelectedRoles(res.data.map(r => r.id!));
      setRoleDialogOpen(true);
    } catch {
      setSnackbar({ open: true, message: 'Failed to fetch roles', severity: 'error' });
    }
  };

  const handleRoleSubmit = async () => {
    if (!activeAdminId) return;
    try {
      await adminRoleUpdateAPI({ adminId: activeAdminId, roleIds: selectedRoles.join(',') });
      setSnackbar({ open: true, message: 'Roles allocated successfully', severity: 'success' });
      setRoleDialogOpen(false);
    } catch {
      setSnackbar({ open: true, message: 'Failed to allocate roles', severity: 'error' });
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
              placeholder="Username or Name"
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
            <Chip label={`${total} users`} size="small" sx={{ ml: 1, height: 22, fontSize: '0.75rem' }} />
          </Box>
          <Button
            variant="contained"
            startIcon={<Add />}
            size="small"
            onClick={openAddDialog}
          >
            Add Admin
          </Button>
        </CardContent>
      </Card>

      <Card>
        <TableContainer>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>ID</TableCell>
                <TableCell>Username</TableCell>
                <TableCell>Name</TableCell>
                <TableCell>Email</TableCell>
                <TableCell>Added Date</TableCell>
                <TableCell>Last Login</TableCell>
                <TableCell align="center">Enabled</TableCell>
                <TableCell align="center" width={140}>Actions</TableCell>
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
                    <Typography sx={{ color: 'text.secondary' }}>No admin users found</Typography>
                  </TableCell>
                </TableRow>
              ) : (
                list.map((row) => (
                  <TableRow key={row.id} hover>
                    <TableCell>{row.id}</TableCell>
                    <TableCell sx={{ fontWeight: 500 }}>{row.username}</TableCell>
                    <TableCell>{row.nickName}</TableCell>
                    <TableCell>{row.email}</TableCell>
                    <TableCell>{formatTime(row.createTime)}</TableCell>
                    <TableCell>{formatTime(row.loginTime)}</TableCell>
                    <TableCell align="center">
                      <Switch
                        checked={row.status === 1}
                        onChange={() => handleStatusChange(row)}
                        size="small"
                        color="primary"
                        disabled={row.username === 'admin'} // Protect superadmin
                      />
                    </TableCell>
                    <TableCell align="center">
                      <Box sx={{ display: 'flex', justifyContent: 'center', gap: 0.5 }}>
                        <IconButton
                          size="small"
                          onClick={() => openRoleDialog(row)}
                          sx={{ color: '#F59E0B' }}
                          title="Allocate Role"
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
                          disabled={row.username === 'admin'}
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
        <TablePagination
          component="div"
          count={total}
          page={pageNum}
          onPageChange={(_, p) => setPageNum(p)}
          rowsPerPage={pageSize}
          onRowsPerPageChange={(e) => { setPageSize(parseInt(e.target.value, 10)); setPageNum(0); }}
          rowsPerPageOptions={[5, 10, 15]}
        />
      </Card>

      {/* Form Dialog */}
      <Dialog open={formDialogOpen} onClose={() => setFormDialogOpen(false)} maxWidth="sm" fullWidth>
        <DialogTitle>{isEdit ? 'Edit Admin User' : 'Add Admin User'}</DialogTitle>
        <DialogContent dividers>
          <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, pt: 1 }}>
            <TextField
              label="Username"
              value={formData.username}
              onChange={e => setFormData({ ...formData, username: e.target.value })}
              fullWidth
              required
              disabled={isEdit && formData.username === 'admin'}
            />
            <TextField
              label="Name"
              value={formData.nickName || ''}
              onChange={e => setFormData({ ...formData, nickName: e.target.value })}
              fullWidth
            />
            <TextField
              label="Email"
              type="email"
              value={formData.email || ''}
              onChange={e => setFormData({ ...formData, email: e.target.value })}
              fullWidth
            />
            <TextField
              label={isEdit ? "Password (leave blank to keep current)" : "Password"}
              type="password"
              value={formData.password || ''}
              onChange={e => setFormData({ ...formData, password: e.target.value })}
              fullWidth
              required={!isEdit}
            />
            <TextField
              label="Notes"
              multiline
              rows={2}
              value={formData.note || ''}
              onChange={e => setFormData({ ...formData, note: e.target.value })}
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

      {/* Role Allocation Dialog */}
      <Dialog open={roleDialogOpen} onClose={() => setRoleDialogOpen(false)} maxWidth="sm" fullWidth>
        <DialogTitle>Allocate Roles</DialogTitle>
        <DialogContent dividers>
          <FormControl fullWidth sx={{ mt: 1 }}>
            <InputLabel>Roles</InputLabel>
            <Select
              multiple
              value={selectedRoles}
              onChange={(e) => setSelectedRoles(typeof e.target.value === 'string' ? e.target.value.split(',').map(Number) : e.target.value)}
              input={<OutlinedInput label="Roles" />}
              renderValue={(selected) => selected.map(id => allRoles.find(r => r.id === id)?.name).join(', ')}
            >
              {allRoles.map((role) => (
                <MenuItem key={role.id} value={role.id}>
                  <Checkbox checked={selectedRoles.indexOf(role.id!) > -1} />
                  <ListItemText primary={role.name} secondary={role.description} />
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setRoleDialogOpen(false)}>Cancel</Button>
          <Button variant="contained" onClick={handleRoleSubmit}>Save</Button>
        </DialogActions>
      </Dialog>

      <Snackbar open={snackbar.open} autoHideDuration={3000} onClose={() => setSnackbar((s) => ({ ...s, open: false }))} anchorOrigin={{ vertical: 'top', horizontal: 'center' }}>
        <Alert severity={snackbar.severity} onClose={() => setSnackbar((s) => ({ ...s, open: false }))}>{snackbar.message}</Alert>
      </Snackbar>
    </Box>
  );
};

export default AdminList;
