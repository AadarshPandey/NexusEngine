import React, { useEffect, useState } from 'react';
import { Card, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography, Chip, Paper, Button } from '@mui/material';
import { getReturnApplyListAPI } from '@/apis/ordering';
import type { OmsOrderReturnApply } from '@/types/ordering';
import { useNavigate } from 'react-router';

const ReturnApplyList: React.FC = () => {
  const [applies, setApplies] = useState<OmsOrderReturnApply[]>([]);
  const navigate = useNavigate();

  useEffect(() => {
    fetchApplies();
  }, []);

  const fetchApplies = async () => {
    try {
      const res = await getReturnApplyListAPI({ pageNum: 1, pageSize: 50 });
      const list = Array.isArray(res) ? res : (res as any)?.content || [];
      const sortedList = list.slice().sort((a: any, b: any) => new Date(b.createTime || b.createdAt).getTime() - new Date(a.createTime || a.createdAt).getTime());
      setApplies(sortedList);
    } catch (e) {
      console.error(e);
    }
  };

  const getStatusChip = (status: number) => {
    switch(status) {
      case 0: return <Chip label="Pending" color="warning" size="small" />;
      case 1: return <Chip label="Returning" color="info" size="small" />;
      case 2: return <Chip label="Completed" color="success" size="small" />;
      case 3: return <Chip label="Rejected" color="error" size="small" />;
      default: return <Chip label="Unknown" size="small" />;
    }
  };

  return (
    <Card sx={{ p: 3, m: 3 }}>
      <Typography variant="h5" gutterBottom>Return Applications</Typography>
      <TableContainer component={Paper} sx={{ mt: 3 }} elevation={0} variant="outlined">
        <Table>
          <TableHead>
            <TableRow sx={{ bgcolor: 'grey.50' }}>
              <TableCell>Service ID</TableCell>
              <TableCell>Applied Time</TableCell>
              <TableCell>Return Amount</TableCell>
              <TableCell>Application Status</TableCell>
              <TableCell>Processing Time</TableCell>
              <TableCell>Action</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {applies.map((apply: any) => {
              const appliedAt = apply.createTime || apply.createdAt;
              const reviewedAt = apply.handleTime || apply.reviewedAt;
              const amount = Number(apply.refundAmount ?? apply.returnAmount ?? 0);
              return (
                <TableRow key={apply.id} hover>
                  <TableCell>{apply.id}</TableCell>
                  <TableCell>{appliedAt ? new Date(appliedAt).toLocaleString() : '-'}</TableCell>
                  <TableCell>₹{amount.toFixed(2)}</TableCell>
                  <TableCell>{getStatusChip(apply.status)}</TableCell>
                  <TableCell>{reviewedAt ? new Date(reviewedAt).toLocaleString() : 'N/A'}</TableCell>
                  <TableCell>
                    <Button size="small" variant="text" onClick={() => navigate(`/ordering/return-requests/${apply.id}`)}>View Details</Button>
                  </TableCell>
                </TableRow>
              );
            })}
            {applies.length === 0 && (
              <TableRow>
                <TableCell colSpan={6} align="center" sx={{ py: 5 }}>No return applications found.</TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </TableContainer>
    </Card>
  );
};

export default ReturnApplyList;
