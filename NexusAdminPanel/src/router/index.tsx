import React, { Suspense, lazy } from 'react';
import { createHashRouter, Navigate } from 'react-router';
import AdminLayout from '@/layouts/AdminLayout';
import AuthGuard from '@/router/AuthGuard';
import { Box, CircularProgress } from '@mui/material';

// Lazy-loaded pages
const Login = lazy(() => import('@/pages/Login'));
const NotFound = lazy(() => import('@/pages/NotFound'));
const Dashboard = lazy(() => import('@/pages/Dashboard'));

// PMS
const ProductList = lazy(() => import('@/pages/pms/ProductList'));
const ProductAdd = lazy(() => import('@/pages/pms/ProductAdd'));
const ProductUpdate = lazy(() => import('@/pages/pms/ProductUpdate'));
const ProductCategoryList = lazy(() => import('@/pages/pms/ProductCategoryList'));
const ProductCategoryAdd = lazy(() => import('@/pages/pms/ProductCategoryAdd'));
const ProductCategoryUpdate = lazy(() => import('@/pages/pms/ProductCategoryUpdate'));
const ProductAttrList = lazy(() => import('@/pages/pms/ProductAttrList'));
const ProductAttrDetailList = lazy(() => import('@/pages/pms/ProductAttrDetailList'));
const ProductAttrAdd = lazy(() => import('@/pages/pms/ProductAttrAdd'));
const ProductAttrUpdate = lazy(() => import('@/pages/pms/ProductAttrUpdate'));
const BrandList = lazy(() => import('@/pages/pms/BrandList'));
const BrandAdd = lazy(() => import('@/pages/pms/BrandAdd'));
const BrandUpdate = lazy(() => import('@/pages/pms/BrandUpdate'));

// OMS
const OrderList = lazy(() => import('@/pages/oms/OrderList'));
const OrderDetail = lazy(() => import('@/pages/oms/OrderDetail'));
const DeliverOrderList = lazy(() => import('@/pages/oms/DeliverOrderList'));
const OrderSetting = lazy(() => import('@/pages/oms/OrderSetting'));
const ReturnApplyList = lazy(() => import('@/pages/oms/ReturnApplyList'));
const ReturnApplyDetail = lazy(() => import('@/pages/oms/ReturnApplyDetail'));
const ReturnReasonList = lazy(() => import('@/pages/oms/ReturnReasonList'));

// SMS
const FlashList = lazy(() => import('@/pages/sms/FlashList'));
const FlashSessionList = lazy(() => import('@/pages/sms/FlashSessionList'));
const FlashProductRelation = lazy(() => import('@/pages/sms/FlashProductRelation'));
const CouponList = lazy(() => import('@/pages/sms/CouponList'));
const CouponAdd = lazy(() => import('@/pages/sms/CouponAdd'));
const CouponUpdate = lazy(() => import('@/pages/sms/CouponUpdate'));
const CouponHistory = lazy(() => import('@/pages/sms/CouponHistory'));
const HomeBrand = lazy(() => import('@/pages/sms/HomeBrand'));
const HomeNew = lazy(() => import('@/pages/sms/HomeNew'));
const HomeHot = lazy(() => import('@/pages/sms/HomeHot'));
const HomeSubject = lazy(() => import('@/pages/sms/HomeSubject'));
const AdvertiseList = lazy(() => import('@/pages/sms/AdvertiseList'));
const AdvertiseAdd = lazy(() => import('@/pages/sms/AdvertiseAdd'));
const AdvertiseUpdate = lazy(() => import('@/pages/sms/AdvertiseUpdate'));

// UMS
const AdminList = lazy(() => import('@/pages/ums/AdminList'));
const RoleList = lazy(() => import('@/pages/ums/RoleList'));
const AllocMenu = lazy(() => import('@/pages/ums/AllocMenu'));
const AllocResource = lazy(() => import('@/pages/ums/AllocResource'));
const MenuList = lazy(() => import('@/pages/ums/MenuList'));
const MenuAdd = lazy(() => import('@/pages/ums/MenuAdd'));
const MenuUpdate = lazy(() => import('@/pages/ums/MenuUpdate'));
const ResourceList = lazy(() => import('@/pages/ums/ResourceList'));
const ResourceCategoryList = lazy(() => import('@/pages/ums/ResourceCategoryList'));

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
      // PMS
      { path: 'pms/product', element: withSuspense(ProductList) },
      { path: 'pms/addProduct', element: withSuspense(ProductAdd) },
      { path: 'pms/updateProduct', element: withSuspense(ProductUpdate) },
      { path: 'pms/productCate', element: withSuspense(ProductCategoryList) },
      { path: 'pms/addProductCate', element: withSuspense(ProductCategoryAdd) },
      { path: 'pms/updateProductCate', element: withSuspense(ProductCategoryUpdate) },
      { path: 'pms/productAttr', element: withSuspense(ProductAttrList) },
      { path: 'pms/productAttrList', element: withSuspense(ProductAttrDetailList) },
      { path: 'pms/addProductAttr', element: withSuspense(ProductAttrAdd) },
      { path: 'pms/updateProductAttr', element: withSuspense(ProductAttrUpdate) },
      { path: 'pms/brand', element: withSuspense(BrandList) },
      { path: 'pms/addBrand', element: withSuspense(BrandAdd) },
      { path: 'pms/updateBrand', element: withSuspense(BrandUpdate) },
      // OMS
      { path: 'oms/order', element: withSuspense(OrderList) },
      { path: 'oms/orderDetail', element: withSuspense(OrderDetail) },
      { path: 'oms/deliverOrderList', element: withSuspense(DeliverOrderList) },
      { path: 'oms/orderSetting', element: withSuspense(OrderSetting) },
      { path: 'oms/returnApply', element: withSuspense(ReturnApplyList) },
      { path: 'oms/returnApplyDetail', element: withSuspense(ReturnApplyDetail) },
      { path: 'oms/returnReason', element: withSuspense(ReturnReasonList) },
      // SMS
      { path: 'sms/flash', element: withSuspense(FlashList) },
      { path: 'sms/flashSession', element: withSuspense(FlashSessionList) },
      { path: 'sms/flashProductRelation', element: withSuspense(FlashProductRelation) },
      { path: 'sms/coupon', element: withSuspense(CouponList) },
      { path: 'sms/addCoupon', element: withSuspense(CouponAdd) },
      { path: 'sms/updateCoupon', element: withSuspense(CouponUpdate) },
      { path: 'sms/couponHistory', element: withSuspense(CouponHistory) },
      { path: 'sms/brand', element: withSuspense(HomeBrand) },
      { path: 'sms/new', element: withSuspense(HomeNew) },
      { path: 'sms/hot', element: withSuspense(HomeHot) },
      { path: 'sms/subject', element: withSuspense(HomeSubject) },
      { path: 'sms/advertise', element: withSuspense(AdvertiseList) },
      { path: 'sms/addAdvertise', element: withSuspense(AdvertiseAdd) },
      { path: 'sms/updateAdvertise', element: withSuspense(AdvertiseUpdate) },
      // UMS
      { path: 'ums/admin', element: withSuspense(AdminList) },
      { path: 'ums/role', element: withSuspense(RoleList) },
      { path: 'ums/allocMenu', element: withSuspense(AllocMenu) },
      { path: 'ums/allocResource', element: withSuspense(AllocResource) },
      { path: 'ums/menu', element: withSuspense(MenuList) },
      { path: 'ums/addMenu', element: withSuspense(MenuAdd) },
      { path: 'ums/updateMenu', element: withSuspense(MenuUpdate) },
      { path: 'ums/resource', element: withSuspense(ResourceList) },
      { path: 'ums/resourceCategory', element: withSuspense(ResourceCategoryList) },
    ],
  },
  {
    path: '*',
    element: <Navigate to="/404" replace />,
  },
]);

export default router;
