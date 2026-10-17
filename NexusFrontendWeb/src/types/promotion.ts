/**
 * Promotion Bounded Context Types for Customer Storefront
 * Aligned with ADR 0003 and modular monolith domain model
 */

export const CouponDiscountType = {
  FIXED_AMOUNT: 0,
  PERCENTAGE: 1,
  FREE_SHIPPING: 2,
} as const;
export type CouponDiscountType = (typeof CouponDiscountType)[keyof typeof CouponDiscountType];

export const CouponApplicabilityScope = {
  ALL: 0,
  CATEGORY: 1,
  PRODUCT: 2,
} as const;
export type CouponApplicabilityScope = (typeof CouponApplicabilityScope)[keyof typeof CouponApplicabilityScope];

export interface Coupon {
  id: number
  discountType: 0 | 1 | 2 // 0: FIXED_AMOUNT, 1: PERCENTAGE, 2: FREE_SHIPPING
  name: string
  code?: string
  discountValue: number
  maxDiscountAmount?: number
  minSpendAmount: number
  remainingQuantity: number
  claimedCount: number
  perMemberLimit: number
  applicabilityScope: 0 | 1 | 2
  startsAt: string
  endsAt: string
  isStackable?: boolean
  isPublic?: boolean
  status?: number
}

export interface FlashSale {
  id: number
  title: string
  startsOn: string
  endsOn: string
  isActive: boolean
  createdAt?: string
}

export interface FlashSaleSlot {
  id: number
  label: string
  startsAtLocal: string
  endsAtLocal: string
  isActive: boolean
}

export interface FlashSaleItem {
  id: number
  flashSaleId: number
  slotId: number
  productId: number
  skuId?: number
  flashPrice: number
  quantityAvailable: number
  perMemberLimit: number
}

export interface Banner {
  id: number
  title: string
  placement: number
  primaryImageUrl: string
  imageUrl?: string
  targetUrl: string
  startsAt?: string
  endsAt?: string
  isActive?: boolean
  clickCount?: number
  orderCount?: number
}

export type PromotionalBanner = Banner;
