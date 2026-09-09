import React, { useEffect, useState } from 'react';
import { Box, Card, Typography, Button, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TablePagination, Paper, Dialog, DialogTitle, DialogContent, DialogActions, TextField } from '@mui/material';
import { Add } from '@mui/icons-material';
import { useNavigate } from 'react-router';
import { getProductAttributeCategoryListAPI, productAttributeCategoryCreateAPI, productAttributeCategoryUpdateAPI, productAttributeCategoryDeleteById } from '@/apis/productAttrCate';
import type { PmsProductAttributeCategory } from '@/types/productAttr';

const ProductAttrList: React.FC = () => {
  const navigate = useNavigate();
  const [list, setList] = useState<PmsProductAttributeCategory[]>([]);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);
  const [pageNum, setPageNum] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  
  const [dialogOpen, setDialogOpen] = useState(false);
  const [editId, setEditId] = useState<number | undefined>(undefined);
  const [typeName, setTypeName] = useState('');

  useEffect(() => {
    fetchList();
  }, [pageNum, pageSize]);

  const fetchList = async () => {
    setLoading(true);
    try {
      const res = await getProductAttributeCategoryListAPI({ pageNum, pageSize });
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

  const handleOpenDialog = (category?: PmsProductAttributeCategory) => {
    if (category) {
      setEditId(category.id);
      setTypeName(category.name);
    } else {
      setEditId(undefined);
      setTypeName('');
    }
    setDialogOpen(true);
  };

  const handleSave = async () => {
    if (!typeName.trim()) return;
    try {
      if (editId) {
        await productAttributeCategoryUpdateAPI(editId, typeName);
      } else {
        await productAttributeCategoryCreateAPI(typeName);
      }
      setDialogOpen(false);
      fetchList();
    } catch (e) {
      console.error(e);
    }
  };

  const handleDelete = async (id: number) => {
    if (window.confirm('Are you sure you want to delete this attribute type?')) {
      try {
        await productAttributeCategoryDeleteById(id);
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
            <Typography variant="h5">Product Attribute Types</Typography>
            <Typography variant="body2" color="text.secondary">
              Manage the master templates (blueprints) used to generate SKUs and Specifications for your products.
            </Typography>
          </Box>
          <Button variant="contained" startIcon={<Add />} onClick={() => handleOpenDialog()}>
            Add Type
          </Button>
        </Box>
      </Card>

      <Card>
        <TableContainer component={Paper} elevation={0} variant="outlined">
          <Table>
            <TableHead>
              <TableRow sx={{ bgcolor: 'background.default' }}>
                <TableCell>ID</TableCell>
                <TableCell>Type Name</TableCell>
                <TableCell align="center">Attribute Count (SKUs)</TableCell>
                <TableCell align="center">Parameter Count (Static Info)</TableCell>
                <TableCell align="center">Setup</TableCell>
                <TableCell align="center">Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {loading ? (
                <TableRow><TableCell colSpan={6} align="center">Loading...</TableCell></TableRow>
              ) : list.length === 0 ? (
                <TableRow><TableCell colSpan={6} align="center" sx={{ py: 5 }}>No product attribute types found.</TableCell></TableRow>
              ) : (
                list.map((row) => (
                  <TableRow key={row.id} hover>
                    <TableCell>{row.id}</TableCell>
                    <TableCell>{row.name}</TableCell>
                    <TableCell align="center">{row.attributeCount === null ? 0 : row.attributeCount}</TableCell>
                    <TableCell align="center">{row.paramCount === null ? 0 : row.paramCount}</TableCell>
                    <TableCell align="center">
                      <Button size="small" variant="outlined" sx={{ mr: 1 }} onClick={() => navigate(`/pms/productAttrList?cid=${row.id}&type=0`)}>
                        Manage Attributes
                      </Button>
                      <Button size="small" variant="outlined" color="secondary" onClick={() => navigate(`/pms/productAttrList?cid=${row.id}&type=1`)}>
                        Manage Parameters
                      </Button>
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

      {/* Add / Edit Dialog */}
      <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)} maxWidth="xs" fullWidth>
        <DialogTitle>{editId ? 'Edit Attribute Type' : 'Add Attribute Type'}</DialogTitle>
        <DialogContent>
          <TextField
            autoFocus
            margin="dense"
            label="Type Name"
            type="text"
            fullWidth
            variant="outlined"
            value={typeName}
            onChange={(e) => setTypeName(e.target.value)}
            placeholder="e.g. Smartphones, T-Shirts"
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDialogOpen(false)}>Cancel</Button>
          <Button onClick={handleSave} variant="contained" disabled={!typeName.trim()}>Save</Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default ProductAttrList;
