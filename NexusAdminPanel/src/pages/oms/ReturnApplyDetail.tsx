import React, { useEffect, useState } from 'react';
import { Card, Typography, Paper, Grid, Box, Button, TextField, Chip, Divider } from '@mui/material';
import { useParams, useNavigate } from 'react-router';
import { getReturnApplyByIdAPI, returnApplyUpdateStatusAPI } from '@/apis/returnApply';
import type { OmsOrderReturnApplyResult } from '@/types/returnApply';

const ReturnApplyDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [detail, setDetail] = useState<OmsOrderReturnApplyResult | null>(null);
  const [handleNote, setHandleNote] = useState('');
  const [returnAmount, setReturnAmount] = useState<number>(0);

  useEffect(() => {
    if (id) fetchDetail(parseInt(id));
  }, [id]);

  const fetchDetail = async (applyId: number) => {
    try {
      const res = await getReturnApplyByIdAPI(applyId);
      setDetail(res.data);
      setReturnAmount(res.data.returnAmount || 0);
      setHandleNote(res.data.handleNote || '');
    } catch (e) {
      console.error(e);
    }
  };

  const handleUpdateStatus = async (status: number) => {
    if (!detail) return;
    try {
      await returnApplyUpdateStatusAPI(detail.id, {
        id: detail.id,
        companyAddressId: 1, // Default address ID
        returnAmount: returnAmount,
        handleNote: handleNote,
        handleMan: 'admin',
        receiveNote: handleNote,
        receiveMan: 'admin',
        status: status
      });
      alert('Status updated successfully!');
      fetchDetail(detail.id);
    } catch (e) {
      console.error(e);
      alert('Failed to update status');
    }
  };

  if (!detail) return <Typography sx={{ p: 3 }}>Loading...</Typography>;

  return (
    <Card sx={{ p: 3, m: 3 }}>
      <Typography variant="h5" gutterBottom>Return Application Details</Typography>
      
      <Paper sx={{ p: 3, mb: 3 }} variant="outlined">
        <Typography variant="h6" gutterBottom>Basic Information</Typography>
        <Grid container spacing={2}>
          <Grid size={{xs: 12, sm: 6}}>
            <Typography color="text.secondary">Service ID: <Typography component="span" color="text.primary">{detail.id}</Typography></Typography>
          </Grid>
          <Grid size={{xs: 12, sm: 6}}>
            <Typography color="text.secondary">Order SN: <Typography component="span" color="text.primary">{detail.orderSn}</Typography></Typography>
          </Grid>
          <Grid size={{xs: 12, sm: 6}}>
            <Typography color="text.secondary">Customer: <Typography component="span" color="text.primary">{detail.memberUsername}</Typography></Typography>
          </Grid>
          <Grid size={{xs: 12, sm: 6}}>
            <Typography color="text.secondary">Status: 
              {detail.status === 0 ? <Chip label="Pending" color="warning" size="small" sx={{ ml: 1 }} /> : 
               detail.status === 1 ? <Chip label="Returning" color="info" size="small" sx={{ ml: 1 }} /> : 
               detail.status === 2 ? <Chip label="Completed" color="success" size="small" sx={{ ml: 1 }} /> : 
               <Chip label="Rejected" color="error" size="small" sx={{ ml: 1 }} />}
            </Typography>
          </Grid>
          <Grid size={{xs: 12, sm: 6}}>
            <Typography color="text.secondary">Applied Time: <Typography component="span" color="text.primary">{new Date(detail.createTime).toLocaleString()}</Typography></Typography>
          </Grid>
        </Grid>
      </Paper>

      <Paper sx={{ p: 3, mb: 3 }} variant="outlined">
        <Typography variant="h6" gutterBottom>Product Information</Typography>
        <Box sx={{ display: 'flex', gap: 3, alignItems: 'center' }}>
          <img src={detail.productPic} alt={detail.productName} style={{ width: 100, height: 100, objectFit: 'cover' }} />
          <Box>
            <Typography variant="subtitle1" fontWeight="bold">{detail.productName}</Typography>
            <Typography color="text.secondary">Brand: {detail.productBrand}</Typography>
            <Typography color="text.secondary">Attributes: {detail.productAttr}</Typography>
            <Typography color="text.secondary">Quantity: {detail.productCount}</Typography>
            <Typography color="text.secondary">Price Paid: ₹{detail.productRealPrice?.toFixed(2)}</Typography>
          </Box>
        </Box>
      </Paper>

      <Paper sx={{ p: 3, mb: 3 }} variant="outlined">
        <Typography variant="h6" gutterBottom>Customer Request</Typography>
        <Typography color="error.main" fontWeight="bold" gutterBottom>Reason: {detail.reason}</Typography>
        <Typography color="text.secondary">Description: {detail.description || 'No additional description provided.'}</Typography>
      </Paper>

      <Paper sx={{ p: 3, mb: 3 }} variant="outlined">
        <Typography variant="h6" gutterBottom>Processing</Typography>
        <Grid container spacing={3}>
          <Grid size={{xs: 12, sm: 6}}>
            <TextField 
              fullWidth 
              label="Refund Amount (₹)" 
              type="number"
              value={returnAmount} 
              onChange={e => setReturnAmount(parseFloat(e.target.value))}
              disabled={detail.status !== 0}
            />
          </Grid>
          <Grid size={{ xs: 12 }}>
            <TextField 
              fullWidth 
              multiline 
              rows={3} 
              label="Handling Note" 
              value={handleNote} 
              onChange={e => setHandleNote(e.target.value)}
              disabled={detail.status !== 0 && detail.status !== 1}
            />
          </Grid>
        </Grid>

        <Box sx={{ mt: 3, display: 'flex', gap: 2 }}>
          {detail.status === 0 && (
            <>
              <Button variant="contained" color="info" onClick={() => handleUpdateStatus(1)}>Approve (Wait for return)</Button>
              <Button variant="contained" color="error" onClick={() => handleUpdateStatus(3)}>Reject Application</Button>
            </>
          )}
          {detail.status === 1 && (
            <Button variant="contained" color="success" onClick={() => handleUpdateStatus(2)}>Confirm Receipt & Complete Refund</Button>
          )}
          <Button variant="outlined" onClick={() => navigate('/oms/returnApply')}>Back to List</Button>
        </Box>
      </Paper>
    </Card>
  );
};

export default ReturnApplyDetail;
