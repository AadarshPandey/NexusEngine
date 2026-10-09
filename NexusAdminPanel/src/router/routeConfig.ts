import type { RouteConfig } from '@/store/slices/permissionSlice';

// Icons mapped to MUI iconUrl names — used by sidebar rendering
export const iconMap: Record<string, string> = {
  home: 'Home',
  dashboard: 'Dashboard',
  document: 'Description',
  video: 'VideoLibrary',
  product: 'Inventory2',
  'product-list': 'ViewList',
  'product-add': 'AddBox',
  'product-cate': 'Category',
  'product-attr': 'Tune',
  'product-brand': 'Storefront',
  order: 'ShoppingCart',
  'order-setting': 'Settings',
  'order-return': 'AssignmentReturn',
  'order-return-reasonId': 'HelpOutline',
  sms: 'Campaign',
  'sms-flash': 'FlashOn',
  'sms-coupon': 'LocalOffer',
  'sms-new': 'FiberNew',
  'sms-hot': 'Whatshot',
  'sms-subject': 'Subject',
  'sms-ad': 'AdsClick',
  ums: 'AdminPanelSettings',
  'ums-admin': 'People',
  'ums-role': 'Security',
  'ums-menu': 'Menu',
  'ums-resource': 'Source',
};

// Static routes (always available)
export const constantRouterMap: RouteConfig[] = [
  {
    path: '/404',
    name: 'notFound',
    hidden: true,
  },
  {
    path: '/login',
    name: 'login',
    hidden: true,
  },
  {
    path: '',
    name: 'root',
    meta: { title: 'Home', iconUrl: 'home' },
    children: [
      {
        path: 'home',
        name: 'home',
        meta: { title: 'Dashboard', iconUrl: 'dashboard' },
      },
    ],
  },
];

// Dynamic routes (assigned based on user permissions)
export const asyncRouterMap: RouteConfig[] = [
  {
    path: '/pms',
    name: 'pms',
    meta: { title: 'merchandise', iconUrl: 'product' },
    children: [
      { path: 'product', name: 'product', meta: { title: 'Product list', iconUrl: 'product-list' } },
      { path: 'addProduct', name: 'addProduct', meta: { title: 'Add product', iconUrl: 'product-add' } },
      { path: 'updateProduct', name: 'updateProduct', meta: { title: 'Update Product', iconUrl: 'product-add' }, hidden: true },
      { path: 'productCate', name: 'productCate', meta: { title: 'Product Categories', iconUrl: 'product-cate' } },
      { path: 'addProductCate', name: 'addProductCate', meta: { title: 'Add Category' }, hidden: true },
      { path: 'updateProductCate', name: 'updateProductCate', meta: { title: 'Update Category' }, hidden: true },
      { path: 'skuAttributes', name: 'skuAttributes', meta: { title: 'Product type', iconUrl: 'product-attr' } },
      { path: 'productAttrList', name: 'productAttrList', meta: { title: 'Attribute List' }, hidden: true },
      { path: 'addProductAttr', name: 'addProductAttr', meta: { title: 'Add Attribute' }, hidden: true },
      { path: 'updateProductAttr', name: 'updateProductAttr', meta: { title: 'Update Attribute' }, hidden: true },
      { path: 'brand', name: 'brand', meta: { title: 'Brand management', iconUrl: 'product-brand' } },
      { path: 'addBrand', name: 'addBrand', meta: { title: 'Add Brand' }, hidden: true },
      { path: 'updateBrand', name: 'updateBrand', meta: { title: 'Update Brand' }, hidden: true },
    ],
  },
  {
    path: '/oms',
    name: 'oms',
    meta: { title: 'Order', iconUrl: 'order' },
    children: [
      { path: 'order', name: 'order', meta: { title: 'order list', iconUrl: 'product-list' } },
      { path: 'orderDetail', name: 'orderDetail', meta: { title: 'Order Detail' }, hidden: true },
      { path: 'deliverOrderList', name: 'deliverOrderList', meta: { title: 'Delivery List' }, hidden: true },
      { path: 'orderSetting', name: 'orderSetting', meta: { title: 'Order settings', iconUrl: 'order-setting' } },
      { path: 'returnApply', name: 'returnApply', meta: { title: 'Return request processing', iconUrl: 'order-return' } },
      { path: 'returnReason', name: 'returnReason', meta: { title: 'Return reasonId settings', iconUrl: 'order-return-reasonId' } },
      { path: 'returnApplyDetail', name: 'returnApplyDetail', meta: { title: 'Return Detail' }, hidden: true },
    ],
  },
  {
    path: '/sms',
    name: 'sms',
    meta: { title: 'marketing', iconUrl: 'sms' },
    children: [
      { path: 'flash', name: 'flash', meta: { title: 'Flash Sales', iconUrl: 'sms-flash' } },
      { path: 'flashSession', name: 'flashSession', meta: { title: 'Flash Sessions' }, hidden: true },
      { path: 'selectSession', name: 'selectSession', meta: { title: 'Select Session' }, hidden: true },
      { path: 'flashProductRelation', name: 'flashProductRelation', meta: { title: 'Flash Products' }, hidden: true },
      { path: 'coupon', name: 'coupon', meta: { title: 'Coupons', iconUrl: 'sms-coupon' } },
      { path: 'addCoupon', name: 'addCoupon', meta: { title: 'Add Coupon' }, hidden: true },
      { path: 'updateCoupon', name: 'updateCoupon', meta: { title: 'Update Coupon' }, hidden: true },
      { path: 'couponHistory', name: 'couponHistory', meta: { title: 'Coupon History' }, hidden: true },
      { path: 'advertise', name: 'homeAdvertise', meta: { title: 'Advertisements', iconUrl: 'sms-ad' } },
      { path: 'addAdvertise', name: 'addHomeAdvertise', meta: { title: 'Add Advertisement' }, hidden: true },
      { path: 'updateAdvertise', name: 'updateHomeAdvertise', meta: { title: 'Update Advertisement' }, hidden: true },
    ],
  },
  {
    path: '/ums',
    name: 'ums',
    meta: { title: 'Permissions', iconUrl: 'ums' },
    children: [
      { path: 'admin', name: 'admin', meta: { title: 'Admin Users', iconUrl: 'ums-admin' } },
      { path: 'role', name: 'role', meta: { title: 'Roles', iconUrl: 'ums-role' } },
      { path: 'allocMenu', name: 'allocMenu', meta: { title: 'Assign Menus' }, hidden: true },
      { path: 'allocResource', name: 'allocResource', meta: { title: 'Assign Resources' }, hidden: true },
      { path: 'addMenu', name: 'addMenu', meta: { title: 'Add Menu' }, hidden: true },
      { path: 'updateMenu', name: 'updateMenu', meta: { title: 'Update Menu' }, hidden: true },
    ],
  },
];
