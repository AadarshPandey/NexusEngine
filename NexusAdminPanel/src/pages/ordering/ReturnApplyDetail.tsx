import React, { useEffect, useState } from 'react';
import {
  Card,
  Typography,
  Paper,
  Grid,
  Box,
  Button,
  TextField,
  Chip,
  MenuItem,
  FormControl,
  InputLabel,
  Select,
} from '@mui/material';
import { useParams, useNavigate } from 'react-router';
import { getReturnApplyByIdAPI, returnApplyUpdateStatusAPI } from '@/apis/ordering';
import { getCompanyAddressListAllAPI } from '@/apis/ordering/merchantAddress';
import type { OmsOrderReturnApplyResult, MerchantAddress } from '@/types/ordering';
import { resolveImageUrl } from '@/utils/media';

const ReturnApplyDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [comment, setDetail] = useState<OmsOrderReturnApplyResult | null>(null);
  const [addresses, setAddresses] = useState<MerchantAddress[]>([]);
  const [selectedAddressId, setSelectedAddressId] = useState<number>(1);
  const [reviewNote, setHandleNote] = useState('');
  const [refundAmount, setReturnAmount] = useState<number>(0);

  useEffect(() => {
    if (id) fetchDetail(parseInt(id));
    loadAddresses();
  }, [id]);

  const loadAddresses = async () => {
    try {
      const res = await getCompanyAddressListAllAPI();
      if (res && res.length > 0) {
        setAddresses(res);
        const defaultReturn = res.find((a: any) => a.receiveStatus === 1 || a.isDefaultReturnTo);
        if (defaultReturn) {
          setSelectedAddressId(defaultReturn.id);
        } else {
          setSelectedAddressId(res[0].id);
        }
      }
    } catch (e) {
      console.error('Failed to load merchant addresses', e);
    }
  };

  const fetchDetail = async (applyId: number) => {
    try {
      const res: any = await getReturnApplyByIdAPI(applyId);
      if (res) {
        setDetail(res);
        setReturnAmount(res.refundAmount ?? res.returnAmount ?? 0);
        setHandleNote(res.reviewNote || res.handleNote || '');
        if (res.companyAddressId || res.returnAddressId) {
          setSelectedAddressId(res.companyAddressId || res.returnAddressId);
        }
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
        returnAddressId: selectedAddressId,
        companyAddressId: selectedAddressId,
        refundAmount: refundAmount,
        returnAmount: refundAmount,
        reviewNote: reviewNote,
        handleNote: reviewNote,
        reviewedByAdminId: 'admin',
        handleMan: 'admin',
        receiptNote: reviewNote,
        receiveNote: reviewNote,
        receivedByAdminId: 'admin',
        receiveMan: 'admin',
        status: status,
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
  const proofList: string[] = Array.isArray(c.evidenceUrls)
    ? c.evidenceUrls
    : typeof (c.evidenceUrls || c.proofPics) === 'string' && (c.evidenceUrls || c.proofPics).trim()
      ? (c.evidenceUrls || c.proofPics).split(',').map((s: string) => s.trim()).filter(Boolean)
      : [];

  const assignedAddress = addresses.find((a) => a.id === selectedAddressId) || c.companyAddress;

  return (
    <Card sx={{ p: 3, m: 3 }}>
      <Typography variant="h5" gutterBottom>Return Application Details</Typography>

      <Paper sx={{ p: 3, mb: 3 }} variant="outlined">
        <Typography variant="h6" gutterBottom>Basic Information</Typography>
        <Grid container spacing={2}>
          <Grid size={{ xs: 12, sm: 6 }}>
            <Typography color="text.secondary">Service ID: <Typography component="span" color="text.primary">{comment.id}</Typography></Typography>
          </Grid>
          <Grid size={{ xs: 12, sm: 6 }}>
            <Typography color="text.secondary">Order SN: <Typography component="span" color="text.primary">{comment.orderNumber || c.orderSn}</Typography></Typography>
          </Grid>
          <Grid size={{ xs: 12, sm: 6 }}>
            <Typography component="div" color="text.secondary">Status: 
              {comment.status === 0 ? <Chip label="Pending Review" color="warning" size="small" sx={{ ml: 1 }} /> : 
               comment.status === 1 ? <Chip label="Approved (Returning)" color="info" size="small" sx={{ ml: 1 }} /> : 
               comment.status === 2 ? <Chip label="Completed & Refunded" color="success" size="small" sx={{ ml: 1 }} /> : 
               <Chip label="Rejected" color="error" size="small" sx={{ ml: 1 }} />}
            </Typography>
          </Grid>
          <Grid size={{ xs: 12, sm: 6 }}>
            <Typography color="text.secondary">Applied Time: <Typography component="span" color="text.primary">{(c.createTime || comment.createdAt) ? new Date(c.createTime || comment.createdAt).toLocaleString() : '-'}</Typography></Typography>
          </Grid>
          <Grid size={{ xs: 12, sm: 6 }}>
            <Typography color="text.secondary">Customer Name: <Typography component="span" color="text.primary">{c.returnName || c.contactName || c.memberUsername || '-'}</Typography></Typography>
          </Grid>
          <Grid size={{ xs: 12, sm: 6 }}>
            <Typography color="text.secondary">Customer Phone: <Typography component="span" color="text.primary">{c.returnPhone || c.contactPhone || '-'}</Typography></Typography>
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
        <Typography variant="h6" gutterBottom>Customer Request & Evidence</Typography>
        <Typography color="error.main" fontWeight="bold" gutterBottom>Reason: {c.reason || comment.reasonId}</Typography>
        <Typography color="text.secondary" sx={{ mb: 2 }}>Description: {comment.customerComment || c.description || 'No additional comment provided.'}</Typography>

        {proofList.length > 0 && (
          <Box sx={{ mt: 2 }}>
            <Typography variant="subtitle2" sx={{ mb: 1, fontWeight: 600 }}>Proof Photos / Evidence:</Typography>
            <Box sx={{ display: 'flex', gap: 2, flexWrap: 'wrap' }}>
              {proofList.map((url, i) => (
                <Box
                  key={i}
                  component="img"
                  src={resolveImageUrl(url)}
                  alt={`Proof ${i + 1}`}
                  sx={{ width: 100, height: 100, objectFit: 'cover', borderRadius: 1, border: '1px solid #ddd' }}
                />
              ))}
            </Box>
          </Box>
        )}
      </Paper>

      {/* Review & Processing History */}
      {(c.handleMan || c.reviewedByAdminId || c.receiveMan || c.receivedByAdminId) && (
        <Paper sx={{ p: 3, mb: 3, bgcolor: 'grey.50' }} variant="outlined">
          <Typography variant="h6" gutterBottom>Audit & Warehouse Info</Typography>
          <Grid container spacing={2}>
            {assignedAddress && (
              <Grid size={{ xs: 12 }}>
                <Typography color="text.secondary">
                  Assigned Return Address: <Typography component="span" fontWeight="500">{assignedAddress.addressName || assignedAddress.label} — {assignedAddress.detailAddress || assignedAddress.addressLine}, {assignedAddress.city}, {assignedAddress.state} (Contact: {assignedAddress.name || assignedAddress.contactName} {assignedAddress.phone || assignedAddress.contactPhone})</Typography>
                </Typography>
              </Grid>
            )}
            {(c.handleMan || c.reviewedByAdminId) && (
              <>
                <Grid size={{ xs: 12, sm: 6 }}>
                  <Typography color="text.secondary">Reviewed By: <Typography component="span" color="text.primary">{c.handleMan || c.reviewedByAdminId}</Typography></Typography>
                </Grid>
                <Grid size={{ xs: 12, sm: 6 }}>
                  <Typography color="text.secondary">Reviewed At: <Typography component="span" color="text.primary">{c.handleTime || c.reviewedAt ? new Date(c.handleTime || c.reviewedAt).toLocaleString() : '-'}</Typography></Typography>
                </Grid>
                <Grid size={{ xs: 12 }}>
                  <Typography color="text.secondary">Review Note: <Typography component="span" color="text.primary">{c.handleNote || c.reviewNote || '-'}</Typography></Typography>
                </Grid>
              </>
            )}
            {(c.receiveMan || c.receivedByAdminId) && (
              <>
                <Grid size={{ xs: 12, sm: 6 }}>
                  <Typography color="text.secondary">Received By: <Typography component="span" color="text.primary">{c.receiveMan || c.receivedByAdminId}</Typography></Typography>
                </Grid>
                <Grid size={{ xs: 12, sm: 6 }}>
                  <Typography color="text.secondary">Received At: <Typography component="span" color="text.primary">{c.receiveTime || c.receivedAt ? new Date(c.receiveTime || c.receivedAt).toLocaleString() : '-'}</Typography></Typography>
                </Grid>
                <Grid size={{ xs: 12 }}>
                  <Typography color="text.secondary">Receipt Note: <Typography component="span" color="text.primary">{c.receiveNote || c.receiptNote || '-'}</Typography></Typography>
                </Grid>
              </>
            )}
          </Grid>
        </Paper>
      )}

      <Paper sx={{ p: 3, mb: 3 }} variant="outlined">
        <Typography variant="h6" gutterBottom>Processing Actions</Typography>
        <Grid container spacing={3}>
          <Grid size={{ xs: 12, sm: 6 }}>
            <FormControl fullWidth size="small">
              <InputLabel id="warehouse-select-label">Return Warehouse Address</InputLabel>
              <Select
                labelId="warehouse-select-label"
                label="Return Warehouse Address"
                value={selectedAddressId}
                onChange={(e) => setSelectedAddressId(Number(e.target.value))}
                disabled={comment.status !== 0}
              >
                {addresses.map((addr) => (
                  <MenuItem key={addr.id} value={addr.id}>
                    {addr.addressName || addr.label} ({addr.city}, {addr.state})
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
          </Grid>
          <Grid size={{ xs: 12, sm: 6 }}>
            <TextField 
              fullWidth 
              size="small"
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
              label="Handling / Review Note" 
              value={reviewNote} 
              onChange={e => setHandleNote(e.target.value)}
              disabled={comment.status !== 0 && comment.status !== 1}
            />
          </Grid>
        </Grid>

        <Box sx={{ mt: 3, display: 'flex', gap: 2 }}>
          {comment.status === 0 && (
            <>
              <Button variant="contained" color="info" onClick={() => handleUpdateStatus(1)}>Approve (Assign Return Warehouse)</Button>
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

