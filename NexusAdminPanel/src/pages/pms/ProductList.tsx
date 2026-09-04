import React, { useEffect, useState } from 'react';
import { Box, Typography, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Button, Switch, TextField, MenuItem, Select, FormControl, InputLabel, Grid, IconButton, Divider, Breadcrumbs, Link } from '@mui/material';
import { useNavigate } from 'react-router';
import { getProductListAPI, productUpdateDeleteStatusAPI, productUpdatePublishStatusAPI, productUpdateNewStatusAPI, productUpdateRecommendStatusAPI } from '@/apis/product';
import { getBrandListAPI } from '@/apis/brand';
import { getProductCategoryListWithChildrenAPI } from '@/apis/productCate';

const ProductList: React.FC = () => {
  const [products, setProducts] = useState<any[]>([]);
  const [brands, setBrands] = useState<any[]>([]);
  const [categories, setCategories] = useState<any[]>([]);
  const [searchParams, setSearchParams] = useState({
    keyword: '',
    productSn: '',
    productCategoryId: '',
    brandId: '',
    publishStatus: '',
    verifyStatus: '',
    pageNum: 1,
    pageSize: 100
  });
  
  const navigate = useNavigate();

  useEffect(() => {
    fetchInitialData();
  }, []);

  const fetchInitialData = async () => {
    try {
      const [brandRes, cateRes] = await Promise.all([
        getBrandListAPI({ pageNum: 1, pageSize: 100 }),
        getProductCategoryListWithChildrenAPI()
      ]);
      setBrands(brandRes.data?.list || []);
      setCategories(cateRes.data || []);
      fetchProducts();
    } catch (e) {
      console.error(e);
    }
  };

  const fetchProducts = async () => {
    try {
      const res = await getProductListAPI(searchParams as any);
      setProducts(res.data?.list || []);
    } catch (error) {
      console.error('Failed to fetch products', error);
    }
  };

  const handleSearch = () => fetchProducts();
  const handleReset = () => {
    setSearchParams({ ...searchParams, keyword: '', productSn: '', productCategoryId: '', brandId: '', publishStatus: '', verifyStatus: '' });
    setTimeout(fetchProducts, 0);
  };

  const handleStatusChange = async (id: number, type: 'publish' | 'new' | 'recommend', checked: boolean) => {
    const val = checked ? 1 : 0;
    try {
      if (type === 'publish') await productUpdatePublishStatusAPI({ ids: id.toString(), publishStatus: val });
      if (type === 'new') await productUpdateNewStatusAPI({ ids: id.toString(), newStatus: val });
      if (type === 'recommend') await productUpdateRecommendStatusAPI({ ids: id.toString(), recommendStatus: val });
      fetchProducts();
    } catch (e) {
      console.error(e);
    }
  };

  const handleDelete = async (id: number) => {
    if (confirm('Are you sure you want to delete this product?')) {
      try {
        await productUpdateDeleteStatusAPI({ ids: id.toString(), deleteStatus: 1 });
        fetchProducts();
      } catch (e) {
        console.error(e);
      }
    }
  };

  return (
    <Box>
      <Breadcrumbs aria-label="breadcrumb" sx={{ mb: 2 }}>
        <Link underline="hover" color="inherit">Homepage</Link>
        <Link underline="hover" color="inherit">Products</Link>
        <Typography color="text.primary">Product List</Typography>
      </Breadcrumbs>

      {/* Filter Card */}
      <Paper sx={{ p: 3, mb: 3 }}>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 3 }}>
          <Typography variant="h6" sx={{ display: 'flex', alignItems: 'center' }}>
            <Box component="span" sx={{ mr: 1 }}>🔍</Box> Q filter search
          </Typography>
          <Box>
            <Button variant="outlined" onClick={handleReset} sx={{ mr: 2 }}>reset</Button>
            <Button variant="contained" color="primary" onClick={handleSearch}>Query results</Button>
          </Box>
        </Box>
        <Grid container spacing={3}>
          <Grid item xs={12} md={4}>
            <TextField fullWidth label="Product name" size="small" value={searchParams.keyword} onChange={(e) => setSearchParams({ ...searchParams, keyword: e.target.value })} />
          </Grid>
          <Grid item xs={12} md={4}>
            <TextField fullWidth label="Product number" size="small" value={searchParams.productSn} onChange={(e) => setSearchParams({ ...searchParams, productSn: e.target.value })} />
          </Grid>
          <Grid item xs={12} md={4}>
            <FormControl fullWidth size="small">
              <InputLabel>Product Category</InputLabel>
              <Select label="Product Category" value={searchParams.productCategoryId} onChange={(e) => setSearchParams({ ...searchParams, productCategoryId: e.target.value } as any)}>
                <MenuItem value="">Please select</MenuItem>
                {categories.map((c: any) => (
                  <MenuItem key={c.id} value={c.id}>{c.name}</MenuItem>
                ))}
              </Select>
            </FormControl>
          </Grid>
          <Grid item xs={12} md={4}>
            <FormControl fullWidth size="small">
              <InputLabel>Product brand</InputLabel>
              <Select label="Product brand" value={searchParams.brandId} onChange={(e) => setSearchParams({ ...searchParams, brandId: e.target.value } as any)}>
                <MenuItem value="">Please select brand</MenuItem>
                {brands.map((b: any) => (
                  <MenuItem key={b.id} value={b.id}>{b.name}</MenuItem>
                ))}
              </Select>
            </FormControl>
          </Grid>
          <Grid item xs={12} md={4}>
            <FormControl fullWidth size="small">
              <InputLabel>Available now</InputLabel>
              <Select label="Available now" value={searchParams.publishStatus} onChange={(e) => setSearchParams({ ...searchParams, publishStatus: e.target.value } as any)}>
                <MenuItem value="">All</MenuItem>
                <MenuItem value={1}>On shelves</MenuItem>
                <MenuItem value={0}>Off shelves</MenuItem>
              </Select>
            </FormControl>
          </Grid>
          <Grid item xs={12} md={4}>
            <FormControl fullWidth size="small">
              <InputLabel>Review status</InputLabel>
              <Select label="Review status" value={searchParams.verifyStatus} onChange={(e) => setSearchParams({ ...searchParams, verifyStatus: e.target.value } as any)}>
                <MenuItem value="">All</MenuItem>
                <MenuItem value={1}>Approved</MenuItem>
                <MenuItem value={0}>Not reviewed</MenuItem>
              </Select>
            </FormControl>
          </Grid>
        </Grid>
      </Paper>

      {/* Target Data List Card */}
      <Paper sx={{ mb: 3 }}>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', p: 2, borderBottom: '1px solid #eee' }}>
          <Typography variant="h6" sx={{ display: 'flex', alignItems: 'center' }}>
            <Box component="span" sx={{ mr: 1 }}>📄</Box> target data list
          </Typography>
          <Button variant="outlined" size="small" onClick={() => navigate('/pms/addProduct')}>Add product</Button>
        </Box>
        <TableContainer>
          <Table size="medium">
            <TableHead>
              <TableRow sx={{ backgroundColor: '#fafafa' }}>
                <TableCell>serial number</TableCell>
                <TableCell>Product pictures</TableCell>
                <TableCell>Product name</TableCell>
                <TableCell>Price/item number</TableCell>
                <TableCell>Label</TableCell>
                <TableCell>SKU inventory</TableCell>
                <TableCell>Sales volume</TableCell>
                <TableCell>Review status</TableCell>
                <TableCell align="center">operate</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {products.map((row) => (
                <TableRow key={row.id}>
                  <TableCell>{row.id}</TableCell>
                  <TableCell>
                    <Box component="img" src={row.pic || 'https://via.placeholder.com/80'} alt="pic" sx={{ width: 80, height: 80, objectFit: 'contain', border: '1px solid #eee', borderRadius: 1 }} />
                  </TableCell>
                  <TableCell>
                    <Typography variant="body2" sx={{ fontWeight: 'bold' }}>{row.name}</Typography>
                    <Typography variant="caption" color="text.secondary" display="block">Brand: {row.brandName}</Typography>
                  </TableCell>
                  <TableCell>
                    <Typography variant="body2">Price: ¥{row.price}</Typography>
                    <Typography variant="caption" color="text.secondary">Item No.: {row.productSn}</Typography>
                  </TableCell>
                  <TableCell>
                    <Box sx={{ display: 'flex', alignItems: 'center', mb: 0.5 }}>
                      <Typography variant="caption" sx={{ width: 80 }}>On shelves:</Typography>
                      <Switch size="small" checked={row.publishStatus === 1} onChange={(e) => handleStatusChange(row.id, 'publish', e.target.checked)} />
                    </Box>
                    <Box sx={{ display: 'flex', alignItems: 'center', mb: 0.5 }}>
                      <Typography variant="caption" sx={{ width: 80 }}>New:</Typography>
                      <Switch size="small" checked={row.newStatus === 1} onChange={(e) => handleStatusChange(row.id, 'new', e.target.checked)} />
                    </Box>
                    <Box sx={{ display: 'flex', alignItems: 'center' }}>
                      <Typography variant="caption" sx={{ width: 80 }}>recommend:</Typography>
                      <Switch size="small" checked={row.recommandStatus === 1} onChange={(e) => handleStatusChange(row.id, 'recommend', e.target.checked)} />
                    </Box>
                  </TableCell>
                  <TableCell align="center">
                    <Button variant="outlined" size="small" sx={{ borderRadius: 20 }}>0</Button>
                  </TableCell>
                  <TableCell>{row.sale || 100}</TableCell>
                  <TableCell>
                    <Typography variant="body2">{row.verifyStatus === 1 ? 'Approved' : 'Not reviewed'}</Typography>
                    <Link href="#" variant="caption" underline="hover">Review details</Link>
                  </TableCell>
                  <TableCell align="center">
                    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
                      <Box sx={{ display: 'flex', gap: 1, justifyContent: 'center' }}>
                        <Button size="small" variant="text">Check</Button>
                        <Button size="small" variant="text" color="primary">edit</Button>
                      </Box>
                      <Box sx={{ display: 'flex', gap: 1, justifyContent: 'center' }}>
                        <Button size="small" variant="text" color="info">log</Button>
                        <Button size="small" variant="text" color="error" onClick={() => handleDelete(row.id)}>delete</Button>
                      </Box>
                    </Box>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      </Paper>
    </Box>
  );
};

export default ProductList;
