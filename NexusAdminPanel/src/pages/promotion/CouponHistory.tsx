import React, { useEffect, useState } from 'react';
import { Card, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography, Chip, Paper, Button, Box } from '@mui/material';
import { getCouponHistoryListAPI } from '@/apis/promotion';
import { useParams, useSearchParams, useNavigate } from 'react-router';

const CouponHistory: React.FC = () => {
  const { id: paramId } = useParams();
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const id = paramId || searchParams.get('couponId');
  const [history, setHistory] = useState<import('@/types/promotion').SmsCouponHistory[]>([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (id) {
      fetchHistory(Number(id));
    }
  }, [id]);

  const fetchHistory = async (couponId: number) => {
    try {
      setLoading(true);
      const res = await getCouponHistoryListAPI({ couponId, pageNum: 1, pageSize: 50 } as unknown as import('@/types/promotion').CouponHistoryQueryParam);
      setHistory(Array.isArray(res) ? res : (res as any)?.content || []);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  const getUseStatusChip = (status: number) => {
    switch(status) {
      case 0: return <Chip label="Unused" color="info" size="small" />;
      case 1: return <Chip label="Used" color="success" size="small" />;
      case 2: return <Chip label="Expired" color="default" size="small" />;
      default: return <Chip label="Unknown" size="small" />;
    }
  };

  return (
    <Card sx={{ p: 3, m: 3 }}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
        <Typography variant="h5">Coupon Claim History</Typography>
        <Button variant="outlined" onClick={() => navigate('/promotion/coupons')}>Back to Coupons</Button>
      </Box>
      <TableContainer component={Paper} sx={{ mt: 2 }} elevation={0} variant="outlined">
        <Table>
          <TableHead>
            <TableRow sx={{ bgcolor: 'grey.50' }}>
              <TableCell>Coupon Code</TableCell>
              <TableCell>Member User</TableCell>
              <TableCell>Claim Type</TableCell>
              <TableCell>Claim Time</TableCell>
              <TableCell>Status</TableCell>
              <TableCell>Order Number</TableCell>
              <TableCell>Use Time</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {history.map((item: any) => (
              <TableRow key={item.id} hover>
                <TableCell>{item.couponCode}</TableCell>
                <TableCell>{item.memberNickname}</TableCell>
                <TableCell>{(item.getType ?? item.acquisitionChannel) === 1 ? 'Active Claim' : 'System Award'}</TableCell>
                <TableCell>{(item.createTime || item.claimedAt) ? new Date(item.createTime || item.claimedAt).toLocaleString() : 'N/A'}</TableCell>
                <TableCell>{getUseStatusChip(item.useStatus ?? item.status ?? 0)}</TableCell>
                <TableCell>{item.orderSn || 'N/A'}</TableCell>
                <TableCell>{(item.useTime || item.redeemedAt) ? new Date(item.useTime || item.redeemedAt).toLocaleString() : 'N/A'}</TableCell>
              </TableRow>
            ))}
            {history.length === 0 && (
              <TableRow>
                <TableCell colSpan={7} align="center" sx={{ py: 5 }}>No coupon history found.</TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </TableContainer>
    </Card>
  );
};

export default CouponHistory;
