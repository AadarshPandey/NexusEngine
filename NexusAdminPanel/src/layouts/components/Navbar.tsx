import React, { useState } from 'react';
import { useNavigate, useLocation } from 'react-router';
import {
  AppBar,
  Toolbar,
  IconButton,
  Box,
  Typography,
  Avatar,
  Menu as MuiMenu,
  MenuItem,
  Breadcrumbs,
  Link,
  Chip,
} from '@mui/material';
import {
  Menu as MenuIcon,
  MenuOpen,
  Home,
  NavigateNext,
  Logout,
  Person,
} from '@mui/icons-material';
import { useAppDispatch, useAppSelector } from '@/store/hooks';
import { toggleSideBar } from '@/store/slices/appSlice';
import { userLogout } from '@/store/slices/userSlice';
import { asyncRouterMap, constantRouterMap } from '@/router/routeConfig';
import type { RouteConfig } from '@/store/slices/permissionSlice';

interface NavbarProps {
  onMobileMenuToggle: () => void;
}

// Find route title from route config
const findRouteTitle = (pathname: string, routes: RouteConfig[]): string[] => {
  const titles: string[] = [];
  const segments = pathname.split('/').filter(Boolean);

  for (const route of routes) {
    const routeBase = route.path.replace(/^\//, '');
    if (segments[0] === routeBase || (route.path === '' && segments.length <= 1)) {
      if (route.meta?.title) titles.push(route.meta.title);
      if (route.children && segments.length > 1) {
        for (const child of route.children) {
          if (child.path === segments[1]) {
            if (child.meta?.title) titles.push(child.meta.title);
            break;
          }
        }
      } else if (route.children && segments.length <= 1) {
        // Root redirect to home
        const homeChild = route.children.find((c) => c.path === 'home');
        if (homeChild?.meta?.title) titles.push(homeChild.meta.title);
      }
      break;
    }
  }
  return titles;
};

const Navbar: React.FC<NavbarProps> = ({ onMobileMenuToggle }) => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const location = useLocation();
  const sidebarOpened = useAppSelector((state) => state.app.sidebar.opened);
  const device = useAppSelector((state) => state.app.device);
  const avatar = useAppSelector((state) => state.user.userInfo.avatar);
  const username = useAppSelector((state) => state.user.userInfo.username);

  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
  const menuOpen = Boolean(anchorEl);

  const handleToggle = () => {
    if (device === 'mobile') {
      onMobileMenuToggle();
    } else {
      dispatch(toggleSideBar());
    }
  };

  const handleLogout = async () => {
    setAnchorEl(null);
    try {
      await dispatch(userLogout()).unwrap();
    } catch {
      // ignore
    }
    location.pathname = '/login';
    window.location.reload();
  };

  const allRoutes = [...constantRouterMap, ...asyncRouterMap];
  const breadcrumbTitles = findRouteTitle(location.pathname, allRoutes);

  return (
    <AppBar
      position="sticky"
      elevation={0}
      sx={{
        backgroundColor: '#FFFFFF',
        borderBottom: '1px solid #F1F5F9',
        zIndex: (theme) => theme.zIndex.appBar,
      }}
    >
      <Toolbar sx={{ minHeight: 56, px: 2 }}>
        <IconButton
          onClick={handleToggle}
          sx={{
            color: '#64748B',
            '&:hover': { color: '#6366F1', backgroundColor: 'rgba(99, 102, 241, 0.08)' },
            transition: 'all 0.2s',
          }}
        >
          {sidebarOpened ? <MenuOpen /> : <MenuIcon />}
        </IconButton>

        {/* Breadcrumbs */}
        <Breadcrumbs
          separator={<NavigateNext sx={{ fontSize: 16, color: '#CBD5E1' }} />}
          sx={{ ml: 2, flex: 1 }}
        >
          <Link
            component="button"
            underline="hover"
            onClick={() => navigate('/home')}
            sx={{
              display: 'flex',
              alignItems: 'center',
              color: '#64748B',
              fontSize: '0.8125rem',
              cursor: 'pointer',
              '&:hover': { color: '#6366F1' },
            }}
          >
            <Home sx={{ mr: 0.5, fontSize: 16 }} />
            Home
          </Link>
          {breadcrumbTitles.map((title, index) => (
            <Typography
              key={index}
              sx={{
                fontSize: '0.8125rem',
                color: index === breadcrumbTitles.length - 1 ? '#1E293B' : '#64748B',
                fontWeight: index === breadcrumbTitles.length - 1 ? 600 : 400,
              }}
            >
              {title}
            </Typography>
          ))}
        </Breadcrumbs>

        {/* User menu */}
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
          <Chip
            label={username || 'Admin'}
            size="small"
            sx={{
              backgroundColor: 'rgba(99, 102, 241, 0.08)',
              color: '#6366F1',
              fontWeight: 500,
              fontSize: '0.75rem',
              display: { xs: 'none', sm: 'flex' },
            }}
          />
          <IconButton
            onClick={(e) => setAnchorEl(e.currentTarget)}
            sx={{ p: 0.5 }}
          >
            <Avatar
              src={avatar}
              sx={{
                width: 34,
                height: 34,
                border: '2px solid #E2E8F0',
                '&:hover': { borderColor: '#6366F1' },
                transition: 'border-color 0.2s',
              }}
            >
              <Person />
            </Avatar>
          </IconButton>
          <MuiMenu
            anchorEl={anchorEl}
            open={menuOpen}
            onClose={() => setAnchorEl(null)}
            transformOrigin={{ horizontal: 'right', vertical: 'top' }}
            anchorOrigin={{ horizontal: 'right', vertical: 'bottom' }}
            PaperProps={{
              sx: {
                mt: 1,
                minWidth: 160,
                borderRadius: 2,
                boxShadow: '0 4px 12px rgba(0, 0, 0, 0.1)',
              },
            }}
          >
            <MenuItem
              onClick={() => { setAnchorEl(null); navigate('/home'); }}
              sx={{ fontSize: '0.8125rem', py: 1 }}
            >
              <Home sx={{ mr: 1.5, fontSize: 18, color: '#64748B' }} />
              Dashboard
            </MenuItem>
            <MenuItem
              onClick={handleLogout}
              sx={{ fontSize: '0.8125rem', py: 1, color: '#EF4444' }}
            >
              <Logout sx={{ mr: 1.5, fontSize: 18 }} />
              Sign Out
            </MenuItem>
          </MuiMenu>
        </Box>
      </Toolbar>
    </AppBar>
  );
};

export default Navbar;
