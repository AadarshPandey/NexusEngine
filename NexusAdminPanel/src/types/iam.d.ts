/**
 * IAM (Identity & Access Management) Bounded Context Type Definitions
 * Aligned with schema_redesign.md (iam schema) and BackendMigrationPlan.md
 */

import type { PageParam } from './common'

export interface ConsoleMenu {
  id?: number
  parentId?: number
  parentReviewId?: number
  title: string
  routeName?: string
  name?: string
  icon?: string
  iconUrl?: string
  depth?: number
  level?: number
  displayOrder?: number
  sort?: number
  isHidden?: boolean | number
  hidden?: boolean | number
  children?: ConsoleMenu[]
  createdAt?: string
  createTime?: string
}

export interface ConsoleMenuNode extends ConsoleMenu {
  parentReviewId?: number
  children?: ConsoleMenuNode[]
}

export interface Role {
  id?: number
  name: string
  description?: string
  customerComment?: string
  isActive?: boolean | number
  status?: boolean | number
  displayOrder?: number
  sort?: number
  adminCount?: number
  createdAt?: string
  createTime?: string
}

export interface Permission {
  id?: number
  name: string
  urlPattern?: string
  url?: string
  description?: string
  customerComment?: string
  permissionGroupId?: number
  categoryId?: number
  createdAt?: string
  createTime?: string
}

export interface PermissionGroup {
  id?: number
  name: string
  displayOrder?: number
  sort?: number
  createdAt?: string
  createTime?: string
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
  status?: boolean | number
  roles?: Role[]
  menus?: ConsoleMenu[]
  lastLoginAt?: string
  loginTime?: string
  createdAt?: string
  createTime?: string
}

export interface LoginParam {
  username: string
  password?: string
}

export interface TokenResponse {
  token: string
  tokenHead?: string
}

export interface AdminInfoResponse {
  username: string
  menus: ConsoleMenu[]
  icon?: string
  iconUrl?: string
  avatarUrl?: string
  roles: string[]
}

export interface UserInfo {
  token: string
  username: string
  roles: string[]
  menus: ConsoleMenu[]
  icon?: string
  avatar?: string
  avatarUrl?: string
  displayName?: string
}

export interface AdminUserParam {
  id?: number
  username: string
  password?: string
  displayName?: string
  nickName?: string
  email?: string
  avatarUrl?: string
  icon?: string
  note?: string
  isEnabled?: boolean | number
  status?: boolean | number
}

export type ResourceQueryParam = PageParam & {
  nameKeyword?: string
  urlFilter?: string
  urlKeyword?: string
  categoryId?: number
  permissionGroupId?: number
}

// Backward-compatible aliases
export type UmsAdmin = AdminUser
export type UmsRole = Role
export type UmsMenu = ConsoleMenu
export type UmsMenuNode = ConsoleMenuNode
export type UmsResource = Permission
export type UmsResourceCategory = PermissionGroup
export type LoginResult = TokenResponse
export type UserInfoResult = AdminInfoResponse
