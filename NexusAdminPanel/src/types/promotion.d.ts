/**
 * Promotion Bounded Context Type Definitions
 * Aligned with schema_redesign.md (promotion schema) and BackendMigrationPlan.md
 */

import type { PageParam } from './common'

export interface Coupon {
  id?: number
  discountType: number // 0: FIXED_AMOUNT, 1: PERCENTAGE, 2: FREE_SHIPPING
  type?: number
  name: string
  code?: string
  discountValue: number // was amount
  amount?: number
  maxDiscountAmount?: number
  minSpendAmount?: number // was minPoint / minOrderAmount
  minOrderAmount?: number
  remainingQuantity?: number // was count
  count?: number
  claimedCount?: number // was receiveCount
  receiveCount?: number
  perMemberLimit?: number // was perLimit
  perLimit?: number
  applicabilityScope?: number // was useType
  useType?: number
  startsAt?: string // was startTime
  startTime?: string
  endsAt?: string // was endTime
  endTime?: string
  isStackable?: boolean
  isPublic?: boolean
  status?: number
}

export interface MemberCoupon {
  id?: number
  couponId?: number
  memberId?: number
  couponCode?: string
  memberNickname?: string
  acquisitionChannel?: number
  getType?: number
  status?: number
  useStatus?: number
  claimedAt?: string
  createTime?: string
  redeemedAt?: string
  orderId?: number
  orderNumber?: string
  orderSn?: string
}

export interface FlashSale {
  id?: number
  title: string
  startsOn: string // was startDate
  startDate?: string
  endsOn: string // was endDate
  endDate?: string
  isActive?: boolean | number
  status?: number
  createdAt?: string
}

export interface FlashSaleSlot {
  id?: number
  label: string // was name
  name?: string
  startsAtLocal?: string // was startTime
  startTime?: string
  endsAtLocal?: string // was endTime
  endTime?: string
  isActive?: boolean | number
  status?: number
  createdAt?: string
}

export interface FlashSaleItem {
  id?: number
  flashSaleId: number
  flashPromotionId?: number
  slotId: number
  flashPromotionSessionId?: number
  productId: number
  skuId?: number
  flashPrice: number
  flashPromotionPrice?: number
  quantityAvailable: number
  flashPromotionCount?: number
  perMemberLimit: number
  flashPromotionLimit?: number
  displayOrder?: number
  sort?: number
}

export interface Banner {
  id?: number
  title?: string
  name?: string
  placement?: number
  type?: number
  imageUrl?: string
  pic?: string
  targetUrl?: string
  url?: string
  startsAt?: string
  startTime?: string
  endsAt?: string
  endTime?: string
  isActive?: boolean | number
  status?: number
  clickCount?: number
  orderCount?: number
  internalNote?: string
  note?: string
  displayOrder?: number
  sort?: number
}

// Backward-compatible aliases
export type SmsCoupon = Coupon
export type SmsCouponHistory = MemberCoupon
export type SmsFlashPromotion = FlashSale
export type SmsFlashPromotionSession = FlashSaleSlot
export type SmsFlashPromotionProductRelation = FlashSaleItem
export type HomeAdvertise = Banner
