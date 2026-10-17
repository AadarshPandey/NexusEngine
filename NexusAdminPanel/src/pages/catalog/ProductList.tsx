import React, { useEffect, useState } from 'react';
import { Box, Typography, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Button, Switch, TextField, MenuItem, Select, FormControl, InputLabel, ListSubheader, Grid, IconButton, Divider, Breadcrumbs, Link, TablePagination, Dialog, DialogTitle, DialogContent, DialogActions } from '@mui/material';
import { useNavigate } from 'react-router';
import {
  getProductListAPI,
  productUpdateDeleteStatusAPI,
  productUpdatePublishStatusAPI,
  productUpdateNewStatusAPI,
  productUpdateRecommendStatusAPI,
  productUpdateApprovalStatusAPI,
  getProductAuditLogsAPI,
  getProductApprovalHistoryAPI,
  getSkuStockByPidAPI,
  skuStockUpdateAPI,
  getBrandListAPI,
  getProductCategoryListWithChildrenAPI,
} from '@/apis/catalog';
import { resolveImageUrl } from '@/utils/media';

interface PmsProductAuditLog {
  id?: number;
  productId?: number;
  priceOld?: number;
  priceNew?: number;
  oldListPrice?: number;
  newListPrice?: number;
  operateMan?: string;
  changedByAdminId?: string;
  createTime?: string;
  createdAt?: string;
}

interface PmsProductApprovalHistory {
  id?: number;
  productId?: number;
  createTime?: string;
  createdAt?: string;
  reviewerName?: string;
  reviewerAdminId?: string;
  status?: number;
  detail?: string;
  comment?: string;
}

interface PmsSpData {
  key: string;
  value: string;
}

const ProductList: React.FC = () => {
  const [products, setProducts] = useState<import('@/types/catalog').PmsProduct[]>([]);
  const [brands, setBrands] = useState<import('@/types/catalog').PmsBrand[]>([]);
  const [categories, setCategories] = useState<import('@/types/catalog').PmsProductCategory[]>([]);
  const [total, setTotal] = useState(0);
  const [searchParams, setSearchParams] = useState({
    keyword: '',
    productCode: '',
    productCategoryId: '',
    brandId: '',
    isPublished: '',
    approvalStatus: '',
    pageNum: 1,
    pageSize: 10
  });
  
  const navigate = useNavigate();
  
  const [verifyModalOpen, setVerifyModalOpen] = useState(false);
  const [logModalOpen, setLogModalOpen] = useState(false);
  const [selectedProductId, setSelectedProductId] = useState<number | null>(null);
  const [approvalStatus, setVerifyStatus] = useState<number>(1);
  const [verifyDetail, setVerifyDetail] = useState('');
  const [auditLogs, setAuditLogs] = useState<PmsProductAuditLog[]>([]);
  const [approvalHistorys, setApprovalHistorys] = useState<PmsProductApprovalHistory[]>([]);
  const [skuModalOpen, setSkuModalOpen] = useState(false);
  const [skuList, setSkuList] = useState<import('@/types/catalog').PmsSkuStock[]>([]);
  const [editingSkuList, setEditingSkuList] = useState<import('@/types/catalog').PmsSkuStock[]>([]);
  const [isEditingSku, setIsEditingSku] = useState(false);

  const saveSkuChanges = async () => {
    if (!selectedProductId) return;
    try {
      await skuStockUpdateAPI(selectedProductId, editingSkuList);
      alert('SKUs updated successfully!');
      setIsEditingSku(false);
      openSkuModal(selectedProductId);
    } catch (e) {
      alert('Failed to update SKUs');
    }
  };
  
  const openSkuModal = async (id: number) => {
    setSelectedProductId(id);
    try {
      const res = await getSkuStockByPidAPI(id);
      const list = Array.isArray(res) ? res : [];
      setSkuList(list);
      setEditingSkuList(JSON.parse(JSON.stringify(list)));
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
      await productUpdateApprovalStatusAPI({
        ids: selectedProductId.toString(),
        approvalStatus,
        detail: verifyDetail,
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
      const [resOperate, resVerify] = await Promise.all([
        getProductAuditLogsAPI(id),
        getProductApprovalHistoryAPI(id),
      ]);
      setAuditLogs(Array.isArray(resOperate) ? resOperate : []);
      setApprovalHistorys(Array.isArray(resVerify) ? resVerify : []);
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
      setBrands(brandRes?.content || []);
      setCategories(Array.isArray(cateRes) ? cateRes : []);
    } catch (e) {
      console.error(e);
    }
  };

  const fetchProducts = async (params = searchParams) => {
    try {
      const res = await getProductListAPI(params as unknown as import('@/types/catalog').ProductQueryParam);
      setProducts(res?.content || []);
      setTotal(res?.totalElements || 0);
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
    const resetParams = { ...searchParams, keyword: '', productCode: '', productCategoryId: '', brandId: '', isPublished: '', approvalStatus: '', pageNum: 1 };
    setSearchParams(resetParams);
    fetchProducts(resetParams);
  };

  const handleStatusChange = async (id: number, type: 'publish' | 'new' | 'recommend', checked: boolean) => {
    const val = checked ? 1 : 0;
    try {
      if (type === 'publish') await productUpdatePublishStatusAPI({ ids: id.toString(), isPublished: val });
      if (type === 'new') await productUpdateNewStatusAPI({ ids: id.toString(), isNewArrival: val });
      if (type === 'recommend') await productUpdateRecommendStatusAPI({ ids: id.toString(), isFeatured: val });
      fetchProducts();
    } catch (e) {
      console.error(e);
    }
  };

  const handleDelete = async (id: number) => {
    if (confirm('Are you sure you want to delete this product?')) {
      try {
        await productUpdateDeleteStatusAPI({ ids: id.toString(), deletedAt: 1 });
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
            <TextField fullWidth label="Product number" size="small" value={searchParams.productCode} onChange={(e) => setSearchParams({ ...searchParams, productCode: e.target.value })} />
          </Grid>
          <Grid size={{ xs: 12, md: 4 }}>
            <FormControl fullWidth size="small">
              <InputLabel>Product Category</InputLabel>
              <Select label="Product Category" value={searchParams.productCategoryId} onChange={(e) => setSearchParams({ ...searchParams, productCategoryId: e.target.value as string })}>
                <MenuItem value="">Please select</MenuItem>
                {categories.map((parent: import('@/types/catalog').PmsProductCategory) => [
                  <ListSubheader key={`header-${parent.id}`}>{parent.name}</ListSubheader>,
                  ...((parent as any).children || []).map((child: import('@/types/catalog').PmsProductCategory) => (
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
                {brands.map((b: import('@/types/catalog').PmsBrand) => (
                  <MenuItem key={b.id} value={b.id}>{b.name}</MenuItem>
                ))}
              </Select>
            </FormControl>
          </Grid>
          <Grid size={{ xs: 12, md: 4 }}>
            <FormControl fullWidth size="small">
              <InputLabel>Published</InputLabel>
              <Select label="Published" value={searchParams.isPublished} onChange={(e) => setSearchParams({ ...searchParams, isPublished: e.target.value as string })}>
                <MenuItem value="">All</MenuItem>
                <MenuItem value={1}>Yes</MenuItem>
                <MenuItem value={0}>No</MenuItem>
              </Select>
            </FormControl>
          </Grid>
          <Grid size={{ xs: 12, md: 4 }}>
            <FormControl fullWidth size="small">
              <InputLabel>Review status</InputLabel>
              <Select label="Review status" value={searchParams.approvalStatus} onChange={(e) => setSearchParams({ ...searchParams, approvalStatus: e.target.value as string })}>
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
          <Button variant="outlined" size="small" onClick={() => navigate('/catalog/products/new')}>Add product</Button>
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
                <TableCell>Sales volume</TableCell>
                <TableCell>Available onHandQty</TableCell>
                <TableCell>Review status</TableCell>
                <TableCell align="center">operate</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {products.map((row) => (
                <TableRow key={row.id}>
                  <TableCell>{row.id}</TableCell>
                  <TableCell>
                    <Box
                      component="img"
                      src={resolveImageUrl(row.primaryImageUrl || row.pic)}
                      alt={row.name || 'product'}
                      onError={(e: React.SyntheticEvent<HTMLImageElement>) => {
                        e.currentTarget.onerror = null;
                        e.currentTarget.src = 'https://via.placeholder.com/80?text=No+Image';
                      }}
                      sx={{ width: 80, height: 80, objectFit: 'contain', border: '1px solid #eee', borderRadius: 1 }}
                    />
                  </TableCell>
                  <TableCell>
                    <Typography variant="body2" sx={{ fontWeight: 'bold' }}>{row.name}</Typography>
                    <Typography variant="caption" color="text.secondary" display="block">Brand: {row.brandName}</Typography>
                  </TableCell>
                  <TableCell>
                    <Typography variant="body2">Price: ₹{row.listPrice}</Typography>
                    <Typography variant="caption" color="text.secondary">Item No.: {row.productCode}</Typography>
                  </TableCell>
                  <TableCell>
                    <Box sx={{ display: 'flex', alignItems: 'center', mb: 0.5 }}>
                      <Typography variant="caption" sx={{ width: 80 }}>Published:</Typography>
                      <Switch size="small" checked={Boolean(row.isPublished)} onChange={(e) => handleStatusChange(row.id!, 'publish', e.target.checked)} />
                    </Box>
                    <Box sx={{ display: 'flex', alignItems: 'center', mb: 0.5 }}>
                      <Typography variant="caption" sx={{ width: 80 }}>New Arrival:</Typography>
                      <Switch size="small" checked={Boolean(row.isNewArrival)} onChange={(e) => handleStatusChange(row.id!, 'new', e.target.checked)} />
                    </Box>
                    <Box sx={{ display: 'flex', alignItems: 'center' }}>
                      <Typography variant="caption" sx={{ width: 80 }}>Featured:</Typography>
                      <Switch size="small" checked={Boolean(row.isFeatured)} onChange={(e) => handleStatusChange(row.id!, 'recommend', e.target.checked)} />
                    </Box>
                  </TableCell>
                  <TableCell>{row.unitsSold || 0}</TableCell>
                  <TableCell>{row.onHandQty || 0}</TableCell>
                  <TableCell>
                    <Typography variant="body2">{row.approvalStatus === 1 ? 'Approved' : 'Not reviewed'}</Typography>
                    <Link href="#" variant="caption" underline="hover" onClick={(e) => { e.preventDefault(); openLogModal(row.id!); }}>Review details</Link>
                  </TableCell>
                  <TableCell align="center">
                    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
                      <Box sx={{ display: 'flex', gap: 1, justifyContent: 'center' }}>
                        <Button size="small" variant="text" onClick={() => openVerifyModal(row.id!)}>Check</Button>
                        <Button size="small" variant="text" color="primary" onClick={() => navigate(`/catalog/products/${row.id}/edit`)}>edit</Button>
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
            <Select value={approvalStatus} onChange={e => setVerifyStatus(e.target.value as number)} label="Status">
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
                {(isEditingSku ? editingSkuList : skuList).map((sku: import('@/types/catalog').PmsSkuStock, idx: number) => {
                  let attrs = '';
                  try {
                    const rawAttr = sku.skuAttributes || sku.spData;
                    if (rawAttr) {
                      const parsed = typeof rawAttr === 'string' ? JSON.parse(rawAttr) : rawAttr;
                      if (Array.isArray(parsed)) {
                        attrs = parsed.map((d: PmsSpData) => `${d.key}: ${d.value}`).join(', ');
                      } else if (typeof parsed === 'object' && parsed !== null) {
                        attrs = Object.entries(parsed).map(([k, v]) => `${k}: ${v}`).join(', ');
                      }
                    }
                  } catch(e) {}
                  return (
                    <TableRow key={idx}>
                      <TableCell>{sku.skuCode || '-'}</TableCell>
                      <TableCell>{attrs || '-'}</TableCell>
                      <TableCell>
                        {isEditingSku ? (
                          <TextField size="small" type="number" value={sku.listPrice} onChange={e => {
                            const newList = [...editingSkuList];
                            newList[idx].listPrice = Number(e.target.value);
                            setEditingSkuList(newList);
                          }} />
                        ) : (
                          sku.listPrice !== undefined ? `${sku.listPrice}` : '-'
                        )}
                      </TableCell>
                      <TableCell>
                        {isEditingSku ? (
                          <TextField size="small" type="number" value={sku.onHandQty} onChange={e => {
                            const newList = [...editingSkuList];
                            newList[idx].onHandQty = Number(e.target.value);
                            setEditingSkuList(newList);
                          }} />
                        ) : (
                          sku.onHandQty !== undefined ? sku.onHandQty : '-'
                        )}
                      </TableCell>
                      <TableCell>
                        {isEditingSku ? (
                          <TextField size="small" type="number" value={sku.lowStockThreshold} onChange={e => {
                            const newList = [...editingSkuList];
                            newList[idx].lowStockThreshold = Number(e.target.value);
                            setEditingSkuList(newList);
                          }} />
                        ) : (
                          sku.lowStockThreshold !== undefined ? sku.lowStockThreshold : '-'
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
          {auditLogs.length === 0 ? (
            <Typography color="text.secondary" sx={{ mb: 3 }}>No listPrice changes found.</Typography>
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
                {auditLogs.map((log: PmsProductAuditLog, idx: number) => (
                  <TableRow key={idx}>
                    <TableCell>{log.operateMan || log.changedByAdminId || 'System'}</TableCell>
                    <TableCell>{(log.createTime || log.createdAt) ? new Date((log.createTime || log.createdAt)!).toLocaleString() : '-'}</TableCell>
                    <TableCell>₹{log.priceOld ?? log.oldListPrice ?? '-'}</TableCell>
                    <TableCell>₹{log.priceNew ?? log.newListPrice ?? '-'}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          )}

          <Typography variant="h6" sx={{ mb: 1 }}>Verification History</Typography>
          {approvalHistorys.length === 0 ? (
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
                {approvalHistorys.map((rec: PmsProductApprovalHistory, idx: number) => (
                  <TableRow key={idx}>
                    <TableCell>{rec.reviewerName || rec.reviewerAdminId || 'System'}</TableCell>
                    <TableCell>{(rec.createTime || rec.createdAt) ? new Date((rec.createTime || rec.createdAt)!).toLocaleString() : '-'}</TableCell>
                    <TableCell>{rec.status === 1 ? 'Approved' : 'Rejected'}</TableCell>
                    <TableCell>{rec.detail || rec.comment || '-'}</TableCell>
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
