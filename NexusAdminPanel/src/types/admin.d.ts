import type { UmsMenu } from './menu'
import type { AdminUser, Role, ConsoleMenu, Permission, PermissionGroup } from './iam'

export type { AdminUser, Role, ConsoleMenu, Permission, PermissionGroup } from './iam'

export type LoginParam = {
  username: string
  password: string
}

export type LoginResult = {
  tokenHead: string
  token: string
}

export type UserInfoResult = {
  username: string
  icon?: string
  iconUrl?: string
  avatarUrl?: string
  menus: UmsMenu[]
  roles: []
}

export type UserInfo = Pick<UserInfoResult, 'username' | 'menus' | 'roles'> & {
  password?: string
  token: string
  avatar?: string
  avatarUrl?: string
}

export type UmsAdmin = {
  /** ID */
  id?: number
  username: string
  password?: string
  icon?: string
  iconUrl?: string
  avatarUrl?: string
  email?: string
  displayName?: string
  nickName?: string
  note?: string
  createdAt?: string
  createTime?: string
  lastLoginAt?: string
  loginTime?: string
  isEnabled?: boolean | number
  status: number
}
