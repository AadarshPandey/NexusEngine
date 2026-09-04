/** Route record extension for React Router */
export interface RouteRecordExt {
  path: string;
  name?: string;
  /** Hidden from sidebar navigation */
  hidden?: boolean;
  /** Sort order for menu display */
  sort?: number;
  /** Child routes */
  children?: RouteRecordExt[];
  /** Always show submenu even with single child */
  alwaysShow?: boolean;
  /** Route metadata */
  meta?: {
    title?: string;
    icon?: string;
  };
}
