export type CommonResult<T> = {
  code?: number
  message?: string
  data: T
}

export interface PageResponse<T> {
  content: T[]
  totalElements: number
  totalPages: number
  size: number
  number: number
  first?: boolean
  last?: boolean
}

export type CommonPage<T> = {
  pageNum?: number
  pageSize?: number
  totalPage?: number
  total?: number
  list?: T[]
  // Support modern Spring Data PageResponse fields
  content?: T[]
  totalElements?: number
  totalPages?: number
  size?: number
  number?: number
}

export type PageParam = {
  pageNum?: number
  pageSize?: number
  page?: number
  size?: number
  keyword?: string
}

export type ElCascaderDataVo = {
  label: string
  value: number
  children?: ElCascaderDataVo[]
}

export type ElSelectDataVo = {
  label: string
  value: string | number
}

export type ElTransferDataVo = {
  key?: number
  label?: string
  disabled?: boolean
}
