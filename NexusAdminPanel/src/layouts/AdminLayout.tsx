import React, { useState, useEffect } from 'react';
import { Outlet, useLocation } from 'react-router';
import { Box, useMediaQuery, useTheme } from '@mui/material';
import Sidebar from './components/Sidebar';
import Navbar from './components/Navbar';
import { useAppDispatch, useAppSelector } from '@/store/hooks';
import { closeSideBar, toggleDevice } from '@/store/slices/appSlice';

const SIDEBAR_WIDTH = 260;
const SIDEBAR_COLLAPSED_WIDTH = 72;

const AdminLayout: React.FC = () => {
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down('md'));
  const dispatch = useAppDispatch();
  const sidebarOpened = useAppSelector((state) => state.app.sidebar.opened);
  const [mobileOpen, setMobileOpen] = useState(false);

  useEffect(() => {
    if (isMobile) {
      dispatch(toggleDevice('mobile'));
      dispatch(closeSideBar(true));
    } else {
      dispatch(toggleDevice('desktop'));
    }
  }, [isMobile, dispatch]);

  const sidebarWidth = sidebarOpened ? SIDEBAR_WIDTH : SIDEBAR_COLLAPSED_WIDTH;

  return (
    <Box sx={{ display: 'flex', height: '100vh', overflow: 'hidden' }}>
      <Sidebar
        width={SIDEBAR_WIDTH}
        collapsedWidth={SIDEBAR_COLLAPSED_WIDTH}
        opened={sidebarOpened}
        mobileOpen={mobileOpen}
        isMobile={isMobile}
        onMobileClose={() => setMobileOpen(false)}
      />
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          flexGrow: 1,
          width: isMobile ? '100%' : `calc(100% - ${sidebarWidth}px)`,
          ml: isMobile ? 0 : `${sidebarWidth}px`,
          transition: 'margin-left 0.3s cubic-bezier(0.4, 0, 0.2, 1), width 0.3s cubic-bezier(0.4, 0, 0.2, 1)',
          overflow: 'hidden',
        }}
      >
        <Navbar onMobileMenuToggle={() => setMobileOpen(true)} />
        <Box
          component="main"
          sx={{
            flexGrow: 1,
            overflow: 'auto',
            backgroundColor: '#F8FAFC',
            p: 3,
          }}
        >
          <Outlet />
        </Box>
      </Box>
    </Box>
  );
};

export default AdminLayout;
