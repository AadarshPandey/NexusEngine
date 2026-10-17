import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router';
import {
  Box,
  Card,
  CardContent,
  Typography,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  TextField,
  MenuItem,
  Button,
  Snackbar,
  Alert,
} from '@mui/material';
import { getOrderListAPI, orderDeliveryAPI } from '@/apis/ordering';
import type { OrderDeliveryParam } from '@/types/ordering';

const CARRIERS = ['UPS Express', 'FedEx Priority', 'DHL Express', 'USPS Priority'];

const DeliverOrderList: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [deliveryList, setDeliveryList] = useState<
    Array<OrderDeliveryParam & { orderSn?: string; receiverName?: string; receiverDetailAddress?: string }>
  >([]);
  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' as 'success' | 'error' });

  useEffect(() => {
    const stateList = (location.state as any)?.list;
    if (Array.isArray(stateList) && stateList.length > 0) {
      setDeliveryList(stateList);
    } else {
      getOrderListAPI({ status: 1, pageNum: 1, pageSize: 20 })
        .then((res) => {
          const rows = Array.isArray(res) ? res : (res as any)?.content || [];
          const pending = rows.map((o: any) => ({
            orderId: o.id!,
            orderSn: o.orderSn || o.orderNumber,
            receiverName: o.receiverName,
            receiverDetailAddress: [o.receiverProvince, o.receiverCity, o.receiverDetailAddress]
              .filter(Boolean)
              .join(', '),
            deliveryCompany: o.deliveryCompany || CARRIERS[0],
            deliverySn: o.deliverySn || `TRK-${o.id}-${Date.now().toString().slice(-6)}`,
          }));
          setDeliveryList(pending);
        })
        .catch(console.error);
    }
  }, [location.state]);

  const handleConfirm = async () => {
    if (deliveryList.length === 0) return;
    try {
      await orderDeliveryAPI(
        deliveryList.map((item) => ({
          orderId: item.orderId,
          deliveryCompany: item.deliveryCompany,
          deliverySn: item.deliverySn,
        })),
      );
      setSnackbar({ open: true, message: 'Orders dispatched successfully', severity: 'success' });
      setTimeout(() => navigate('/ordering/orders'), 1000);
    } catch {
      setSnackbar({ open: true, message: 'Failed to dispatch orders', severity: 'error' });
    }
  };

  return (
    <Box>
      <Typography variant="h5" sx={{ fontWeight: 'bold', mb: 3 }}>
        Dispatch Orders
      </Typography>
      <Card>
        <CardContent sx={{ p: 0 }}>
          <TableContainer>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>Order Number</TableCell>
                  <TableCell>Recipient</TableCell>
                  <TableCell>Address</TableCell>
                  <TableCell>Carrier</TableCell>
                  <TableCell>Tracking Number</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {deliveryList.length === 0 ? (
                  <TableRow>
                    <TableCell colSpan={5} align="center" sx={{ py: 4, color: 'text.secondary' }}>
                      No paid orders awaiting shipment.
                    </TableCell>
                  </TableRow>
                ) : (
                  deliveryList.map((row, idx) => (
                    <TableRow key={row.orderId}>
                      <TableCell sx={{ fontFamily: 'monospace', fontWeight: 600 }}>{row.orderSn}</TableCell>
                      <TableCell>{row.receiverName}</TableCell>
                      <TableCell>{row.receiverDetailAddress}</TableCell>
                      <TableCell width={200}>
                        <TextField
                          select
                          size="small"
                          fullWidth
                          value={row.deliveryCompany}
                          onChange={(e) => {
                            const next = [...deliveryList];
                            next[idx].deliveryCompany = e.target.value;
                            setDeliveryList(next);
                          }}
                        >
                          {CARRIERS.map((c) => (
                            <MenuItem key={c} value={c}>
                              {c}
                            </MenuItem>
                          ))}
                        </TextField>
                      </TableCell>
                      <TableCell width={240}>
                        <TextField
                          size="small"
                          fullWidth
                          value={row.deliverySn}
                          onChange={(e) => {
                            const next = [...deliveryList];
                            next[idx].deliverySn = e.target.value;
                            setDeliveryList(next);
                          }}
                        />
                      </TableCell>
                    </TableRow>
                  ))
                )}
              </TableBody>
            </Table>
          </TableContainer>
          <Box sx={{ display: 'flex', justifyContent: 'flex-end', gap: 2, p: 2.5 }}>
            <Button variant="outlined" onClick={() => navigate('/ordering/orders')}>
              Cancel
            </Button>
            <Button variant="contained" onClick={handleConfirm} disabled={deliveryList.length === 0}>
              Confirm Dispatch
            </Button>
          </Box>
        </CardContent>
      </Card>
      <Snackbar open={snackbar.open} autoHideDuration={3000} onClose={() => setSnackbar((s) => ({ ...s, open: false }))}>
        <Alert severity={snackbar.severity}>{snackbar.message}</Alert>
      </Snackbar>
    </Box>
  );
};

export default DeliverOrderList;
