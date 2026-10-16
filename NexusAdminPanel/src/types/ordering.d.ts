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
  orderSn?: string
  currencyCode?: string
  subtotalAmount: number // was totalAmount
  totalAmount?: number
  shippingAmount: number // was freightAmount
  freightAmount?: number
  promotionDiscountAmount?: number // was promotionAmount
  promotionAmount?: number
  couponDiscountAmount?: number // was couponAmount
  couponAmount?: number
  manualAdjustmentAmount?: number // was discountAmount
  discountAmount?: number
  grandTotalAmount: number // was payAmount
  payAmount?: number
  paymentMethod: number // 0: UNSET, 1: RAZORPAY, 2: STRIPE
  payType?: number
  status: number // 0: PENDING_PAYMENT, 1: AWAITING_SHIPMENT, 2: SHIPPED, 3: COMPLETED, 4: CANCELLED
  orderType: number // 0: STANDARD, 1: FLASH_SALE
  carrierName?: string // was deliveryCompany
  deliveryCompany?: string
  trackingNumber?: string // was deliverySn
  deliverySn?: string
  recipientName?: string // was receiverName
  receiverName?: string
  recipientPhone?: string // was receiverPhone
  receiverPhone?: string
  shippingPostalCode?: string // was receiverPostCode
  receiverPostCode?: string
  shippingAddressLine?: string // was receiverDetailAddress
  receiverDetailAddress?: string
  shippingCity?: string
  receiverCity?: string
  shippingState?: string
  receiverProvince?: string
  shippingCountryCode?: string
  customerNote?: string // was note
  note?: string
  isReceiptConfirmed?: boolean | number
  confirmStatus?: number
  paidAt?: string
  paymentTime?: string
  shippedAt?: string
  deliveryTime?: string
  deliveredAt?: string
  receiveTime?: string
  reviewedAt?: string
  commentTime?: string
  createdAt: string
  createTime?: string
  updatedAt?: string
  modifyTime?: string
  orderLines?: OrderLine[]
  orderItemList?: OrderLine[]
  historyList?: OrderStatusHistory[]
  deletedAt?: string | number | null
  deleteStatus?: number
}

export interface OrderLine {
  id: number
  orderId: number
  productId?: number
  orderLineId?: number
  skuId?: number
  productSkuId?: number
  skuCode?: string
  productSkuCode?: string
  productName: string
  productImageUrl: string
  productPic?: string
  brandName?: string
  productBrand?: string
  categoryId?: number
  productCategoryId?: number
  skuAttributes?: string | Record<string, any>
  spData?: string
  unitPrice: number
  productPrice?: number
  quantity: number
  productQuantity?: number
  promotionDiscountAmount?: number
  couponDiscountAmount?: number
  lineTotalAmount: number
  realAmount?: number
}

export interface OrderStatusHistory {
  id?: number
  orderId: number
  changedByAdminId?: string
  operateMan?: string
  actorRef?: string
  actorType?: number
  fromStatus?: number
  toStatus: number
  orderStatus?: number
  note?: string
  createdAt: string
  createTime?: string
}

export interface ReturnRequest {
  id: number
  orderId: number
  orderNumber?: string
  orderSn?: string
  orderLineId?: number
  productId?: number
  productName?: string
  productImageUrl?: string
  productPic?: string
  brandName?: string
  productBrand?: string
  skuAttributes?: string | Record<string, any>
  productAttr?: string
  productCount?: number
  productPrice?: number
  productRealPrice?: number
  memberId?: number
  memberUsername?: string
  returnAddressId?: number
  companyAddressId?: number
  quantity: number
  unitRefundAmount?: number
  refundAmount: number
  returnAmount?: number
  contactName?: string
  returnName?: string
  contactPhone?: string
  returnPhone?: string
  reasonId?: number
  reason?: string
  customerComment?: string
  description?: string
  evidenceUrls?: string[] | string
  proofPics?: string
  status: number // 0: PENDING, 1: APPROVED, 2: COMPLETED, 3: REJECTED
  reviewNote?: string
  handleNote?: string
  reviewedByAdminId?: number | string
  handleMan?: string
  reviewedAt?: string
  handleTime?: string
  receivedByAdminId?: number | string
  receiveMan?: string
  receivedAt?: string
  receiveTime?: string
  receiptNote?: string
  receiveNote?: string
  createdAt: string
  createTime?: string
}

export interface ReturnReason {
  id?: number
  name?: string
  label?: string
  displayOrder?: number
  sort?: number
  isActive?: boolean | number
  status?: number
  createdAt?: string
  createTime?: string
}

export interface MerchantAddress {
  id: number
  addressName?: string
  label: string
  isDefaultShipFrom?: boolean | number
  sendStatus?: number
  isDefaultReturnTo?: boolean | number
  receiveStatus?: number
  contactName: string
  name?: string
  contactPhone: string
  phone?: string
  state?: string
  province?: string
  city?: string
  region?: string
  addressLine: string
  detailAddress?: string
  postalCode?: string
  postCode?: string
  countryCode?: string
}

export interface OrderPolicy {
  id: number
  flashPaymentTimeoutMin: number
  flashOrderOvertime?: number
  paymentTimeoutMin: number
  normalOrderOvertime?: number
  autoConfirmReceiptDays: number
  confirmOvertime?: number
  autoCompleteDays: number
  finishOvertime?: number
  reviewWindowDays: number
  commentOvertime?: number
}

export type OrderQueryParams = PageParam & {
  orderNumber?: string
  orderSn?: string
  recipientKeyword?: string
  receiverKeyword?: string
  status?: number
  orderType?: number
  createdAt?: string
  createTime?: string
}

export interface OrderDeliveryParam {
  orderId: number
  carrierName?: string
  deliveryCompany?: string
  trackingNumber?: string
  deliverySn?: string
}

export interface OrderReceiverInfoParam {
  orderId: number
  recipientName?: string
  receiverName?: string
  recipientPhone?: string
  receiverPhone?: string
  shippingPostalCode?: string
  receiverPostCode?: string
  shippingAddressLine?: string
  receiverDetailAddress?: string
  shippingCity?: string
  receiverCity?: string
  shippingState?: string
  receiverProvince?: string
  status?: number
}

export interface OrderMoneyInfoParam {
  orderId: number
  shippingAmount?: number
  freightAmount?: number
  manualAdjustmentAmount?: number
  discountAmount?: number
  status?: number
}

export interface ReturnApplyStatusParam {
  id: number
  returnAddressId?: number
  companyAddressId?: number
  unitRefundAmount?: number
  refundAmount?: number
  returnAmount?: number
  reviewNote?: string
  handleNote?: string
  reviewedByAdminId?: string | number
  handleMan?: string
  receivedByAdminId?: string | number
  receiveMan?: string
  receiptNote?: string
  receiveNote?: string
  status: number
}

// Backward-compatible aliases
export type OmsOrder = CustomerOrder
export type OmsOrderItem = OrderLine
export type OmsOrderOperateHistory = OrderStatusHistory
export type OmsOrderDetail = CustomerOrder
export type OmsOrderReturnApply = ReturnRequest
export type OmsOrderReturnApplyResult = ReturnRequest
export type OmsOrderReturnReason = ReturnReason
export type OmsCompanyAddress = MerchantAddress
export type OmsOrderSetting = OrderPolicy
export type OrderSetting = OrderPolicy
export type OrderQueryParam = OrderQueryParams
export type OmsMoneyInfoParam = OrderMoneyInfoParam
export type OmsOrderDeliveryParam = OrderDeliveryParam
export type OmsReceiverInfoParam = OrderReceiverInfoParam
export type OmsUpdateStatusParam = ReturnApplyStatusParam
export type ReturnApplyQueryParam = PageParam & { id?: number; orderSn?: string; status?: number; handleTime?: string }

