import { createSlice, type PayloadAction } from '@reduxjs/toolkit';
import type { UmsMenu, ConsoleMenu } from '@/types/iam';
import { asyncRouterMap, constantRouterMap } from '@/router/routeConfig';

// Route definition for the React app
export interface RouteConfig {
  path: string;
  name?: string;
  hidden?: boolean;
  displayOrder?: number;
  alwaysShow?: boolean;
  meta?: {
    title?: string;
    iconUrl?: string;
    icon?: string;
  };
  children?: RouteConfig[];
}

interface PermissionState {
  addRouters: RouteConfig[];
  routers: RouteConfig[];
  menus: ConsoleMenu[];
}

const initialState: PermissionState = {
  addRouters: [],
  routers: [],
  menus: [],
};

// Route name aliases across legacy and modern bounded contexts
const ROUTE_NAME_ALIASES: Record<string, string[]> = {
  catalog: ['catalog', 'pms'],
  pms: ['pms', 'catalog'],
  ordering: ['ordering', 'oms'],
  oms: ['oms', 'ordering'],
  iam: ['iam', 'ums'],
  ums: ['ums', 'iam'],
  promotion: ['promotion', 'sms'],
  sms: ['sms', 'promotion'],
  product: ['product', 'products'],
  products: ['products', 'product'],
  order: ['order', 'orders'],
  orders: ['orders', 'order'],
  brand: ['brand', 'brands'],
  brands: ['brands', 'brand'],
  productCate: ['productCate', 'categories'],
  categories: ['categories', 'productCate'],
  skuAttributes: ['skuAttributes', 'attributes'],
  attributes: ['attributes', 'skuAttributes'],
  coupon: ['coupon', 'coupons'],
  coupons: ['coupons', 'coupon'],
  homeAdvertise: ['homeAdvertise', 'banners', 'advertise'],
  banners: ['banners', 'homeAdvertise', 'advertise'],
  advertise: ['advertise', 'banners', 'homeAdvertise'],
  admin: ['admin', 'admins'],
  admins: ['admins', 'admin'],
  role: ['role', 'roles'],
  roles: ['roles', 'role'],
  menu: ['menu', 'menus'],
  menus: ['menus', 'menu'],
  resource: ['resource', 'permissions'],
  permissions: ['permissions', 'resource'],
};

// Check if a route has permission based on menus
function hasPermission(menus: (UmsMenu | ConsoleMenu)[], route: RouteConfig): boolean {
  if (route.name) {
    const aliasCandidates = ROUTE_NAME_ALIASES[route.name] || [route.name];
    const currMenu = menus.find((menu) => {
      const menuIdentifier = menu.routeName || menu.name;
      return Boolean(menuIdentifier && aliasCandidates.includes(menuIdentifier));
    }) || null;

    if (currMenu != null) {
      if (currMenu.title != null && currMenu.title !== '') {
        if (!route.meta) route.meta = {};
        route.meta.title = currMenu.title;
      }
      const icon = currMenu.icon || (currMenu as any).iconUrl;
      if (icon != null && icon !== '') {
        if (!route.meta) route.meta = {};
        route.meta.iconUrl = icon;
        route.meta.icon = icon;
      }
      if (currMenu.isHidden !== undefined) {
        route.hidden = Boolean(currMenu.isHidden);
      } else if ((currMenu as any).is_hidden !== undefined) {
        route.hidden = Boolean((currMenu as any).is_hidden);
      } else if ((currMenu as any).hidden !== undefined) {
        route.hidden = (currMenu as any).hidden !== 0;
      }
      if (currMenu.displayOrder != null) {
        route.displayOrder = currMenu.displayOrder;
      }
      return true;
    } else {
      route.displayOrder = 0;
      if (route.hidden === true) {
        route.displayOrder = -1;
        return true;
      }
      return false;
    }
  }
  return true;
}

function sortRouters(routers: RouteConfig[]) {
  routers.forEach((r) => {
    if (r.children && r.children.length > 0) {
      r.children.sort((a, b) => (b.displayOrder || 0) - (a.displayOrder || 0));
    }
  });
  routers.sort((a, b) => (b.displayOrder || 0) - (a.displayOrder || 0));
}

// Deep clone route config to avoid mutating the original
function deepCloneRoutes(routes: RouteConfig[]): RouteConfig[] {
  return routes.map((r) => ({
    ...r,
    meta: r.meta ? { ...r.meta } : undefined,
    children: r.children ? deepCloneRoutes(r.children) : undefined,
  }));
}

const permissionSlice = createSlice({
  name: 'permission',
  initialState,
  reducers: {
    generateRoutes(
      state,
      action: PayloadAction<{ menus: (UmsMenu | ConsoleMenu)[]; username: string }>,
    ) {
      const { menus, username } = action.payload;
      state.menus = menus;

      // Deep clone to avoid mutating the original config
      const clonedAsyncRoutes = deepCloneRoutes(asyncRouterMap);
      
      let accessedRouters;
      if (username === 'admin') {
        accessedRouters = clonedAsyncRoutes;
      } else {
        accessedRouters = clonedAsyncRoutes.filter((v) => {
          if (hasPermission(menus, v)) {
            if (v.children && v.children.length > 0) {
              v.children = v.children.filter((child) => hasPermission(menus, child));
              return true;
            }
            return true;
          }
          return false;
        });
      }

      sortRouters(accessedRouters);
      state.addRouters = accessedRouters;
      state.routers = [...constantRouterMap, ...accessedRouters];
    },
    setMenus(state, action: PayloadAction<ConsoleMenu[]>) {
      state.menus = action.payload.filter(
        (m) => !m.isHidden && !(m as any).is_hidden && (m as any).hidden !== 1
      );
    },
    clearPermissions(state) {
      state.addRouters = [];
      state.routers = [];
      state.menus = [];
    },
  },
});

export const { generateRoutes, setMenus, clearPermissions } = permissionSlice.actions;
export default permissionSlice.reducer;
