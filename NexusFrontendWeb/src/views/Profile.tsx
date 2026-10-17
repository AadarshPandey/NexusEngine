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
  IconButton,
  Collapse,
} from '@mui/material';
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import KeyboardArrowUpIcon from '@mui/icons-material/KeyboardArrowUp';
import { useSelector } from 'react-redux';
import type { RootState } from '../store';
import {
  getOrders,
  submitReturnApply,
  getReturnReasons,
  cancelUserOrder,
  confirmReceiveOrder,
} from '../api/ordering';
import { OrderStatus, type CustomerOrder, type OrderLine, type ReturnRequest } from '../types/ordering';
import {
  getCustomerProfile,
  getCustomerAddresses,
  createCustomerAddress,
  updateCustomerAddress,
  deleteCustomerAddress,
  updateCustomerProfile,
  uploadAvatar,
  getMemberLoginEvents,
} from '../api/customer';
import { getMemberCoupons } from '../api/promotion';
import type { Coupon } from '../types/promotion';
import type { Member, MemberAddress, MemberLoginEvent } from '../types/customer';
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
  const [loginEvents, setLoginEvents] = useState<MemberLoginEvent[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [openAddAddress, setOpenAddAddress] = useState(false);
  const [openEditAddress, setOpenEditAddress] = useState(false);
  const [editingAddress, setEditingAddress] = useState<Partial<MemberAddress>>({});
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
  const [editProfileData, setEditProfileData] = useState({
    displayName: '',
    phoneE164: '',
    email: '',
    gender: 0,
    iconUrl: '',
  });

  const handleEditProfileOpen = () => {
    setEditProfileData({
      displayName: memberInfo?.displayName || '',
      phoneE164: memberInfo?.phoneE164 || (memberInfo as any)?.contactPhone || '',
      email: memberInfo?.email || '',
      gender: (memberInfo?.gender as any) ?? 0,
      iconUrl: memberInfo?.avatarUrl || '',
    });
    setOpenEditProfile(true);
  };

  const handleEditProfileSubmit = async () => {
    try {
      await updateCustomerProfile(editProfileData as any);
      setOpenEditProfile(false);
      loadDashboardData();
    } catch (e) {
      console.error(e);
      alert('Failed to update profile');
    }
  };

  const handleOpenEditAddress = (addr: MemberAddress) => {
    setEditingAddress({ ...addr });
    setOpenEditAddress(true);
  };

  const handleUpdateAddress = async () => {
    if (!editingAddress.id) return;
    try {
      await updateCustomerAddress(editingAddress.id, editingAddress);
      setOpenEditAddress(false);
      const addressRes = await getCustomerAddresses();
      setAddresses(addressRes || []);
      alert('Address updated successfully!');
    } catch {
      alert('Failed to update address');
    }
  };

  const handleDeleteAddress = async (id: number) => {
    if (!window.confirm('Are you sure you want to delete this address?')) return;
    try {
      await deleteCustomerAddress(id);
      const addressRes = await getCustomerAddresses();
      setAddresses(addressRes || []);
      alert('Address deleted successfully!');
    } catch {
      alert('Failed to delete address');
    }
  };

  const handleSetDefaultAddress = async (addr: MemberAddress) => {
    try {
      await updateCustomerAddress(addr.id, { ...addr, isDefault: true, defaultStatus: true } as any);
      const addressRes = await getCustomerAddresses();
      setAddresses(addressRes || []);
      alert('Default address updated!');
    } catch {
      alert('Failed to update default address');
    }
  };

  const [refundOrder, setRefundOrder] = useState<CustomerOrder | null>(null);
  const [selectedRefundItems, setSelectedRefundItems] = useState<(OrderLine & { returnQuantity: number })[]>([]);
  const [refundReason, setRefundReason] = useState('');
  const [evidenceUrlsText, setEvidenceUrlsText] = useState('');
  const [returnReasonsList, setReturnReasonsList] = useState<{ id: number; name: string }[]>([]);
  const [expandedOrderIds, setExpandedOrderIds] = useState<number[]>([]);

  const toggleOrderExpand = (orderId: number) => {
    setExpandedOrderIds((prev) =>
      prev.includes(orderId) ? prev.filter((id) => id !== orderId) : [...prev, orderId]
    );
  };

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
      const [memberRes, orderRes, addressRes, couponRes, loginRes] = await Promise.all([
        getCustomerProfile(),
        getOrders(-1, 1, 100),
        getCustomerAddresses(),
        getMemberCoupons(0).catch(() => []),
        getMemberLoginEvents().catch(() => []),
      ]);
      setMemberInfo(memberRes);
      setOrders(orderRes.content || []);
      setAddresses(addressRes || []);
      setCoupons(Array.isArray(couponRes) ? couponRes : []);
      setLoginEvents(Array.isArray(loginRes) ? loginRes : []);
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
    setEvidenceUrlsText('');
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
            evidenceUrls: evidenceUrlsText.trim() ? evidenceUrlsText.trim() : undefined,
            proofPics: evidenceUrlsText.trim() ? evidenceUrlsText.trim() : undefined,
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
              <Typography sx={{ fontWeight: 600 }}>{memberInfo?.username}</Typography>
              
              <Typography color="text.secondary">Display Name:</Typography>
              <Typography>{memberInfo?.displayName || '-'}</Typography>
              
              <Typography color="text.secondary">Phone (phone_e164):</Typography>
              <Typography>{memberInfo?.phoneE164 || (memberInfo as any)?.contactPhone || '-'}</Typography>

              <Typography color="text.secondary">Email:</Typography>
              <Typography>{memberInfo?.email || '-'}</Typography>

              <Typography color="text.secondary">Gender:</Typography>
              <Typography>
                {memberInfo?.gender === 1 ? 'Male' : memberInfo?.gender === 2 ? 'Female' : 'Not specified'}
              </Typography>

              <Typography color="text.secondary">Account Status:</Typography>
              <Box>
                <Chip
                  label={memberInfo?.accountStatus === 1 ? 'Active' : 'Disabled'}
                  color={memberInfo?.accountStatus === 1 ? 'success' : 'default'}
                  size="small"
                />
              </Box>

              <Typography color="text.secondary">Signup Channel:</Typography>
              <Box>
                <Chip
                  label="Web Storefront"
                  size="small"
                  color="primary"
                  variant="outlined"
                />
              </Box>

              <Typography color="text.secondary">Member Since:</Typography>
              <Typography>
                {memberInfo?.createdAt ? new Date(memberInfo.createdAt).toLocaleDateString() : '-'}
              </Typography>
            </Box>

            {/* Recent Login Events (customer.member_login_event) */}
            <Box sx={{ mt: 4 }}>
              <Typography variant="subtitle1" sx={{ fontWeight: 600, mb: 1 }}>
                Recent Security & Login Events (customer.member_login_event)
              </Typography>
              <TableContainer component={Paper} variant="outlined">
                <Table size="small">
                  <TableHead>
                    <TableRow sx={{ bgcolor: 'action.hover' }}>
                      <TableCell sx={{ fontWeight: 600 }}>Timestamp</TableCell>
                      <TableCell sx={{ fontWeight: 600 }}>IP Address</TableCell>
                      <TableCell sx={{ fontWeight: 600 }}>Method</TableCell>
                      <TableCell sx={{ fontWeight: 600 }}>Region / Network</TableCell>
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {loginEvents.length === 0 ? (
                      <TableRow>
                        <TableCell colSpan={4} align="center" sx={{ py: 2, color: 'text.secondary' }}>
                          No login events recorded yet.
                        </TableCell>
                      </TableRow>
                    ) : (
                      loginEvents.map((evt, idx) => (
                        <TableRow key={evt.id || idx}>
                          <TableCell>
                            {evt.occurredAt || evt.createTime
                              ? new Date(evt.occurredAt || evt.createTime!).toLocaleString()
                              : '-'}
                          </TableCell>
                          <TableCell sx={{ fontFamily: 'monospace' }}>
                            {evt.ipAddress || evt.ip || '127.0.0.1'}
                          </TableCell>
                          <TableCell>
                            <Chip
                              label={
                                (evt.loginMethod || evt.loginType) === 1
                                  ? 'Web / REST API'
                                  : 'Mobile OAuth'
                              }
                              size="small"
                              color="primary"
                              variant="outlined"
                              sx={{ height: 22, fontSize: '0.7rem' }}
                            />
                          </TableCell>
                          <TableCell>
                            {[evt.city, evt.region || evt.province].filter(Boolean).join(', ') ||
                              'Local Docker Network'}
                          </TableCell>
                        </TableRow>
                      ))
                    )}
                  </TableBody>
                </Table>
              </TableContainer>
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
                      <TableCell width={50} />
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
                      const isExpanded = expandedOrderIds.includes(order.id);
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
                        <React.Fragment key={order.id}>
                          <TableRow hover sx={{ '& > *': { borderBottom: 'unset' } }}>
                            <TableCell>
                              <IconButton
                                aria-label="expand row"
                                size="small"
                                onClick={() => toggleOrderExpand(order.id)}
                              >
                                {isExpanded ? <KeyboardArrowUpIcon /> : <KeyboardArrowDownIcon />}
                              </IconButton>
                            </TableCell>
                            <TableCell sx={{ fontWeight: 600 }}>{order.orderNumber}</TableCell>
                            <TableCell>{new Date(order.createdAt).toLocaleDateString()}</TableCell>
                            <TableCell>
                              <Typography variant="body2" sx={{ fontWeight: 500 }}>
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
                              <Typography variant="caption" color="text.secondary" sx={{ display: 'block', mt: 0.5 }}>
                                Qty: {totalOrderQty} | Refunded: {totalReturnedQty}
                              </Typography>
                            </TableCell>
                            <TableCell align="right" sx={{ fontWeight: 600 }}>
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
                              {order.status === 2 && (
                                <Button
                                  variant="contained"
                                  size="small"
                                  color="success"
                                  sx={{ mr: 1 }}
                                  onClick={async () => {
                                    if (window.confirm('Have you received your shipment? Confirm receipt now.')) {
                                      try {
                                        await confirmReceiveOrder(order.id);
                                        alert('Order receipt confirmed! Thank you.');
                                        loadDashboardData();
                                      } catch (e) {
                                        console.error(e);
                                        alert('Failed to confirm order receipt');
                                      }
                                    }
                                  }}
                                >
                                  Confirm Receipt
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

                          {/* Expandable Order Detail Accordion Row */}
                          <TableRow>
                            <TableCell style={{ paddingBottom: 0, paddingTop: 0 }} colSpan={6}>
                              <Collapse in={isExpanded} timeout="auto" unmountOnExit>
                                <Box sx={{ py: 2.5, px: 3, bgcolor: 'background.default', borderRadius: 2, my: 1.5, border: '1px solid', borderColor: 'divider' }}>
                                  {/* 1. Ordered Products */}
                                  <Typography variant="subtitle2" sx={{ fontWeight: 600, mb: 1.5, color: 'text.primary' }}>
                                    Ordered Products ({items.length})
                                  </Typography>
                                  <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1, mb: 2 }}>
                                    {items.map((item: OrderLine) => (
                                      <Box
                                        key={item.id}
                                        sx={{
                                          display: 'flex',
                                          alignItems: 'center',
                                          gap: 2,
                                          bgcolor: 'background.paper',
                                          p: 1.5,
                                          borderRadius: 1.5,
                                          border: '1px solid',
                                          borderColor: 'divider',
                                        }}
                                      >
                                        <Box
                                          component="img"
                                          src={resolveImageUrl(item.productImageUrl)}
                                          alt={item.productName}
                                          sx={{ width: 44, height: 44, objectFit: 'contain', borderRadius: 1 }}
                                        />
                                        <Box sx={{ flex: 1 }}>
                                          <Typography variant="body2" sx={{ fontWeight: 500 }}>
                                            {item.productName}
                                          </Typography>
                                          <Typography variant="caption" color="text.secondary">
                                            {item.brandName ? `Brand: ${item.brandName} | ` : ''}
                                            {item.skuAttributes ? `Specs: ${typeof item.skuAttributes === 'object' ? JSON.stringify(item.skuAttributes) : item.skuAttributes} | ` : ''}
                                            Qty: {item.quantity} × ₹{Number(item.unitPrice).toFixed(2)}
                                          </Typography>
                                        </Box>
                                        <Typography variant="body2" sx={{ fontWeight: 600 }}>
                                          ₹{Number(item.lineTotalAmount).toFixed(2)}
                                        </Typography>
                                      </Box>
                                    ))}
                                    {items.length === 0 && (
                                      <Typography variant="caption" color="text.secondary">
                                        No item details found.
                                      </Typography>
                                    )}
                                  </Box>

                                  {/* 2. Shipping & Timeline */}
                                  <Divider sx={{ my: 1.5 }} />
                                  <Grid container spacing={2}>
                                    <Grid size={{ xs: 12, sm: 4 }}>
                                      <Typography variant="caption" color="text.secondary" sx={{ display: 'block' }}>Carrier / Logistics</Typography>
                                      <Typography variant="body2" sx={{ fontWeight: 500 }}>
                                        {order.carrierName || order.deliveryCompany || 'Standard Logistics'}
                                      </Typography>
                                    </Grid>
                                    <Grid size={{ xs: 12, sm: 4 }}>
                                      <Typography variant="caption" color="text.secondary" sx={{ display: 'block' }}>Tracking Number</Typography>
                                      <Typography variant="body2" sx={{ fontWeight: 500 }}>
                                        {order.trackingNumber || order.deliverySn || 'Pending carrier scan'}
                                      </Typography>
                                    </Grid>
                                    <Grid size={{ xs: 12, sm: 4 }}>
                                      <Typography variant="caption" color="text.secondary" sx={{ display: 'block' }}>Delivery / Receipt</Typography>
                                      <Typography variant="body2" sx={{ fontWeight: 500 }}>
                                        {order.isReceiptConfirmed ? 'Confirmed by Buyer' : (order.status === 3 ? 'Completed' : 'In Transit')}
                                      </Typography>
                                    </Grid>
                                    <Grid size={{ xs: 12, sm: 4 }}>
                                      <Typography variant="caption" color="text.secondary" sx={{ display: 'block' }}>Paid Time</Typography>
                                      <Typography variant="caption">
                                        {order.paidAt || order.paymentTime ? new Date(order.paidAt || order.paymentTime!).toLocaleString() : 'Unpaid'}
                                      </Typography>
                                    </Grid>
                                    <Grid size={{ xs: 12, sm: 4 }}>
                                      <Typography variant="caption" color="text.secondary" sx={{ display: 'block' }}>Shipped Time</Typography>
                                      <Typography variant="caption">
                                        {order.shippedAt || order.deliveryTime ? new Date(order.shippedAt || order.deliveryTime!).toLocaleString() : 'Awaiting Shipment'}
                                      </Typography>
                                    </Grid>
                                    <Grid size={{ xs: 12, sm: 4 }}>
                                      <Typography variant="caption" color="text.secondary" sx={{ display: 'block' }}>Completed Time</Typography>
                                      <Typography variant="caption">
                                        {order.deliveredAt || order.receiveTime ? new Date(order.deliveredAt || order.receiveTime!).toLocaleString() : '-'}
                                      </Typography>
                                    </Grid>
                                  </Grid>

                                  {/* 3. Payment Transactions */}
                                  {order.paymentTransactions && order.paymentTransactions.length > 0 && (
                                    <Box sx={{ mt: 2 }}>
                                      <Typography variant="caption" color="text.secondary" sx={{ display: 'block', mb: 0.5, fontWeight: 600 }}>
                                        Payment & Refund Audit Records:
                                      </Typography>
                                      <Box sx={{ display: 'flex', gap: 1, flexWrap: 'wrap' }}>
                                        {order.paymentTransactions.map((tx, idx) => {
                                          const isRefund = (tx.transactionType || tx.txnType || '').toUpperCase() === 'REFUND';

                                          return (
                                            <Chip
                                              key={tx.id || idx}
                                              label={`${tx.transactionType || tx.txnType}: ₹${Number(tx.amount).toFixed(2)} (${tx.status}) — ${tx.transactionId || tx.providerTxnId}`}
                                              color={isRefund ? 'warning' : 'success'}
                                              variant="outlined"
                                              size="small"
                                            />
                                          );
                                        })}
                                      </Box>
                                    </Box>
                                  )}
                                </Box>
                              </Collapse>
                            </TableCell>
                          </TableRow>
                        </React.Fragment>
                      );
                    })}
                  </TableBody>
                </Table>
              </TableContainer>
            )}
          </TabPanel>

          <TabPanel value={tabValue} index={2}>
            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
              <Typography variant="h6">My Addresses (customer.member_address)</Typography>
              <Button variant="contained" onClick={() => setOpenAddAddress(true)}>
                Add Address
              </Button>
            </Box>
            {addresses.length === 0 ? (
              <Typography color="text.secondary">You have no saved addresses.</Typography>
            ) : (
              <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                {addresses.map((address) => {
                  const isDefaultAddr = address.isDefault || (address as any).defaultStatus;
                  return (
                    <Paper
                      key={address.id}
                      elevation={0}
                      sx={{
                        p: 2.5,
                        borderRadius: 2,
                        bgcolor: 'background.default',
                        border: '1px solid',
                        borderColor: 'divider',
                      }}
                    >
                      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', mb: 1 }}>
                        <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                          <Typography sx={{ fontWeight: 700, fontSize: '1rem' }}>
                            {address.recipientName}
                          </Typography>
                          <Typography color="text.secondary">
                            ({address.recipientPhone})
                          </Typography>
                          {isDefaultAddr && (
                            <Chip label="Default Address" color="primary" size="small" sx={{ fontWeight: 600, height: 22 }} />
                          )}
                        </Box>
                        <Box sx={{ display: 'flex', gap: 1 }}>
                          {!isDefaultAddr && (
                            <Button size="small" variant="text" color="primary" onClick={() => handleSetDefaultAddress(address)}>
                              Set Default
                            </Button>
                          )}
                          <Button size="small" variant="outlined" onClick={() => handleOpenEditAddress(address)}>
                            Edit
                          </Button>
                          <Button size="small" variant="outlined" color="error" onClick={() => handleDeleteAddress(address.id)}>
                            Delete
                          </Button>
                        </Box>
                      </Box>
                      <Typography variant="body2" color="text.secondary">
                        {[address.addressLine, address.district, address.city, address.state].filter(Boolean).join(', ')}{' '}
                        — PIN: {address.postalCode}
                      </Typography>
                    </Paper>
                  );
                })}
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
                      elevation={0}
                      sx={{
                        p: 2.5,
                        borderRadius: 2,
                        bgcolor: 'background.default',
                        border: '1px solid',
                        borderColor: 'divider',
                        borderLeft: '5px solid #6366F1',
                        display: 'flex',
                        flexDirection: 'column',
                        gap: 1.5,
                        transition: 'transform 0.15s, box-shadow 0.15s',
                        '&:hover': {
                          transform: 'translateY(-2px)',
                          boxShadow: '0 8px 24px rgba(0,0,0,0.4)',
                        },
                      }}
                    >
                      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                        <Typography variant="subtitle1" sx={{ fontWeight: 700, color: 'text.primary' }}>
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
                          color="primary"
                          size="small"
                          sx={{ fontWeight: 'bold' }}
                        />
                      </Box>
                      {coupon.code && (
                        <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                          <Typography
                            variant="caption"
                            sx={{
                              fontFamily: 'monospace',
                              fontWeight: 700,
                              bgcolor: 'rgba(99, 102, 241, 0.15)',
                              color: '#90caf9',
                              border: '1px dashed #6366F1',
                              px: 1.5,
                              py: 0.5,
                              borderRadius: 1,
                              width: 'fit-content',
                            }}
                          >
                            CODE: {coupon.code}
                          </Typography>
                        </Box>
                      )}
                      <Typography variant="body2" sx={{ color: 'text.secondary' }}>
                        {coupon.minSpendAmount > 0
                          ? `Valid on orders above ₹${coupon.minSpendAmount}`
                          : 'No minimum order value required'}
                      </Typography>
                      <Typography variant="caption" sx={{ color: 'text.secondary' }}>
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

      {/* Edit Address Dialog */}
      <Dialog open={openEditAddress} onClose={() => setOpenEditAddress(false)} maxWidth="sm" fullWidth>
        <DialogTitle>Edit Shipping Address</DialogTitle>
        <DialogContent>
          <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, mt: 2 }}>
            <TextField
              label="Recipient Name"
              fullWidth
              value={editingAddress.recipientName || ''}
              onChange={(e) => setEditingAddress({ ...editingAddress, recipientName: e.target.value })}
            />
            <TextField
              label="Phone Number"
              fullWidth
              value={editingAddress.recipientPhone || ''}
              onChange={(e) => setEditingAddress({ ...editingAddress, recipientPhone: e.target.value })}
            />
            <TextField
              label="Full Address Line"
              fullWidth
              multiline
              rows={3}
              value={editingAddress.addressLine || ''}
              onChange={(e) => setEditingAddress({ ...editingAddress, addressLine: e.target.value })}
            />
            <TextField
              label="District / Landmark"
              fullWidth
              value={editingAddress.district || ''}
              onChange={(e) => setEditingAddress({ ...editingAddress, district: e.target.value })}
            />
            <TextField
              label="PIN Code"
              fullWidth
              value={editingAddress.postalCode || ''}
              onChange={(e) => setEditingAddress({ ...editingAddress, postalCode: e.target.value })}
            />
            <TextField
              label="City"
              fullWidth
              value={editingAddress.city || ''}
              onChange={(e) => setEditingAddress({ ...editingAddress, city: e.target.value })}
            />
            <TextField
              label="State"
              fullWidth
              value={editingAddress.state || ''}
              onChange={(e) => setEditingAddress({ ...editingAddress, state: e.target.value })}
            />
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenEditAddress(false)}>Cancel</Button>
          <Button variant="contained" onClick={handleUpdateAddress}>
            Update Address
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

            <TextField
              fullWidth
              size="small"
              multiline
              rows={2}
              label="Evidence Photo URLs (optional, comma-separated)"
              placeholder="e.g. https://images.unsplash.com/photo-defective-item.jpg"
              value={evidenceUrlsText}
              onChange={(e) => setEvidenceUrlsText(e.target.value)}
              sx={{ mt: 2 }}
            />
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
            <TextField
              label="Phone (phone_e164)"
              fullWidth
              value={editProfileData.phoneE164}
              onChange={(e) => setEditProfileData({ ...editProfileData, phoneE164: e.target.value })}
            />
            <TextField
              label="Email"
              fullWidth
              value={editProfileData.email}
              onChange={(e) => setEditProfileData({ ...editProfileData, email: e.target.value })}
            />
            <FormControl fullWidth>
              <InputLabel>Gender</InputLabel>
              <Select
                value={editProfileData.gender}
                label="Gender"
                onChange={(e) => setEditProfileData({ ...editProfileData, gender: Number(e.target.value) })}
              >
                <MenuItem value={0}>Not specified</MenuItem>
                <MenuItem value={1}>Male</MenuItem>
                <MenuItem value={2}>Female</MenuItem>
              </Select>
            </FormControl>

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
