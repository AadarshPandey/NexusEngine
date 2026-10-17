import React, { useEffect, useState } from 'react';
import {
  Box,
  Card,
  Typography,
  Paper,
  Grid,
  Button,
  Chip,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  CircularProgress,
} from '@mui/material';
import { useParams, useNavigate } from 'react-router';
import { getOrderDetailAPI } from '@/apis/ordering';
import { resolveImageUrl } from '@/utils/media';

const getStatusChip = (status?: number) => {
  switch (status) {
    case 0:
      return <Chip label="Unpaid" color="warning" size="small" />;
    case 1:
      return <Chip label="Paid / To Ship" color="info" size="small" />;
    case 2:
      return <Chip label="Shipped" color="primary" size="small" />;
    case 3:
      return <Chip label="Completed" color="success" size="small" />;
    case 4:
      return <Chip label="Canceled" color="error" size="small" />;
    case 5:
      return <Chip label="Out for Delivery" color="primary" variant="outlined" size="small" />;
    case 6:
      return <Chip label="Refunded" color="default" size="small" />;
    default:
      return <Chip label="Unknown" size="small" />;
  }
};

const OrderDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [detail, setDetail] = useState<any>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (id) {
      getOrderDetailAPI(Number(id))
        .then((res) => setDetail(res))
        .catch((err) => console.error('Failed to load order detail', err))
        .finally(() => setLoading(false));
    }
  }, [id]);

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', p: 8 }}>
        <CircularProgress />
      </Box>
    );
  }

  if (!detail) {
    return (
      <Box sx={{ p: 3 }}>
        <Typography>Order not found.</Typography>
        <Button sx={{ mt: 2 }} variant="outlined" onClick={() => navigate('/ordering/orders')}>
          Back to Orders
        </Button>
      </Box>
    );
  }

  const items: any[] = detail.orderItemList || detail.orderLines || [];
  const historyList: any[] = detail.historyList || [];

  return (
    <Card sx={{ p: 3, m: 3 }}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
        <Typography variant="h5">Order Detail — {detail.orderSn || detail.orderNumber}</Typography>
        <Button variant="outlined" onClick={() => navigate('/ordering/orders')}>
          Back to Orders
        </Button>
      </Box>

      <Paper sx={{ p: 3, mb: 3 }} variant="outlined">
        <Typography variant="h6" gutterBottom>Order Summary</Typography>
        <Grid container spacing={2}>
          <Grid size={{ xs: 12, sm: 4 }}>
            <Typography color="text.secondary">
              Order Number: <Typography component="span" color="text.primary">{detail.orderSn || detail.orderNumber}</Typography>
            </Typography>
          </Grid>
          <Grid size={{ xs: 12, sm: 4 }}>
            <Typography component="div" color="text.secondary">
              Status: <Box component="span" sx={{ ml: 1 }}>{getStatusChip(detail.status)}</Box>
            </Typography>
          </Grid>
          <Grid size={{ xs: 12, sm: 4 }}>
            <Typography color="text.secondary">
              Created: <Typography component="span" color="text.primary">{(detail.createTime || detail.createdAt) ? new Date(detail.createTime || detail.createdAt).toLocaleString() : '-'}</Typography>
            </Typography>
          </Grid>
          <Grid size={{ xs: 12, sm: 4 }}>
            <Typography color="text.secondary">
              Recipient: <Typography component="span" color="text.primary">{detail.receiverName || detail.recipientName || '-'}</Typography>
            </Typography>
          </Grid>
          <Grid size={{ xs: 12, sm: 4 }}>
            <Typography color="text.secondary">
              Phone: <Typography component="span" color="text.primary">{detail.receiverPhone || detail.recipientPhone || '-'}</Typography>
            </Typography>
          </Grid>
          <Grid size={{ xs: 12, sm: 4 }}>
            <Typography color="text.secondary">
              Carrier / Tracking: <Typography component="span" color="text.primary">{detail.deliveryCompany || '-'} ({detail.deliverySn || '-'})</Typography>
            </Typography>
          </Grid>
          <Grid size={{ xs: 12 }}>
            <Typography color="text.secondary">
              Shipping Address: <Typography component="span" color="text.primary">{detail.receiverDetailAddress || detail.shippingAddressLine || '-'} {detail.receiverPostCode || ''}</Typography>
            </Typography>
          </Grid>
        </Grid>
      </Paper>

      <Paper sx={{ p: 3, mb: 3 }} variant="outlined">
        <Typography variant="h6" gutterBottom>Order Items</Typography>
        <TableContainer>
          <Table size="small">
            <TableHead>
              <TableRow sx={{ bgcolor: 'grey.50' }}>
                <TableCell>Image</TableCell>
                <TableCell>Product</TableCell>
                <TableCell>Brand</TableCell>
                <TableCell>Unit Price</TableCell>
                <TableCell>Quantity</TableCell>
                <TableCell>Line Total</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {items.map((item, idx) => (
                <TableRow key={item.id || idx}>
                  <TableCell>
                    <Box
                      component="img"
                      src={resolveImageUrl(item.productImageUrl || item.productPic)}
                      alt={item.productName}
                      sx={{ width: 56, height: 56, objectFit: 'contain', border: '1px solid #eee', borderRadius: 1 }}
                    />
                  </TableCell>
                  <TableCell>{item.productName}</TableCell>
                  <TableCell>{item.brandName || item.productBrand || '-'}</TableCell>
                  <TableCell>₹{Number(item.unitPrice ?? item.productPrice ?? 0).toFixed(2)}</TableCell>
                  <TableCell>{item.quantity ?? item.productQuantity ?? 1}</TableCell>
                  <TableCell>₹{Number(item.lineTotalAmount ?? item.realAmount ?? 0).toFixed(2)}</TableCell>
                </TableRow>
              ))}
              {items.length === 0 && (
                <TableRow>
                  <TableCell colSpan={6} align="center">No line items recorded.</TableCell>
                </TableRow>
              )}
            </TableBody>
          </Table>
        </TableContainer>
      </Paper>

      <Paper sx={{ p: 3 }} variant="outlined">
        <Typography variant="h6" gutterBottom>Order Status History</Typography>
        <TableContainer>
          <Table size="small">
            <TableHead>
              <TableRow sx={{ bgcolor: 'grey.50' }}>
                <TableCell>Time</TableCell>
                <TableCell>Actor</TableCell>
                <TableCell>Status</TableCell>
                <TableCell>Note</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {historyList.map((h, idx) => (
                <TableRow key={h.id || idx}>
                  <TableCell>{(h.createTime || h.createdAt) ? new Date(h.createTime || h.createdAt).toLocaleString() : '-'}</TableCell>
                  <TableCell>{h.operateMan || h.actorRef || 'System'}</TableCell>
                  <TableCell>{getStatusChip(h.orderStatus ?? h.toStatus)}</TableCell>
                  <TableCell>{h.note || '-'}</TableCell>
                </TableRow>
              ))}
              {historyList.length === 0 && (
                <TableRow>
                  <TableCell colSpan={4} align="center">No status history recorded.</TableCell>
                </TableRow>
              )}
            </TableBody>
          </Table>
        </TableContainer>
      </Paper>
    </Card>
  );
};

export default OrderDetail;
