import type { UmsMenu } from './menu'

export type LoginParam = {
  username: string
  passwordHash: string
}

export type LoginResult = {
  tokenHead: string
  token: string
}

export type UserInfoResult = {
  username: string
  iconUrl: string
  menus: UmsMenu[]
  roles: []
}

export type UserInfo = Pick<UserInfoResult, 'username' | 'menus' | 'roles'> & {
  passwordHash?: string
  token: string
  avatar
}

export type UmsAdmin = {
  /** ID */
  id?: number
  username: string
  passwordHash: string
  iconUrl?: string
  email?: string
  nickName?: string
  note?: string
  createdAt?: string
  lastLoginAt?: string
  status: number
}
