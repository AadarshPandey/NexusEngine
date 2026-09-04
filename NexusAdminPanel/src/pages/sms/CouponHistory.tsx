import React, { useEffect, useState } from 'react';
import { Card, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography, Chip, Paper } from '@mui/material';
import { getCouponHistoryListAPI } from '@/apis/coupon';

const CouponHistory: React.FC = () => {
  const [history, setHistory] = useState<any[]>([]);

  useEffect(() => {
    fetchHistory();
  }, []);

  const fetchHistory = async () => {
    try {
      const res = await getCouponHistoryListAPI({ pageNum: 1, pageSize: 50 } as any);
      // @ts-ignore
      setHistory(res.data?.list || []);
    } catch (e) {
      console.error(e);
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
      <Typography variant="h5" gutterBottom>Coupon Claim History</Typography>
      <TableContainer component={Paper} sx={{ mt: 3 }} elevation={0} variant="outlined">
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
            {history.map((item) => (
              <TableRow key={item.id} hover>
                <TableCell>{item.couponCode}</TableCell>
                <TableCell>{item.memberNickname}</TableCell>
                <TableCell>{item.getType === 1 ? 'Active Claim' : 'System Award'}</TableCell>
                <TableCell>{new Date(item.createTime).toLocaleString()}</TableCell>
                <TableCell>{getUseStatusChip(item.useStatus)}</TableCell>
                <TableCell>{item.orderSn || 'N/A'}</TableCell>
                <TableCell>{item.useTime ? new Date(item.useTime).toLocaleString() : 'N/A'}</TableCell>
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
