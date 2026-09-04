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
  Chip,
  Snackbar,
  Alert,
  alpha,
} from '@mui/material';
import {
  Search,
  Add,
  Edit,
  Delete,
  FilterList,
  ViewList,
} from '@mui/icons-material';
import { getBrandListAPI, brandUpdateShowStatusAPI, brandUpdateFactoryStatusAPI, brandDeleteByIdAPI } from '@/apis/brand';
import type { PmsBrand } from '@/types/brand';

const BrandList: React.FC = () => {
  const navigate = useNavigate();

  const [list, setList] = useState<PmsBrand[]>([]);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(true);
  const [keyword, setKeyword] = useState('');
  const [pageNum, setPageNum] = useState(0);
  const [pageSize, setPageSize] = useState(10);
  const [selected, setSelected] = useState<number[]>([]);
  const [operateType, setOperateType] = useState('');
  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' as 'success' | 'error' });

  const fetchList = useCallback(async () => {
    setLoading(true);
    try {
      const res = await getBrandListAPI({ keyword, pageNum: pageNum + 1, pageSize });
      setList(res.data.list);
      setTotal(res.data.total);
    } catch (error) {
      console.error('Failed to fetch brand list:', error);
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
    if (window.confirm('Are you sure you want to delete this brand?')) {
      try {
        await brandDeleteByIdAPI(id);
        setSnackbar({ open: true, message: 'Brand deleted successfully', severity: 'success' });
        fetchList();
      } catch {
        setSnackbar({ open: true, message: 'Failed to delete brand', severity: 'error' });
      }
    }
  };

  const handleFactoryStatusChange = async (row: PmsBrand) => {
    const newStatus = row.factoryStatus === 1 ? 0 : 1;
    try {
      await brandUpdateFactoryStatusAPI({ ids: String(row.id), factoryStatus: newStatus });
      setList((prev) => prev.map((item) => (item.id === row.id ? { ...item, factoryStatus: newStatus } : item)));
      setSnackbar({ open: true, message: 'Status updated', severity: 'success' });
    } catch {
      setSnackbar({ open: true, message: 'Failed to update status', severity: 'error' });
    }
  };

  const handleShowStatusChange = async (row: PmsBrand) => {
    const newStatus = row.showStatus === 1 ? 0 : 1;
    try {
      await brandUpdateShowStatusAPI({ ids: String(row.id), showStatus: newStatus });
      setList((prev) => prev.map((item) => (item.id === row.id ? { ...item, showStatus: newStatus } : item)));
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

  const handleBatchOperate = async () => {
    if (selected.length === 0) {
      setSnackbar({ open: true, message: 'Please select at least one item', severity: 'error' });
      return;
    }
    if (!operateType) {
      setSnackbar({ open: true, message: 'Please select an operation type', severity: 'error' });
      return;
    }
    const showStatus = operateType === 'show' ? 1 : 0;
    try {
      await brandUpdateShowStatusAPI({ ids: selected.join(','), showStatus });
      setSnackbar({ open: true, message: 'Batch operation successful', severity: 'success' });
      fetchList();
    } catch {
      setSnackbar({ open: true, message: 'Batch operation failed', severity: 'error' });
    }
  };

  return (
    <Box>
      {/* Search Filter */}
      <Card sx={{ mb: 2 }}>
        <CardContent sx={{ p: 2.5, '&:last-child': { pb: 2.5 } }}>
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 2 }}>
            <FilterList sx={{ fontSize: 18, color: '#6366F1' }} />
            <Typography sx={{ fontWeight: 600, fontSize: '0.875rem' }}>Filter & Search</Typography>
          </Box>
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, flexWrap: 'wrap' }}>
            <TextField
              size="small"
              placeholder="Brand name / keyword"
              value={keyword}
              onChange={(e) => setKeyword(e.target.value)}
              onKeyDown={(e) => e.key === 'Enter' && handleSearch()}
              sx={{ width: 260 }}
              InputProps={{
                startAdornment: (
                  <InputAdornment position="start">
                    <Search sx={{ fontSize: 18, color: '#94A3B8' }} />
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

      {/* Data List Header */}
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
            onClick={() => navigate('/pms/addBrand')}
          >
            Add Brand
          </Button>
        </CardContent>
      </Card>

      {/* Table */}
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
                <TableCell>Brand Name</TableCell>
                <TableCell>First Letter</TableCell>
                <TableCell align="center">Sort</TableCell>
                <TableCell align="center">Manufacturer</TableCell>
                <TableCell align="center">Visible</TableCell>
                <TableCell align="center">Related</TableCell>
                <TableCell align="center">Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {loading ? (
                <TableRow>
                  <TableCell colSpan={9} sx={{ textAlign: 'center', py: 6 }}>
                    <Typography sx={{ color: '#94A3B8' }}>Loading...</Typography>
                  </TableCell>
                </TableRow>
              ) : list.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={9} sx={{ textAlign: 'center', py: 6 }}>
                    <Typography sx={{ color: '#94A3B8' }}>No brands found</Typography>
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
                    <TableCell sx={{ fontWeight: 500 }}>{row.name}</TableCell>
                    <TableCell>{row.firstLetter}</TableCell>
                    <TableCell align="center">{row.sort}</TableCell>
                    <TableCell align="center">
                      <Switch
                        checked={row.factoryStatus === 1}
                        onChange={() => handleFactoryStatusChange(row)}
                        size="small"
                        color="primary"
                      />
                    </TableCell>
                    <TableCell align="center">
                      <Switch
                        checked={row.showStatus === 1}
                        onChange={() => handleShowStatusChange(row)}
                        size="small"
                        color="primary"
                      />
                    </TableCell>
                    <TableCell align="center">
                      <Typography component="span" sx={{ fontSize: '0.8125rem', color: '#64748B' }}>
                        Products: <Button size="small" sx={{ minWidth: 'auto', p: 0 }}>100</Button>
                      </Typography>
                      {' '}
                      <Typography component="span" sx={{ fontSize: '0.8125rem', color: '#64748B' }}>
                        Reviews: <Button size="small" sx={{ minWidth: 'auto', p: 0 }}>1000</Button>
                      </Typography>
                    </TableCell>
                    <TableCell align="center">
                      <Box sx={{ display: 'flex', justifyContent: 'center', gap: 0.5 }}>
                        <IconButton
                          size="small"
                          onClick={() => navigate(`/pms/updateBrand?id=${row.id}`)}
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
                      </Box>
                    </TableCell>
                  </TableRow>
                ))
              )}
            </TableBody>
          </Table>
        </TableContainer>
        <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', px: 2, py: 1 }}>
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1.5 }}>
            <FormControl size="small" sx={{ minWidth: 160 }}>
              <Select
                value={operateType}
                onChange={(e) => setOperateType(e.target.value)}
                displayEmpty
                sx={{ fontSize: '0.8125rem' }}
              >
                <MenuItem value="" disabled>
                  <em>Batch Operation</em>
                </MenuItem>
                <MenuItem value="show">Show Brand</MenuItem>
                <MenuItem value="hide">Hide Brand</MenuItem>
              </Select>
            </FormControl>
            <Button variant="outlined" size="small" onClick={handleBatchOperate}>
              Apply
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

      <Snackbar
        open={snackbar.open}
        autoHideDuration={3000}
        onClose={() => setSnackbar((s) => ({ ...s, open: false }))}
        anchorOrigin={{ vertical: 'top', horizontal: 'center' }}
      >
        <Alert severity={snackbar.severity} onClose={() => setSnackbar((s) => ({ ...s, open: false }))}>
          {snackbar.message}
        </Alert>
      </Snackbar>
    </Box>
  );
};

export default BrandList;
