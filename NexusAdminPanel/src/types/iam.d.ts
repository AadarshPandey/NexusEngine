/**
 * IAM (Identity & Access Management) Bounded Context Type Definitions
 * Aligned with schema_redesign.md (iam schema) and BackendMigrationPlan.md
 */

export interface ConsoleMenu {
  id?: number
  parentId?: number
  title: string
  routeName?: string
  name?: string
  icon?: string
  depth?: number
  level?: number
  displayOrder?: number
  sort?: number
  isHidden?: boolean | number
  hidden?: number
  children?: ConsoleMenu[]
  createdAt?: string
}

export interface Role {
  id?: number
  name: string
  description?: string
  isActive?: boolean | number
  status?: number
  displayOrder?: number
  sort?: number
  adminCount?: number
  createdAt?: string
}

export interface Permission {
  id?: number
  name: string
  urlPattern?: string
  url?: string
  description?: string
  permissionGroupId?: number
  categoryId?: number
  createdAt?: string
}

export interface PermissionGroup {
  id?: number
  name: string
  displayOrder?: number
  sort?: number
  createdAt?: string
}

export interface AdminUser {
  id?: number
  username: string
  password?: string
  displayName?: string
  nickName?: string
  email?: string
  avatarUrl?: string
  icon?: string
  iconUrl?: string
  note?: string
  isEnabled?: boolean | number
  status?: number
  roles?: Role[]
  menus?: ConsoleMenu[]
  lastLoginAt?: string
  createdAt?: string
}

// Backward-compatible aliases
export type UmsAdmin = AdminUser
export type UmsRole = Role
export type UmsMenu = ConsoleMenu
export type UmsResource = Permission
export type UmsResourceCategory = PermissionGroup
