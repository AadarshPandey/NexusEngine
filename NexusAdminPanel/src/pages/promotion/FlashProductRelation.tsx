import React, { useEffect, useState } from 'react';
import { Card, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography, Paper, Button, Box, TextField, InputAdornment, Chip } from '@mui/material';
import { useParams, useSearchParams, useNavigate } from 'react-router';
import { getFlashProductRelationListAPI } from '@/apis/promotion';
import type { SmsFlashPromotionProductRelation } from '@/types/promotion';
import { resolveImageUrl } from '@/utils/media';

const FlashProductRelation: React.FC = () => {
  const { id: paramId } = useParams<{ id: string }>();
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const flashSaleId = searchParams.get('flashSaleId') || paramId || '1';
  const slotId = searchParams.get('slotId') || '1';

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
      setList(res?.content || []);
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
            <Button variant="outlined" sx={{ mr: 2 }} onClick={() => navigate(`/promotion/flash-sales/select-session?flashSaleId=${flashSaleId}`)}>Back to Sessions</Button>
            <Button variant="contained" color="secondary">🚀 Pre-warm Redis</Button>
          </Box>
        </Box>
      </Card>
      
      <TableContainer component={Paper} elevation={0} variant="outlined">
        <Table>
          <TableHead>
            <TableRow sx={{ bgcolor: 'background.default' }}>
              <TableCell>Product ID</TableCell>
              <TableCell>Image</TableCell>
              <TableCell>Product Name</TableCell>
              <TableCell>Original Price</TableCell>
              <TableCell>Flash Price</TableCell>
              <TableCell>Redis Allocated Stock</TableCell>
              <TableCell>Per-User Limit</TableCell>
              <TableCell align="center">Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {list.map((row: any) => (
              <TableRow key={row.id} hover>
                <TableCell>{row.productId ?? row.orderLineId ?? row.product?.id}</TableCell>
                <TableCell>
                  {(row.product?.primaryImageUrl || row.product?.pic) ? (
                    <Box
                      component="img"
                      src={resolveImageUrl(row.product?.primaryImageUrl || row.product?.pic)}
                      alt={row.product?.name}
                      sx={{ width: 56, height: 56, objectFit: 'contain', border: '1px solid #eee', borderRadius: 1 }}
                    />
                  ) : '-'}
                </TableCell>
                <TableCell>{row.product?.name}</TableCell>
                <TableCell>₹{row.product?.listPrice ?? row.product?.price}</TableCell>
                <TableCell>
                  <TextField size="small" defaultValue={row.flashPromotionPrice ?? row.flashPrice} InputProps={{ startAdornment: <InputAdornment position="start">₹</InputAdornment> }} sx={{ width: 120 }} />
                </TableCell>
                <TableCell>
                  <TextField size="small" defaultValue={row.flashPromotionCount ?? row.quantityAvailable} sx={{ width: 100 }} />
                  <Chip size="small" label="Strict Limit" color="error" sx={{ ml: 1 }} />
                </TableCell>
                <TableCell>
                  <TextField size="small" defaultValue={row.flashPromotionLimit ?? row.perMemberLimit} sx={{ width: 80 }} />
                </TableCell>
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
