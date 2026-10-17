import { createSlice, type PayloadAction } from '@reduxjs/toolkit';
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
}

const initialState: PermissionState = {
  addRouters: [],
  routers: [],
};

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
      action: PayloadAction<{ roles?: string[]; username: string; menus?: any[] }>,
    ) {
      const { roles, username } = action.payload;
      const isSuperAdmin = username === 'admin' || (roles && roles.some((r) => r.toLowerCase().includes('super') || r.toLowerCase().includes('admin')));

      const clonedAsyncRoutes = deepCloneRoutes(asyncRouterMap);

      const accessedRouters = clonedAsyncRoutes.filter((route) => {
        // Non-super-admins cannot access IAM administration
        if (route.path === '/iam' && !isSuperAdmin) {
          return false;
        }
        return true;
      });

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
