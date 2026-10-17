import React, { useEffect } from 'react';
import { Outlet, Link } from 'react-router';
import { AppBar, Toolbar, Typography, Button, Container, Box, Badge, IconButton } from '@mui/material';
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import { useSelector, useDispatch } from 'react-redux';
import type { RootState, AppDispatch } from '../store';
import { logout, updateUser } from '../store/slices/authSlice';
import { loadCartData, clearCart } from '../store/slices/cartSlice';
import { getCustomerProfile } from '../api/customer';
import ChatWidget from '../components/ChatWidget';

const Layout: React.FC = () => {
  const { isAuthenticated, user } = useSelector((state: RootState) => state.auth);
  const { totalQuantity } = useSelector((state: RootState) => state.cart);
  const dispatch = useDispatch<AppDispatch>();

  useEffect(() => {
    if (isAuthenticated) {
      dispatch(loadCartData());
      if (!user?.id) {
        getCustomerProfile()
          .then((member) => {
            if (member) dispatch(updateUser(member));
          })
          .catch(() => {});
      }
    } else {
      dispatch(clearCart());
    }
  }, [isAuthenticated, user?.id, dispatch]);

  const handleLogout = () => {
    dispatch(logout());
    dispatch(clearCart());
  };

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            <Link to="/" style={{ color: 'white', textDecoration: 'none' }}>Nexus Storefront</Link>
          </Typography>
          
          <IconButton color="inherit" component={Link} to="/cart" sx={{ mr: 2 }}>
            <Badge badgeContent={totalQuantity} color="error">
              <ShoppingCartIcon />
            </Badge>
          </IconButton>

          {isAuthenticated ? (
            <Box sx={{ display: 'flex', alignItems: 'center' }}>
              <Button color="inherit" component={Link} to="/profile" sx={{ mr: 1 }}>My Account</Button>
              <Button color="inherit" onClick={handleLogout}>Logout</Button>
            </Box>
          ) : (
            <Box sx={{ display: 'flex', alignItems: 'center' }}>
              <Button color="inherit" component={Link} to="/login" sx={{ mr: 1 }}>Login</Button>
              <Button color="inherit" component={Link} to="/register">Sign Up</Button>
            </Box>
          )}
        </Toolbar>
      </AppBar>

      <Container component="main" sx={{ mt: 4, mb: 4, flex: 1 }}>
        <Outlet />
      </Container>

      <Box component="footer" sx={{ py: 3, px: 2, mt: 'auto', backgroundColor: 'background.paper' }}>
        <Container maxWidth="sm">
          <Typography variant="body2" color="text.secondary" align="center">
            {'© '} {new Date().getFullYear()} NexusEngine Inc. All rights reserved.
          </Typography>
        </Container>
      </Box>

      <ChatWidget />
    </Box>
  );
};

export default Layout;
