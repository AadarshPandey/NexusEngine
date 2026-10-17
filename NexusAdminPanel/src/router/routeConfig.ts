import type { RouteConfig } from '@/store/slices/permissionSlice';

// Icons mapped to MUI iconUrl names — used by sidebar rendering
export const iconMap: Record<string, string> = {
  home: 'Home',
  dashboard: 'Dashboard',
  catalog: 'Inventory2',
  ordering: 'ShoppingCart',
  iam: 'AdminPanelSettings',
  promotion: 'Campaign',
  analytics: 'Dashboard',
  infra: 'Source',
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
  'sms-coupon': 'LocalOffer',
  'sms-new': 'FiberNew',
  'sms-hot': 'Whatshot',
  'sms-subject': 'Subject',
  'sms-ad': 'AdsClick',
  ums: 'AdminPanelSettings',
  'ums-admin': 'People',
  'ums-role': 'Security',
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
  // Bounded Context: Catalog
  {
    path: '/catalog',
    name: 'catalog',
    meta: { title: 'Catalog', iconUrl: 'catalog' },
    children: [
      { path: 'products', name: 'product', meta: { title: 'Product list', iconUrl: 'product-list' } },
      { path: 'products/new', name: 'addProduct', meta: { title: 'Add product', iconUrl: 'product-add' } },
      { path: 'products/:id/edit', name: 'updateProduct', meta: { title: 'Update Product', iconUrl: 'product-add' }, hidden: true },
      { path: 'categories', name: 'productCate', meta: { title: 'Product Categories', iconUrl: 'product-cate' } },
      { path: 'categories/new', name: 'addProductCate', meta: { title: 'Add Category' }, hidden: true },
      { path: 'categories/:id/edit', name: 'updateProductCate', meta: { title: 'Update Category' }, hidden: true },
      { path: 'attributes', name: 'skuAttributes', meta: { title: 'Product type', iconUrl: 'product-attr' } },
      { path: 'attributes/:id/items', name: 'productAttrList', meta: { title: 'Attribute List' }, hidden: true },
      { path: 'brands', name: 'brand', meta: { title: 'Brand management', iconUrl: 'product-brand' } },
      { path: 'brands/new', name: 'addBrand', meta: { title: 'Add Brand' }, hidden: true },
      { path: 'brands/:id/edit', name: 'updateBrand', meta: { title: 'Update Brand' }, hidden: true },
      { path: 'reviews', name: 'productReview', meta: { title: 'Product Reviews', iconUrl: 'document' } },
    ],
  },
  // Bounded Context: Ordering
  {
    path: '/ordering',
    name: 'ordering',
    meta: { title: 'Ordering', iconUrl: 'ordering' },
    children: [
      { path: 'orders', name: 'order', meta: { title: 'Order list', iconUrl: 'product-list' } },
      { path: 'orders/:id', name: 'orderDetail', meta: { title: 'Order Detail' }, hidden: true },
      { path: 'deliveries', name: 'deliverOrderList', meta: { title: 'Delivery List' }, hidden: true },
      { path: 'policies', name: 'orderSetting', meta: { title: 'Order settings', iconUrl: 'order-setting' } },
      { path: 'return-requests', name: 'returnApply', meta: { title: 'Return request processing', iconUrl: 'order-return' } },
      { path: 'return-requests/:id', name: 'returnApplyDetail', meta: { title: 'Return Detail' }, hidden: true },
      { path: 'return-reasons', name: 'returnReason', meta: { title: 'Return reasonId settings', iconUrl: 'order-return-reasonId' } },
    ],
  },
  // Bounded Context: Promotion
  {
    path: '/promotion',
    name: 'promotion',
    meta: { title: 'Promotion', iconUrl: 'promotion' },
    children: [
      { path: 'coupons', name: 'coupon', meta: { title: 'Coupons', iconUrl: 'sms-coupon' } },
      { path: 'coupons/new', name: 'addCoupon', meta: { title: 'Add Coupon' }, hidden: true },
      { path: 'coupons/:id/edit', name: 'updateCoupon', meta: { title: 'Update Coupon' }, hidden: true },
      { path: 'coupons/:id/history', name: 'couponHistory', meta: { title: 'Coupon History' }, hidden: true },
      { path: 'banners', name: 'homeAdvertise', meta: { title: 'Advertisements', iconUrl: 'sms-ad' } },
      { path: 'banners/new', name: 'addHomeAdvertise', meta: { title: 'Add Advertisement' }, hidden: true },
      { path: 'banners/:id/edit', name: 'updateHomeAdvertise', meta: { title: 'Update Advertisement' }, hidden: true },
    ],
  },
  // Bounded Context: IAM
  {
    path: '/iam',
    name: 'iam',
    meta: { title: 'Permissions', iconUrl: 'iam' },
    children: [
      { path: 'admins', name: 'admin', meta: { title: 'Admin Users', iconUrl: 'ums-admin' } },
      { path: 'roles', name: 'role', meta: { title: 'Roles', iconUrl: 'ums-role' } },
    ],
  },
];
