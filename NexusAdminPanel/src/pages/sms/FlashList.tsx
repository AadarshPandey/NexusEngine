import React, { useEffect, useState } from 'react';
import { Card, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography, Switch, Paper, Button, Box, TextField, Dialog, DialogTitle, DialogContent, DialogActions } from '@mui/material';
import { useNavigate } from 'react-router';
import { getFlashListAPI, flashUpdateStatusByIdAPI, flashDeleteByIdAPI, flashCreateAPI, flashUpdateByIdAPI } from '@/apis/flash';
import type { SmsFlashPromotion } from '@/types/flash';
import dayjs from 'dayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';

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
    if (confirm('Delete this flash sale campaign?')) {
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
    setEditData(data || { title: '', startsOn: '', endsOn: '', status: 0 });
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
                  <Switch size="small" checked={Boolean(row.isActive ?? (row.status === 1))} onChange={() => handleStatusChange(row.id!, row.status)} />
                </TableCell>
                <TableCell>{(row.startsOn || (row as any).startDate) ? dayjs(row.startsOn || (row as any).startDate).format('DD/MM/YYYY') : 'N/A'}</TableCell>
                <TableCell>{(row.endsOn || (row as any).endDate) ? dayjs(row.endsOn || (row as any).endDate).format('DD/MM/YYYY') : 'N/A'}</TableCell>
                <TableCell align="center">
                  <Button size="small" onClick={() => navigate(`/sms/selectSession?flashSaleId=${row.id}`)}>Manage Sessions</Button>
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
        <LocalizationProvider dateAdapter={AdapterDayjs}>
          <DatePicker
            label="Start Date"
            format="DD/MM/YYYY"
            value={editData?.startsOn ? dayjs(editData.startsOn) : null}
            onChange={(newValue) => setEditData(prev => ({ ...prev!, startsOn: newValue ? newValue.format('YYYY-MM-DD') : '' }))}
            slotProps={{ textField: { fullWidth: true, margin: 'normal' } }}
          />
          <DatePicker
            label="End Date"
            format="DD/MM/YYYY"
            value={editData?.endsOn ? dayjs(editData.endsOn) : null}
            onChange={(newValue) => setEditData(prev => ({ ...prev!, endsOn: newValue ? newValue.format('YYYY-MM-DD') : '' }))}
            slotProps={{ textField: { fullWidth: true, margin: 'normal' } }}
          />
        </LocalizationProvider>
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
