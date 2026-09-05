import React, { useEffect, useState } from 'react';
import { Card, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography, Switch, Paper, Button, Box, IconButton, Chip } from '@mui/material';
import { getProductCategoryListAPI, productCategoryUpdateShowStatusAPI, productCategoryUpdateNavStatusAPI, productCategoryDeleteByIdAPI } from '@/apis/productCate';
import type { PmsProductCategory } from '@/types/productCate';
import { useNavigate } from 'react-router';

const ProductCategoryList: React.FC = () => {
  const [categories, setCategories] = useState<PmsProductCategory[]>([]);
  const [parentId, setParentId] = useState<number>(0);
  const navigate = useNavigate();

  useEffect(() => {
    fetchCategories();
  }, [parentId]);

  const fetchCategories = async () => {
    try {
      const res = await getProductCategoryListAPI(parentId, { pageNum: 1, pageSize: 100 });
      setCategories(res.data.list);
    } catch (e) {
      console.error(e);
    }
  };

  const handleShowStatusChange = async (id: number, checked: boolean) => {
    try {
      await productCategoryUpdateShowStatusAPI({ ids: id.toString(), showStatus: checked ? 1 : 0 });
      fetchCategories();
    } catch (e) {
      console.error(e);
    }
  };

  const handleNavStatusChange = async (id: number, checked: boolean) => {
    try {
      await productCategoryUpdateNavStatusAPI({ ids: id.toString(), navStatus: checked ? 1 : 0 });
      fetchCategories();
    } catch (e) {
      console.error(e);
    }
  };

  const handleDelete = async (id: number) => {
    if (window.confirm('Are you sure you want to delete this category?')) {
      try {
        await productCategoryDeleteByIdAPI(id);
        fetchCategories();
      } catch (e) {
        console.error(e);
      }
    }
  };

  return (
    <Card sx={{ p: 3, m: 3 }}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 3 }}>
        <Typography variant="h5">Product Categories</Typography>
        <Box>
          {parentId !== 0 && (
            <Button variant="outlined" sx={{ mr: 2 }} onClick={() => setParentId(0)}>Back to Root</Button>
          )}
          <Button variant="contained" color="primary" onClick={() => navigate('/pms/addProductCate')}>Add Category</Button>
        </Box>
      </Box>

      <TableContainer component={Paper} elevation={0} variant="outlined">
        <Table>
          <TableHead>
            <TableRow sx={{ bgcolor: 'grey.50' }}>
              <TableCell>ID</TableCell>
              <TableCell>Category Name</TableCell>
              <TableCell>Level</TableCell>
              <TableCell>Product Count</TableCell>
              <TableCell>Product Unit</TableCell>
              <TableCell>Nav Status</TableCell>
              <TableCell>Show Status</TableCell>
              <TableCell>Sort</TableCell>
              <TableCell align="center">Settings</TableCell>
              <TableCell align="right">Action</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {categories.map((row) => (
              <TableRow key={row.id} hover>
                <TableCell>{row.id}</TableCell>
                <TableCell>{row.name}</TableCell>
                <TableCell>{row.level === 0 ? 'Level 1' : 'Level 2'}</TableCell>
                <TableCell>{row.productCount}</TableCell>
                <TableCell>{row.productUnit}</TableCell>
                <TableCell>
                  <Switch 
                    checked={row.navStatus === 1} 
                    onChange={(e) => handleNavStatusChange(row.id as number, e.target.checked)} 
                    color="primary" 
                  />
                </TableCell>
                <TableCell>
                  <Switch 
                    checked={row.showStatus === 1} 
                    onChange={(e) => handleShowStatusChange(row.id as number, e.target.checked)} 
                    color="primary" 
                  />
                </TableCell>
                <TableCell>{row.sort}</TableCell>
                <TableCell align="center">
                  <Button 
                    size="small" 
                    variant="text" 
                    disabled={row.level !== 0}
                    onClick={() => setParentId(row.id as number)}
                  >
                    View Children
                  </Button>
                </TableCell>
                <TableCell align="right">
                  <Button size="small" sx={{ mr: 1 }} onClick={() => navigate(`/pms/updateProductCate/${row.id}`)}>Edit</Button>
                  <Button size="small" color="error" onClick={() => handleDelete(row.id as number)}>Delete</Button>
                </TableCell>
              </TableRow>
            ))}
            {categories.length === 0 && (
              <TableRow>
                <TableCell colSpan={10} align="center" sx={{ py: 5 }}>No categories found.</TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </TableContainer>
    </Card>
  );
};

export default ProductCategoryList;
