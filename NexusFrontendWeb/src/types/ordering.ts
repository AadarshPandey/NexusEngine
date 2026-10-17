/**
 * Ordering Bounded Context Types for Customer Storefront
 * Aligned with ADR 0003 and modular monolith domain model
 */

export interface CustomerOrder {
  id: number
  memberId: number
  couponId?: number | null
  orderNumber: string
  currencyCode?: string
  subtotalAmount: number
  shippingAmount: number
  promotionDiscountAmount?: number
  couponDiscountAmount?: number
  manualAdjustmentAmount?: number
  grandTotalAmount: number
  paymentMethod: number // 0: UNSET, 1: RAZORPAY, 2: STRIPE
  status: number // 0: PENDING_PAYMENT, 1: AWAITING_SHIPMENT, 2: SHIPPED, 3: COMPLETED, 4: CANCELLED
  orderType: number // 0: STANDARD, 1: FLASH_SALE
  carrierName?: string
  trackingNumber?: string
  recipientName: string
  recipientPhone: string
  shippingPostalCode?: string
  shippingAddressLine?: string
  shippingCity?: string
  shippingState?: string
  shippingCountryCode?: string
  customerNote?: string
  isReceiptConfirmed?: boolean
  paidAt?: string
  shippedAt?: string
  deliveredAt?: string
  reviewedAt?: string
  createdAt: string
  updatedAt?: string
  orderLines?: OrderLine[]
  returnApplyList?: ReturnRequest[]
}

export interface OrderLine {
  id: number
  orderId: number
  productId: number
  skuId: number
  skuCode: string
  productName: string
  productImageUrl: string
  brandName?: string
  categoryId?: number
  skuAttributes?: Record<string, any>
  unitPrice: number
  quantity: number
  promotionDiscountAmount?: number
  couponDiscountAmount?: number
  lineTotalAmount: number
}

export interface CartItemRecord {
  id: number
  memberId: number
  productId: number
  skuId: number
  quantity: number
  unitPriceAtAdd: number
  createdAt: string
  updatedAt: string
}

export interface EnrichedCartItem {
  id: number
  productId: number
  skuId: number
  quantity: number
  unitPriceAtAdd?: number
  name: string
  productCode?: string
  primaryImageUrl: string
  brandName?: string
  skuCode?: string
  currentListPrice: number
  currentSalePrice?: number
  price: number
  availableQty: number
  skuAttributes?: Record<string, any>
  hasPriceChanged?: boolean
  isAvailable?: boolean
  productCategoryId?: number
}

export interface ReturnRequest {
  id: number
  orderId: number
  orderLineId?: number
  memberId?: number
  quantity: number
  productId?: number
  unitRefundAmount?: number
  contactName?: string
  contactPhone?: string
  reason?: string
  reasonId?: number
  customerComment?: string
  evidenceUrls?: string[]
  status: number // 0: PENDING, 1: APPROVED, 2: COMPLETED, 3: REJECTED
  reviewNote?: string
  handleNote?: string
  createdAt?: string
}

export const PaymentMethod = {
  UNSET: 0,
  RAZORPAY: 1,
  STRIPE: 2,
} as const;
export type PaymentMethod = (typeof PaymentMethod)[keyof typeof PaymentMethod];

export const OrderStatus = {
  PENDING_PAYMENT: 0,
  AWAITING_SHIPMENT: 1,
  SHIPPED: 2,
  COMPLETED: 3,
  CANCELLED: 4,
} as const;
export type OrderStatus = (typeof OrderStatus)[keyof typeof OrderStatus];

export type Order = CustomerOrder
export type OrderItem = OrderLine
export type CartItem = EnrichedCartItem
