import type { PageParam } from './common'
import type { PmsProduct } from './product'

export type SmsFlashPromotion = {
  /** ID */
  id?: number
  title: string
  startDate?: string
  endDate?: string
  status: number
  createTime?: string
}

export type SmsFlashPromotionSession = {
  id?: number
  name: string
  startTime?: string
  endTime?: string
  status: number
  createTime?: string
  productCount?: number
}

export type SmsFlashPromotionProductRelation = {
  id?: number
  flashPromotionId: number
  flashPromotionSessionId: number
  productId: number
  flashPromotionPrice?: number
  flashPromotionCount?: number
  flashPromotionLimit?: number
  sort?: number
  product?: PmsProduct
}

export type FlashProductQueryParam = PageParam & {
  flashPromotionId?: number
  flashPromotionSessionId?: number
}
