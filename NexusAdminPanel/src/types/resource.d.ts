import type { PageParam } from './common'

export type UmsResource = {
  /** ID */
  id?: number
  createdAt?: string
  name: string
  url: string
  customerComment?: string
  permissionGroupId: number
  checked?: boolean
}

export type UmsResourceCategory = {
  /** ID */
  id?: number
  createdAt?: string
  name: string
  displayOrder: number
  checked?: boolean
}

export type ResourceQueryParam = PageParam & {
  nameKeyword?: string
  urlKeyword?: string
  permissionGroupId?: number
}
