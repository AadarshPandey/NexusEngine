import React, { useEffect, useState } from 'react';
import { Box, Typography, Button, Paper, Divider, Alert, CircularProgress, Select, MenuItem, FormControl, InputLabel, TextField } from '@mui/material';
import { useSelector, useDispatch } from 'react-redux';
import type { RootState, AppDispatch } from '../store';
import { generateConfirmOrder, generateOrder, createRazorpayOrder, verifyRazorpayPayment, paySuccess } from '../api/order';
import type { ConfirmOrderResult } from '../api/order';
import { clearCart } from '../store/slices/cartSlice';
import { useNavigate } from 'react-router';

const Checkout: React.FC = () => {
  const { items } = useSelector((state: RootState) => state.cart);
  const { isAuthenticated } = useSelector((state: RootState) => state.auth);
  const dispatch = useDispatch<AppDispatch>();
  const navigate = useNavigate();

  const [confirmData, setConfirmData] = useState<ConfirmOrderResult | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [placingOrder, setPlacingOrder] = useState(false);
  const [selectedAddressId, setSelectedAddressId] = useState<number | ''>('');

  const [showMockPayment, setShowMockPayment] = useState(false);
  const [mockOrderId, setMockOrderId] = useState<number | null>(null);
  const [cardData, setCardData] = useState({ number: '', expiry: '', cvv: '' });

  useEffect(() => {
    if (!isAuthenticated || items.length === 0) {
      navigate('/cart');
      return;
    }
    loadConfirmOrder();
  }, [isAuthenticated, items]);

  const loadConfirmOrder = async () => {
    try {
      setLoading(true);
      const cartIds = items.map(i => i.id);
      const res = await generateConfirmOrder(cartIds);
      setConfirmData(res.data);
      if (res.data.memberReceiveAddressList && res.data.memberReceiveAddressList.length > 0) {
        const defaultAddr = res.data.memberReceiveAddressList.find((a: any) => a.defaultStatus === 1) || res.data.memberReceiveAddressList[0];
        setSelectedAddressId(defaultAddr.id);
      }
    } catch (err: any) {
      setError(err.message || 'Failed to load order confirmation data.');
    } finally {
      setLoading(false);
    }
  };

  const handlePlaceOrder = async () => {
    try {
      setPlacingOrder(true);
      if (!selectedAddressId) {
        setError('Please select a shipping address.');
        setPlacingOrder(false);
        return;
      }
      
      const orderRes = await generateOrder({
        memberReceiveAddressId: selectedAddressId as number,
        payType: 2,
        cartIds: items.map(i => i.id)
      });
      
      const orderId = orderRes.data.order.id;
      
      // Open Mock Payment Modal for Interview Demo
      setMockOrderId(orderId);
      setShowMockPayment(true);
      setPlacingOrder(false);

    } catch (err: any) {
      setError(err.message || 'Failed to place order or process payment.');
      setPlacingOrder(false);
    }
  };

  const handleMockPaymentSubmit = async () => {
    if (!cardData.number || !cardData.expiry || !cardData.cvv) {
      alert("Please enter the test card details (e.g. 4111 1111 1111 1111).");
      return;
    }
    try {
      // Simulate Razorpay network delay
      setPlacingOrder(true);
      await new Promise(r => setTimeout(r, 1500));
      
      // Directly call backend to mark order as paid
      await paySuccess(mockOrderId as number, 2);
      
      alert('Payment Successful! Order has been placed and paid.');
      dispatch(clearCart());
      setShowMockPayment(false);
      navigate('/profile');
    } catch(err) {
      alert("Payment failed!");
    } finally {
      setPlacingOrder(false);
    }
  };

  if (loading) {
    return <Box sx={{ display: 'flex', justifyContent: 'center', mt: 10 }}><CircularProgress /></Box>;
  }

  return (
    <Box sx={{ mt: 4, maxWidth: 800, mx: 'auto' }}>
      <Typography variant="h4" gutterBottom>Checkout</Typography>
      
      {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}

      <Paper sx={{ p: 4, mb: 4 }}>
        <Typography variant="h6" gutterBottom>Shipping Address</Typography>
        {confirmData?.memberReceiveAddressList && confirmData.memberReceiveAddressList.length > 0 ? (
          <FormControl fullWidth sx={{ mt: 2 }}>
            <InputLabel>Select Shipping Address</InputLabel>
            <Select
              value={selectedAddressId}
              label="Select Shipping Address"
              onChange={(e) => setSelectedAddressId(e.target.value as number)}
            >
              {confirmData.memberReceiveAddressList.map((addr: any) => (
                <MenuItem key={addr.id} value={addr.id}>
                  {addr.name} - {addr.phoneNumber} ({addr.province}, {addr.city}, {addr.region}, {addr.detailAddress} - PIN: {addr.postCode})
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        ) : (
          <Alert severity="warning" sx={{ mt: 2 }}>
            You have no shipping addresses. Please add one in your Profile before checking out.
          </Alert>
        )}
      </Paper>

      <Paper sx={{ p: 4, mb: 4 }}>
        <Typography variant="h6" gutterBottom>Order Items</Typography>
        {confirmData?.cartPromotionItemList.map((item) => (
          <Box key={item.id} sx={{ display: 'flex', justifyContent: 'space-between', mb: 2 }}>
            <Typography>{item.productName} x {item.quantity}</Typography>
            <Typography>₹{(item.price * item.quantity).toFixed(2)}</Typography>
          </Box>
        ))}
        <Divider sx={{ my: 2 }} />
        
        <Box sx={{ display: 'flex', justifyContent: 'flex-end', gap: 4, mb: 1 }}>
          <Typography color="text.secondary">Total Amount:</Typography>
          <Typography>₹{confirmData?.calcAmount.totalAmount?.toFixed(2)}</Typography>
        </Box>
        <Box sx={{ display: 'flex', justifyContent: 'flex-end', gap: 4, mb: 1 }}>
          <Typography color="text.secondary">Freight:</Typography>
          <Typography>₹{confirmData?.calcAmount.freightAmount?.toFixed(2)}</Typography>
        </Box>
        <Box sx={{ display: 'flex', justifyContent: 'flex-end', gap: 4, mb: 3 }}>
          <Typography variant="h6" sx={{ fontWeight: 'bold' }}>Payable Amount:</Typography>
          <Typography variant="h6" color="primary" sx={{ fontWeight: 'bold' }}>₹{confirmData?.calcAmount.payAmount?.toFixed(2)}</Typography>
        </Box>

        <Button 
          variant="contained" 
          color="primary" 
          fullWidth 
          size="large" 
          onClick={handlePlaceOrder}
          disabled={placingOrder}
        >
          {placingOrder ? 'Processing...' : 'Place Order'}
        </Button>
      </Paper>

      {/* MOCK PAYMENT MODAL */}
      {showMockPayment && (
        <Paper elevation={24} sx={{
          position: 'fixed', top: '50%', left: '50%', transform: 'translate(-50%, -50%)',
          p: 4, width: 400, zIndex: 9999, borderRadius: 2
        }}>
          <Typography variant="h5" gutterBottom sx={{ fontWeight: 'bold', color: '#6366F1' }}>Razorpay Test Gateway</Typography>
          <Typography variant="body2" color="text.secondary" sx={{ mb: 3 }}>
            Use a test card (e.g. 4111 1111 1111 1111) to simulate a successful payment.
          </Typography>
          
          <TextField 
            fullWidth label="Card Number" variant="outlined" sx={{ mb: 2 }}
            value={cardData.number} onChange={e => setCardData({...cardData, number: e.target.value})}
            placeholder="4111 1111 1111 1111"
          />
          <Box sx={{ display: 'flex', gap: 2, mb: 3 }}>
            <TextField 
              label="Expiry" variant="outlined" placeholder="12/28"
              value={cardData.expiry} onChange={e => setCardData({...cardData, expiry: e.target.value})}
            />
            <TextField 
              label="CVV" variant="outlined" placeholder="123"
              value={cardData.cvv} onChange={e => setCardData({...cardData, cvv: e.target.value})}
            />
          </Box>
          
          <Box sx={{ display: 'flex', justifyContent: 'flex-end', gap: 2 }}>
            <Button color="inherit" onClick={() => { setShowMockPayment(false); setPlacingOrder(false); }}>Cancel</Button>
            <Button variant="contained" onClick={handleMockPaymentSubmit} disabled={placingOrder}>
              {placingOrder ? 'Authenticating...' : `Pay ₹${confirmData?.calcAmount.payAmount?.toFixed(2)}`}
            </Button>
          </Box>
        </Paper>
      )}
      
      {showMockPayment && (
        <Box sx={{ position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, bgcolor: 'rgba(0,0,0,0.5)', zIndex: 9998 }} />
      )}
    </Box>
  );
};

export default Checkout;
