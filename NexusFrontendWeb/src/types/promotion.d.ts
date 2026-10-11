/**
 * Promotion Bounded Context Types for Customer Storefront
 * Aligned with ADR 0003 and modular monolith domain model
 */

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
  // Legacy aliases
  type?: number
  amount?: number
  startTime?: string
  endTime?: string
  useStatus?: number
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
  // Legacy aliases
  flashPromotionPrice?: number
  flashPromotionCount?: number
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
  // Legacy aliases
  name?: string
  type?: number
  pic?: string
  url?: string
}

export type PromotionalBanner = Banner;
