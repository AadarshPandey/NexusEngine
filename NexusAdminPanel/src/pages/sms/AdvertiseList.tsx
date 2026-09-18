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
  Checkbox,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  Chip,
  Snackbar,
  Alert,
} from '@mui/material';
import {
  Search,
  Add,
  Delete,
  Edit,
  FilterList,
  ViewList,
} from '@mui/icons-material';
import { 
  getHomeAdvertiseListAPI, 
  homeAdvertiseUpdateStatusAPI, 
  deleteHomeAdvertiseAPI
} from '@/apis/homeAdvertise';
import type { SmsHomeAdvertise } from '@/types/homeAdvertist';

const AdvertiseList: React.FC = () => {
  const navigate = useNavigate();

  const [list, setList] = useState<SmsHomeAdvertise[]>([]);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(true);
  const [keyword, setKeyword] = useState('');
  const [type, setType] = useState<number | ''>('');
  const [endTime, setEndTime] = useState('');
  const [pageNum, setPageNum] = useState(0);
  const [pageSize, setPageSize] = useState(10);
  const [selected, setSelected] = useState<number[]>([]);
  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' as 'success' | 'error' });

  const fetchList = useCallback(async () => {
    setLoading(true);
    try {
      const res = await getHomeAdvertiseListAPI({ 
        name: keyword || undefined, 
        type: type === '' ? undefined : type,
        endTime: endTime || undefined,
        pageNum: pageNum + 1, 
        pageSize 
      });
      setList(res.data.list);
      setTotal(res.data.total);
    } catch (error) {
      console.error('Failed to fetch advertise list:', error);
    } finally {
      setLoading(false);
    }
  }, [keyword, type, endTime, pageNum, pageSize]);

  useEffect(() => {
    fetchList();
  }, [fetchList]);

  const handleSearch = () => {
    setPageNum(0);
    fetchList();
  };

  const handleDelete = async (id: number) => {
    if (window.confirm('Are you sure you want to delete this advertisement?')) {
      try {
        await deleteHomeAdvertiseAPI({ ids: String(id) });
        setSnackbar({ open: true, message: 'Deleted successfully', severity: 'success' });
        fetchList();
      } catch {
        setSnackbar({ open: true, message: 'Failed to delete', severity: 'error' });
      }
    }
  };

  const handleStatusChange = async (row: SmsHomeAdvertise) => {
    const newStatus = row.status === 1 ? 0 : 1;
    try {
      await homeAdvertiseUpdateStatusAPI({ id: row.id!, status: newStatus });
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

  const handleBatchDelete = async () => {
    if (selected.length === 0) {
      setSnackbar({ open: true, message: 'Please select at least one item', severity: 'error' });
      return;
    }
    if (!window.confirm('Are you sure you want to delete selected items?')) return;
    try {
      await deleteHomeAdvertiseAPI({ ids: selected.join(',') });
      setSnackbar({ open: true, message: 'Batch delete successful', severity: 'success' });
      setSelected([]);
      fetchList();
    } catch {
      setSnackbar({ open: true, message: 'Batch delete failed', severity: 'error' });
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
              placeholder="Advertisement name"
              value={keyword}
              onChange={(e) => setKeyword(e.target.value)}
              onKeyDown={(e) => e.key === 'Enter' && handleSearch()}
              sx={{ width: 200 }}
              InputProps={{
                startAdornment: (
                  <InputAdornment position="start">
                    <Search sx={{ fontSize: 18, color: 'text.secondary' }} />
                  </InputAdornment>
                ),
              }}
            />
            <TextField
              size="small"
              type="date"
              label="End Time Before"
              InputLabelProps={{ shrink: true }}
              value={endTime}
              onChange={(e) => setEndTime(e.target.value)}
              sx={{ width: 200 }}
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
            <Chip label={`${total} items`} size="small" sx={{ ml: 1, height: 22, fontSize: '0.75rem' }} />
          </Box>
          <Button
            variant="contained"
            startIcon={<Add />}
            size="small"
            onClick={() => navigate('/sms/addAdvertise')}
          >
            Add Advertisement
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
                <TableCell>Name</TableCell>
                <TableCell>Image</TableCell>
                <TableCell>Time Range</TableCell>
                <TableCell align="center">Online/Offline</TableCell>
                <TableCell align="center">Clicks</TableCell>
                <TableCell align="center">Actions</TableCell>
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
                    <Typography sx={{ color: 'text.secondary' }}>No advertisements found</Typography>
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
                      />
                    </TableCell>
                    <TableCell>{row.id}</TableCell>
                    <TableCell sx={{ fontWeight: 500 }}>
                      {row.name}
                    </TableCell>
                    <TableCell>
                      <Box
                        component="img"
                        src={row.pic}
                        alt="Ad"
                        sx={{ height: 60, width: 120, objectFit: 'cover', borderRadius: 1 }}
                      />
                    </TableCell>
                    <TableCell>
                      <Typography variant="caption" display="block">Start: {formatTime(row.startTime)}</Typography>
                      <Typography variant="caption" display="block">End: {formatTime(row.endTime)}</Typography>
                    </TableCell>
                    <TableCell align="center">
                      <Switch
                        checked={row.status === 1}
                        onChange={() => handleStatusChange(row)}
                        size="small"
                        color="primary"
                      />
                    </TableCell>
                    <TableCell align="center">{row.clickCount || 0}</TableCell>
                    <TableCell align="center">
                      <IconButton
                        size="small"
                        onClick={() => navigate(`/sms/updateAdvertise?id=${row.id}`)}
                        sx={{ color: '#6366F1' }}
                      >
                        <Edit sx={{ fontSize: 18 }} />
                      </IconButton>
                      <IconButton
                        size="small"
                        onClick={() => handleDelete(row.id!)}
                        sx={{ color: '#EF4444' }}
                      >
                        <Delete sx={{ fontSize: 18 }} />
                      </IconButton>
                    </TableCell>
                  </TableRow>
                ))
              )}
            </TableBody>
          </Table>
        </TableContainer>
        <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', px: 2, py: 1 }}>
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1.5 }}>
            <Button variant="outlined" color="error" size="small" onClick={handleBatchDelete} disabled={selected.length === 0}>
              Batch Delete
            </Button>
          </Box>
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

      <Snackbar open={snackbar.open} autoHideDuration={3000} onClose={() => setSnackbar((s) => ({ ...s, open: false }))} anchorOrigin={{ vertical: 'top', horizontal: 'center' }}>
        <Alert severity={snackbar.severity} onClose={() => setSnackbar((s) => ({ ...s, open: false }))}>{snackbar.message}</Alert>
      </Snackbar>
    </Box>
  );
};

export default AdvertiseList;
