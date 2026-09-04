import { createSlice, type PayloadAction } from '@reduxjs/toolkit';
import type { UmsMenu } from '@/types/menu';
import { asyncRouterMap, constantRouterMap } from '@/router/routeConfig';

// Route definition for the React app (mirrors the Vue RouteRecordExt)
export interface RouteConfig {
  path: string;
  name?: string;
  hidden?: boolean;
  sort?: number;
  alwaysShow?: boolean;
  meta?: {
    title?: string;
    icon?: string;
  };
  children?: RouteConfig[];
}

interface PermissionState {
  addRouters: RouteConfig[];
  routers: RouteConfig[];
}

const initialState: PermissionState = {
  addRouters: [],
  routers: [],
};

// Check if a route has permission based on menus
function hasPermission(menus: UmsMenu[], route: RouteConfig): boolean {
  if (route.name) {
    const currMenu = menus.find((menu) => menu.name === route.name) || null;
    if (currMenu != null) {
      if (currMenu.title != null && currMenu.title !== '') {
        if (!route.meta) route.meta = {};
        route.meta.title = currMenu.title;
      }
      if (currMenu.icon != null && currMenu.icon !== '') {
        if (!route.meta) route.meta = {};
        route.meta.icon = currMenu.icon;
      }
      if (currMenu.hidden != null) {
        route.hidden = currMenu.hidden !== 0;
      }
      if (currMenu.sort != null) {
        route.sort = currMenu.sort;
      }
      return true;
    } else {
      route.sort = 0;
      if (route.hidden === true) {
        route.sort = -1;
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
      r.children.sort((a, b) => (b.sort || 0) - (a.sort || 0));
    }
  });
  routers.sort((a, b) => (b.sort || 0) - (a.sort || 0));
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
      action: PayloadAction<{ menus: UmsMenu[]; username: string }>,
    ) {
      const { menus, username } = action.payload;

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
    clearPermissions(state) {
      state.addRouters = [];
      state.routers = [];
    },
  },
});

export const { generateRoutes, clearPermissions } = permissionSlice.actions;
export default permissionSlice.reducer;
