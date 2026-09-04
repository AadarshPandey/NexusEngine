import React, { useEffect } from 'react';
import { Box, Typography, Button, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, IconButton } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import { useSelector, useDispatch } from 'react-redux';
import type { RootState, AppDispatch } from '../store';
import { loadCartData, removeCartItem } from '../store/slices/cartSlice';
import { useNavigate } from 'react-router';

const Cart: React.FC = () => {
  const { items, totalQuantity } = useSelector((state: RootState) => state.cart);
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

  const totalPrice = items.reduce((total, item) => total + (item.price * item.quantity), 0);

  return (
    <Box sx={{ mt: 4 }}>
      <Typography variant="h4" gutterBottom>Shopping Cart</Typography>
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
                    <TableCell align="right">Quantity</TableCell>
                    <TableCell align="right">Total</TableCell>
                    <TableCell align="right">Action</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {items.map((item) => (
                    <TableRow key={item.id}>
                      <TableCell sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                        <img src={item.productPic || 'https://via.placeholder.com/50'} alt={item.productName} style={{ width: 50, height: 50, objectFit: 'contain' }} />
                        <Typography variant="body2">{item.productName}</Typography>
                      </TableCell>
                      <TableCell align="right">₹{item.price?.toFixed(2)}</TableCell>
                      <TableCell align="right">{item.quantity}</TableCell>
                      <TableCell align="right">₹{(item.price * item.quantity).toFixed(2)}</TableCell>
                      <TableCell align="right">
                        <IconButton color="error" onClick={() => dispatch(removeCartItem(item.id))}>
                          <DeleteIcon />
                        </IconButton>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>
          </Box>
          <Box sx={{ flex: 1 }}>
            <Paper sx={{ p: 3 }}>
              <Typography variant="h6" gutterBottom>Order Summary</Typography>
              <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 2 }}>
                <Typography>Items ({totalQuantity}):</Typography>
                <Typography>₹{totalPrice.toFixed(2)}</Typography>
              </Box>
              <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 3 }}>
                <Typography variant="h5" sx={{ fontWeight: 'bold' }}>Total:</Typography>
                <Typography variant="h5" color="primary" sx={{ fontWeight: 'bold' }}>₹{totalPrice.toFixed(2)}</Typography>
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
