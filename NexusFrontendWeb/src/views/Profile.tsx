import React, { useState, useEffect } from 'react';
import { Box, Typography, Paper, Tabs, Tab, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Avatar, Divider, CircularProgress, Button, TextField, Dialog, DialogTitle, DialogContent, DialogActions, Chip } from '@mui/material';
import { useSelector } from 'react-redux';
import type { RootState } from '../store';
import { fetchOrderList, submitReturnApply } from '../api/order';
import type { OmsOrderDetail } from '../api/order';
import { fetchMemberInfo, fetchAddressList, addAddress, updateProfile } from '../api/member';
import type { UmsMember, UmsMemberReceiveAddress } from '../api/member';
import { useNavigate } from 'react-router';

interface TabPanelProps {
  children?: React.ReactNode;
  index: number;
  value: number;
}

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
  const [memberInfo, setMemberInfo] = useState<UmsMember | null>(null);
  const [orders, setOrders] = useState<OmsOrderDetail[]>([]);
  const [addresses, setAddresses] = useState<UmsMemberReceiveAddress[]>([]);
  const [loading, setLoading] = useState(true);
  const [openAddAddress, setOpenAddAddress] = useState(false);
  const [newAddress, setNewAddress] = useState<Partial<UmsMemberReceiveAddress> & { coordinates?: string }>({
    name: '', phoneNumber: '', province: '', city: '', region: '', detailAddress: '', postCode: '', coordinates: ''
  });

  // Refund dialog states
  const [openRefundDialog, setOpenRefundDialog] = useState(false);
  const [openEditProfile, setOpenEditProfile] = useState(false);
  const [editProfileData, setEditProfileData] = useState({ nickname: '', phone: '', icon: '' });

  const handleEditProfileOpen = () => {
    setEditProfileData({
      nickname: memberInfo?.nickname || '',
      phone: memberInfo?.phone || '',
      icon: memberInfo?.icon || ''
    });
    setOpenEditProfile(true);
  };

  const handleEditProfileSubmit = async () => {
    try {
      await updateProfile(editProfileData);
      setOpenEditProfile(false);
      loadData();
    } catch (e) {
      console.error(e);
      alert('Failed to update profile');
    }
  };

  const [refundOrder, setRefundOrder] = useState<OmsOrderDetail | null>(null);
  const [selectedRefundItems, setSelectedRefundItems] = useState<(import('../api/order').OmsOrderItem & { returnQuantity: number })[]>([]);
  const [refundReason, setRefundReason] = useState('');

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
      const [memberRes, orderRes, addressRes] = await Promise.all([
        fetchMemberInfo(),
        fetchOrderList(-1, 1, 100),
        fetchAddressList()
      ]);
      setMemberInfo(memberRes.data);
      setOrders(orderRes.data.list || []);
      setAddresses(addressRes.data || []);
    } catch (error) {
      console.error('Failed to load dashboard data', error);
    } finally {
      setLoading(false);
    }
  };

  const handleTabChange = (_event: React.SyntheticEvent, newValue: number) => {
    setTabValue(newValue);
  };

  const handleAddAddress = async () => {
    try {
      const addressToSave = { ...newAddress };
      if (addressToSave.coordinates) {
        addressToSave.detailAddress = `${addressToSave.detailAddress} [Coords: ${addressToSave.coordinates}]`;
      }
      await addAddress(addressToSave);
      setOpenAddAddress(false);
      const addressRes = await fetchAddressList();
      setAddresses(addressRes.data || []);
      setNewAddress({ name: '', phoneNumber: '', province: '', city: '', region: '', detailAddress: '', postCode: '', coordinates: '' });
      alert('Address added successfully!');
    } catch (error) {
      alert('Failed to add address');
    }
  };

  const handleOpenRefund = (order: OmsOrderDetail) => {
    setRefundOrder(order);
    setSelectedRefundItems([]);
    setRefundReason('');
    setOpenRefundDialog(true);
  };

  const handleRefundItemToggle = (item: import('../api/order').OmsOrderItem) => {
    const currentIndex = selectedRefundItems.findIndex((i) => i.id === item.id);
    const newSelected = [...selectedRefundItems];
    if (currentIndex === -1) {
      const totalReturnedQty = refundOrder?.returnApplyList?.filter((r: import('../api/order').OmsOrderReturnApply) => r.productId === item.productId).reduce((sum, r) => sum + (r.productCount || 0), 0) || 0;
      const maxReturnableQty = item.productQuantity - totalReturnedQty;
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
      await Promise.all(selectedRefundItems.map(item => 
        submitReturnApply({
          orderId: refundOrder.id,
          productId: item.productId,
          orderSn: refundOrder.orderSn,
          memberUsername: memberInfo?.username,
          returnName: memberInfo?.nickname || memberInfo?.username,
          returnPhone: memberInfo?.phone || '',
          productPic: item.productPic,
          productName: item.productName,
          productBrand: item.productBrand,
          productAttr: item.productAttr,
          productCount: item.returnQuantity,
          productPrice: item.productPrice,
          productRealPrice: (item.realAmount / item.productQuantity) * item.returnQuantity,
          reason: refundReason,
          description: refundReason
        })
      ));
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
    return <Box sx={{ display: 'flex', justifyContent: 'center', mt: 10 }}><CircularProgress /></Box>;
  }

  return (
    <Box sx={{ mt: 4, display: 'flex', flexDirection: { xs: 'column', md: 'row' }, gap: 4 }}>
      {/* Sidebar Profile Summary */}
      <Box sx={{ flex: 1 }}>
        <Paper sx={{ p: 4, textAlign: 'center' }}>
          <Avatar 
            src={memberInfo?.icon?.includes('file') ? 'https://via.placeholder.com/100' : (memberInfo?.icon || 'https://via.placeholder.com/100')} 
            sx={{ width: 100, height: 100, mx: 'auto', mb: 2 }} 
          />
          <Typography variant="h5" gutterBottom>{memberInfo?.nickname || authUser?.username}</Typography>
          <Typography variant="body2" color="text.secondary">Member Level: {memberInfo?.memberLevelId || 1}</Typography>
          <Divider sx={{ my: 2 }} />
          <Typography variant="body2">Points: {memberInfo?.integration || 0}</Typography>
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
            </Tabs>
          </Box>
          
          <TabPanel value={tabValue} index={0}>
            
            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
              <Typography variant="h6">Personal Information</Typography>
              <Button size="small" variant="outlined" onClick={handleEditProfileOpen}>Edit Profile</Button>
            </Box>

            <Box sx={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 2 }}>
              <Typography color="text.secondary">Username:</Typography>
              <Typography>{memberInfo?.username}</Typography>
              
              <Typography color="text.secondary">Phone Number:</Typography>
              <Typography>{memberInfo?.phone || 'Not set'}</Typography>
              
              

              
            </Box>
          </TabPanel>
          
          <TabPanel value={tabValue} index={1}>
            <Typography variant="h6" gutterBottom>Order History</Typography>
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
                      const totalOrderQty = order.orderItemList?.reduce((sum: number, item: import('../api/order').OmsOrderItem) => sum + item.productQuantity, 0) || 0;
                      const totalReturnedQty = order.returnApplyList?.reduce((sum: number, r: import('../api/order').OmsOrderReturnApply) => sum + (r.productCount || 0), 0) || 0;
                      const isOrderFullyRefunded = totalOrderQty > 0 && totalReturnedQty >= totalOrderQty;

                      return (
                      <TableRow key={order.id}>
                        <TableCell>{order.orderSn}</TableCell>
                        <TableCell>{new Date(order.createTime).toLocaleDateString()}</TableCell>
                        <TableCell>
                          <Typography variant="body2">
                            {isOrderFullyRefunded ? 'Refunded' :
                             order.status === 0 ? 'Pending Payment' : 
                             order.status === 1 ? 'Awaiting Shipment' : 
                             order.status === 2 ? 'Shipped' : 
                             order.status === 3 ? 'Completed' : 
                             order.status === 5 ? 'Out for Delivery' : 
                             order.status === 6 ? 'Refunded' : 'Cancelled'}
                          </Typography>
                          {order.returnApplyList && order.returnApplyList.length > 0 && (
                            <Chip 
                              label={`${order.returnApplyList.length} Return(s) Logged`} 
                              size="small" 
                              color="warning" 
                              sx={{ mt: 0.5, fontSize: '0.7rem', height: 20 }} 
                            />
                          )}
                        </TableCell>
                        <TableCell align="right">₹{order.totalAmount?.toFixed(2)}</TableCell>
                        <TableCell align="center">
                          {order.status === 0 && (
                            <Button 
                              variant="contained" 
                              size="small" 
                              color="primary"
                              onClick={async () => {
                                try {
                                  const { createRazorpayOrder, verifyRazorpayPayment } = await import('../api/order');
                                  
                                  // 1. Get Razorpay Order details from backend
                                  const res = await createRazorpayOrder(order.id);
                                  const { razorpayOrderId, keyId, amount } = res.data;

                                  // 2. Load Razorpay script dynamically
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
                                    return;
                                  }

                                  // 3. Open Razorpay Checkout
                                  const options = {
                                    key: keyId, 
                                    amount: amount, 
                                    currency: 'INR',
                                    name: 'Nexus Engine',
                                    description: `Payment for Order #${order.orderSn}`,
                                    order_id: razorpayOrderId,
                                    handler: async function (response: RazorpayResponse) {
                                      try {
                                        // 4. Verify payment on backend
                                        await verifyRazorpayPayment(
                                          order.id, 
                                          response.razorpay_payment_id, 
                                          response.razorpay_order_id, 
                                          response.razorpay_signature
                                        );
                                        alert('Payment successful!');
                                        loadDashboardData();
                                      } catch (err) {
                                        alert('Payment verification failed.');
                                      }
                                    },
                                    prefill: {
                                      name: memberInfo?.nickname || memberInfo?.username || 'Test Customer',
                                      email: 'test@example.com',
                                      contact: '9000090000'
                                    },
                                    theme: { color: '#3399cc' }
                                  };
                                  const rzp = new window.Razorpay(options);
                                  rzp.on('payment.failed', function (response: RazorpayFailedResponse){
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
              <Button variant="contained" onClick={() => setOpenAddAddress(true)}>Add Address</Button>
            </Box>
            {addresses.length === 0 ? (
              <Typography color="text.secondary">You have no saved addresses.</Typography>
            ) : (
              <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                {addresses.map((address) => (
                  <Paper key={address.id} variant="outlined" sx={{ p: 2 }}>
                    <Typography sx={{ fontWeight: 'bold' }}>{address.name} ({address.phoneNumber})</Typography>
                    <Typography color="text.secondary">
                      {address.province}, {address.city}, {address.region} - {address.postCode}
                    </Typography>
                    <Typography color="text.secondary">{address.detailAddress}</Typography>
                  </Paper>
                ))}
              </Box>
            )}
          </TabPanel>
        </Paper>
      </Box>

      {/* Add Address Dialog */}
      <Dialog open={openAddAddress} onClose={() => setOpenAddAddress(false)} maxWidth="sm" fullWidth>
        <DialogTitle>Add New Address</DialogTitle>
        <DialogContent>
          <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, mt: 2 }}>
            <TextField label="Name" fullWidth value={newAddress.name} onChange={e => setNewAddress({...newAddress, name: e.target.value})} />
            <TextField label="Phone Number" fullWidth value={newAddress.phoneNumber} onChange={e => setNewAddress({...newAddress, phoneNumber: e.target.value})} />
            <TextField label="State" fullWidth value={newAddress.province} onChange={e => setNewAddress({...newAddress, province: e.target.value})} />
            <TextField label="City" fullWidth value={newAddress.city} onChange={e => setNewAddress({...newAddress, city: e.target.value})} />
            <TextField label="Street" fullWidth value={newAddress.region} onChange={e => setNewAddress({...newAddress, region: e.target.value})} />
            <TextField label="Detailed Address" fullWidth value={newAddress.detailAddress} onChange={e => setNewAddress({...newAddress, detailAddress: e.target.value})} />
            <TextField label="PIN Code" fullWidth value={newAddress.postCode} onChange={e => setNewAddress({...newAddress, postCode: e.target.value})} />
            <TextField label="Geographic Coordinates (Lat, Long)" fullWidth value={newAddress.coordinates} onChange={e => setNewAddress({...newAddress, coordinates: e.target.value})} />
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenAddAddress(false)}>Cancel</Button>
          <Button variant="contained" onClick={handleAddAddress}>Save Address</Button>
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
            {refundOrder?.orderItemList?.map((item: import('../api/order').OmsOrderItem) => {
              const totalReturnedQty = refundOrder?.returnApplyList?.filter((r: import('../api/order').OmsOrderReturnApply) => r.productId === item.productId).reduce((sum, r) => sum + (r.productCount || 0), 0) || 0;
              const maxReturnableQty = item.productQuantity - totalReturnedQty;
              const isFullyReturned = maxReturnableQty <= 0;
              const isSelected = selectedRefundItems.some(i => i.id === item.id);
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
                    bgcolor: isSelected ? 'rgba(99, 102, 241, 0.08)' : isFullyReturned ? 'action.disabledBackground' : 'background.paper',
                    opacity: isFullyReturned ? 0.6 : 1
                  }}
                  onClick={() => {
                    if (!isFullyReturned) handleRefundItemToggle(item);
                  }}
                >
                  <img src={item.productPic?.includes('file') ? 'https://via.placeholder.com/60' : (item.productPic || 'https://via.placeholder.com/60')} alt={item.productName} style={{ width: 60, height: 60, objectFit: 'cover' }} />
                  <Box sx={{ flex: 1 }}>
                    <Typography variant="subtitle2">{item.productName}</Typography>
                    <Typography variant="body2" color="text.secondary">{item.productAttr}</Typography>
                    <Typography variant="body2">Qty: {item.productQuantity} | ₹{item.realAmount}</Typography>
                    {totalReturnedQty > 0 && isFullyReturned && (
                      <Typography variant="caption" color="error" sx={{ fontWeight: 'bold' }}>Refund Requested (Fully)</Typography>
                    )}
                    {totalReturnedQty > 0 && !isFullyReturned && (
                      <Typography variant="caption" color="warning.main" sx={{ fontWeight: 'bold', display: 'block' }}>Refunded: {totalReturnedQty}</Typography>
                    )}
                    {isSelected && (
                      <TextField
                        type="number"
                        label="Return Qty"
                        size="small"
                        slotProps={{ htmlInput: { min: 1, max: maxReturnableQty } }} //{{ min: 1, max: maxReturnableQty }}
                        value={selectedRefundItems.find(i => i.id === item.id)?.returnQuantity || 1}
                        onChange={(e) => {
                          const val = parseInt(e.target.value);
                          if (!isNaN(val) && val >= 1 && val <= maxReturnableQty) {
                            const newSelected = [...selectedRefundItems];
                            const idx = newSelected.findIndex(i => i.id === item.id);
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
            <TextField 
              label="Reason for Refund" 
              fullWidth 
              multiline 
              rows={3} 
              value={refundReason} 
              onChange={e => setRefundReason(e.target.value)} 
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
            <TextField label="Nickname" fullWidth value={editProfileData.nickname} onChange={e => setEditProfileData({...editProfileData, nickname: e.target.value})} />
            <TextField label="Phone Number" fullWidth value={editProfileData.phone} onChange={e => setEditProfileData({...editProfileData, phone: e.target.value})} />
            <TextField label="Avatar URL" fullWidth value={editProfileData.icon} onChange={e => setEditProfileData({...editProfileData, icon: e.target.value})} />
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenEditProfile(false)}>Cancel</Button>
          <Button variant="contained" onClick={handleEditProfileSubmit}>Save Changes</Button>
        </DialogActions>
      </Dialog>
    </Box>

  );
};

export default Profile;
