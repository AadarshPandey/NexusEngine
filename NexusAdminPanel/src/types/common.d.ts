export interface PageResponse<T> {
  content: T[]
  totalElements: number
  totalPages: number
  size: number
  number: number
  first?: boolean
  last?: boolean
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
