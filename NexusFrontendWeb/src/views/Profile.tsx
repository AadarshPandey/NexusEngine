import React, { useState, useEffect } from 'react';
import { Box, Typography, Paper, Tabs, Tab, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Avatar, Divider, CircularProgress, Button, TextField, Dialog, DialogTitle, DialogContent, DialogActions } from '@mui/material';
import { useSelector } from 'react-redux';
import type { RootState } from '../store';
import { fetchOrderList } from '../api/order';
import type { OmsOrderDetail } from '../api/order';
import { fetchMemberInfo, fetchAddressList, addAddress } from '../api/member';
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

  useEffect(() => {
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }
    loadDashboardData();
  }, [isAuthenticated]);

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
            src={memberInfo?.icon || 'https://via.placeholder.com/100'} 
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
            <Typography variant="h6" gutterBottom>Personal Information</Typography>
            <Box sx={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 2 }}>
              <Typography color="text.secondary">Username:</Typography>
              <Typography>{memberInfo?.username}</Typography>
              
              <Typography color="text.secondary">Phone Number:</Typography>
              <Typography>{memberInfo?.phone || 'Not set'}</Typography>
              
              <Typography color="text.secondary">Job:</Typography>
              <Typography>{memberInfo?.job || 'Not set'}</Typography>

              <Typography color="text.secondary">City:</Typography>
              <Typography>{memberInfo?.city || 'Not set'}</Typography>
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
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {orders.map((order) => (
                      <TableRow key={order.id}>
                        <TableCell>{order.orderSn}</TableCell>
                        <TableCell>{new Date(order.createTime).toLocaleDateString()}</TableCell>
                        <TableCell>
                          {order.status === 0 ? 'Pending Payment' : 
                           order.status === 1 ? 'Awaiting Shipment' : 
                           order.status === 2 ? 'Shipped' : 
                           order.status === 3 ? 'Completed' : 'Cancelled'}
                        </TableCell>
                        <TableCell align="right">₹{order.totalAmount?.toFixed(2)}</TableCell>
                      </TableRow>
                    ))}
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
    </Box>
  );
};

export default Profile;
