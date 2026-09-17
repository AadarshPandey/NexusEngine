import React, { useEffect, useState } from 'react';
import { Box, Typography, Button, Paper, Divider, Alert, CircularProgress, Select, MenuItem, FormControl, InputLabel } from '@mui/material';
import { useSelector, useDispatch } from 'react-redux';
import type { RootState, AppDispatch } from '../store';
import { generateConfirmOrder, generateOrder } from '../api/order';
import type { ConfirmOrderResult } from '../api/order';
import { clearCart } from '../store/slices/cartSlice';
import { useNavigate } from 'react-router';

const Checkout: React.FC = () => {
  const { items } = useSelector((state: RootState) => state.cart);
  const { isAuthenticated, user } = useSelector((state: RootState) => state.auth);
  const dispatch = useDispatch<AppDispatch>();
  const navigate = useNavigate();

  const [confirmData, setConfirmData] = useState<ConfirmOrderResult | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [placingOrder, setPlacingOrder] = useState(false);
  const [selectedAddressId, setSelectedAddressId] = useState<number | ''>('');

  useEffect(() => {
    if (!isAuthenticated || items.length === 0) {
      navigate('/cart');
      return;
    }
    loadConfirmOrder();
  }, [isAuthenticated, items.length]);

  const loadConfirmOrder = async () => {
    try {
      setLoading(true);
      const cartIds = items.map(i => i.id);
      const res = await generateConfirmOrder(cartIds);
      setConfirmData(res.data);
      if (res.data.memberReceiveAddressList && res.data.memberReceiveAddressList.length > 0) {
        const defaultAddr = res.data.memberReceiveAddressList.find((a: import('../api/member').UmsMemberReceiveAddress) => a.defaultStatus === 1) || res.data.memberReceiveAddressList[0];
        setSelectedAddressId(defaultAddr.id);
      }
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : 'Failed to load order confirmation data.');
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
      
      // 1. Generate the actual order in the backend
      const orderRes = await generateOrder({
        memberReceiveAddressId: selectedAddressId as number,
        payType: 2, // 2 = Razorpay
        cartIds: items.map(i => i.id)
      });
      
      const order = ((orderRes as any).data).order;
      
      // 2. Import Razorpay APIs
      const { createRazorpayOrder, verifyRazorpayPayment } = await import('../api/order');
      
      // 3. Get Razorpay specific order details from backend
      const rzpRes = await createRazorpayOrder(order.id);
      const { razorpayOrderId, keyId, amount } = rzpRes.data;

      // 4. Load Razorpay script
      const loadScript = (src: string) => new Promise((resolve) => {
        if (window.Razorpay) {
          resolve(true);
          return;
        }
        const script = document.createElement('script');
        script.src = src;
        script.onload = () => resolve(true);
        script.onerror = () => resolve(false);
        document.body.appendChild(script);
      });
      
      const scriptLoaded = await loadScript('https://checkout.razorpay.com/v1/checkout.js');
      if (!scriptLoaded) {
        alert('Failed to load Razorpay SDK. Are you online?');
        setPlacingOrder(false);
        return;
      }

      // 5. Open Razorpay Checkout overlay
      const options = {
        key: keyId, 
        amount: amount, 
        currency: 'INR',
        name: 'Nexus Engine',
        description: `Payment for Order #${order.orderSn}`,
        image: 'https://cdn.razorpay.com/logos/FF7H1D9qU0R55R_medium.png',
        order_id: razorpayOrderId,
        handler: async function (response: any) {
          try {
            await verifyRazorpayPayment(
              order.id, 
              response.razorpay_payment_id, 
              response.razorpay_order_id, 
              response.razorpay_signature
            );
            alert('Payment successful!');
            dispatch(clearCart());
            navigate('/profile');
          } catch (err) {
            alert('Payment verification failed.');
            navigate('/profile'); // Redirect to profile so they can retry payment later
          }
        },
        prefill: {
          name: user?.nickname || user?.username || 'Test Customer',
          email: 'test@example.com',
          contact: '9000090000'
        },
        theme: { color: '#3399cc' },
        modal: {
          ondismiss: function() {
            setPlacingOrder(false);
            // If they close the modal, the order is created but unpaid.
            // Redirect to profile to let them pay later.
            navigate('/profile');
          }
        }
      };
      
      const rzp = new window.Razorpay(options);
      rzp.on('payment.failed', function (response: any){
        alert(`Payment Failed: ${response.error.description}`);
      });
      rzp.open();

    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : 'Failed to place order or process payment.');
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
              {confirmData.memberReceiveAddressList.map((addr: import('../api/member').UmsMemberReceiveAddress) => (
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
          {placingOrder ? 'Processing...' : 'Place Order & Pay'}
        </Button>
      </Paper>
    </Box>
  );
};

export default Checkout;
