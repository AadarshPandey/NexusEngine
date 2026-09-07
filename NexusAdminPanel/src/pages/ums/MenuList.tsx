import React, { useState, useEffect, useCallback } from 'react';
import {
  Box,
  Card,
  CardContent,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Button,
  Typography,
  IconButton,
  Snackbar,
  Alert,
  TablePagination,
  Switch,
  Chip,
  Breadcrumbs,
  Link as MuiLink,
} from '@mui/material';
import { Add, Delete, Edit, Visibility, VisibilityOff } from '@mui/icons-material';
import { useNavigate, useSearchParams } from 'react-router';
import { getMenuListByParentIdAPI, deleteMenuByIdAPI, menuUpdateHiddenByIdAPI } from '@/apis/menu';
import type { UmsMenu } from '@/types/menu';

const MenuList: React.FC = () => {
  const navigate = useNavigate();
  const [searchParams, setSearchParams] = useSearchParams();
  const parentId = Number(searchParams.get('parentId')) || 0;

  const [list, setList] = useState<UmsMenu[]>([]);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);
  const [pageNum, setPageNum] = useState(0);
  const [pageSize, setPageSize] = useState(10);
  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' as 'success' | 'error' });

  const fetchList = useCallback(async () => {
    setLoading(true);
    try {
      // Force pageNum > 0 check on backend
      const res = await getMenuListByParentIdAPI(parentId, { pageNum: pageNum > 0 ? pageNum : 0, pageSize });
      setList(res.data.list);
      setTotal(res.data.total);
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  }, [parentId, pageNum, pageSize]);

  useEffect(() => {
    fetchList();
  }, [fetchList]);

  const handlePageChange = (event: unknown, newPage: number) => {
    setPageNum(newPage);
  };

  const handlePageSizeChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setPageSize(parseInt(event.target.value, 10));
    setPageNum(0);
  };

  const handleDelete = async (id: number) => {
    if (!window.confirm('Are you sure you want to delete this menu?')) return;
    try {
      await deleteMenuByIdAPI(id);
      setSnackbar({ open: true, message: 'Deleted successfully', severity: 'success' });
      fetchList();
    } catch {
      setSnackbar({ open: true, message: 'Delete failed', severity: 'error' });
    }
  };

  const handleToggleHidden = async (id: number, hidden: number) => {
    try {
      await menuUpdateHiddenByIdAPI(id, { hidden });
      setSnackbar({ open: true, message: 'Status updated', severity: 'success' });
      fetchList();
    } catch {
      setSnackbar({ open: true, message: 'Update failed', severity: 'error' });
    }
  };

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 3 }}>
        <Box>
          <Breadcrumbs aria-label="breadcrumb">
            <MuiLink 
              color="inherit" 
              sx={{ cursor: 'pointer', textDecoration: 'none', '&:hover': { textDecoration: 'underline' } }} 
              onClick={() => {
                setPageNum(0);
                setSearchParams({ parentId: '0' });
              }}
            >
              Root Menus
            </MuiLink>
            {parentId !== 0 && (
              <Typography color="text.primary">Sub Menus</Typography>
            )}
          </Breadcrumbs>
          <Typography variant="h5" sx={{ mt: 1, fontWeight: 'bold', color: '#1E293B' }}>
            Menu Management
          </Typography>
        </Box>
        <Button
          variant="contained"
          startIcon={<Add />}
          onClick={() => navigate('/ums/addMenu')}
          sx={{ bgcolor: '#6366F1', '&:hover': { bgcolor: '#4F46E5' }, textTransform: 'none' }}
        >
          Add Menu
        </Button>
      </Box>

      <Card sx={{ boxShadow: '0px 4px 20px rgba(0, 0, 0, 0.05)', borderRadius: 2 }}>
        <CardContent sx={{ p: 0 }}>
          <TableContainer component={Paper} elevation={0}>
            <Table>
              <TableHead sx={{ bgcolor: 'action.hover' }}>
                <TableRow>
                  <TableCell>ID</TableCell>
                  <TableCell>Title</TableCell>
                  <TableCell>Level</TableCell>
                  <TableCell>Frontend Name</TableCell>
                  <TableCell>Icon</TableCell>
                  <TableCell>Visible</TableCell>
                  <TableCell>Sort</TableCell>
                  <TableCell align="center">Actions</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {list.map((row) => (
                  <TableRow key={row.id} hover>
                    <TableCell>{row.id}</TableCell>
                    <TableCell sx={{ fontWeight: 500 }}>{row.title}</TableCell>
                    <TableCell>
                      <Chip label={row.level === 0 ? 'Top Level' : 'Sub Level'} size="small" color={row.level === 0 ? 'primary' : 'default'} variant="outlined" />
                    </TableCell>
                    <TableCell>{row.name}</TableCell>
                    <TableCell>{row.icon}</TableCell>
                    <TableCell>
                      <Switch
                        checked={row.hidden === 0}
                        onChange={(e) => handleToggleHidden(row.id!, e.target.checked ? 0 : 1)}
                        color="success"
                      />
                    </TableCell>
                    <TableCell>{row.sort}</TableCell>
                    <TableCell align="center">
                      <Button 
                        size="small" 
                        disabled={row.level !== 0} 
                        onClick={() => {
                          setPageNum(0);
                          setSearchParams({ parentId: row.id!.toString() });
                        }}
                        sx={{ mr: 1, textTransform: 'none' }}
                      >
                        View Next Level
                      </Button>
                      <IconButton size="small" onClick={() => navigate(`/ums/updateMenu?id=${row.id}`)} sx={{ color: '#6366F1' }}>
                        <Edit fontSize="small" />
                      </IconButton>
                      <IconButton size="small" onClick={() => handleDelete(row.id!)} sx={{ color: '#EF4444' }}>
                        <Delete fontSize="small" />
                      </IconButton>
                    </TableCell>
                  </TableRow>
                ))}
                {list.length === 0 && !loading && (
                  <TableRow>
                    <TableCell colSpan={8} align="center" sx={{ py: 4, color: 'text.secondary' }}>
                      No menus found.
                    </TableCell>
                  </TableRow>
                )}
              </TableBody>
            </Table>
          </TableContainer>
          <TablePagination
            component="div"
            count={total}
            page={pageNum}
            onPageChange={handlePageChange}
            rowsPerPage={pageSize}
            onRowsPerPageChange={handlePageSizeChange}
            rowsPerPageOptions={[5, 10, 20]}
          />
        </CardContent>
      </Card>

      <Snackbar open={snackbar.open} autoHideDuration={3000} onClose={() => setSnackbar(s => ({ ...s, open: false }))} anchorOrigin={{ vertical: 'top', horizontal: 'center' }}>
        <Alert severity={snackbar.severity} onClose={() => setSnackbar(s => ({ ...s, open: false }))}>{snackbar.message}</Alert>
      </Snackbar>
    </Box>
  );
};

export default MenuList;
