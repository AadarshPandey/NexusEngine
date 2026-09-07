import React, { useEffect, useState } from 'react';
import { Card, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography, Switch, Paper, Button, Box, TextField, Dialog, DialogTitle, DialogContent, DialogActions } from '@mui/material';
import { useNavigate } from 'react-router';
import { getFlashListAPI, flashUpdateStatusByIdAPI, flashDeleteByIdAPI, flashCreateAPI, flashUpdateByIdAPI } from '@/apis/flash';
import type { SmsFlashPromotion } from '@/types/flash';
import dayjs from 'dayjs';

const FlashList: React.FC = () => {
  const navigate = useNavigate();
  const [list, setList] = useState<SmsFlashPromotion[]>([]);
  const [open, setOpen] = useState(false);
  const [editData, setEditData] = useState<SmsFlashPromotion | null>(null);

  useEffect(() => {
    fetchList();
  }, []);

  const fetchList = async () => {
    try {
      const res = await getFlashListAPI({ pageNum: 1, pageSize: 100 });
      setList(res.data.list || []);
    } catch (e) {
      console.error(e);
    }
  };

  const handleStatusChange = async (id: number, status: number) => {
    try {
      await flashUpdateStatusByIdAPI(id, { status: status === 1 ? 0 : 1 });
      fetchList();
    } catch (e) {
      console.error(e);
    }
  };

  const handleDelete = async (id: number) => {
    if(confirm('Delete this flash sale campaign?')) {
      try {
        await flashDeleteByIdAPI(id);
        fetchList();
      } catch (e) {
        console.error(e);
      }
    }
  };

  const handleSave = async () => {
    if (!editData) return;
    try {
      if (editData.id) {
        await flashUpdateByIdAPI(editData.id, editData);
      } else {
        await flashCreateAPI(editData);
      }
      setOpen(false);
      fetchList();
    } catch (e) {
      console.error(e);
    }
  };

  const openDialog = (data?: SmsFlashPromotion) => {
    setEditData(data || { title: '', startDate: '', endDate: '', status: 0 });
    setOpen(true);
  };

  return (
    <Box sx={{ p: 3 }}>
      <Card sx={{ p: 3, mb: 3 }}>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <Typography variant="h5">Flash Sale Campaigns (Redis Sync)</Typography>
          <Button variant="contained" color="primary" onClick={() => openDialog()}>Add Campaign</Button>
        </Box>
      </Card>
      
      <TableContainer component={Paper} elevation={0} variant="outlined">
        <Table>
          <TableHead>
            <TableRow sx={{ bgcolor: 'background.default' }}>
              <TableCell>ID</TableCell>
              <TableCell>Campaign Title</TableCell>
              <TableCell>Active Status</TableCell>
              <TableCell>Start Date</TableCell>
              <TableCell>End Date</TableCell>
              <TableCell align="center">Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {list.map((row) => (
              <TableRow key={row.id} hover>
                <TableCell>{row.id}</TableCell>
                <TableCell>{row.title}</TableCell>
                <TableCell>
                  <Switch size="small" checked={row.status === 1} onChange={() => handleStatusChange(row.id!, row.status)} />
                </TableCell>
                <TableCell>{row.startDate ? dayjs(row.startDate).format('YYYY-MM-DD') : 'N/A'}</TableCell>
                <TableCell>{row.endDate ? dayjs(row.endDate).format('YYYY-MM-DD') : 'N/A'}</TableCell>
                <TableCell align="center">
                  <Button size="small" onClick={() => navigate(`/sms/selectSession?flashPromotionId=${row.id}`)}>Manage Sessions</Button>
                  <Button size="small" color="primary" onClick={() => openDialog(row)}>Edit</Button>
                  <Button size="small" color="error" onClick={() => handleDelete(row.id!)}>Delete</Button>
                </TableCell>
              </TableRow>
            ))}
            {list.length === 0 && (
              <TableRow>
                <TableCell colSpan={6} align="center" sx={{ py: 5 }}>No campaigns found.</TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </TableContainer>

      <Dialog open={open} onClose={() => setOpen(false)} maxWidth="sm" fullWidth>
        <DialogTitle>{editData?.id ? 'Edit Campaign' : 'Add Campaign'}</DialogTitle>
        <DialogContent>
          <TextField
            fullWidth margin="normal" label="Campaign Title"
            value={editData?.title || ''}
            onChange={(e) => setEditData(prev => ({ ...prev!, title: e.target.value }))}
          />
          <TextField
            fullWidth margin="normal" label="Start Date (YYYY-MM-DD)"
            value={editData?.startDate?.split('T')[0] || ''}
            onChange={(e) => setEditData(prev => ({ ...prev!, startDate: e.target.value }))}
          />
          <TextField
            fullWidth margin="normal" label="End Date (YYYY-MM-DD)"
            value={editData?.endDate?.split('T')[0] || ''}
            onChange={(e) => setEditData(prev => ({ ...prev!, endDate: e.target.value }))}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpen(false)}>Cancel</Button>
          <Button onClick={handleSave} variant="contained">Save</Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default FlashList;
