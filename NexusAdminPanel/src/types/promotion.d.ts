/**
 * Promotion Bounded Context Type Definitions
 * Aligned with schema_redesign.md (promotion schema) and BackendMigrationPlan.md
 */

import type { PageParam } from './common'

export interface Coupon {
  id?: number
  discountType?: number // 0: FIXED_AMOUNT, 1: PERCENTAGE, 2: FREE_SHIPPING
  type?: number
  name: string
  code?: string
  discountValue?: number // was amount
  amount?: number
  maxDiscountAmount?: number
  minSpendAmount?: number // was minPoint / minOrderAmount
  minPoint?: number
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
  useCount?: number
  publishCount?: number
  memberLevel?: number
  note?: string
}

export interface CouponParam extends Coupon {
  productRelationList?: Array<{ productId: number; productSn?: string; productName?: string }>
  productCategoryRelationList?: Array<{ productCategoryId: number; parentCategoryName?: string; productCategoryName?: string }>
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
  useTime?: string
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
  createTime?: string
}

export interface FlashSaleSlot {
  id?: number
  label: string // was name
  name?: string
  startsAtLocal?: string // was startTime
  startTime?: string
  startsAt?: string
  endsAtLocal?: string // was endTime
  endTime?: string
  endsAt?: string
  isActive?: boolean | number
  status?: number
  createdAt?: string
  createTime?: string
}

export interface FlashSaleItem {
  id?: number
  flashSaleId: number
  flashPromotionId?: number
  slotId: number
  flashPromotionSessionId?: number
  productId: number
  orderLineId?: number
  skuId?: number
  flashPrice: number
  flashPromotionPrice?: number
  quantityAvailable: number
  flashPromotionCount?: number
  perMemberLimit: number
  flashPromotionLimit?: number
  displayOrder?: number
  sort?: number
  product?: any
}

export interface Banner {
  id?: number
  title?: string
  name?: string
  placement?: number
  type?: number
  imageUrl?: string
  primaryImageUrl?: string
  pic?: string
  targetUrl?: string
  url?: string
  startsAt?: string
  startTime?: string
  endsAt?: string
  endTime?: string
  isActive?: boolean | number
  status?: boolean | number
  clickCount?: number
  orderCount?: number
  internalNote?: string
  note?: string
  displayOrder?: number
  sort?: number
}

export type CouponQueryParams = PageParam & {
  name?: string
  type?: number
}

export type HomeAdvertiseQueryParam = PageParam & {
  name?: string
  title?: string
  type?: number
  endTime?: string
  endsAt?: string
}

export type FlashProductQueryParam = PageParam & {
  flashSaleId?: number
  flashPromotionId?: number
  slotId?: number
  flashPromotionSessionId?: number
}

// Home recommendations types
export interface RecommendProduct {
  id?: number
  productId?: number
  productName?: string
  displayOrder?: number
  sort?: number
  recommendStatus?: number
  isFeatured?: boolean | number
}

export type RecommendProductQueryParam = PageParam & {
  productName?: string
  recommendStatus?: number
  isFeatured?: boolean | number
}

export interface HomeBrandItem {
  id?: number
  brandId?: number
  brandName?: string
  displayOrder?: number
  sort?: number
  recommendStatus?: number
  isFeatured?: boolean | number
}

export type HomeBrandQueryParam = PageParam & {
  brandName?: string
  recommendStatus?: number
  isFeatured?: boolean | number
}

export interface HomeNewItem {
  id?: number
  productId?: number
  productName?: string
  displayOrder?: number
  sort?: number
  recommendStatus?: number
  isFeatured?: boolean | number
}

export type NewProductQueryParam = PageParam & {
  productName?: string
  recommendStatus?: number
  isFeatured?: boolean | number
}

export interface HomeSubjectItem {
  id?: number
  subjectId?: number
  subjectName?: string
  displayOrder?: number
  sort?: number
  recommendStatus?: number
  isFeatured?: boolean | number
}

export type HomeSubjectQueryParam = PageParam & {
  subjectName?: string
  recommendStatus?: number
  isFeatured?: boolean | number
}

export interface Subject {
  id?: number
  title?: string
  categoryName?: string
  pic?: string
  productCount?: number
  recommendStatus?: number
  description?: string
  showStatus?: number
  content?: string
}

export interface PrefrenceArea {
  id?: number
  name?: string
  subTitle?: string
  pic?: string
  sort?: number
  showStatus?: number
}

// Backward-compatible aliases
export type SmsCoupon = Coupon
export type SmsCouponParam = CouponParam
export type SmsCouponExt = CouponParam
export type SmsCouponHistory = MemberCoupon
export type SmsFlashPromotion = FlashSale
export type SmsFlashPromotionSession = FlashSaleSlot
export type SmsFlashPromotionProductRelation = FlashSaleItem
export type HomeAdvertise = Banner
export type SmsHomeAdvertise = Banner
export type SmsHomeRecommendProduct = RecommendProduct
export type SmsHomeBrand = HomeBrandItem
export type SmsHomeNewProduct = HomeNewItem
export type SmsHomeRecommendSubject = HomeSubjectItem
export type CmsSubject = Subject
export type CmsPrefrenceArea = PrefrenceArea
export type CouponQueryParam = CouponQueryParams
export type CouponHistoryQueryParam = PageParam & { couponId?: number; useStatus?: number; orderSn?: string }

