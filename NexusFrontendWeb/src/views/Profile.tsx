import React, { useState, useEffect } from 'react';
import {
  Box,
  Typography,
  Paper,
  Tabs,
  Tab,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Avatar,
  Divider,
  CircularProgress,
  Button,
  TextField,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Chip,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  Grid,
} from '@mui/material';
import { useSelector } from 'react-redux';
import type { RootState } from '../store';
import { getOrders, submitReturnApply, getReturnReasons, cancelUserOrder } from '../api/ordering';
import { OrderStatus, type CustomerOrder, type OrderLine, type ReturnRequest } from '../types/ordering';
import {
  getCustomerProfile,
  getCustomerAddresses,
  createCustomerAddress,
  updateCustomerProfile,
  uploadAvatar,
} from '../api/customer';
import { getMemberCoupons } from '../api/promotion';
import type { Coupon } from '../types/promotion';
import type { Member, MemberAddress } from '../types/customer';
import { createRazorpayOrder, verifyRazorpayPayment } from '../api/payment';
import { resolveImageUrl } from '../utils/media';
import { useNavigate } from 'react-router';

interface TabPanelProps {
  children?: React.ReactNode;
  index: number;
  value: number;
}

const ORDER_STATUS_LABELS: Record<number, string> = {
  [OrderStatus.PENDING_PAYMENT]: 'Pending Payment',
  [OrderStatus.AWAITING_SHIPMENT]: 'Awaiting Shipment',
  [OrderStatus.SHIPPED]: 'Shipped',
  [OrderStatus.COMPLETED]: 'Completed',
  [OrderStatus.CANCELLED]: 'Cancelled',
  5: 'Out for Delivery',
  6: 'Refunded',
};

function TabPanel(props: TabPanelProps) {
  const { children, value, index, ...other } = props;
  return (
    <div role="tabpanel" hidden={value !== index} {...other}>
      {value === index && <Box sx={{ p: 3 }}>{children}</Box>}
    </div>
  );
}

const Profile: React.FC = () => {
  const { isAuthenticated, user: authUser } = useSelector((state: RootState) => state.auth);
  const navigate = useNavigate();

  const [tabValue, setTabValue] = useState(0);
  const [memberInfo, setMemberInfo] = useState<Member | null>(null);
  const [orders, setOrders] = useState<CustomerOrder[]>([]);
  const [addresses, setAddresses] = useState<MemberAddress[]>([]);
  const [coupons, setCoupons] = useState<Coupon[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [openAddAddress, setOpenAddAddress] = useState(false);
  const [newAddress, setNewAddress] = useState<Partial<MemberAddress>>({
    recipientName: '',
    recipientPhone: '',
    addressLine: '',
    postalCode: '',
    city: '',
    state: '',
    district: '',
  });

  // Refund dialog states
  const [openRefundDialog, setOpenRefundDialog] = useState(false);
  const [openEditProfile, setOpenEditProfile] = useState(false);
  const [editProfileData, setEditProfileData] = useState({ displayName: '', iconUrl: '' });

  const handleEditProfileOpen = () => {
    setEditProfileData({
      displayName: memberInfo?.displayName || '',
      iconUrl: memberInfo?.avatarUrl || '',
    });
    setOpenEditProfile(true);
  };

  const handleEditProfileSubmit = async () => {
    try {
      await updateCustomerProfile(editProfileData);
      setOpenEditProfile(false);
      loadDashboardData();
    } catch (e) {
      console.error(e);
      alert('Failed to update profile');
    }
  };

  const [refundOrder, setRefundOrder] = useState<CustomerOrder | null>(null);
  const [selectedRefundItems, setSelectedRefundItems] = useState<(OrderLine & { returnQuantity: number })[]>([]);
  const [refundReason, setRefundReason] = useState('');
  const [returnReasonsList, setReturnReasonsList] = useState<{ id: number; name: string }[]>([]);

  useEffect(() => {
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }
    loadDashboardData();
  }, [isAuthenticated, navigate]);

  const loadDashboardData = async () => {
    try {
      setLoading(true);
      setError(null);
      const [memberRes, orderRes, addressRes, couponRes] = await Promise.all([
        getCustomerProfile(),
        getOrders(-1, 1, 100),
        getCustomerAddresses(),
        getMemberCoupons(0).catch(() => []),
      ]);
      setMemberInfo(memberRes);
      setOrders(orderRes.content || []);
      setAddresses(addressRes || []);
      setCoupons(Array.isArray(couponRes) ? couponRes : []);
    } catch (err: unknown) {
      console.error('Failed to load dashboard data', err);
      setError(err instanceof Error ? err.message : 'Failed to load dashboard data');
    } finally {
      setLoading(false);
    }
  };

  const handleTabChange = (_event: React.SyntheticEvent, newValue: number) => {
    setTabValue(newValue);
  };

  const handleAddAddress = async () => {
    try {
      await createCustomerAddress(newAddress);
      setOpenAddAddress(false);
      const addressRes = await getCustomerAddresses();
      setAddresses(addressRes || []);
      setNewAddress({ recipientName: '', recipientPhone: '', addressLine: '', postalCode: '', city: '', state: '', district: '' });
      alert('Address added successfully!');
    } catch {
      alert('Failed to add address');
    }
  };

  const handleOpenRefund = async (order: CustomerOrder) => {
    setRefundOrder(order);
    setSelectedRefundItems([]);
    setRefundReason('');
    try {
      const res = await getReturnReasons();
      const reasons = res || [];
      setReturnReasonsList(reasons);
      if (reasons.length > 0) {
        setRefundReason(reasons[0].name);
      }
    } catch (e) {
      console.error(e);
    }
    setOpenRefundDialog(true);
  };

  const handleRefundItemToggle = (item: OrderLine) => {
    const currentIndex = selectedRefundItems.findIndex((i) => i.id === item.id);
    const newSelected = [...selectedRefundItems];
    if (currentIndex === -1) {
      const totalReturnedQty =
        refundOrder?.returnApplyList
          ?.filter((r: ReturnRequest) => r.productId === item.productId)
          .reduce((sum, r) => sum + (r.quantity || 0), 0) || 0;
      const qty = item.quantity ?? 1;
      const maxReturnableQty = Math.max(0, qty - totalReturnedQty);
      newSelected.push({ ...item, returnQuantity: maxReturnableQty });
    } else {
      newSelected.splice(currentIndex, 1);
    }
    setSelectedRefundItems(newSelected);
  };

  const handleRefundSubmit = async () => {
    if (selectedRefundItems.length === 0) {
      alert('Please select at least one item to refund.');
      return;
    }
    if (!refundReason.trim()) {
      alert('Please provide a reason for the refund.');
      return;
    }
    if (!refundOrder) return;

    try {
      await Promise.all(
        selectedRefundItems.map((item) => {
          const itemQty = item.quantity ?? 1;
          const itemAmt = item.lineTotalAmount ?? 0;
          return submitReturnApply({
            orderId: refundOrder.id,
            productId: item.productId,
            orderNumber: refundOrder.orderNumber,
            contactName: memberInfo?.displayName || memberInfo?.username,
            contactPhone: memberInfo?.phoneE164 || '',
            productImageUrl: item.productImageUrl,
            productName: item.productName,
            brandName: item.brandName,
            skuAttributes:
              typeof item.skuAttributes === 'string'
                ? item.skuAttributes
                : JSON.stringify(item.skuAttributes || ''),
            quantity: item.returnQuantity,
            unitPrice: item.unitPrice,
            unitRefundAmount: itemAmt / itemQty,
            reason: refundReason,
            customerComment: refundReason,
          });
        }),
      );
      alert('Refund application(s) submitted successfully!');
      setOpenRefundDialog(false);
      loadDashboardData();
    } catch (e) {
      alert('Failed to submit refund application.');
      console.error(e);
    }
  };

  if (!isAuthenticated) return null;

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', mt: 10 }}>
        <CircularProgress />
      </Box>
    );
  }

  if (error) {
    return (
      <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center', mt: 10, gap: 2 }}>
        <Typography color="error">{error}</Typography>
        <Button variant="contained" onClick={loadDashboardData}>
          Retry
        </Button>
      </Box>
    );
  }

  return (
    <Box sx={{ mt: 4, display: 'flex', flexDirection: { xs: 'column', md: 'row' }, gap: 4 }}>
      {/* Sidebar Profile Summary */}
      <Box sx={{ flex: 1 }}>
        <Paper sx={{ p: 4, textAlign: 'center' }}>
          <Avatar
            src={resolveImageUrl(memberInfo?.avatarUrl) || 'https://via.placeholder.com/100'}
            sx={{ width: 100, height: 100, mx: 'auto', mb: 2 }}
          />
          <Typography variant="h5" gutterBottom>
            {memberInfo?.displayName || authUser?.displayName || authUser?.username}
          </Typography>
          <Divider sx={{ my: 2 }} />
        </Paper>
      </Box>

      {/* Main Content Area */}
      <Box sx={{ flex: 3 }}>
        <Paper sx={{ width: '100%' }}>
          <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
            <Tabs value={tabValue} onChange={handleTabChange}>
              <Tab label="Account Info" />
              <Tab label="My Orders" />
              <Tab label="Address Book" />
              <Tab label="My Coupons" />
            </Tabs>
          </Box>

          <TabPanel value={tabValue} index={0}>
            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
              <Typography variant="h6">Personal Information</Typography>
              <Button size="small" variant="outlined" onClick={handleEditProfileOpen}>
                Edit Profile
              </Button>
            </Box>

            <Box sx={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 2 }}>
              <Typography color="text.secondary">Username:</Typography>
              <Typography>{memberInfo?.username}</Typography>
              <Typography color="text.secondary">Display Name:</Typography>
              <Typography>{memberInfo?.displayName || '-'}</Typography>
              <Typography color="text.secondary">Phone:</Typography>
              <Typography>{memberInfo?.phoneE164 || '-'}</Typography>
            </Box>
          </TabPanel>

          <TabPanel value={tabValue} index={1}>
            <Typography variant="h6" gutterBottom>
              Order History
            </Typography>
            {orders.length === 0 ? (
              <Typography color="text.secondary">You have no past orders.</Typography>
            ) : (
              <TableContainer>
                <Table>
                  <TableHead>
                    <TableRow>
                      <TableCell>Order Number</TableCell>
                      <TableCell>Date</TableCell>
                      <TableCell>Status</TableCell>
                      <TableCell align="right">Total Amount</TableCell>
                      <TableCell align="center">Action</TableCell>
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {orders.map((order) => {
                      const items = order.orderLines ?? [];
                      const totalOrderQty =
                        items.reduce(
                          (sum: number, item: OrderLine) => sum + (item.quantity ?? 0),
                          0,
                        );
                      const totalReturnedQty =
                        order.returnApplyList?.reduce(
                          (sum: number, r: ReturnRequest) => sum + (r.quantity || 0),
                          0,
                        ) || 0;
                      const isOrderFullyRefunded = totalOrderQty > 0 && totalReturnedQty >= totalOrderQty;

                      return (
                        <TableRow key={order.id}>
                          <TableCell>{order.orderNumber}</TableCell>
                          <TableCell>{new Date(order.createdAt).toLocaleDateString()}</TableCell>
                          <TableCell>
                            <Typography variant="body2">
                              {isOrderFullyRefunded
                                ? 'Refunded'
                                : (ORDER_STATUS_LABELS[order.status] ?? 'Unknown')}
                            </Typography>
                            {order.returnApplyList && order.returnApplyList.length > 0 && (
                              <Chip
                                label={`${order.returnApplyList.length} Return(s) Logged`}
                                size="small"
                                color="warning"
                                sx={{ mt: 0.5, fontSize: '0.7rem', height: 20 }}
                              />
                            )}
                            <Typography variant="caption" color="text.secondary" sx={{ display: 'block', mt: 1 }}>
                              Ordered: {totalOrderQty} | Refunded: {totalReturnedQty} | Remaining:{' '}
                              {Math.max(0, totalOrderQty - totalReturnedQty)}
                            </Typography>
                          </TableCell>
                          <TableCell align="right">
                            ₹{(order.grandTotalAmount ?? 0).toFixed(2)}
                          </TableCell>
                          <TableCell align="center">
                            {order.status === 0 && (
                              <Button
                                variant="contained"
                                size="small"
                                color="primary"
                                onClick={async () => {
                                  try {
                                    // 1. Get Razorpay Order details from backend
                                    const res = await createRazorpayOrder(order.id);
                                    const { razorpayOrderId, keyId, amount } = res;

                                    // 2. Load Razorpay script dynamically
                                    const loadScript = (src: string) =>
                                      new Promise((resolve) => {
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
                                    const scriptLoaded = await loadScript(
                                      'https://checkout.razorpay.com/v1/checkout.js',
                                    );
                                    if (!scriptLoaded) {
                                      alert('Failed to load Razorpay SDK. Are you online?');
                                      return;
                                    }

                                    // 3. Open Razorpay Checkout
                                    const options: RazorpayOptions = {
                                      key: keyId,
                                      amount: amount,
                                      currency: 'INR',
                                      name: 'Nexus Engine',
                                      description: `Payment for Order #${order.orderNumber}`,
                                      order_id: razorpayOrderId,
                                      handler: async function (response: RazorpayResponse) {
                                        try {
                                          // 4. Verify payment on backend
                                          await verifyRazorpayPayment(
                                            order.id,
                                            response.razorpay_payment_id,
                                            response.razorpay_order_id,
                                            response.razorpay_signature,
                                          );
                                          alert('Payment successful!');
                                          loadDashboardData();
                                        } catch {
                                          alert('Payment verification failed.');
                                        }
                                      },
                                      prefill: {
                                        name: memberInfo?.displayName || memberInfo?.username || 'Test Customer',
                                        email: memberInfo?.email || 'test@example.com',
                                        contact: memberInfo?.phoneE164 || '9000090000',
                                      },
                                      theme: { color: '#3399cc' },
                                    };
                                    const rzp = new window.Razorpay(options);
                                    rzp.on('payment.failed', function (response: RazorpayFailedResponse) {
                                      alert(`Payment Failed: ${response.error.description}`);
                                    });
                                    rzp.open();
                                  } catch (e: unknown) {
                                    alert('Failed to initiate payment. Please try again later.');
                                    console.error(e);
                                  }
                                }}
                              >
                                Pay Now
                              </Button>
                            )}
                            {order.status === 0 && (
                              <Button
                                variant="outlined"
                                size="small"
                                color="error"
                                sx={{ ml: 1 }}
                                onClick={async () => {
                                  if (window.confirm('Are you sure you want to cancel this order?')) {
                                    try {
                                      await cancelUserOrder(order.id);
                                      alert('Order cancelled successfully.');
                                      loadDashboardData();
                                    } catch (e) {
                                      alert('Failed to cancel order.');
                                      console.error(e);
                                    }
                                  }
                                }}
                              >
                                Cancel Order
                              </Button>
                            )}
                            {[1, 2, 3, 5].includes(order.status) && !isOrderFullyRefunded && (
                              <Button
                                variant="outlined"
                                size="small"
                                color="secondary"
                                onClick={() => handleOpenRefund(order)}
                              >
                                {order.status === 3 ? 'Request Refund' : 'Request Cancellation'}
                              </Button>
                            )}
                          </TableCell>
                        </TableRow>
                      );
                    })}
                  </TableBody>
                </Table>
              </TableContainer>
            )}
          </TabPanel>

          <TabPanel value={tabValue} index={2}>
            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
              <Typography variant="h6">My Addresses</Typography>
              <Button variant="contained" onClick={() => setOpenAddAddress(true)}>
                Add Address
              </Button>
            </Box>
            {addresses.length === 0 ? (
              <Typography color="text.secondary">You have no saved addresses.</Typography>
            ) : (
              <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                {addresses.map((address) => (
                  <Paper key={address.id} variant="outlined" sx={{ p: 2 }}>
                    <Typography sx={{ fontWeight: 'bold' }}>
                      {address.recipientName} ({address.recipientPhone})
                    </Typography>
                    <Typography color="text.secondary">
                      {[address.addressLine, address.district, address.city, address.state].filter(Boolean).join(', ')}{' '}
                      - PIN: {address.postalCode}
                    </Typography>
                  </Paper>
                ))}
              </Box>
            )}
          </TabPanel>

          <TabPanel value={tabValue} index={3}>
            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
              <Typography variant="h6">My Claimed Coupons (promotion.member_coupon)</Typography>
              <Chip label={`${coupons.length} Available`} color="primary" size="small" />
            </Box>
            {coupons.length === 0 ? (
              <Typography color="text.secondary">
                You have not claimed any coupons yet. Visit product pages to claim applicable discounts!
              </Typography>
            ) : (
              <Grid container spacing={2}>
                {coupons.map((coupon) => (
                  <Grid size={{ xs: 12, sm: 6 }} key={coupon.id}>
                    <Paper
                      variant="outlined"
                      sx={{
                        p: 2.5,
                        borderRadius: 2,
                        borderLeft: '5px solid #6366F1',
                        display: 'flex',
                        flexDirection: 'column',
                        gap: 1,
                      }}
                    >
                      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                        <Typography variant="subtitle1" sx={{ fontWeight: 700 }}>
                          {coupon.name}
                        </Typography>
                        <Chip
                          label={
                            coupon.discountType === 0
                              ? `₹${coupon.discountValue} OFF`
                              : coupon.discountType === 1
                              ? `${coupon.discountValue}% OFF`
                              : 'FREE SHIPPING'
                          }
                          color="success"
                          size="small"
                          sx={{ fontWeight: 'bold' }}
                        />
                      </Box>
                      {coupon.code && (
                        <Typography variant="caption" sx={{ fontFamily: 'monospace', bgcolor: '#F1F5F9', px: 1, py: 0.5, borderRadius: 1, width: 'fit-content' }}>
                          Code: {coupon.code}
                        </Typography>
                      )}
                      <Typography variant="body2" color="text.secondary">
                        {coupon.minSpendAmount > 0
                          ? `Valid on orders above ₹${coupon.minSpendAmount}`
                          : 'No minimum order value required'}
                      </Typography>
                      <Typography variant="caption" color="text.secondary">
                        Expires:{' '}
                        {coupon.endsAt
                          ? new Date(coupon.endsAt).toLocaleDateString()
                          : 'No expiration date'}
                      </Typography>
                    </Paper>
                  </Grid>
                ))}
              </Grid>
            )}
          </TabPanel>
        </Paper>
      </Box>

      {/* Add Address Dialog */}
      <Dialog open={openAddAddress} onClose={() => setOpenAddAddress(false)} maxWidth="sm" fullWidth>
        <DialogTitle>Add New Address</DialogTitle>
        <DialogContent>
          <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, mt: 2 }}>
            <TextField
              label="Recipient Name"
              fullWidth
              value={newAddress.recipientName || ''}
              onChange={(e) => setNewAddress({ ...newAddress, recipientName: e.target.value })}
            />
            <TextField
              label="Phone Number"
              fullWidth
              value={newAddress.recipientPhone || ''}
              onChange={(e) => setNewAddress({ ...newAddress, recipientPhone: e.target.value })}
            />
            <TextField
              label="Full Address"
              fullWidth
              multiline
              rows={3}
              value={newAddress.addressLine || ''}
              onChange={(e) => setNewAddress({ ...newAddress, addressLine: e.target.value })}
            />
            <TextField
              label="PIN Code"
              fullWidth
              value={newAddress.postalCode || ''}
              onChange={(e) => setNewAddress({ ...newAddress, postalCode: e.target.value })}
            />
            <TextField
              label="City"
              fullWidth
              value={newAddress.city || ''}
              onChange={(e) => setNewAddress({ ...newAddress, city: e.target.value })}
            />
            <TextField
              label="State"
              fullWidth
              value={newAddress.state || ''}
              onChange={(e) => setNewAddress({ ...newAddress, state: e.target.value })}
            />
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenAddAddress(false)}>Cancel</Button>
          <Button variant="contained" onClick={handleAddAddress}>
            Save Address
          </Button>
        </DialogActions>
      </Dialog>

      {/* Refund Dialog */}
      <Dialog open={openRefundDialog} onClose={() => setOpenRefundDialog(false)} maxWidth="sm" fullWidth>
        <DialogTitle>{refundOrder?.status === 3 ? 'Request Refund' : 'Request Cancellation'}</DialogTitle>
        <DialogContent>
          <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
            Select the items you wish to return and provide a reason.
          </Typography>
          <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
            {(refundOrder?.orderLines ?? []).map((item: OrderLine) => {
              const totalReturnedQty =
                refundOrder?.returnApplyList
                  ?.filter((r: ReturnRequest) => r.productId === item.productId)
                  .reduce((sum, r) => sum + (r.quantity || 0), 0) || 0;
              const qty = item.quantity ?? 1;
              const maxReturnableQty = Math.max(0, qty - totalReturnedQty);
              const isFullyReturned = maxReturnableQty <= 0;
              const isSelected = selectedRefundItems.some((i) => i.id === item.id);
              return (
                <Paper
                  key={item.id}
                  variant="outlined"
                  sx={{
                    p: 2,
                    display: 'flex',
                    alignItems: 'center',
                    gap: 2,
                    cursor: isFullyReturned ? 'not-allowed' : 'pointer',
                    borderColor: isSelected ? 'primary.main' : 'divider',
                    bgcolor: isSelected
                      ? 'rgba(99, 102, 241, 0.08)'
                      : isFullyReturned
                        ? 'action.disabledBackground'
                        : 'background.paper',
                    opacity: isFullyReturned ? 0.6 : 1,
                  }}
                  onClick={() => {
                    if (!isFullyReturned) handleRefundItemToggle(item);
                  }}
                >
                  <img
                    src={resolveImageUrl(item.productImageUrl) || 'https://via.placeholder.com/60'}
                    alt={item.productName}
                    style={{ width: 60, height: 60, objectFit: 'cover' }}
                  />
                  <Box sx={{ flex: 1 }}>
                    <Typography variant="subtitle2">{item.productName}</Typography>
                    <Typography variant="body2" color="text.secondary">
                      {typeof item.skuAttributes === 'string'
                        ? item.skuAttributes
                        : JSON.stringify(item.skuAttributes || '')}
                    </Typography>
                    <Typography variant="body2">
                      Qty: {item.quantity} | ₹{item.lineTotalAmount}
                    </Typography>
                    {totalReturnedQty > 0 && isFullyReturned && (
                      <Typography variant="caption" color="error" sx={{ fontWeight: 'bold' }}>
                        Refund Requested (Fully)
                      </Typography>
                    )}
                    {totalReturnedQty > 0 && !isFullyReturned && (
                      <Typography variant="caption" color="warning.main" sx={{ fontWeight: 'bold', display: 'block' }}>
                        Refunded: {totalReturnedQty}
                      </Typography>
                    )}
                    {isSelected && (
                      <TextField
                        type="number"
                        label="Return Qty"
                        size="small"
                        slotProps={{ htmlInput: { min: 1, max: maxReturnableQty } }}
                        value={selectedRefundItems.find((i) => i.id === item.id)?.returnQuantity || 1}
                        onChange={(e) => {
                          const val = parseInt(e.target.value);
                          if (!isNaN(val) && val >= 1 && val <= maxReturnableQty) {
                            const newSelected = [...selectedRefundItems];
                            const idx = newSelected.findIndex((i) => i.id === item.id);
                            if (idx > -1) {
                              newSelected[idx].returnQuantity = val;
                              setSelectedRefundItems(newSelected);
                            }
                          }
                        }}
                        sx={{ width: 100, mt: 1 }}
                        onClick={(e) => e.stopPropagation()}
                      />
                    )}
                  </Box>
                  <input
                    type="checkbox"
                    checked={isSelected || isFullyReturned}
                    disabled={isFullyReturned}
                    readOnly
                    style={{ width: 20, height: 20 }}
                  />
                </Paper>
              );
            })}
            <FormControl fullWidth sx={{ mt: 2 }}>
              <InputLabel>Reason for Refund</InputLabel>
              <Select
                value={refundReason}
                label="Reason for Refund"
                onChange={(e) => setRefundReason(e.target.value)}
              >
                {returnReasonsList.map((reason) => (
                  <MenuItem key={reason.id} value={reason.name}>
                    {reason.name}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenRefundDialog(false)}>Cancel</Button>
          <Button
            variant="contained"
            color="primary"
            onClick={handleRefundSubmit}
            disabled={selectedRefundItems.length === 0}
          >
            Submit Request
          </Button>
        </DialogActions>
      </Dialog>

      {/* Edit Profile Dialog */}
      <Dialog open={openEditProfile} onClose={() => setOpenEditProfile(false)} maxWidth="sm" fullWidth>
        <DialogTitle>Edit Profile</DialogTitle>
        <DialogContent>
          <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, mt: 2 }}>
            <TextField
              label="Display Name"
              fullWidth
              value={editProfileData.displayName}
              onChange={(e) => setEditProfileData({ ...editProfileData, displayName: e.target.value })}
            />

            <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
              <Avatar src={resolveImageUrl(editProfileData.iconUrl)} sx={{ width: 64, height: 64 }} />
              <input
                type="file"
                accept="image/*"
                style={{ display: 'none' }}
                id="avatar-upload"
                onChange={async (e) => {
                  if (e.target.files && e.target.files[0]) {
                    const file = e.target.files[0];
                    try {
                      const res = await uploadAvatar(file);
                      if (res) {
                        setEditProfileData({ ...editProfileData, iconUrl: res });
                      }
                    } catch (err) {
                      console.error(err);
                      alert('Error uploading avatar');
                    }
                  }
                }}
              />
              <label htmlFor="avatar-upload">
                <Button variant="outlined" component="span">
                  Change Image
                </Button>
              </label>
            </Box>
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenEditProfile(false)}>Cancel</Button>
          <Button variant="contained" onClick={handleEditProfileSubmit}>
            Save Changes
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default Profile;
