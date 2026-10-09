import React, { useEffect, useState } from 'react';
import { Card, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography, Paper, Button, Box, TextField, InputAdornment, Chip } from '@mui/material';
import { useSearchParams } from 'react-router';
import { getFlashProductRelationListAPI } from '@/apis/flashProductRelation';
import type { SmsFlashPromotionProductRelation } from '@/types/flash';

const FlashProductRelation: React.FC = () => {
  const [searchParams] = useSearchParams();
  const flashSaleId = searchParams.get('flashSaleId');
  const slotId = searchParams.get('slotId');

  const [list, setList] = useState<SmsFlashPromotionProductRelation[]>([]);

  useEffect(() => {
    if (flashSaleId && slotId) {
      fetchList();
    }
  }, [flashSaleId, slotId]);

  const fetchList = async () => {
    try {
      const res = await getFlashProductRelationListAPI({
        flashSaleId: Number(flashSaleId),
        slotId: Number(slotId),
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
              Configure strict inventory limits. These values are pushed to Redis to prevent overselling during the flash unitsSold.
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
              <TableCell align="center">Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {list.map((row) => (
              <TableRow key={row.id} hover>
                <TableCell>{row.orderLineId}</TableCell>
                <TableCell>{row.product?.name}</TableCell>
                <TableCell>₹{row.product?.listPrice}</TableCell>
                <TableCell>
                  <TextField size="small" defaultValue={row.flashPrice} InputProps={{ startAdornment: <InputAdornment position="start">₹</InputAdornment> }} sx={{ width: 100 }} />
                </TableCell>
                <TableCell>
                  <TextField size="small" defaultValue={row.quantityAvailable} sx={{ width: 100 }} />
                  <Chip size="small" label="Strict Limit" color="error" sx={{ ml: 1 }} />
                </TableCell>
                <TableCell>
                  <TextField size="small" defaultValue={row.perMemberLimit} sx={{ width: 80 }} />
                </TableCell>
                <TableCell align="center">
                  <Button size="small" color="primary">Save Config</Button>
                  <Button size="small" color="error">Unbind</Button>
                </TableCell>
              </TableRow>
            ))}
            {list.length === 0 && (
              <TableRow>
                <TableCell colSpan={7} align="center" sx={{ py: 5 }}>No products configured for this session.</TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
};

export default FlashProductRelation;
