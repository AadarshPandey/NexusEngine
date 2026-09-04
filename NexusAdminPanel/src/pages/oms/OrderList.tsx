import React, { useEffect, useState } from 'react';
import { Card, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography, Chip, Paper } from '@mui/material';
import { getOrderListAPI } from '@/apis/order';
import type { OmsOrder } from '@/types/order';

const OrderList: React.FC = () => {
  const [orders, setOrders] = useState<OmsOrder[]>([]);

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

  const getStatusChip = (status: number) => {
    switch(status) {
      case 0: return <Chip label="Unpaid" color="warning" size="small" />;
      case 1: return <Chip label="Paid/To Ship" color="info" size="small" />;
      case 2: return <Chip label="Shipped" color="primary" size="small" />;
      case 3: return <Chip label="Completed" color="success" size="small" />;
      case 4: return <Chip label="Canceled" color="error" size="small" />;
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
              </TableRow>
            ))}
            {orders.length === 0 && (
              <TableRow>
                <TableCell colSpan={7} align="center" sx={{ py: 5 }}>No orders found.</TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </TableContainer>
    </Card>
  );
};

export default OrderList;
