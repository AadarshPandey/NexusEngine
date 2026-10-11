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
  // Legacy aliases
  orderSn?: string
  totalAmount?: number
  payAmount?: number
  freightAmount?: number
  promotionAmount?: number
  couponAmount?: number
  discountAmount?: number
  orderItemList?: OrderLine[]
  returnApplyList?: ReturnRequest[]
  createTime?: string
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
  // Legacy aliases
  productPic?: string
  productQuantity?: number
  productPrice?: number
  realAmount?: number
  productBrand?: string
  productAttr?: string
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
  // Legacy aliases
  productPic?: string
  productName?: string
  productSkuCode?: string
  productAttr?: string
}

export interface ReturnRequest {
  id: number
  orderId: number
  orderLineId?: number
  memberId?: number
  quantity: number
  productCount?: number
  productId?: number
  unitRefundAmount?: number
  refundAmount?: number
  returnAmount?: number
  contactName?: string
  contactPhone?: string
  reason?: string
  reasonId?: number
  customerComment?: string
  description?: string
  evidenceUrls?: string[]
  proofPics?: string
  status: number // 0: PENDING, 1: APPROVED, 2: COMPLETED, 3: REJECTED
  reviewNote?: string
  handleNote?: string
  createdAt?: string
  createTime?: string
}

export type Order = CustomerOrder
export type OrderItem = OrderLine
export type CartItem = EnrichedCartItem
