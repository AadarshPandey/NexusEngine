import type { PageParam } from './common'

export type UmsResource = {
  /** ID */
  id?: number
  createTime?: string
  name: string
  url: string
  description?: string
  categoryId: number
  checked?: boolean
}

export type UmsResourceCategory = {
  /** ID */
  id?: number
  createTime?: string
  name: string
  sort: number
  checked?: boolean
}

export type ResourceQueryParam = PageParam & {
  nameKeyword?: string
  urlKeyword?: string
  categoryId?: number
}
