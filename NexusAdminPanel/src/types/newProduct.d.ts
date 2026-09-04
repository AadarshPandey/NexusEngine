import type { PageParam } from './common'

export type SmsHomeNewProduct = {
  /** ID */
  id?: number
  productId: number
  productName: string
  recommendStatus?: number
  sort?: number
}

export type NewProductQueryParam = PageParam & {
  productName?: string
  recommendStatus?: number
}
