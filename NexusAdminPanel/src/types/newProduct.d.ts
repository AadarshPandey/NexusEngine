import type { PageParam } from './common'

export type SmsHomeNewProduct = {
  /** ID */
  id?: number
  orderLineId: number
  productName: string
  isFeatured?: number
  displayOrder?: number
}

export type NewProductQueryParam = PageParam & {
  productName?: string
  isFeatured?: number
}
