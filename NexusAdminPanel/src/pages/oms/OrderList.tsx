import React, { useEffect, useState } from 'react';
import { Card, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography, Chip, Paper, Button, Menu, MenuItem, TextField, Grid, FormControl, InputLabel, Select, Box } from '@mui/material';
import { getOrderListAPI, orderUpdateStatusAPI } from '@/apis/order';
import type { OmsOrder } from '@/types/order';

const OrderList: React.FC = () => {
  const [orders, setOrders] = useState<OmsOrder[]>([]);
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
  const [selectedOrder, setSelectedOrder] = useState<OmsOrder | null>(null);

  // Search states
  const [searchId, setSearchId] = useState('');
  const [searchSn, setSearchSn] = useState('');
  const [searchMember, setSearchMember] = useState('');
  const [searchAmount, setSearchAmount] = useState('');
  const [searchPayType, setSearchPayType] = useState<number | ''>('');
  const [searchStatus, setSearchStatus] = useState<number | ''>('');
  const [searchDate, setSearchDate] = useState('');

  useEffect(() => {
    fetchOrders();
  }, []);

  const fetchOrders = async () => {
    try {
      const res = await getOrderListAPI({ pageNum: 1, pageSize: 500 });
      setOrders(res.data.list);
    } catch (e) {
      console.error(e);
    }
  };

  const handleUpdateClick = (event: React.MouseEvent<HTMLButtonElement>, order: OmsOrder) => {
    setAnchorEl(event.currentTarget);
    setSelectedOrder(order);
  };

  const handleStatusChange = async (status: number) => {
    if (selectedOrder) {
      try {
        await orderUpdateStatusAPI({ ids: selectedOrder.id.toString(), status });
        fetchOrders();
      } catch (e) {
        console.error(e);
      }
    }
    setAnchorEl(null);
    setSelectedOrder(null);
  };

  const handleClose = () => {
    setAnchorEl(null);
    setSelectedOrder(null);
  };

  const getStatusChip = (status: number) => {
    switch(status) {
      case 0: return <Chip label="Unpaid" color="warning" size="small" />;
      case 1: return <Chip label="Paid/To Ship" color="info" size="small" />;
      case 2: return <Chip label="Shipped" color="primary" size="small" />;
      case 3: return <Chip label="Completed" color="success" size="small" />;
      case 4: return <Chip label="Canceled" color="error" size="small" />;
      case 5: return <Chip label="Out for Delivery" color="primary" variant="outlined" size="small" />;
      case 6: return <Chip label="Refunded" color="default" size="small" />;
      default: return <Chip label="Unknown" size="small" />;
    }
  };

  const resetSearch = () => {
    setSearchId('');
    setSearchSn('');
    setSearchMember('');
    setSearchAmount('');
    setSearchPayType('');
    setSearchStatus('');
    setSearchDate('');
  };

  const filteredOrders = orders.filter(order => {
    if (searchId && order.id.toString() !== searchId && !order.orderSn?.includes(searchId)) return false;
    if (searchSn && !order.orderSn?.includes(searchSn)) return false;
    if (searchMember && !order.memberUsername?.toLowerCase().includes(searchMember.toLowerCase())) return false;
    if (searchAmount && order.totalAmount?.toString() !== searchAmount) return false;
    if (searchPayType !== '' && order.payType !== searchPayType) return false;
    if (searchStatus !== '' && order.status !== searchStatus) return false;
    if (searchDate && !order.createTime?.startsWith(searchDate)) return false;
    return true;
  });

  return (
    <Card sx={{ p: 3, m: 3 }}>
      <Typography variant="h5" gutterBottom>Order Management</Typography>
      
      <Paper sx={{ p: 2, mb: 3 }} variant="outlined">
        <Typography variant="subtitle1" gutterBottom sx={{ fontWeight: 'bold' }}>Search Filters</Typography>
        <Grid container spacing={2} alignItems="center">
          <Grid item xs={12} sm={6} md={3} lg={2}>
            <TextField fullWidth label="Order ID" size="small" value={searchId} onChange={e => setSearchId(e.target.value)} />
          </Grid>
          <Grid item xs={12} sm={6} md={3} lg={2}>
            <TextField fullWidth label="Serial Number" size="small" value={searchSn} onChange={e => setSearchSn(e.target.value)} />
          </Grid>
          <Grid item xs={12} sm={6} md={3} lg={2}>
            <TextField fullWidth label="Member User" size="small" value={searchMember} onChange={e => setSearchMember(e.target.value)} />
          </Grid>
          <Grid item xs={12} sm={6} md={3} lg={2}>
            <TextField fullWidth label="Total Amount" size="small" value={searchAmount} onChange={e => setSearchAmount(e.target.value)} />
          </Grid>
          <Grid item xs={12} sm={6} md={3} lg={2}>
            <FormControl fullWidth size="small">
              <InputLabel>Payment Type</InputLabel>
              <Select value={searchPayType} label="Payment Type" onChange={e => setSearchPayType(e.target.value as any)}>
                <MenuItem value=""><em>All</em></MenuItem>
                <MenuItem value={0}>Unpaid</MenuItem>
                <MenuItem value={1}>Alipay</MenuItem>
                <MenuItem value={2}>Razorpay</MenuItem>
              </Select>
            </FormControl>
          </Grid>
          <Grid item xs={12} sm={6} md={3} lg={2}>
            <FormControl fullWidth size="small">
              <InputLabel>Status</InputLabel>
              <Select value={searchStatus} label="Status" onChange={e => setSearchStatus(e.target.value as any)}>
                <MenuItem value=""><em>All</em></MenuItem>
                <MenuItem value={0}>Unpaid</MenuItem>
                <MenuItem value={1}>Paid/To Ship</MenuItem>
                <MenuItem value={2}>Shipped</MenuItem>
                <MenuItem value={5}>Out for Delivery</MenuItem>
                <MenuItem value={3}>Completed</MenuItem>
                <MenuItem value={6}>Refunded</MenuItem>
                <MenuItem value={4}>Canceled</MenuItem>
              </Select>
            </FormControl>
          </Grid>
          <Grid item xs={12} sm={6} md={3} lg={2}>
            <TextField fullWidth label="Created Date (YYYY-MM-DD)" size="small" value={searchDate} onChange={e => setSearchDate(e.target.value)} />
          </Grid>
          <Grid item xs={12} sm={6} md={3} lg={2}>
            <Button variant="outlined" color="secondary" fullWidth onClick={resetSearch}>Reset</Button>
          </Grid>
        </Grid>
      </Paper>

      <TableContainer component={Paper} sx={{ mt: 3 }} elevation={0} variant="outlined">
        <Table>
          <TableHead>
            <TableRow sx={{ bgcolor: 'grey.50' }}>
              <TableCell>Order ID</TableCell>
              <TableCell>Order Serial Number</TableCell>
              <TableCell>Member User</TableCell>
              <TableCell>Total Amount</TableCell>
              <TableCell>Payment Type</TableCell>
              <TableCell>Status</TableCell>
              <TableCell>Created Date</TableCell>
              <TableCell align="right">Action</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {filteredOrders.map((order) => (
              <TableRow key={order.id} hover>
                <TableCell>{order.id}</TableCell>
                <TableCell>{order.orderSn}</TableCell>
                <TableCell>{order.memberUsername}</TableCell>
                <TableCell>₹{order.totalAmount?.toFixed(2)}</TableCell>
                <TableCell>{order.payType === 1 ? 'Alipay' : order.payType === 2 ? 'Razorpay' : 'Unpaid'}</TableCell>
                <TableCell>{getStatusChip(order.status)}</TableCell>
                <TableCell>{new Date(order.createTime).toLocaleString()}</TableCell>
                <TableCell align="right">
                  <Button size="small" variant="outlined" onClick={(e) => handleUpdateClick(e, order)}>Update Status</Button>
                </TableCell>
              </TableRow>
            ))}
            {filteredOrders.length === 0 && (
              <TableRow>
                <TableCell colSpan={8} align="center" sx={{ py: 5 }}>No orders found.</TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </TableContainer>

      <Menu
        anchorEl={anchorEl}
        open={Boolean(anchorEl)}
        onClose={handleClose}
      >
        <MenuItem onClick={() => handleStatusChange(1)}>Mark as Paid/To Ship</MenuItem>
        <MenuItem onClick={() => handleStatusChange(2)}>Mark as Shipped</MenuItem>
        <MenuItem onClick={() => handleStatusChange(5)}>Mark as Out for Delivery</MenuItem>
        <MenuItem onClick={() => handleStatusChange(3)}>Mark as Completed</MenuItem>
        <MenuItem onClick={() => handleStatusChange(6)}>Mark as Refunded</MenuItem>
        <MenuItem onClick={() => handleStatusChange(4)}>Mark as Canceled</MenuItem>
      </Menu>
    </Card>
  );
};

export default OrderList;
