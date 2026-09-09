import React, { useEffect, useState } from 'react';
import { Box, Card, CardContent, Typography, Button, TextField, Select, MenuItem, FormControl, InputLabel, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TablePagination, Paper, Chip } from '@mui/material';
import { Search, Add } from '@mui/icons-material';
import { useNavigate } from 'react-router';
import { getCouponListAPI, couponDeleteByIdAPI } from '@/apis/coupon';
import type { SmsCoupon, CouponQueryParam } from '@/types/coupon';
import dayjs from 'dayjs';

const CouponList: React.FC = () => {
  const navigate = useNavigate();
  const [list, setList] = useState<SmsCoupon[]>([]);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);
  const [queryParams, setQueryParams] = useState<CouponQueryParam>({
    pageNum: 1,
    pageSize: 10,
    name: '',
    type: undefined,
  });

  useEffect(() => {
    fetchList();
  }, [queryParams.pageNum, queryParams.pageSize]);

  const fetchList = async () => {
    setLoading(true);
    try {
      const res = await getCouponListAPI(queryParams);
      if (res.data) {
        setList(res.data.list || []);
        setTotal(res.data.total || 0);
      }
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = () => {
    if (queryParams.pageNum !== 1) {
      setQueryParams({ ...queryParams, pageNum: 1 });
    } else {
      fetchList();
    }
  };

  const handleReset = () => {
    setQueryParams({ pageNum: 1, pageSize: 10, name: '', type: undefined });
    setTimeout(fetchList, 0);
  };

  const handleDelete = async (id: number) => {
    if (window.confirm('Are you sure you want to delete this coupon?')) {
      try {
        await couponDeleteByIdAPI(id);
        fetchList();
      } catch (e) {
        console.error(e);
      }
    }
  };

  const formatType = (type: number) => {
    switch (type) {
      case 0: return 'Full Discount';
      case 1: return 'Discount';
      case 2: return 'Free Shipping';
      default: return 'Unknown';
    }
  };

  const formatPlatform = (platform: number) => {
    switch (platform) {
      case 0: return 'All Platforms';
      case 1: return 'Mobile App';
      case 2: return 'PC Web';
      default: return 'Unknown';
    }
  };

  return (
    <Box sx={{ p: 3 }}>
      {/* Search and Filter */}
      <Card sx={{ mb: 3 }}>
        <CardContent>
          <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 2 }}>
            <Typography variant="h6" sx={{ display: 'flex', alignItems: 'center' }}>
              <Search sx={{ mr: 1 }} /> Filter & Search
            </Typography>
            <Box>
              <Button variant="outlined" onClick={handleReset} sx={{ mr: 2 }}>Reset</Button>
              <Button variant="contained" onClick={handleSearch}>Search</Button>
            </Box>
          </Box>
          <Box sx={{ display: 'flex', gap: 2 }}>
            <TextField
              size="small"
              label="Coupon Name"
              value={queryParams.name || ''}
              onChange={(e) => setQueryParams({ ...queryParams, name: e.target.value })}
              sx={{ width: 250 }}
            />
            <FormControl size="small" sx={{ width: 200 }}>
              <InputLabel>Coupon Type</InputLabel>
              <Select
                label="Coupon Type"
                value={queryParams.type === undefined ? '' : queryParams.type}
                onChange={(e) => setQueryParams({ ...queryParams, type: String(e.target.value) === '' ? undefined : Number(e.target.value) })}
              >
                <MenuItem value="">All</MenuItem>
                <MenuItem value={0}>Full Discount</MenuItem>
                <MenuItem value={1}>Discount</MenuItem>
                <MenuItem value={2}>Free Shipping</MenuItem>
              </Select>
            </FormControl>
          </Box>
        </CardContent>
      </Card>

      {/* Data List */}
      <Card>
        <Box sx={{ p: 2, display: 'flex', justifyContent: 'space-between', alignItems: 'center', borderBottom: '1px solid', borderColor: 'divider' }}>
          <Typography variant="h6">Coupon List</Typography>
          <Button variant="contained" startIcon={<Add />} onClick={() => navigate('/sms/addCoupon')}>
            Add Coupon
          </Button>
        </Box>
        <TableContainer component={Paper} elevation={0}>
          <Table>
            <TableHead>
              <TableRow sx={{ bgcolor: 'background.default' }}>
                <TableCell>ID</TableCell>
                <TableCell>Coupon Name</TableCell>
                <TableCell>Type</TableCell>
                <TableCell>Platform</TableCell>
                <TableCell>Value</TableCell>
                <TableCell>Minimum Spend</TableCell>
                <TableCell>Valid Range</TableCell>
                <TableCell>Status</TableCell>
                <TableCell align="center">Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {loading ? (
                <TableRow><TableCell colSpan={9} align="center">Loading...</TableCell></TableRow>
              ) : list.length === 0 ? (
                <TableRow><TableCell colSpan={9} align="center" sx={{ py: 5 }}>No coupons found.</TableCell></TableRow>
              ) : (
                list.map((row) => (
                  <TableRow key={row.id} hover>
                    <TableCell>{row.id}</TableCell>
                    <TableCell>{row.name}</TableCell>
                    <TableCell>{formatType(row.type)}</TableCell>
                    <TableCell>{formatPlatform(row.platform)}</TableCell>
                    <TableCell>₹{row.amount}</TableCell>
                    <TableCell>₹{row.minPoint}</TableCell>
                    <TableCell>
                      <Typography variant="caption" display="block">
                        {row.startTime ? dayjs(row.startTime).format('YYYY-MM-DD') : 'N/A'}
                      </Typography>
                      <Typography variant="caption" display="block">
                        to {row.endTime ? dayjs(row.endTime).format('YYYY-MM-DD') : 'N/A'}
                      </Typography>
                    </TableCell>
                    <TableCell>
                      {row.endTime && new Date(row.endTime).getTime() < new Date().getTime() ? (
                        <Chip label="Expired" size="small" color="error" />
                      ) : (
                        <Chip label="Active" size="small" color="success" />
                      )}
                    </TableCell>
                    <TableCell align="center">
                      <Button size="small" onClick={() => navigate(`/sms/couponHistory?couponId=${row.id}`)}>History</Button>
                      <Button size="small" color="primary" onClick={() => navigate(`/sms/updateCoupon?id=${row.id}`)}>Edit</Button>
                      <Button size="small" color="error" onClick={() => handleDelete(row.id!)}>Delete</Button>
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
          page={(queryParams.pageNum || 1) - 1}
          onPageChange={(e, newPage) => setQueryParams({ ...queryParams, pageNum: newPage + 1 })}
          rowsPerPage={queryParams.pageSize || 10}
          onRowsPerPageChange={(e) => setQueryParams({ ...queryParams, pageSize: parseInt(e.target.value, 10), pageNum: 1 })}
          rowsPerPageOptions={[5, 10, 15]}
        />
      </Card>
    </Box>
  );
};

export default CouponList;
