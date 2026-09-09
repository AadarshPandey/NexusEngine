import React, { useEffect, useState } from 'react';
import { Card, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography, Paper, Button, Box, TextField, InputAdornment, Chip } from '@mui/material';
import { useSearchParams } from 'react-router';
import { getFlashProductRelationListAPI } from '@/apis/flashProductRelation';
import type { SmsFlashPromotionProductRelation } from '@/types/flash';

const FlashProductRelation: React.FC = () => {
  const [searchParams] = useSearchParams();
  const flashPromotionId = searchParams.get('flashPromotionId');
  const flashPromotionSessionId = searchParams.get('flashPromotionSessionId');

  const [list, setList] = useState<SmsFlashPromotionProductRelation[]>([]);

  useEffect(() => {
    if (flashPromotionId && flashPromotionSessionId) {
      fetchList();
    }
  }, [flashPromotionId, flashPromotionSessionId]);

  const fetchList = async () => {
    try {
      const res = await getFlashProductRelationListAPI({
        flashPromotionId: Number(flashPromotionId),
        flashPromotionSessionId: Number(flashPromotionSessionId),
        pageNum: 1,
        pageSize: 100
      });
      setList(res.data.list || []);
    } catch (e) {
      console.error(e);
    }
  };

  return (
    <Box sx={{ p: 3 }}>
      <Card sx={{ p: 3, mb: 3 }}>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <Box>
            <Typography variant="h5">Session Products & Redis Configuration</Typography>
            <Typography variant="body2" color="text.secondary">
              Configure strict inventory limits. These values are pushed to Redis to prevent overselling during the flash sale.
            </Typography>
          </Box>
          <Box>
            <Button variant="outlined" sx={{ mr: 2 }}>Bind Product</Button>
            <Button variant="contained" color="secondary">🚀 Pre-warm Redis</Button>
          </Box>
        </Box>
      </Card>
      
      <TableContainer component={Paper} elevation={0} variant="outlined">
        <Table>
          <TableHead>
            <TableRow sx={{ bgcolor: 'background.default' }}>
              <TableCell>Product ID</TableCell>
              <TableCell>Product Name</TableCell>
              <TableCell>Original Price</TableCell>
              <TableCell>Flash Price</TableCell>
              <TableCell>Redis Allocated Stock</TableCell>
              <TableCell>Per-User Limit</TableCell>
              <TableCell>Sort</TableCell>
              <TableCell align="center">Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {list.map((row) => (
              <TableRow key={row.id} hover>
                <TableCell>{row.productId}</TableCell>
                <TableCell>{row.product?.name}</TableCell>
                <TableCell>₹{row.product?.price}</TableCell>
                <TableCell>
                  <TextField size="small" defaultValue={row.flashPromotionPrice} InputProps={{ startAdornment: <InputAdornment position="start">₹</InputAdornment> }} sx={{ width: 100 }} />
                </TableCell>
                <TableCell>
                  <TextField size="small" defaultValue={row.flashPromotionCount} sx={{ width: 100 }} />
                  <Chip size="small" label="Strict Limit" color="error" sx={{ ml: 1 }} />
                </TableCell>
                <TableCell>
                  <TextField size="small" defaultValue={row.flashPromotionLimit} sx={{ width: 80 }} />
                </TableCell>
                <TableCell>{row.sort}</TableCell>
                <TableCell align="center">
                  <Button size="small" color="primary">Save Config</Button>
                  <Button size="small" color="error">Unbind</Button>
                </TableCell>
              </TableRow>
            ))}
            {list.length === 0 && (
              <TableRow>
                <TableCell colSpan={8} align="center" sx={{ py: 5 }}>No products configured for this session.</TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
};

export default FlashProductRelation;
