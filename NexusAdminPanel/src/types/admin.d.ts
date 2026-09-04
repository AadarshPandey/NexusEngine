import type { UmsMenu } from './menu'

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
  icon: string
  menus: UmsMenu[]
  roles: []
}

export type UserInfo = Pick<UserInfoResult, 'username' | 'menus' | 'roles'> & {
  password: string
  token: string
  avatar
}

export type UmsAdmin = {
  /** ID */
  id?: number
  username: string
  password: string
  icon?: string
  email?: string
  nickName?: string
  note?: string
  createTime?: string
  loginTime?: string
  status: number
}
