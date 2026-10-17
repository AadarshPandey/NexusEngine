import React, { useEffect, useState } from 'react';
import { Card, Typography, Paper, Grid, Box, Button, TextField, Chip, Divider } from '@mui/material';
import { useParams, useNavigate } from 'react-router';
import { getReturnApplyByIdAPI, returnApplyUpdateStatusAPI } from '@/apis/ordering';
import type { OmsOrderReturnApplyResult } from '@/types/ordering';
import { resolveImageUrl } from '@/utils/media';

const ReturnApplyDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [comment, setDetail] = useState<OmsOrderReturnApplyResult | null>(null);
  const [reviewNote, setHandleNote] = useState('');
  const [refundAmount, setReturnAmount] = useState<number>(0);

  useEffect(() => {
    if (id) fetchDetail(parseInt(id));
  }, [id]);

  const fetchDetail = async (applyId: number) => {
    try {
      const res: any = await getReturnApplyByIdAPI(applyId);
      if (res) {
        setDetail(res);
        setReturnAmount(res.refundAmount ?? res.returnAmount ?? 0);
        setHandleNote(res.reviewNote || res.handleNote || '');
      }
    } catch (e) {
      console.error(e);
    }
  };

  const handleUpdateStatus = async (status: number) => {
    if (!comment) return;
    try {
      await returnApplyUpdateStatusAPI(comment.id, {
        id: comment.id,
        returnAddressId: 1, // Default address ID
        refundAmount: refundAmount,
        reviewNote: reviewNote,
        reviewedByAdminId: 'admin',
        receiptNote: reviewNote,
        receivedByAdminId: 'admin',
        status: status
      });
      alert('Status updated successfully!');
      fetchDetail(comment.id);
    } catch (e) {
      console.error(e);
      alert('Failed to update status');
    }
  };

  if (!comment) return <Typography sx={{ p: 3 }}>Loading...</Typography>;

  const c: any = comment;

  return (
    <Card sx={{ p: 3, m: 3 }}>
      <Typography variant="h5" gutterBottom>Return Application Details</Typography>
      
      <Paper sx={{ p: 3, mb: 3 }} variant="outlined">
        <Typography variant="h6" gutterBottom>Basic Information</Typography>
        <Grid container spacing={2}>
          <Grid size={{xs: 12, sm: 6}}>
            <Typography color="text.secondary">Service ID: <Typography component="span" color="text.primary">{comment.id}</Typography></Typography>
          </Grid>
          <Grid size={{xs: 12, sm: 6}}>
            <Typography color="text.secondary">Order SN: <Typography component="span" color="text.primary">{comment.orderNumber || c.orderSn}</Typography></Typography>
          </Grid>
          <Grid size={{xs: 12, sm: 6}}>
            <Typography component="div" color="text.secondary">Status: 
              {comment.status === 0 ? <Chip label="Pending" color="warning" size="small" sx={{ ml: 1 }} /> : 
               comment.status === 1 ? <Chip label="Returning" color="info" size="small" sx={{ ml: 1 }} /> : 
               comment.status === 2 ? <Chip label="Completed" color="success" size="small" sx={{ ml: 1 }} /> : 
               <Chip label="Rejected" color="error" size="small" sx={{ ml: 1 }} />}
            </Typography>
          </Grid>
          <Grid size={{xs: 12, sm: 6}}>
            <Typography color="text.secondary">Applied Time: <Typography component="span" color="text.primary">{(c.createTime || comment.createdAt) ? new Date(c.createTime || comment.createdAt).toLocaleString() : '-'}</Typography></Typography>
          </Grid>
        </Grid>
      </Paper>

      <Paper sx={{ p: 3, mb: 3 }} variant="outlined">
        <Typography variant="h6" gutterBottom>Product Information</Typography>
        <Box sx={{ display: 'flex', gap: 3, alignItems: 'center' }}>
          <img src={resolveImageUrl(comment.productImageUrl || c.productPic)} alt={comment.productName} style={{ width: 100, height: 100, objectFit: 'contain' }} />
          <Box>
            <Typography variant="subtitle1" fontWeight="bold">{comment.productName}</Typography>
            <Typography color="text.secondary">Brand: {comment.brandName || c.productBrand}</Typography>
            <Typography color="text.secondary">Attributes: {typeof comment.skuAttributes === 'object' ? JSON.stringify(comment.skuAttributes) : (comment.skuAttributes || c.productAttr || '-')}</Typography>
            <Typography color="text.secondary">Quantity: {comment.quantity ?? c.productCount}</Typography>
            <Typography color="text.secondary">Price Paid: ₹{Number(comment.unitRefundAmount ?? c.productRealPrice ?? 0).toFixed(2)}</Typography>
          </Box>
        </Box>
      </Paper>

      <Paper sx={{ p: 3, mb: 3 }} variant="outlined">
        <Typography variant="h6" gutterBottom>Customer Request</Typography>
        <Typography color="error.main" fontWeight="bold" gutterBottom>Reason: {c.reason || comment.reasonId}</Typography>
        <Typography color="text.secondary">Description: {comment.customerComment || c.description || 'No additional comment provided.'}</Typography>
      </Paper>

      <Paper sx={{ p: 3, mb: 3 }} variant="outlined">
        <Typography variant="h6" gutterBottom>Processing</Typography>
        <Grid container spacing={3}>
          <Grid size={{xs: 12, sm: 6}}>
            <TextField 
              fullWidth 
              label="Refund Amount (₹)" 
              type="number"
              value={Number.isNaN(refundAmount) ? '' : refundAmount} 
              onChange={e => setReturnAmount(e.target.value ? parseFloat(e.target.value) : 0)}
              disabled={comment.status !== 0}
            />
          </Grid>
          <Grid size={{ xs: 12 }}>
            <TextField 
              fullWidth 
              multiline 
              rows={3} 
              label="Handling Note" 
              value={reviewNote} 
              onChange={e => setHandleNote(e.target.value)}
              disabled={comment.status !== 0 && comment.status !== 1}
            />
          </Grid>
        </Grid>

        <Box sx={{ mt: 3, display: 'flex', gap: 2 }}>
          {comment.status === 0 && (
            <>
              <Button variant="contained" color="info" onClick={() => handleUpdateStatus(1)}>Approve (Wait for return)</Button>
              <Button variant="contained" color="error" onClick={() => handleUpdateStatus(3)}>Reject Application</Button>
            </>
          )}
          {comment.status === 1 && (
            <Button variant="contained" color="success" onClick={() => handleUpdateStatus(2)}>Confirm Receipt & Complete Refund</Button>
          )}
          <Button variant="outlined" onClick={() => navigate('/ordering/return-requests')}>Back to List</Button>
        </Box>
      </Paper>
    </Card>
  );
};

export default ReturnApplyDetail;
