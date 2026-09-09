import React, { useEffect, useState } from 'react';
import { Box, Card, Typography, Button, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TablePagination, Paper, Checkbox, Switch, Dialog, DialogTitle, DialogContent, DialogActions, TextField } from '@mui/material';
import { Add } from '@mui/icons-material';
import { getReturnReasonListAPI, returnReasonCreateAPI, returnReasonUpdateAPI, returnReasonDeleteByIdsAPI, returnReasonUpdateStatusAPI } from '@/apis/returnReason';
import type { OmsOrderReturnReason } from '@/types/returnReason';
import dayjs from 'dayjs';

const ReturnReasonList: React.FC = () => {
  const [list, setList] = useState<OmsOrderReturnReason[]>([]);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);
  const [pageNum, setPageNum] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  
  const [selected, setSelected] = useState<number[]>([]);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [editData, setEditData] = useState<OmsOrderReturnReason | null>(null);

  useEffect(() => {
    fetchList();
  }, [pageNum, pageSize]);

  const fetchList = async () => {
    setLoading(true);
    try {
      const res = await getReturnReasonListAPI({ pageNum, pageSize });
      if (res.data) {
        setList(res.data.list || []);
        setTotal(res.data.total || 0);
      }
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  const handleSelectAll = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (event.target.checked) {
      setSelected(list.map((item) => item.id!));
    } else {
      setSelected([]);
    }
  };

  const handleSelectOne = (id: number) => {
    const selectedIndex = selected.indexOf(id);
    let newSelected: number[] = [];
    if (selectedIndex === -1) {
      newSelected = newSelected.concat(selected, id);
    } else if (selectedIndex === 0) {
      newSelected = newSelected.concat(selected.slice(1));
    } else if (selectedIndex === selected.length - 1) {
      newSelected = newSelected.concat(selected.slice(0, -1));
    } else if (selectedIndex > 0) {
      newSelected = newSelected.concat(selected.slice(0, selectedIndex), selected.slice(selectedIndex + 1));
    }
    setSelected(newSelected);
  };

  const handleStatusChange = async (id: number, currentStatus: number) => {
    try {
      const newStatus = currentStatus === 1 ? 0 : 1;
      await returnReasonUpdateStatusAPI({ ids: String(id), status: newStatus });
      fetchList();
    } catch (e) {
      console.error(e);
    }
  };

  const handleDelete = async (ids: number[]) => {
    if (ids.length === 0) return;
    if (window.confirm('Are you sure you want to delete selected return reasons?')) {
      try {
        await returnReasonDeleteByIdsAPI({ ids: ids.join(',') });
        setSelected([]);
        fetchList();
      } catch (e) {
        console.error(e);
      }
    }
  };

  const handleOpenDialog = (data?: OmsOrderReturnReason) => {
    if (data) {
      setEditData({ ...data });
    } else {
      setEditData({ name: '', sort: 0, status: 1 });
    }
    setDialogOpen(true);
  };

  const handleSave = async () => {
    if (!editData || !editData.name.trim()) return;
    try {
      if (editData.id) {
        await returnReasonUpdateAPI(editData.id, editData);
      } else {
        await returnReasonCreateAPI(editData);
      }
      setDialogOpen(false);
      fetchList();
    } catch (e) {
      console.error(e);
    }
  };

  return (
    <Box sx={{ p: 3 }}>
      <Card sx={{ mb: 3 }}>
        <Box sx={{ p: 3, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <Box>
            <Typography variant="h5">Return Reasons</Typography>
            <Typography variant="body2" color="text.secondary">
              Manage the predefined reasons a customer can select when requesting a refund/return.
            </Typography>
          </Box>
          <Button variant="contained" startIcon={<Add />} onClick={() => handleOpenDialog()}>
            Add Reason
          </Button>
        </Box>
      </Card>

      <Card>
        <TableContainer component={Paper} elevation={0} variant="outlined">
          <Table>
            <TableHead>
              <TableRow sx={{ bgcolor: 'background.default' }}>
                <TableCell padding="checkbox">
                  <Checkbox
                    indeterminate={selected.length > 0 && selected.length < list.length}
                    checked={list.length > 0 && selected.length === list.length}
                    onChange={handleSelectAll}
                  />
                </TableCell>
                <TableCell>ID</TableCell>
                <TableCell>Reason Name</TableCell>
                <TableCell align="center">Sort Order</TableCell>
                <TableCell align="center">Status</TableCell>
                <TableCell>Create Time</TableCell>
                <TableCell align="center">Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {loading ? (
                <TableRow><TableCell colSpan={7} align="center">Loading...</TableCell></TableRow>
              ) : list.length === 0 ? (
                <TableRow><TableCell colSpan={7} align="center" sx={{ py: 5 }}>No return reasons found.</TableCell></TableRow>
              ) : (
                list.map((row) => (
                  <TableRow key={row.id} hover selected={selected.indexOf(row.id!) !== -1}>
                    <TableCell padding="checkbox">
                      <Checkbox
                        checked={selected.indexOf(row.id!) !== -1}
                        onChange={() => handleSelectOne(row.id!)}
                      />
                    </TableCell>
                    <TableCell>{row.id}</TableCell>
                    <TableCell>{row.name}</TableCell>
                    <TableCell align="center">{row.sort}</TableCell>
                    <TableCell align="center">
                      <Switch size="small" checked={row.status === 1} onChange={() => handleStatusChange(row.id!, row.status)} />
                    </TableCell>
                    <TableCell>{row.createTime ? dayjs(row.createTime).format('YYYY-MM-DD HH:mm:ss') : 'N/A'}</TableCell>
                    <TableCell align="center">
                      <Button size="small" color="primary" onClick={() => handleOpenDialog(row)}>Edit</Button>
                      <Button size="small" color="error" onClick={() => handleDelete([row.id!])}>Delete</Button>
                    </TableCell>
                  </TableRow>
                ))
              )}
            </TableBody>
          </Table>
        </TableContainer>
        <Box sx={{ p: 2, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <Button variant="outlined" color="error" disabled={selected.length === 0} onClick={() => handleDelete(selected)}>
            Batch Delete
          </Button>
          <TablePagination
            component="div"
            count={total}
            page={pageNum - 1}
            onPageChange={(e, newPage) => setPageNum(newPage + 1)}
            rowsPerPage={pageSize}
            onRowsPerPageChange={(e) => { setPageSize(parseInt(e.target.value, 10)); setPageNum(1); }}
            rowsPerPageOptions={[5, 10, 15]}
          />
        </Box>
      </Card>

      <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)} maxWidth="xs" fullWidth>
        <DialogTitle>{editData?.id ? 'Edit Return Reason' : 'Add Return Reason'}</DialogTitle>
        <DialogContent>
          <TextField
            autoFocus
            margin="dense"
            label="Reason Name"
            fullWidth
            value={editData?.name || ''}
            onChange={(e) => setEditData({ ...editData!, name: e.target.value })}
            placeholder="e.g. Item defective, Wrong size"
          />
          <TextField
            margin="dense"
            label="Sort Order"
            type="number"
            fullWidth
            value={editData?.sort ?? 0}
            onChange={(e) => setEditData({ ...editData!, sort: Number(e.target.value) })}
          />
          <Box sx={{ display: 'flex', alignItems: 'center', mt: 2 }}>
            <Typography sx={{ mr: 2 }}>Status:</Typography>
            <Switch
              checked={editData?.status === 1}
              onChange={(e) => setEditData({ ...editData!, status: e.target.checked ? 1 : 0 })}
            />
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDialogOpen(false)}>Cancel</Button>
          <Button onClick={handleSave} variant="contained" disabled={!editData?.name?.trim()}>Save</Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default ReturnReasonList;
