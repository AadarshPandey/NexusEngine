import React, { useEffect, useState } from 'react';
import { Card, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography, Chip, Paper, Button, Menu, MenuItem } from '@mui/material';
import { getOrderListAPI, orderUpdateStatusAPI } from '@/apis/order';
import type { OmsOrder } from '@/types/order';

const OrderList: React.FC = () => {
  const [orders, setOrders] = useState<OmsOrder[]>([]);
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
  const [selectedOrder, setSelectedOrder] = useState<OmsOrder | null>(null);

  useEffect(() => {
    fetchOrders();
  }, []);

  const fetchOrders = async () => {
    try {
      const res = await getOrderListAPI({ pageNum: 1, pageSize: 50 });
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

  return (
    <Card sx={{ p: 3, m: 3 }}>
      <Typography variant="h5" gutterBottom>Order Management</Typography>
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
            {orders.map((order) => (
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
            {orders.length === 0 && (
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
