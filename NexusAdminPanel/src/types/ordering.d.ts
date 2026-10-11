/**
 * Ordering Bounded Context Type Definitions
 * Aligned with schema_redesign.md (ordering schema) and BackendMigrationPlan.md
 */

import type { PageParam } from './common'

export interface CustomerOrder {
  id: number
  memberId?: number
  memberUsername?: string
  couponId?: number | null
  orderNumber: string // was orderSn
  currencyCode?: string
  subtotalAmount: number // was totalAmount
  shippingAmount: number // was freightAmount
  promotionDiscountAmount?: number // was promotionAmount
  couponDiscountAmount?: number // was couponAmount
  manualAdjustmentAmount?: number // was discountAmount
  grandTotalAmount: number // was payAmount
  paymentMethod: number // 0: UNSET, 1: RAZORPAY, 2: STRIPE
  status: number // 0: PENDING_PAYMENT, 1: AWAITING_SHIPMENT, 2: SHIPPED, 3: COMPLETED, 4: CANCELLED
  orderType: number // 0: STANDARD, 1: FLASH_SALE
  carrierName?: string // was deliveryCompany
  trackingNumber?: string // was deliverySn
  recipientName?: string // was receiverName
  recipientPhone?: string // was receiverPhone
  shippingPostalCode?: string // was receiverPostCode
  shippingAddressLine?: string // was receiverDetailAddress
  shippingCity?: string
  shippingState?: string
  shippingCountryCode?: string
  customerNote?: string // was note
  isReceiptConfirmed?: boolean | number
  paidAt?: string
  shippedAt?: string
  deliveredAt?: string
  reviewedAt?: string
  createdAt: string
  updatedAt?: string
  orderLines?: OrderLine[]
  orderItemList?: OrderLine[]
  historyList?: OrderStatusHistory[]
  deletedAt?: string | number | null
}

export interface OrderLine {
  id: number
  orderId: number
  productId?: number
  orderLineId?: number
  skuId?: number
  skuCode?: string
  productName: string
  productImageUrl: string
  brandName?: string
  categoryId?: number
  skuAttributes?: string | Record<string, any>
  unitPrice: number
  quantity: number
  promotionDiscountAmount?: number
  couponDiscountAmount?: number
  lineTotalAmount: number
  giftIntegration?: number
  giftGrowth?: number
}

export interface OrderStatusHistory {
  id?: number
  orderId: number
  changedByAdminId?: string
  actorRef?: string
  actorType?: number
  fromStatus?: number
  toStatus: number
  note?: string
  createdAt: string
}

export interface ReturnRequest {
  id: number
  orderId: number
  orderLineId?: number
  memberId?: number
  returnAddressId?: number
  quantity: number
  unitRefundAmount?: number
  refundAmount: number
  contactName?: string
  contactPhone?: string
  reasonId?: number
  customerComment?: string
  evidenceUrls?: string[] | string
  status: number // 0: PENDING, 1: APPROVED, 2: COMPLETED, 3: REJECTED
  reviewNote?: string
  reviewedByAdminId?: number | string
  reviewedAt?: string
  receivedByAdminId?: number | string
  receivedAt?: string
  receiptNote?: string
  createdAt: string
}

export interface ReturnReason {
  id: number
  label: string
  displayOrder?: number
  isActive: boolean | number
  createdAt?: string
}

export interface MerchantAddress {
  id: number
  label: string
  isDefaultShipFrom?: boolean | number
  isDefaultReturnTo?: boolean | number
  contactName: string
  contactPhone: string
  state?: string
  city?: string
  addressLine: string
  postalCode?: string
  countryCode?: string
}

export interface OrderPolicy {
  id: number
  flashPaymentTimeoutMin: number
  paymentTimeoutMin: number
  autoConfirmReceiptDays: number
  autoCompleteDays: number
  reviewWindowDays: number
}

export type OrderQueryParams = PageParam & {
  orderNumber?: string
  recipientKeyword?: string
  receiverKeyword?: string
  status?: number
  orderType?: number
  createdAt?: string
}

// Backward-compatible aliases
export type OmsOrder = CustomerOrder
export type OmsOrderItem = OrderLine
export type OmsOrderOperateHistory = OrderStatusHistory
export type OmsOrderDetail = CustomerOrder
export type OmsOrderReturnApply = ReturnRequest
export type OmsOrderReturnReason = ReturnReason
export type OmsCompanyAddress = MerchantAddress
export type OmsOrderSetting = OrderPolicy
