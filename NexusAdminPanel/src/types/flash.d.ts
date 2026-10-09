import type { PageParam } from './common'
import type { PmsProduct } from './product'

export type SmsFlashPromotion = {
  /** ID */
  id?: number
  title: string
  startsOn?: string
  endsOn?: string
  status: number
  createdAt?: string
}

export type SmsFlashPromotionSession = {
  id?: number
  name: string
  startsAt?: string
  endsAt?: string
  status: number
  createdAt?: string
  quantity?: number
}

export type SmsFlashPromotionProductRelation = {
  id?: number
  flashSaleId: number
  slotId: number
  orderLineId: number
  flashPrice?: number
  quantityAvailable?: number
  perMemberLimit?: number
  product?: PmsProduct
}

export type FlashProductQueryParam = PageParam & {
  flashSaleId?: number
  slotId?: number
}
