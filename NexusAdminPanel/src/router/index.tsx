import React, { Suspense, lazy } from 'react';
import { createHashRouter, Navigate } from 'react-router';
import AdminLayout from '@/layouts/AdminLayout';
import AuthGuard from '@/router/AuthGuard';
import { Box, CircularProgress } from '@mui/material';

// Lazy-loaded pages
const Login = lazy(() => import('@/pages/Login'));
const NotFound = lazy(() => import('@/pages/NotFound'));
const Dashboard = lazy(() => import('@/pages/Dashboard'));

// Catalog
const ProductList = lazy(() => import('@/pages/catalog/ProductList'));
const ProductAdd = lazy(() => import('@/pages/catalog/ProductAdd'));
const ProductUpdate = lazy(() => import('@/pages/catalog/ProductUpdate'));
const ProductCategoryList = lazy(() => import('@/pages/catalog/ProductCategoryList'));
const ProductCategoryAdd = lazy(() => import('@/pages/catalog/ProductCategoryAdd'));
const ProductCategoryUpdate = lazy(() => import('@/pages/catalog/ProductCategoryUpdate'));
const ProductAttrList = lazy(() => import('@/pages/catalog/ProductAttrList'));
const ProductAttrDetailList = lazy(() => import('@/pages/catalog/ProductAttrDetailList'));
const BrandList = lazy(() => import('@/pages/catalog/BrandList'));
const BrandAdd = lazy(() => import('@/pages/catalog/BrandAdd'));
const BrandUpdate = lazy(() => import('@/pages/catalog/BrandUpdate'));

// Ordering
const OrderList = lazy(() => import('@/pages/ordering/OrderList'));
const OrderDetail = lazy(() => import('@/pages/ordering/OrderDetail'));
const DeliverOrderList = lazy(() => import('@/pages/ordering/DeliverOrderList'));
const OrderSetting = lazy(() => import('@/pages/ordering/OrderSetting'));
const ReturnApplyList = lazy(() => import('@/pages/ordering/ReturnApplyList'));
const ReturnApplyDetail = lazy(() => import('@/pages/ordering/ReturnApplyDetail'));
const ReturnReasonList = lazy(() => import('@/pages/ordering/ReturnReasonList'));

// Promotion
const FlashList = lazy(() => import('@/pages/promotion/FlashList'));
const FlashSessionList = lazy(() => import('@/pages/promotion/FlashSessionList'));
const FlashProductRelation = lazy(() => import('@/pages/promotion/FlashProductRelation'));
const CouponList = lazy(() => import('@/pages/promotion/CouponList'));
const CouponAdd = lazy(() => import('@/pages/promotion/CouponAdd'));
const CouponUpdate = lazy(() => import('@/pages/promotion/CouponUpdate'));
const CouponHistory = lazy(() => import('@/pages/promotion/CouponHistory'));
const AdvertiseList = lazy(() => import('@/pages/promotion/AdvertiseList'));
const AdvertiseAdd = lazy(() => import('@/pages/promotion/AdvertiseAdd'));
const AdvertiseUpdate = lazy(() => import('@/pages/promotion/AdvertiseUpdate'));
const SelectSession = lazy(() => import('@/pages/promotion/SelectSession'));

// IAM
const AdminList = lazy(() => import('@/pages/iam/AdminList'));
const RoleList = lazy(() => import('@/pages/iam/RoleList'));
const AllocMenu = lazy(() => import('@/pages/iam/AllocMenu'));
const AllocResource = lazy(() => import('@/pages/iam/AllocResource'));
const MenuList = lazy(() => import('@/pages/iam/MenuList'));
const MenuAdd = lazy(() => import('@/pages/iam/MenuAdd'));
const MenuUpdate = lazy(() => import('@/pages/iam/MenuUpdate'));
const ResourceList = lazy(() => import('@/pages/iam/ResourceList'));
const ResourceCategoryList = lazy(() => import('@/pages/iam/ResourceCategoryList'));

const Loading = () => (
  <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '60vh' }}>
    <CircularProgress sx={{ color: '#6366F1' }} />
  </Box>
);

const withSuspense = (Component: React.LazyExoticComponent<React.ComponentType>) => (
  <Suspense fallback={<Loading />}>
    <Component />
  </Suspense>
);

const router = createHashRouter([
  {
    path: '/login',
    element: withSuspense(Login),
  },
  {
    path: '/404',
    element: withSuspense(NotFound),
  },
  {
    path: '/',
    element: (
      <AuthGuard>
        <AdminLayout />
      </AuthGuard>
    ),
    children: [
      { index: true, element: <Navigate to="/home" replace /> },
      { path: 'home', element: withSuspense(Dashboard) },
      // Modern Catalog Routes
      { path: 'catalog/products', element: withSuspense(ProductList) },
      { path: 'catalog/products/new', element: withSuspense(ProductAdd) },
      { path: 'catalog/products/:id/edit', element: withSuspense(ProductUpdate) },
      { path: 'catalog/categories', element: withSuspense(ProductCategoryList) },
      { path: 'catalog/categories/new', element: withSuspense(ProductCategoryAdd) },
      { path: 'catalog/categories/:id/edit', element: withSuspense(ProductCategoryUpdate) },
      { path: 'catalog/attributes', element: withSuspense(ProductAttrList) },
      { path: 'catalog/attributes/:id/items', element: withSuspense(ProductAttrDetailList) },
      { path: 'catalog/brands', element: withSuspense(BrandList) },
      { path: 'catalog/brands/new', element: withSuspense(BrandAdd) },
      { path: 'catalog/brands/:id/edit', element: withSuspense(BrandUpdate) },
      // Modern Ordering Routes
      { path: 'ordering/orders', element: withSuspense(OrderList) },
      { path: 'ordering/orders/:id', element: withSuspense(OrderDetail) },
      { path: 'ordering/deliveries', element: withSuspense(DeliverOrderList) },
      { path: 'ordering/policies', element: withSuspense(OrderSetting) },
      { path: 'ordering/return-requests', element: withSuspense(ReturnApplyList) },
      { path: 'ordering/return-requests/:id', element: withSuspense(ReturnApplyDetail) },
      { path: 'ordering/return-reasons', element: withSuspense(ReturnReasonList) },
      // Modern Promotion Routes
      { path: 'promotion/coupons', element: withSuspense(CouponList) },
      { path: 'promotion/coupons/new', element: withSuspense(CouponAdd) },
      { path: 'promotion/coupons/:id/edit', element: withSuspense(CouponUpdate) },
      { path: 'promotion/coupons/:id/history', element: withSuspense(CouponHistory) },
      { path: 'promotion/flash-sales', element: withSuspense(FlashList) },
      { path: 'promotion/flash-sales/:id/slots', element: withSuspense(FlashSessionList) },
      { path: 'promotion/flash-sales/select-session', element: withSuspense(SelectSession) },
      { path: 'promotion/flash-sales/:id/items', element: withSuspense(FlashProductRelation) },
      { path: 'promotion/banners', element: withSuspense(AdvertiseList) },
      { path: 'promotion/banners/new', element: withSuspense(AdvertiseAdd) },
      { path: 'promotion/banners/:id/edit', element: withSuspense(AdvertiseUpdate) },
      // Modern IAM Routes
      { path: 'iam/admins', element: withSuspense(AdminList) },
      { path: 'iam/roles', element: withSuspense(RoleList) },
      { path: 'iam/permissions', element: withSuspense(ResourceList) },
      { path: 'iam/permission-groups', element: withSuspense(ResourceCategoryList) },
      { path: 'iam/menus', element: withSuspense(MenuList) },
      { path: 'iam/menus/new', element: withSuspense(MenuAdd) },
      { path: 'iam/menus/:id/edit', element: withSuspense(MenuUpdate) },
      { path: 'iam/allocMenu', element: withSuspense(AllocMenu) },
      { path: 'iam/allocResource', element: withSuspense(AllocResource) },
    ],
  },
  {
    path: '*',
    element: <Navigate to="/404" replace />,
  },
]);

export default router;
