import React, { useState } from 'react';
import { useNavigate, useLocation } from 'react-router';
import {
  Box,
  Drawer,
  List,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Collapse,
  Typography,
  alpha,
  Tooltip,
} from '@mui/material';
import {
  ExpandLess,
  ExpandMore,
  Dashboard,
  Inventory2,
  ShoppingCart,
  Campaign,
  AdminPanelSettings,
  ViewList,
  AddBox,
  Category,
  Tune,
  Storefront,
  Settings,
  AssignmentReturn,
  HelpOutline,
  FlashOn,
  LocalOffer,
  FiberNew,
  Whatshot,
  Subject,
  AdsClick,
  People,
  Security,
  Menu as MenuIcon,
  Source,
} from '@mui/icons-material';
import { useAppSelector } from '@/store/hooks';
import type { RouteConfig } from '@/store/slices/permissionSlice';
import { constantRouterMap, asyncRouterMap } from '@/router/routeConfig';

const iconComponents: Record<string, React.ReactElement> = {
  dashboard: <Dashboard />,
  home: <Dashboard />,
  product: <Inventory2 />,
  'product-list': <ViewList />,
  'product-add': <AddBox />,
  'product-cate': <Category />,
  'product-attr': <Tune />,
  'product-brand': <Storefront />,
  order: <ShoppingCart />,
  'order-setting': <Settings />,
  'order-return': <AssignmentReturn />,
  'order-return-reason': <HelpOutline />,
  sms: <Campaign />,
  'sms-flash': <FlashOn />,
  'sms-coupon': <LocalOffer />,
  'sms-new': <FiberNew />,
  'sms-hot': <Whatshot />,
  'sms-subject': <Subject />,
  'sms-ad': <AdsClick />,
  ums: <AdminPanelSettings />,
  'ums-admin': <People />,
  'ums-role': <Security />,
  'ums-menu': <MenuIcon />,
  'ums-resource': <Source />,
};

const getIcon = (iconName?: string): React.ReactElement => {
  if (!iconName) return <Dashboard />;
  return iconComponents[iconName] || <Dashboard />;
};

interface SidebarProps {
  width: number;
  collapsedWidth: number;
  opened: boolean;
  mobileOpen: boolean;
  isMobile: boolean;
  onMobileClose: () => void;
}

const Sidebar: React.FC<SidebarProps> = ({
  width,
  collapsedWidth,
  opened,
  mobileOpen,
  isMobile,
  onMobileClose,
}) => {
  const navigate = useNavigate();
  const location = useLocation();
  const permissionRouters = useAppSelector((state) => state.permission.routers);

  // Use permission routers if available, otherwise use static routes
  const routes = permissionRouters.length > 0 ? permissionRouters : constantRouterMap;

  const [openMenus, setOpenMenus] = useState<Record<string, boolean>>({});

  const handleToggleMenu = (name: string) => {
    setOpenMenus((prev) => ({ ...prev, [name]: !prev[name] }));
  };

  const handleNavigate = (path: string) => {
    navigate(path);
    if (isMobile) onMobileClose();
  };

  const isActive = (path: string) => location.pathname === path;

  const renderMenuItem = (route: RouteConfig, parentPath: string = '') => {
    if (route.hidden) return null;

    const fullPath = parentPath ? `${parentPath}/${route.path}` : route.path || '/';
    const visibleChildren = route.children?.filter((c) => !c.hidden) || [];

    // If only one visible child and no alwaysShow, render flat
    if (visibleChildren.length === 1 && !route.alwaysShow && !visibleChildren[0].children) {
      const child = visibleChildren[0];
      const childPath = fullPath ? `${fullPath}/${child.path}`.replace('//', '/') : `/${child.path}`;
      const normalizedChildPath = childPath.startsWith('/') ? childPath : `/${childPath}`;
      const active = isActive(normalizedChildPath);

      const button = (
        <ListItemButton
          key={child.name}
          onClick={() => handleNavigate(normalizedChildPath)}
          sx={{
            mx: 1,
            my: 0.3,
            borderRadius: 2,
            minHeight: 44,
            justifyContent: opened ? 'initial' : 'center',
            backgroundColor: active ? alpha('#6366F1', 0.12) : 'transparent',
            color: active ? '#6366F1' : '#94A3B8',
            '&:hover': {
              backgroundColor: active ? alpha('#6366F1', 0.16) : alpha('#fff', 0.06),
              color: active ? '#6366F1' : '#E2E8F0',
            },
            transition: 'all 0.15s ease',
          }}
        >
          <ListItemIcon
            sx={{
              minWidth: 0,
              mr: opened ? 2 : 'auto',
              justifyContent: 'center',
              color: 'inherit',
              '& .MuiSvgIcon-root': { fontSize: 20 },
            }}
          >
            {getIcon(child.meta?.icon || route.meta?.icon)}
          </ListItemIcon>
          {opened && (
            <ListItemText
              primary={child.meta?.title}
              primaryTypographyProps={{
                fontSize: '0.8125rem',
                fontWeight: active ? 600 : 400,
              }}
            />
          )}
        </ListItemButton>
      );

      return opened ? button : (
        <Tooltip title={child.meta?.title || ''} placement="right" key={child.name}>
          {button}
        </Tooltip>
      );
    }

    // Multi-child: render collapsible submenu
    if (visibleChildren.length > 0) {
      const menuKey = route.name || route.path;
      const isOpen = openMenus[menuKey] ?? true;

      return (
        <React.Fragment key={menuKey}>
          {opened ? (
            <ListItemButton
              onClick={() => handleToggleMenu(menuKey)}
              sx={{
                mx: 1,
                my: 0.3,
                borderRadius: 2,
                color: '#94A3B8',
                '&:hover': { backgroundColor: alpha('#fff', 0.06), color: '#E2E8F0' },
              }}
            >
              <ListItemIcon
                sx={{
                  minWidth: 0,
                  mr: 2,
                  color: 'inherit',
                  '& .MuiSvgIcon-root': { fontSize: 20 },
                }}
              >
                {getIcon(route.meta?.icon)}
              </ListItemIcon>
              <ListItemText
                primary={route.meta?.title}
                primaryTypographyProps={{ fontSize: '0.8125rem', fontWeight: 500 }}
              />
              {isOpen ? <ExpandLess sx={{ fontSize: 18 }} /> : <ExpandMore sx={{ fontSize: 18 }} />}
            </ListItemButton>
          ) : (
            <Tooltip title={route.meta?.title || ''} placement="right">
              <ListItemButton
                onClick={() => handleToggleMenu(menuKey)}
                sx={{
                  mx: 1,
                  my: 0.3,
                  borderRadius: 2,
                  justifyContent: 'center',
                  color: '#94A3B8',
                  '&:hover': { backgroundColor: alpha('#fff', 0.06), color: '#E2E8F0' },
                }}
              >
                <ListItemIcon
                  sx={{
                    minWidth: 0,
                    color: 'inherit',
                    '& .MuiSvgIcon-root': { fontSize: 20 },
                  }}
                >
                  {getIcon(route.meta?.icon)}
                </ListItemIcon>
              </ListItemButton>
            </Tooltip>
          )}
          {opened && (
            <Collapse in={isOpen} timeout="auto" unmountOnExit>
              <List component="div" disablePadding sx={{ pl: 2 }}>
                {visibleChildren.map((child) => {
                  const childPath = `${fullPath}/${child.path}`.replace('//', '/');
                  const normalizedChildPath = childPath.startsWith('/') ? childPath : `/${childPath}`;
                  const active = isActive(normalizedChildPath);

                  // Handle external links
                  if (child.path.startsWith('http')) {
                    return (
                      <ListItemButton
                        key={child.name}
                        component="a"
                        href={child.path}
                        target="_blank"
                        sx={{
                          mx: 1,
                          my: 0.2,
                          borderRadius: 2,
                          minHeight: 38,
                          color: '#94A3B8',
                          '&:hover': { backgroundColor: alpha('#fff', 0.06), color: '#E2E8F0' },
                        }}
                      >
                        <ListItemIcon
                          sx={{
                            minWidth: 0,
                            mr: 2,
                            color: 'inherit',
                            '& .MuiSvgIcon-root': { fontSize: 18 },
                          }}
                        >
                          {getIcon(child.meta?.icon)}
                        </ListItemIcon>
                        <ListItemText
                          primary={child.meta?.title}
                          primaryTypographyProps={{ fontSize: '0.8125rem' }}
                        />
                      </ListItemButton>
                    );
                  }

                  return (
                    <ListItemButton
                      key={child.name}
                      onClick={() => handleNavigate(normalizedChildPath)}
                      sx={{
                        mx: 1,
                        my: 0.2,
                        borderRadius: 2,
                        minHeight: 38,
                        backgroundColor: active ? alpha('#6366F1', 0.12) : 'transparent',
                        color: active ? '#6366F1' : '#94A3B8',
                        '&:hover': {
                          backgroundColor: active ? alpha('#6366F1', 0.16) : alpha('#fff', 0.06),
                          color: active ? '#6366F1' : '#E2E8F0',
                        },
                      }}
                    >
                      <ListItemIcon
                        sx={{
                          minWidth: 0,
                          mr: 2,
                          color: 'inherit',
                          '& .MuiSvgIcon-root': { fontSize: 18 },
                        }}
                      >
                        {getIcon(child.meta?.icon)}
                      </ListItemIcon>
                      <ListItemText
                        primary={child.meta?.title}
                        primaryTypographyProps={{
                          fontSize: '0.8125rem',
                          fontWeight: active ? 600 : 400,
                        }}
                      />
                    </ListItemButton>
                  );
                })}
              </List>
            </Collapse>
          )}
        </React.Fragment>
      );
    }

    return null;
  };

  const drawerContent = (
    <Box
      sx={{
        height: '100%',
        display: 'flex',
        flexDirection: 'column',
        background: 'linear-gradient(180deg, #1E293B 0%, #0F172A 100%)',
        color: '#E2E8F0',
      }}
    >
      {/* Logo */}
      <Box
        sx={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: opened ? 'flex-start' : 'center',
          px: opened ? 3 : 1,
          py: 2.5,
          borderBottom: '1px solid rgba(255,255,255,0.06)',
        }}
      >
        <Box
          sx={{
            width: 36,
            height: 36,
            borderRadius: 2,
            background: 'linear-gradient(135deg, #6366F1 0%, #818CF8 100%)',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            fontWeight: 700,
            fontSize: '1rem',
            color: '#fff',
            flexShrink: 0,
          }}
        >
          N
        </Box>
        {opened && (
          <Typography
            variant="h6"
            sx={{
              ml: 1.5,
              fontWeight: 700,
              fontSize: '1rem',
              background: 'linear-gradient(135deg, #6366F1, #EC4899)',
              backgroundClip: 'text',
              WebkitBackgroundClip: 'text',
              WebkitTextFillColor: 'transparent',
              whiteSpace: 'nowrap',
            }}
          >
            Nexus Admin
          </Typography>
        )}
      </Box>

      {/* Menu */}
      <Box sx={{ flex: 1, overflow: 'auto', py: 1 }}>
        <List component="nav" disablePadding>
          {routes.map((route) => renderMenuItem(route))}
        </List>
      </Box>
    </Box>
  );

  if (isMobile) {
    return (
      <Drawer
        variant="temporary"
        open={mobileOpen}
        onClose={onMobileClose}
        ModalProps={{ keepMounted: true }}
        sx={{
          '& .MuiDrawer-paper': {
            width,
            boxSizing: 'border-box',
            border: 'none',
          },
        }}
      >
        {drawerContent}
      </Drawer>
    );
  }

  return (
    <Drawer
      variant="permanent"
      sx={{
        '& .MuiDrawer-paper': {
          width: opened ? width : collapsedWidth,
          boxSizing: 'border-box',
          transition: 'width 0.3s cubic-bezier(0.4, 0, 0.2, 1)',
          overflowX: 'hidden',
          border: 'none',
        },
      }}
      open
    >
      {drawerContent}
    </Drawer>
  );
};

export default Sidebar;
