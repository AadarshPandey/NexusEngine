import React, { useEffect, useState } from 'react';
import { Box, Typography, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Button, Switch, TextField, MenuItem, Select, FormControl, InputLabel, ListSubheader, Grid, IconButton, Divider, Breadcrumbs, Link, TablePagination, Dialog, DialogTitle, DialogContent, DialogActions } from '@mui/material';
import { useNavigate } from 'react-router';
import { getProductListAPI, productUpdateDeleteStatusAPI, productUpdatePublishStatusAPI, productUpdateNewStatusAPI, productUpdateRecommendStatusAPI } from '@/apis/product';
import http from '@/utils/http';
import { getBrandListAPI } from '@/apis/brand';
import { getProductCategoryListWithChildrenAPI } from '@/apis/productCate';

const ProductList: React.FC = () => {
  const [products, setProducts] = useState<import('@/types/product').PmsProduct[]>([]);
  const [brands, setBrands] = useState<import('@/types/brand').PmsBrand[]>([]);
  const [categories, setCategories] = useState<import('@/types/productCate').PmsProductCategory[]>([]);
  const [total, setTotal] = useState(0);
  const [searchParams, setSearchParams] = useState({
    keyword: '',
    productSn: '',
    productCategoryId: '',
    brandId: '',
    publishStatus: '',
    verifyStatus: '',
    pageNum: 1,
    pageSize: 10
  });
  
  const navigate = useNavigate();
  
  const [verifyModalOpen, setVerifyModalOpen] = useState(false);
  const [logModalOpen, setLogModalOpen] = useState(false);
  const [selectedProductId, setSelectedProductId] = useState<number | null>(null);
  const [verifyStatus, setVerifyStatus] = useState<number>(1);
  const [verifyDetail, setVerifyDetail] = useState('');
  const [operateLogs, setOperateLogs] = useState<any[]>([]);
  const [verifyRecords, setVerifyRecords] = useState<any[]>([]);
  const [skuModalOpen, setSkuModalOpen] = useState(false);
  const [skuList, setSkuList] = useState<any[]>([]);
  const [editingSkuList, setEditingSkuList] = useState<any[]>([]);
  const [isEditingSku, setIsEditingSku] = useState(false);

  const saveSkuChanges = async () => {
    if (!selectedProductId) return;
    try {
      await http.post('/sku/update/' + selectedProductId, editingSkuList);
      alert('SKUs updated successfully!');
      setIsEditingSku(false);
      openSkuModal(selectedProductId); // refresh
    } catch (e) {
      alert('Failed to update SKUs');
    }
  };
  
  const openSkuModal = async (id: number) => {
    setSelectedProductId(id);
    try {
      const res = await http.get('/sku/' + id);
      setSkuList(res.data || []);
      setEditingSkuList(JSON.parse(JSON.stringify(res.data || [])));
      setIsEditingSku(false);
      setSkuModalOpen(true);
    } catch (e) {
      alert('Failed to fetch SKUs');
    }
  };

  const openVerifyModal = (id: number) => {
    setSelectedProductId(id);
    setVerifyStatus(1);
    setVerifyDetail('');
    setVerifyModalOpen(true);
  };

  const submitVerify = async () => {
    if (!selectedProductId) return;
    try {
      await http.post('/product/update/verifyStatus', null, {
        params: { ids: selectedProductId.toString(), verifyStatus, detail: verifyDetail }
      });
      alert('Verification updated successfully!');
      setVerifyModalOpen(false);
      fetchProducts(searchParams);
    } catch (e) {
      alert('Failed to update verification');
    }
  };

  const openLogModal = async (id: number) => {
    setSelectedProductId(id);
    try {
      const resOperate = await http.get('/product/operateLog/' + id);
      setOperateLogs(resOperate.data || []);
      const resVerify = await http.get('/product/verifyRecord/' + id);
      setVerifyRecords(resVerify.data || []);
      setLogModalOpen(true);
    } catch (e) {
      alert('Failed to fetch logs');
    }
  };

  useEffect(() => {
    fetchInitialData();
  }, []);

  useEffect(() => {
    fetchProducts(searchParams);
  }, [searchParams.pageNum, searchParams.pageSize]);

  const fetchInitialData = async () => {
    try {
      const [brandRes, cateRes] = await Promise.all([
        getBrandListAPI({ pageNum: 1, pageSize: 100 }),
        getProductCategoryListWithChildrenAPI()
      ]);
      setBrands(brandRes.data?.list || []);
      setCategories(cateRes.data || []);
    } catch (e) {
      console.error(e);
    }
  };

  const fetchProducts = async (params = searchParams) => {
    try {
      const res = await getProductListAPI(params as unknown as import('@/types/product').ProductQueryParam);
      setProducts(res.data?.list || []);
      setTotal(res.data?.total || 0);
    } catch (error) {
      console.error('Failed to fetch products', error);
    }
  };

  const handleSearch = () => {
    const newParams = { ...searchParams, pageNum: 1 };
    setSearchParams(newParams);
    fetchProducts(newParams);
  };

  const handleReset = () => {
    const resetParams = { ...searchParams, keyword: '', productSn: '', productCategoryId: '', brandId: '', publishStatus: '', verifyStatus: '', pageNum: 1 };
    setSearchParams(resetParams);
    fetchProducts(resetParams);
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
          <Grid size={{ xs: 12, md: 4 }}>
            <TextField fullWidth label="Product name" size="small" value={searchParams.keyword} onChange={(e) => setSearchParams({ ...searchParams, keyword: e.target.value })} />
          </Grid>
          <Grid size={{ xs: 12, md: 4 }}>
            <TextField fullWidth label="Product number" size="small" value={searchParams.productSn} onChange={(e) => setSearchParams({ ...searchParams, productSn: e.target.value })} />
          </Grid>
          <Grid size={{ xs: 12, md: 4 }}>
            <FormControl fullWidth size="small">
              <InputLabel>Product Category</InputLabel>
              <Select label="Product Category" value={searchParams.productCategoryId} onChange={(e) => setSearchParams({ ...searchParams, productCategoryId: e.target.value as string })}>
                <MenuItem value="">Please select</MenuItem>
                {categories.map((parent: any) => [
                  <ListSubheader key={`header-${parent.id}`}>{parent.name}</ListSubheader>,
                  ...(parent.children || []).map((child: any) => (
                    <MenuItem key={child.id} value={child.id} sx={{ pl: 4 }}>
                      {child.name}
                    </MenuItem>
                  ))
                ])}
              </Select>
            </FormControl>
          </Grid>
          <Grid size={{ xs: 12, md: 4 }}>
            <FormControl fullWidth size="small">
              <InputLabel>Product brand</InputLabel>
              <Select label="Product brand" value={searchParams.brandId} onChange={(e) => setSearchParams({ ...searchParams, brandId: e.target.value as string })}>
                <MenuItem value="">Please select brand</MenuItem>
                {brands.map((b: import('@/types/brand').PmsBrand) => (
                  <MenuItem key={b.id} value={b.id}>{b.name}</MenuItem>
                ))}
              </Select>
            </FormControl>
          </Grid>
          <Grid size={{ xs: 12, md: 4 }}>
            <FormControl fullWidth size="small">
              <InputLabel>Available now</InputLabel>
              <Select label="Available now" value={searchParams.publishStatus} onChange={(e) => setSearchParams({ ...searchParams, publishStatus: e.target.value as string })}>
                <MenuItem value="">All</MenuItem>
                <MenuItem value={1}>On shelves</MenuItem>
                <MenuItem value={0}>Off shelves</MenuItem>
              </Select>
            </FormControl>
          </Grid>
          <Grid size={{ xs: 12, md: 4 }}>
            <FormControl fullWidth size="small">
              <InputLabel>Review status</InputLabel>
              <Select label="Review status" value={searchParams.verifyStatus} onChange={(e) => setSearchParams({ ...searchParams, verifyStatus: e.target.value as string })}>
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
                <TableCell>Available stock</TableCell>
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
                    <Typography variant="body2">Price: ₹{row.price}</Typography>
                    <Typography variant="caption" color="text.secondary">Item No.: {row.productSn}</Typography>
                  </TableCell>
                  <TableCell>
                    <Box sx={{ display: 'flex', alignItems: 'center', mb: 0.5 }}>
                      <Typography variant="caption" sx={{ width: 80 }}>On shelves:</Typography>
                      <Switch size="small" checked={row.publishStatus === 1} onChange={(e) => handleStatusChange(row.id!, 'publish', e.target.checked)} />
                    </Box>
                    <Box sx={{ display: 'flex', alignItems: 'center', mb: 0.5 }}>
                      <Typography variant="caption" sx={{ width: 80 }}>New:</Typography>
                      <Switch size="small" checked={row.newStatus === 1} onChange={(e) => handleStatusChange(row.id!, 'new', e.target.checked)} />
                    </Box>
                    <Box sx={{ display: 'flex', alignItems: 'center' }}>
                      <Typography variant="caption" sx={{ width: 80 }}>recommend:</Typography>
                      <Switch size="small" checked={row.recommendStatus === 1} onChange={(e) => handleStatusChange(row.id!, 'recommend', e.target.checked)} />
                    </Box>
                  </TableCell>
                  <TableCell align="center">
                    <Button variant="outlined" size="small" sx={{ borderRadius: 20 }}>0</Button>
                  </TableCell>
                  <TableCell>{row.sale || 0}</TableCell>
                  <TableCell>{row.stock || 0}</TableCell>
                  <TableCell>
                    <Typography variant="body2">{row.verifyStatus === 1 ? 'Approved' : 'Not reviewed'}</Typography>
                    <Link href="#" variant="caption" underline="hover">Review details</Link>
                  </TableCell>
                  <TableCell align="center">
                    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
                      <Box sx={{ display: 'flex', gap: 1, justifyContent: 'center' }}>
                        <Button size="small" variant="text" onClick={() => openVerifyModal(row.id!)}>Check</Button>
                        <Button size="small" variant="text" color="primary" onClick={() => navigate(`/pms/updateProduct?id=${row.id}`)}>edit</Button>
                      </Box>
                      <Box sx={{ display: 'flex', gap: 1, justifyContent: 'center' }}>
                        <Button size="small" variant="text" color="success" onClick={() => openSkuModal(row.id!)}>SKUs</Button>
                        <Button size="small" variant="text" color="info" onClick={() => openLogModal(row.id!)}>log</Button>
                        <Button size="small" variant="text" color="error" onClick={() => handleDelete(row.id!)}>delete</Button>
                      </Box>
                    </Box>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
        <TablePagination
          component="div"
          count={total}
          page={searchParams.pageNum - 1}
          onPageChange={(_, p) => setSearchParams({ ...searchParams, pageNum: p + 1 })}
          rowsPerPage={searchParams.pageSize}
          onRowsPerPageChange={(e) => setSearchParams({ ...searchParams, pageSize: parseInt(e.target.value, 10), pageNum: 1 })}
        />
      </Paper>

      {/* Verify Modal */}
      <Dialog open={verifyModalOpen} onClose={() => setVerifyModalOpen(false)} maxWidth="sm" fullWidth>
        <DialogTitle>Check / Verify Product</DialogTitle>
        <DialogContent dividers>
          <FormControl fullWidth sx={{ mb: 2, mt: 1 }}>
            <InputLabel>Status</InputLabel>
            <Select value={verifyStatus} onChange={e => setVerifyStatus(e.target.value as number)} label="Status">
              <MenuItem value={1}>Approve</MenuItem>
              <MenuItem value={2}>Reject</MenuItem>
            </Select>
          </FormControl>
          <TextField fullWidth label="Details (Optional)" multiline rows={3} value={verifyDetail} onChange={e => setVerifyDetail(e.target.value)} />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setVerifyModalOpen(false)}>Cancel</Button>
          <Button onClick={submitVerify} variant="contained" color="primary">Submit</Button>
        </DialogActions>
      </Dialog>

      {/* SKU Modal */}
      <Dialog open={skuModalOpen} onClose={() => setSkuModalOpen(false)} maxWidth="md" fullWidth>
        <DialogTitle>Product SKUs</DialogTitle>
        <DialogContent dividers>
          {skuList.length === 0 ? (
            <Typography color="text.secondary">No SKUs found for this product.</Typography>
          ) : (
            <Table size="small">
              <TableHead>
                <TableRow>
                  <TableCell>SKU Code</TableCell>
                  <TableCell>Attributes</TableCell>
                  <TableCell>Price</TableCell>
                  <TableCell>Stock</TableCell>
                  <TableCell>Low Stock Warning</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {(isEditingSku ? editingSkuList : skuList).map((sku:any, idx:number) => {
                  let attrs = '';
                  try {
                    if (sku.spData) {
                      const spData = JSON.parse(sku.spData);
                      attrs = spData.map((d:any) => `${d.key}: ${d.value}`).join(', ');
                    }
                  } catch(e) {}
                  return (
                    <TableRow key={idx}>
                      <TableCell>{sku.skuCode || '-'}</TableCell>
                      <TableCell>{attrs || '-'}</TableCell>
                      <TableCell>
                        {isEditingSku ? (
                          <TextField size="small" type="number" value={sku.price} onChange={e => {
                            const newList = [...editingSkuList];
                            newList[idx].price = e.target.value;
                            setEditingSkuList(newList);
                          }} />
                        ) : (
                          sku.price !== undefined ? `${sku.price}` : '-'
                        )}
                      </TableCell>
                      <TableCell>
                        {isEditingSku ? (
                          <TextField size="small" type="number" value={sku.stock} onChange={e => {
                            const newList = [...editingSkuList];
                            newList[idx].stock = e.target.value;
                            setEditingSkuList(newList);
                          }} />
                        ) : (
                          sku.stock !== undefined ? sku.stock : '-'
                        )}
                      </TableCell>
                      <TableCell>
                        {isEditingSku ? (
                          <TextField size="small" type="number" value={sku.lowStock} onChange={e => {
                            const newList = [...editingSkuList];
                            newList[idx].lowStock = e.target.value;
                            setEditingSkuList(newList);
                          }} />
                        ) : (
                          sku.lowStock !== undefined ? sku.lowStock : '-'
                        )}
                      </TableCell>
                    </TableRow>
                  );
                })}
              </TableBody>
            </Table>
          )}
        </DialogContent>
        <DialogActions>
          {isEditingSku ? (
            <>
              <Button onClick={() => setIsEditingSku(false)}>Cancel</Button>
              <Button onClick={saveSkuChanges} variant="contained" color="primary">Save Changes</Button>
            </>
          ) : (
            <>
              <Button onClick={() => setSkuModalOpen(false)}>Close</Button>
              <Button onClick={() => setIsEditingSku(true)} variant="contained" color="primary">Edit SKUs</Button>
            </>
          )}
        </DialogActions>
      </Dialog>

      {/* Log Modal */}
      <Dialog open={logModalOpen} onClose={() => setLogModalOpen(false)} maxWidth="md" fullWidth>
        <DialogTitle>Audit Logs</DialogTitle>
        <DialogContent dividers>
          <Typography variant="h6" sx={{ mb: 1 }}>SKU Price Changes</Typography>
          {operateLogs.length === 0 ? (
            <Typography color="text.secondary" sx={{ mb: 3 }}>No price changes found.</Typography>
          ) : (
            <Table size="small" sx={{ mb: 3 }}>
              <TableHead>
                <TableRow>
                  <TableCell>Operator</TableCell>
                  <TableCell>Time</TableCell>
                  <TableCell>Old Price</TableCell>
                  <TableCell>New Price</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {operateLogs.map((log:any, idx:number) => (
                  <TableRow key={idx}>
                    <TableCell>{log.operateMan || 'System'}</TableCell>
                    <TableCell>{log.createTime ? new Date(log.createTime).toLocaleString() : '-'}</TableCell>
                    <TableCell>{log.priceOld || '-'}</TableCell>
                    <TableCell>{log.priceNew || '-'}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          )}

          <Typography variant="h6" sx={{ mb: 1 }}>Verification History</Typography>
          {verifyRecords.length === 0 ? (
            <Typography color="text.secondary">No verification history found.</Typography>
          ) : (
            <Table size="small">
              <TableHead>
                <TableRow>
                  <TableCell>Reviewer</TableCell>
                  <TableCell>Time</TableCell>
                  <TableCell>Status</TableCell>
                  <TableCell>Details</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {verifyRecords.map((rec:any, idx:number) => (
                  <TableRow key={idx}>
                    <TableCell>{rec.reviewerName || 'System'}</TableCell>
                    <TableCell>{rec.createTime ? new Date(rec.createTime).toLocaleString() : '-'}</TableCell>
                    <TableCell>{rec.status === 1 ? 'Approved' : 'Rejected'}</TableCell>
                    <TableCell>{rec.detail || '-'}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setLogModalOpen(false)}>Close</Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default ProductList;
