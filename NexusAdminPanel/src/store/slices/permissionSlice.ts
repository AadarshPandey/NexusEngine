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

// Check if a route has permission based on menus
function hasPermission(menus: (UmsMenu | ConsoleMenu)[], route: RouteConfig): boolean {
  if (route.name) {
    const currMenu =
      menus.find((menu) => (menu.routeName || menu.name) === route.name) || null;

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
      state.menus = action.payload.filter((m) => !m.isHidden);
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
