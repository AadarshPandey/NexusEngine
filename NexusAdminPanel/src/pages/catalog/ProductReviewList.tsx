import React, { useState, useEffect, useCallback } from 'react';
import {
  Box,
  Card,
  CardContent,
  Typography,
  Button,
  TextField,
  InputAdornment,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  TablePagination,
  Chip,
  IconButton,
  Rating,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  Snackbar,
  Alert,
  Avatar,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
} from '@mui/material';
import {
  Search,
  CheckCircle,
  Cancel,
  Delete,
  PhotoCamera,
  FilterList,
} from '@mui/icons-material';
import {
  getReviewListAPI,
  updateReviewModerationAPI,
  deleteReviewAPI,
  type ReviewModerationDTO,
} from '@/apis/engagement';
import { resolveImageUrl } from '@/utils/media';

const STATUS_CONFIG: Record<number, { label: string; color: 'warning' | 'success' | 'error' | 'default' }> = {
  0: { label: 'Pending Review', color: 'warning' },
  1: { label: 'Approved', color: 'success' },
  2: { label: 'Rejected', color: 'error' },
};

const ProductReviewList: React.FC = () => {
  const [list, setList] = useState<ReviewModerationDTO[]>([]);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(true);
  const [keyword, setKeyword] = useState('');
  const [statusFilter, setStatusFilter] = useState<string>('ALL');
  const [pageNum, setPageNum] = useState(0);
  const [pageSize, setPageSize] = useState(10);
  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' as 'success' | 'error' });

  // Photo preview modal
  const [previewImage, setPreviewImage] = useState<string | null>(null);

  const fetchList = useCallback(async (
    overridePageNum?: number,
    overrideKeyword?: string,
    overrideStatus?: string
  ) => {
    setLoading(true);
    const finalPageNum = overridePageNum !== undefined ? overridePageNum : pageNum;
    const finalKeyword = overrideKeyword !== undefined ? overrideKeyword : keyword;
    const finalStatus = overrideStatus !== undefined ? overrideStatus : statusFilter;

    try {
      const res = await getReviewListAPI({
        keyword: finalKeyword || undefined,
        status: finalStatus !== 'ALL' ? Number(finalStatus) : undefined,
        pageNum: finalPageNum + 1,
        pageSize,
      });
      setList(res.content || []);
      setTotal(res.totalElements || 0);
    } catch {
      setSnackbar({ open: true, message: 'Failed to load reviews', severity: 'error' });
    } finally {
      setLoading(false);
    }
  }, [pageNum, pageSize, keyword, statusFilter]);

  useEffect(() => {
    fetchList();
  }, [fetchList]);

  const handleSearch = () => {
    setPageNum(0);
    fetchList(0);
  };

  const handleReset = () => {
    setKeyword('');
    setStatusFilter('ALL');
    setPageNum(0);
    fetchList(0, '', 'ALL');
  };

  const handleStatusChange = async (id: number, newStatus: number) => {
    try {
      await updateReviewModerationAPI(id, newStatus);
      setSnackbar({
        open: true,
        message: newStatus === 1 ? 'Review approved successfully' : 'Review rejected',
        severity: 'success',
      });
      fetchList();
    } catch {
      setSnackbar({ open: true, message: 'Failed to update review status', severity: 'error' });
    }
  };

  const handleDelete = async (id: number) => {
    if (!window.confirm('Are you sure you want to delete this review permanently?')) return;
    try {
      await deleteReviewAPI(id);
      setSnackbar({ open: true, message: 'Review deleted successfully', severity: 'success' });
      fetchList();
    } catch {
      setSnackbar({ open: true, message: 'Failed to delete review', severity: 'error' });
    }
  };

  return (
    <Box sx={{ p: 3 }}>
      {/* Header */}
      <Box sx={{ mb: 3 }}>
        <Typography variant="h5" sx={{ fontWeight: 700 }}>
          Product Reviews & Moderation
        </Typography>
        <Typography variant="body2" color="text.secondary">
          Manage user reviews, moderation approval workflows, and buyer media attachments (engagement.product_review & engagement.review_media).
        </Typography>
      </Box>

      {/* Filter Card */}
      <Card sx={{ mb: 3 }}>
        <CardContent sx={{ p: 2.5 }}>
          <Box sx={{ display: 'flex', gap: 2, flexWrap: 'wrap', alignItems: 'center' }}>
            <TextField
              size="small"
              placeholder="Search by review text..."
              value={keyword}
              onChange={(e) => setKeyword(e.target.value)}
              onKeyDown={(e) => e.key === 'Enter' && handleSearch()}
              sx={{ minWidth: 260 }}
              slotProps={{
                input: {
                  startAdornment: (
                    <InputAdornment position="start">
                      <Search fontSize="small" />
                    </InputAdornment>
                  ),
                },
              }}
            />

            <FormControl size="small" sx={{ minWidth: 180 }}>
              <InputLabel>Moderation Status</InputLabel>
              <Select
                value={statusFilter}
                label="Moderation Status"
                onChange={(e) => {
                  setStatusFilter(e.target.value);
                  setPageNum(0);
                  fetchList(0, keyword, e.target.value);
                }}
              >
                <MenuItem value="ALL">All Statuses</MenuItem>
                <MenuItem value="0">Pending Review</MenuItem>
                <MenuItem value="1">Approved</MenuItem>
                <MenuItem value="2">Rejected</MenuItem>
              </Select>
            </FormControl>

            <Button variant="contained" onClick={handleSearch} startIcon={<FilterList />}>
              Filter
            </Button>
            <Button variant="outlined" onClick={handleReset}>
              Reset
            </Button>
          </Box>
        </CardContent>
      </Card>

      {/* Table Card */}
      <Card>
        <TableContainer>
          <Table>
            <TableHead>
              <TableRow sx={{ bgcolor: 'action.hover' }}>
                <TableCell sx={{ fontWeight: 600 }}>ID</TableCell>
                <TableCell sx={{ fontWeight: 600 }}>Product</TableCell>
                <TableCell sx={{ fontWeight: 600 }}>Buyer</TableCell>
                <TableCell sx={{ fontWeight: 600 }}>Rating</TableCell>
                <TableCell sx={{ fontWeight: 600, minWidth: 250 }}>Review Content & Media</TableCell>
                <TableCell sx={{ fontWeight: 600 }}>Date</TableCell>
                <TableCell sx={{ fontWeight: 600 }}>Status</TableCell>
                <TableCell align="center" sx={{ fontWeight: 600 }}>Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {loading ? (
                <TableRow>
                  <TableCell colSpan={8} align="center" sx={{ py: 4 }}>
                    Loading reviews...
                  </TableCell>
                </TableRow>
              ) : list.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={8} align="center" sx={{ py: 4, color: 'text.secondary' }}>
                    No reviews found matching the filters.
                  </TableCell>
                </TableRow>
              ) : (
                list.map((review) => {
                  const statusInfo = STATUS_CONFIG[review.status] || { label: 'Unknown', color: 'default' };
                  const media = review.mediaUrls || [];

                  return (
                    <TableRow key={review.id} hover>
                      <TableCell>{review.id}</TableCell>
                      <TableCell>
                        <Box sx={{ display: 'flex', alignItems: 'center', gap: 1.5 }}>
                          <Avatar
                            src={resolveImageUrl(review.productPic)}
                            variant="rounded"
                            sx={{ width: 44, height: 44, bgcolor: '#F1F5F9' }}
                          />
                          <Box>
                            <Typography variant="body2" sx={{ fontWeight: 600 }}>
                              {review.productName}
                            </Typography>
                            {review.brandName && (
                              <Chip
                                label={review.brandName}
                                size="small"
                                variant="outlined"
                                sx={{ height: 18, fontSize: '0.65rem', mt: 0.25 }}
                              />
                            )}
                          </Box>
                        </Box>
                      </TableCell>
                      <TableCell>
                        <Typography variant="body2" sx={{ fontWeight: 600 }}>
                          {review.memberNickName || review.memberUsername || 'Customer'}
                        </Typography>
                        {review.memberUsername && (
                          <Typography variant="caption" color="text.secondary" sx={{ display: 'block' }}>
                            @{review.memberUsername}
                          </Typography>
                        )}
                      </TableCell>
                      <TableCell>
                        <Rating value={review.rating} readOnly size="small" />
                      </TableCell>
                      <TableCell>
                        <Typography variant="body2" sx={{ mb: 1, lineHeight: 1.5 }}>
                          {review.content}
                        </Typography>
                        {media.length > 0 && (
                          <Box sx={{ display: 'flex', gap: 1, flexWrap: 'wrap' }}>
                            {media.map((url, i) => (
                              <Box
                                key={i}
                                onClick={() => setPreviewImage(resolveImageUrl(url))}
                                sx={{
                                  width: 48,
                                  height: 48,
                                  borderRadius: 1,
                                  overflow: 'hidden',
                                  cursor: 'pointer',
                                  border: '1px solid #CBD5E1',
                                  '&:hover': { opacity: 0.8 },
                                }}
                              >
                                <img
                                  src={resolveImageUrl(url)}
                                  alt="attachment"
                                  style={{ width: '100%', height: '100%', objectFit: 'cover' }}
                                />
                              </Box>
                            ))}
                          </Box>
                        )}
                      </TableCell>
                      <TableCell>
                        <Typography variant="caption" color="text.secondary">
                          {review.createdAt ? new Date(review.createdAt).toLocaleDateString() : '-'}
                        </Typography>
                      </TableCell>
                      <TableCell>
                        <Chip
                          label={statusInfo.label}
                          color={statusInfo.color}
                          size="small"
                          sx={{ fontWeight: 600, height: 24 }}
                        />
                      </TableCell>
                      <TableCell align="center">
                        <Box sx={{ display: 'flex', justifyContent: 'center', gap: 0.5 }}>
                          {review.status !== 1 && (
                            <IconButton
                              size="small"
                              color="success"
                              title="Approve Review"
                              onClick={() => handleStatusChange(review.id, 1)}
                            >
                              <CheckCircle fontSize="small" />
                            </IconButton>
                          )}
                          {review.status !== 2 && (
                            <IconButton
                              size="small"
                              color="warning"
                              title="Reject Review"
                              onClick={() => handleStatusChange(review.id, 2)}
                            >
                              <Cancel fontSize="small" />
                            </IconButton>
                          )}
                          <IconButton
                            size="small"
                            color="error"
                            title="Delete Review"
                            onClick={() => handleDelete(review.id)}
                          >
                            <Delete fontSize="small" />
                          </IconButton>
                        </Box>
                      </TableCell>
                    </TableRow>
                  );
                })
              )}
            </TableBody>
          </Table>
        </TableContainer>

        <TablePagination
          component="div"
          count={total}
          page={pageNum}
          rowsPerPage={pageSize}
          onPageChange={(_, p) => setPageNum(p)}
          onRowsPerPageChange={(e) => {
            setPageSize(parseInt(e.target.value, 10));
            setPageNum(0);
          }}
          rowsPerPageOptions={[5, 10, 20]}
        />
      </Card>

      {/* Image Preview Dialog */}
      <Dialog open={!!previewImage} onClose={() => setPreviewImage(null)} maxWidth="md">
        <DialogTitle sx={{ m: 0, p: 2 }}>Review Photo Attachment</DialogTitle>
        <DialogContent sx={{ p: 2, textAlign: 'center' }}>
          {previewImage && (
            <img
              src={previewImage}
              alt="Review Media Preview"
              style={{ maxWidth: '100%', maxHeight: '70vh', borderRadius: 8 }}
            />
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setPreviewImage(null)}>Close</Button>
        </DialogActions>
      </Dialog>

      {/* Notification Snackbar */}
      <Snackbar
        open={snackbar.open}
        autoHideDuration={4000}
        onClose={() => setSnackbar({ ...snackbar, open: false })}
      >
        <Alert severity={snackbar.severity} sx={{ width: '100%' }}>
          {snackbar.message}
        </Alert>
      </Snackbar>
    </Box>
  );
};

export default ProductReviewList;
