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
  Checkbox,
  Select,
  MenuItem,
  FormControl,
  Chip,
  Snackbar,
  Alert,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
} from '@mui/material';
import {
  Search,
  Add,
  Delete,
  FilterList,
  ViewList,
} from '@mui/icons-material';
import { 
  getHomeRecommendProductListAPI, 
  homeRecommendProductUpdateRecommendStatusAPI, 
  homeRecommendProductDeleteByIdsAPI, 
  homeRecommendProductUpdateSortByIdAPI,
  homeRecommendProductCreateAPI
} from '@/apis/recommendProduct';
import { getProductListAPI } from '@/apis/product';
import type { SmsHomeRecommendProduct } from '@/types/recommendProduct';
import type { PmsProduct } from '@/types/product';

const HomeHot: React.FC = () => {
  const [list, setList] = useState<SmsHomeRecommendProduct[]>([]);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(true);
  const [keyword, setKeyword] = useState('');
  const [recommendStatus, setRecommendStatus] = useState<number | ''>('');
  const [pageNum, setPageNum] = useState(0);
  const [pageSize, setPageSize] = useState(10);
  const [selected, setSelected] = useState<number[]>([]);
  const [operateType, setOperateType] = useState('');
  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' as 'success' | 'error' });

  // Dialog state
  const [dialogOpen, setDialogOpen] = useState(false);
  const [allProducts, setAllProducts] = useState<PmsProduct[]>([]);
  const [selectedProductsToAdd, setSelectedProductsToAdd] = useState<number[]>([]);
  const [productSearchKeyword, setProductSearchKeyword] = useState('');

  const fetchList = useCallback(async () => {
    setLoading(true);
    try {
      const res = await getHomeRecommendProductListAPI({ 
        productName: keyword || undefined, 
        recommendStatus: recommendStatus === '' ? undefined : recommendStatus,
        pageNum: pageNum + 1, 
        pageSize 
      });
      setList(res.data.list);
      setTotal(res.data.total);
    } catch (error) {
      console.error('Failed to fetch hot product list:', error);
    } finally {
      setLoading(false);
    }
  }, [keyword, recommendStatus, pageNum, pageSize]);

  useEffect(() => {
    fetchList();
  }, [fetchList]);

  const handleSearch = () => {
    setPageNum(0);
    fetchList();
  };

  const handleDelete = async (id: number) => {
    if (window.confirm('Are you sure you want to delete this hot recommendation?')) {
      try {
        await homeRecommendProductDeleteByIdsAPI({ ids: String(id) });
        setSnackbar({ open: true, message: 'Deleted successfully', severity: 'success' });
        fetchList();
      } catch {
        setSnackbar({ open: true, message: 'Failed to delete', severity: 'error' });
      }
    }
  };

  const handleRecommendStatusChange = async (row: SmsHomeRecommendProduct) => {
    const newStatus = row.recommendStatus === 1 ? 0 : 1;
    try {
      await homeRecommendProductUpdateRecommendStatusAPI({ ids: String(row.id), recommendStatus: newStatus });
      setList((prev) => prev.map((item) => (item.id === row.id ? { ...item, recommendStatus: newStatus } : item)));
      setSnackbar({ open: true, message: 'Status updated', severity: 'success' });
    } catch {
      setSnackbar({ open: true, message: 'Failed to update status', severity: 'error' });
    }
  };

  const handleSortChange = async (row: SmsHomeRecommendProduct, newSort: number) => {
    try {
      await homeRecommendProductUpdateSortByIdAPI({ id: row.id!, sort: newSort });
      setList((prev) => prev.map((item) => (item.id === row.id ? { ...item, sort: newSort } : item)));
      setSnackbar({ open: true, message: 'Sort updated', severity: 'success' });
    } catch {
      setSnackbar({ open: true, message: 'Failed to update sort', severity: 'error' });
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
    try {
      if (operateType === 'recommend' || operateType === 'unrecommend') {
        const status = operateType === 'recommend' ? 1 : 0;
        await homeRecommendProductUpdateRecommendStatusAPI({ ids: selected.join(','), recommendStatus: status });
      } else if (operateType === 'delete') {
        if (!window.confirm('Are you sure you want to delete selected items?')) return;
        await homeRecommendProductDeleteByIdsAPI({ ids: selected.join(',') });
      }
      setSnackbar({ open: true, message: 'Batch operation successful', severity: 'success' });
      setSelected([]);
      fetchList();
    } catch {
      setSnackbar({ open: true, message: 'Batch operation failed', severity: 'error' });
    }
  };

  const openAddDialog = async () => {
    setDialogOpen(true);
    fetchProductsToAdd();
  };

  const fetchProductsToAdd = async () => {
    try {
      const res = await getProductListAPI({ keyword: productSearchKeyword, pageNum: 1, pageSize: 50 });
      setAllProducts(res.data.list);
    } catch (e) {
      console.error(e);
    }
  };

  const handleAddSubmit = async () => {
    if (selectedProductsToAdd.length === 0) {
      setSnackbar({ open: true, message: 'Please select at least one product', severity: 'error' });
      return;
    }
    const data: SmsHomeRecommendProduct[] = selectedProductsToAdd.map(id => {
      const product = allProducts.find(p => p.id === id);
      return {
        productId: product!.id!,
        productName: product!.name,
        recommendStatus: 1,
        sort: 0
      };
    });
    
    try {
      await homeRecommendProductCreateAPI(data);
      setSnackbar({ open: true, message: 'Products added successfully', severity: 'success' });
      setDialogOpen(false);
      setSelectedProductsToAdd([]);
      fetchList();
    } catch {
      setSnackbar({ open: true, message: 'Failed to add products', severity: 'error' });
    }
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
              placeholder="Product name"
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
            <FormControl size="small" sx={{ width: 180 }}>
              <Select
                value={recommendStatus}
                onChange={(e) => setRecommendStatus(e.target.value as any)}
                displayEmpty
              >
                <MenuItem value="">All Status</MenuItem>
                <MenuItem value={1}>Recommended</MenuItem>
                <MenuItem value={0}>Not Recommended</MenuItem>
              </Select>
            </FormControl>
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
            onClick={openAddDialog}
          >
            Select Hot Products
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
                <TableCell>Product Name</TableCell>
                <TableCell align="center">Recommend Status</TableCell>
                <TableCell align="center">Sort</TableCell>
                <TableCell align="center">Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {loading ? (
                <TableRow>
                  <TableCell colSpan={6} sx={{ textAlign: 'center', py: 6 }}>
                    <Typography sx={{ color: 'text.secondary' }}>Loading...</Typography>
                  </TableCell>
                </TableRow>
              ) : list.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={6} sx={{ textAlign: 'center', py: 6 }}>
                    <Typography sx={{ color: 'text.secondary' }}>No hot product recommendations found</Typography>
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
                    <TableCell sx={{ fontWeight: 500 }}>{row.productName}</TableCell>
                    <TableCell align="center">
                      <Switch
                        checked={row.recommendStatus === 1}
                        onChange={() => handleRecommendStatusChange(row)}
                        size="small"
                        color="primary"
                      />
                    </TableCell>
                    <TableCell align="center">
                      <TextField
                        size="small"
                        type="number"
                        defaultValue={row.sort}
                        onBlur={(e) => handleSortChange(row, Number(e.target.value))}
                        sx={{ width: 80 }}
                        inputProps={{ min: 0 }}
                      />
                    </TableCell>
                    <TableCell align="center">
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
            <FormControl size="small" sx={{ minWidth: 160 }}>
              <Select
                value={operateType}
                onChange={(e) => setOperateType(e.target.value)}
                displayEmpty
                sx={{ fontSize: '0.8125rem' }}
              >
                <MenuItem value="" disabled><em>Batch Operation</em></MenuItem>
                <MenuItem value="recommend">Set Recommended</MenuItem>
                <MenuItem value="unrecommend">Cancel Recommended</MenuItem>
                <MenuItem value="delete">Delete</MenuItem>
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

      <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)} maxWidth="sm" fullWidth>
        <DialogTitle>Select Hot Products to Recommend</DialogTitle>
        <DialogContent dividers>
          <Box sx={{ display: 'flex', gap: 1, mb: 2 }}>
            <TextField
              size="small"
              placeholder="Search products"
              value={productSearchKeyword}
              onChange={e => setProductSearchKeyword(e.target.value)}
              fullWidth
            />
            <Button variant="contained" onClick={fetchProductsToAdd}>Search</Button>
          </Box>
          <TableContainer>
            <Table size="small">
              <TableHead>
                <TableRow>
                  <TableCell padding="checkbox">
                    <Checkbox
                      checked={allProducts.length > 0 && selectedProductsToAdd.length === allProducts.length}
                      onChange={(e) => setSelectedProductsToAdd(e.target.checked ? allProducts.map(b => b.id!) : [])}
                    />
                  </TableCell>
                  <TableCell>Product Name</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {allProducts.map(product => (
                  <TableRow key={product.id}>
                    <TableCell padding="checkbox">
                      <Checkbox
                        checked={selectedProductsToAdd.includes(product.id!)}
                        onChange={() => {
                          setSelectedProductsToAdd(prev => 
                            prev.includes(product.id!) ? prev.filter(id => id !== product.id) : [...prev, product.id!]
                          );
                        }}
                      />
                    </TableCell>
                    <TableCell>{product.name}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDialogOpen(false)}>Cancel</Button>
          <Button variant="contained" onClick={handleAddSubmit}>Add Selected</Button>
        </DialogActions>
      </Dialog>

      <Snackbar open={snackbar.open} autoHideDuration={3000} onClose={() => setSnackbar((s) => ({ ...s, open: false }))} anchorOrigin={{ vertical: 'top', horizontal: 'center' }}>
        <Alert severity={snackbar.severity} onClose={() => setSnackbar((s) => ({ ...s, open: false }))}>{snackbar.message}</Alert>
      </Snackbar>
    </Box>
  );
};

export default HomeHot;
