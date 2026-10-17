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
const ProductForm = lazy(() => import('@/pages/catalog/ProductForm'));
const ProductCategoryList = lazy(() => import('@/pages/catalog/ProductCategoryList'));
const ProductCategoryForm = lazy(() => import('@/pages/catalog/ProductCategoryForm'));
const ProductAttrList = lazy(() => import('@/pages/catalog/ProductAttrList'));
const ProductAttrDetailList = lazy(() => import('@/pages/catalog/ProductAttrDetailList'));
const BrandList = lazy(() => import('@/pages/catalog/BrandList'));
const BrandForm = lazy(() => import('@/pages/catalog/BrandForm'));

// Ordering
const OrderList = lazy(() => import('@/pages/ordering/OrderList'));
const OrderDetail = lazy(() => import('@/pages/ordering/OrderDetail'));
const DeliverOrderList = lazy(() => import('@/pages/ordering/DeliverOrderList'));
const OrderSetting = lazy(() => import('@/pages/ordering/OrderSetting'));
const ReturnApplyList = lazy(() => import('@/pages/ordering/ReturnApplyList'));
const ReturnApplyDetail = lazy(() => import('@/pages/ordering/ReturnApplyDetail'));
const ReturnReasonList = lazy(() => import('@/pages/ordering/ReturnReasonList'));

// Promotion
const CouponList = lazy(() => import('@/pages/promotion/CouponList'));
const CouponForm = lazy(() => import('@/pages/promotion/CouponForm'));
const CouponHistory = lazy(() => import('@/pages/promotion/CouponHistory'));
const AdvertiseList = lazy(() => import('@/pages/promotion/AdvertiseList'));
const AdvertiseForm = lazy(() => import('@/pages/promotion/AdvertiseForm'));

// IAM
const AdminList = lazy(() => import('@/pages/iam/AdminList'));
const RoleList = lazy(() => import('@/pages/iam/RoleList'));

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
      { path: 'catalog/products/new', element: withSuspense(ProductForm) },
      { path: 'catalog/products/:id/edit', element: withSuspense(ProductForm) },
      { path: 'catalog/categories', element: withSuspense(ProductCategoryList) },
      { path: 'catalog/categories/new', element: withSuspense(ProductCategoryForm) },
      { path: 'catalog/categories/:id/edit', element: withSuspense(ProductCategoryForm) },
      { path: 'catalog/attributes', element: withSuspense(ProductAttrList) },
      { path: 'catalog/attributes/:id/items', element: withSuspense(ProductAttrDetailList) },
      { path: 'catalog/brands', element: withSuspense(BrandList) },
      { path: 'catalog/brands/new', element: withSuspense(BrandForm) },
      { path: 'catalog/brands/:id/edit', element: withSuspense(BrandForm) },
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
      { path: 'promotion/coupons/new', element: withSuspense(CouponForm) },
      { path: 'promotion/coupons/:id/edit', element: withSuspense(CouponForm) },
      { path: 'promotion/coupons/:id/history', element: withSuspense(CouponHistory) },
      { path: 'promotion/banners', element: withSuspense(AdvertiseList) },
      { path: 'promotion/banners/new', element: withSuspense(AdvertiseForm) },
      { path: 'promotion/banners/:id/edit', element: withSuspense(AdvertiseForm) },
      // Modern IAM Routes
      { path: 'iam/admins', element: withSuspense(AdminList) },
      { path: 'iam/roles', element: withSuspense(RoleList) },
    ],
  },
  {
    path: '*',
    element: <Navigate to="/404" replace />,
  },
]);

export default router;
