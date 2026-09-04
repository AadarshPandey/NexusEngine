export type CommonResult<T> = {
  code: number
  message: string
  data: T
}

export type CommonPage<T> = {
  pageNum: number
  pageSize: number
  totalPage: number
  total: number
  list: T[]
}

export type PageParam = {
  pageNum: number
  pageSize: number
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
