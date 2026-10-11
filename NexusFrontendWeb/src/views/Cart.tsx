import React, { useEffect } from 'react';
import { Box, Typography, Button, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, IconButton, Chip } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import AddIcon from '@mui/icons-material/Add';
import RemoveIcon from '@mui/icons-material/Remove';
import { useSelector, useDispatch } from 'react-redux';
import type { RootState, AppDispatch } from '../store';
import { loadCartData, removeCartItem, updateQuantity } from '../store/slices/cartSlice';
import { resolveImageUrl } from '../utils/media';
import { useNavigate } from 'react-router';

const Cart: React.FC = () => {
  const { items, totalQuantity, totalSubtotal } = useSelector((state: RootState) => state.cart);
  const { isAuthenticated } = useSelector((state: RootState) => state.auth);
  const dispatch = useDispatch<AppDispatch>();
  const navigate = useNavigate();

  useEffect(() => {
    if (isAuthenticated) {
      dispatch(loadCartData());
    }
  }, [isAuthenticated, dispatch]);

  const handleCheckout = () => {
    navigate('/checkout');
  };

  if (!isAuthenticated) {
    return (
      <Box sx={{ mt: 10, textAlign: 'center' }}>
        <Typography variant="h5" gutterBottom>Please login to view your cart</Typography>
        <Button variant="contained" onClick={() => navigate('/login')}>Login</Button>
      </Box>
    );
  }

  const effectiveTotal = totalSubtotal > 0 ? totalSubtotal : items.reduce((total, item) => {
    const itemPrice = item.currentSalePrice ?? item.currentListPrice ?? item.price ?? 0;
    return total + itemPrice * item.quantity;
  }, 0);

  return (
    <Box sx={{ mt: 4 }}>
      <Typography variant="h4" gutterBottom>Shopping Cart ({totalQuantity} items)</Typography>
      {items.length === 0 ? (
        <Paper sx={{ p: 4, textAlign: 'center' }}>
          <Typography variant="h6">Your cart is empty.</Typography>
          <Button variant="outlined" sx={{ mt: 2 }} onClick={() => navigate('/')}>Continue Shopping</Button>
        </Paper>
      ) : (
        <Box sx={{ display: 'flex', flexDirection: { xs: 'column', md: 'row' }, gap: 4 }}>
          <Box sx={{ flex: 3 }}>
            <TableContainer component={Paper}>
              <Table>
                <TableHead>
                  <TableRow>
                    <TableCell>Product</TableCell>
                    <TableCell align="right">Price</TableCell>
                    <TableCell align="center">Quantity</TableCell>
                    <TableCell align="right">Total</TableCell>
                    <TableCell align="right">Action</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {items.map((item) => {
                    const itemImg = resolveImageUrl(item.primaryImageUrl);
                    const itemName = item.name;
                    const itemPrice = item.currentSalePrice ?? item.currentListPrice ?? item.price ?? 0;
                    const lineTotal = itemPrice * item.quantity;

                    return (
                      <TableRow key={item.id}>
                        <TableCell sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                          <img
                            src={itemImg}
                            alt={itemName}
                            style={{ width: 50, height: 50, objectFit: 'contain', borderRadius: 4 }}
                          />
                          <Box>
                            <Typography variant="body2" sx={{ fontWeight: 600 }}>{itemName}</Typography>
                            {item.skuCode && (
                              <Typography variant="caption" color="text.secondary" sx={{ display: 'block' }}>
                                SKU: {item.skuCode}
                              </Typography>
                            )}
                            {item.hasPriceChanged && (
                              <Chip label="Price updated" color="warning" size="small" sx={{ mt: 0.5, height: 20, fontSize: '0.7rem' }} />
                            )}
                          </Box>
                        </TableCell>
                        <TableCell align="right">₹{Number(itemPrice).toFixed(2)}</TableCell>
                        <TableCell align="center">
                          <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                            <IconButton size="small" onClick={() => dispatch(updateQuantity({ id: item.id, quantity: Math.max(1, item.quantity - 1) }))}>
                              <RemoveIcon fontSize="small" />
                            </IconButton>
                            <Typography sx={{ mx: 2, minWidth: 20, textAlign: 'center' }}>{item.quantity}</Typography>
                            <IconButton size="small" onClick={() => dispatch(updateQuantity({ id: item.id, quantity: item.quantity + 1 }))}>
                              <AddIcon fontSize="small" />
                            </IconButton>
                          </Box>
                        </TableCell>
                        <TableCell align="right">₹{Number(lineTotal).toFixed(2)}</TableCell>
                        <TableCell align="right">
                          <IconButton color="error" onClick={() => dispatch(removeCartItem(item.id))}>
                            <DeleteIcon />
                          </IconButton>
                        </TableCell>
                      </TableRow>
                    );
                  })}
                </TableBody>
              </Table>
            </TableContainer>
          </Box>
          <Box sx={{ flex: 1 }}>
            <Paper sx={{ p: 3 }}>
              <Typography variant="h6" gutterBottom>Order Summary</Typography>
              <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                <Typography>Items ({totalQuantity}):</Typography>
                <Typography>₹{Number(effectiveTotal).toFixed(2)}</Typography>
              </Box>
              <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 2 }}>
                <Typography variant="h6">Subtotal:</Typography>
                <Typography variant="h6" color="primary">₹{Number(effectiveTotal).toFixed(2)}</Typography>
              </Box>
              <Button variant="contained" color="primary" fullWidth size="large" onClick={handleCheckout}>
                Proceed to Checkout
              </Button>
            </Paper>
          </Box>
        </Box>
      )}
    </Box>
  );
};

export default Cart;
