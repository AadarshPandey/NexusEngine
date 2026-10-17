import React, { useEffect, useState } from 'react';
import { Box, Card, Typography, Button, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TablePagination, Paper, Dialog, DialogTitle, DialogContent, DialogActions, TextField, Select, MenuItem, FormControl, InputLabel } from '@mui/material';
import { Add } from '@mui/icons-material';
import { useNavigate, useParams, useSearchParams } from 'react-router';
import { getProductAttributeListAPI, productAttributeDeleteByIds, productAttributeCreateAPI, productAttributeUpdateAPI } from '@/apis/catalog';
import type { PmsProductAttribute } from '@/types/catalog';

const ProductAttrDetailList: React.FC = () => {
  const navigate = useNavigate();
  const { id: paramId } = useParams<{ id: string }>();
  const [searchParams] = useSearchParams();
  const cid = Number(paramId || searchParams.get('cid') || 0);
  const type = Number(searchParams.get('type') || 0);

  const [list, setList] = useState<PmsProductAttribute[]>([]);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);
  const [pageNum, setPageNum] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  
  const [dialogOpen, setDialogOpen] = useState(false);
  const [editData, setEditData] = useState<Partial<PmsProductAttribute>>({});

  useEffect(() => {
    if (cid) {
      fetchList();
    }
  }, [cid, type, pageNum, pageSize]);

  const fetchList = async () => {
    setLoading(true);
    try {
      const res = await getProductAttributeListAPI(cid, { pageNum, pageSize, type });
      if (res) {
        setList(res.content || []);
        setTotal(res.totalElements || 0);
      }
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  const handleOpenDialog = (attr?: PmsProductAttribute) => {
    if (attr) {
      setEditData({ ...attr });
    } else {
      setEditData({
        productTypeId: cid,
        type: type,
        name: '',
        selectionMode: 0,
        inputMode: 0,
        allowedValues: '',
      });
    }
    setDialogOpen(true);
  };

  const handleSave = async () => {
    if (!editData.name) return;
    try {
      if (editData.id) {
        await productAttributeUpdateAPI(editData.id, editData as PmsProductAttribute);
      } else {
        await productAttributeCreateAPI(editData as PmsProductAttribute);
      }
      setDialogOpen(false);
      fetchList();
    } catch (e) {
      console.error(e);
    }
  };

  const handleDelete = async (id: number) => {
    if (window.confirm('Are you sure you want to delete this?')) {
      try {
        await productAttributeDeleteByIds({ ids: String(id) });
        fetchList();
      } catch (e) {
        console.error(e);
      }
    }
  };

  return (
    <Box sx={{ p: 3 }}>
      <Card sx={{ mb: 3 }}>
        <Box sx={{ p: 3, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <Box>
            <Typography variant="h5">{type === 0 ? 'Attributes' : 'Attribute Definitions'} List</Typography>
            <Typography variant="body2" color="text.secondary">
              Manage {type === 0 ? 'SKU variant attributes (e.g. Color, Size)' : 'product attribute definitions (e.g. Screen Size, CPU)'}.
            </Typography>
          </Box>
          <Box>
            <Button variant="outlined" sx={{ mr: 2 }} onClick={() => navigate('/catalog/attributes')}>
              Back to Types
            </Button>
            <Button variant="contained" startIcon={<Add />} onClick={() => handleOpenDialog()}>
              Add {type === 0 ? 'Attribute' : 'Attribute Definition'}
            </Button>
          </Box>
        </Box>
      </Card>

      <Card>
        <TableContainer component={Paper} elevation={0} variant="outlined">
          <Table>
            <TableHead>
              <TableRow sx={{ bgcolor: 'background.default' }}>
                <TableCell>ID</TableCell>
                <TableCell>Name</TableCell>
                <TableCell>Input List Options</TableCell>
                <TableCell align="center">Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {loading ? (
                <TableRow><TableCell colSpan={6} align="center">Loading...</TableCell></TableRow>
              ) : list.length === 0 ? (
                <TableRow><TableCell colSpan={6} align="center" sx={{ py: 5 }}>No items found.</TableCell></TableRow>
              ) : (
                list.map((row) => (
                  <TableRow key={row.id} hover>
                    <TableCell>{row.id}</TableCell>
                    <TableCell>{row.name}</TableCell>
                    <TableCell>
                      {row.allowedValues ? (() => {
                        const opts = Array.isArray(row.allowedValues)
                          ? row.allowedValues
                          : String(row.allowedValues).split(',').filter(Boolean);
                        return opts.length > 0 ? (
                          <Select size="small" value={opts[0]} sx={{ minWidth: 100 }}>
                            {opts.map((opt: string, idx: number) => (
                              <MenuItem key={idx} value={opt}>{opt}</MenuItem>
                            ))}
                          </Select>
                        ) : '-';
                      })() : '-'}

                    </TableCell>
                    <TableCell align="center">
                      <Button size="small" color="primary" onClick={() => handleOpenDialog(row)}>Edit</Button>
                      <Button size="small" color="error" onClick={() => handleDelete(row.id!)}>Delete</Button>
                    </TableCell>
                  </TableRow>
                ))
              )}
            </TableBody>
          </Table>
        </TableContainer>
        <TablePagination
          component="div"
          count={total}
          page={pageNum - 1}
          onPageChange={(e, newPage) => setPageNum(newPage + 1)}
          rowsPerPage={pageSize}
          onRowsPerPageChange={(e) => { setPageSize(parseInt(e.target.value, 10)); setPageNum(1); }}
          rowsPerPageOptions={[5, 10, 15]}
        />
      </Card>

      <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)} maxWidth="sm" fullWidth>
        <DialogTitle>{editData.id ? 'Edit' : 'Add'} {type === 0 ? 'Attribute' : 'Attribute Definition'}</DialogTitle>
        <DialogContent>
          <Box sx={{ pt: 1, display: 'flex', flexDirection: 'column', gap: 2 }}>
            <TextField
              label="Name"
              fullWidth
              variant="outlined"
              value={editData.name || ''}
              onChange={(e) => setEditData({ ...editData, name: e.target.value })}
            />
            
            <FormControl fullWidth>
              <InputLabel>Input Type</InputLabel>
              <Select
                value={editData.inputMode || 0}
                label="Input Type"
                onChange={(e) => setEditData({ ...editData, inputMode: Number(e.target.value) })}
              >
                <MenuItem value={0}>Manual Input</MenuItem>
                <MenuItem value={1}>Select from List</MenuItem>
              </Select>
            </FormControl>

            <FormControl fullWidth>
              <InputLabel>Select Type</InputLabel>
              <Select
                value={editData.selectionMode || 0}
                label="Select Type"
                onChange={(e) => setEditData({ ...editData, selectionMode: Number(e.target.value) })}
              >
                <MenuItem value={0}>Unique</MenuItem>
                <MenuItem value={1}>Single Choice</MenuItem>
                <MenuItem value={2}>Multi Choice</MenuItem>
              </Select>
            </FormControl>

            <TextField
              label="Input List Options (Comma Separated)"
              fullWidth
              variant="outlined"
              value={editData.allowedValues || ''}
              onChange={(e) => setEditData({ ...editData, allowedValues: e.target.value })}
              helperText="Only used if Input Type is 'Select from List'"
            />
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDialogOpen(false)}>Cancel</Button>
          <Button onClick={handleSave} variant="contained" disabled={!editData.name}>Save</Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default ProductAttrDetailList;
