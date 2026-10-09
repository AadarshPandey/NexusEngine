import type { PageParam } from './common'

export type OmsOrder = {
  id: number
  memberId?: number
  memberUsername?: string
  couponId?: number
  orderNumber?: string
  createdAt: string
  subtotalAmount: number
  grandTotalAmount: number
  shippingAmount: number
  promotionDiscountAmount?: number
  couponDiscountAmount?: number
  manualAdjustmentAmount: number
  paymentMethod: number
  status: number
  orderType: number
  carrierName?: string
  trackingNumber?: string
  recipientName?: string
  recipientPhone?: string
  shippingPostalCode?: string
  shippingAddressLine?: string
  note?: string
  isReceiptConfirmed?: number
  deletedAt?: number
  useIntegration?: number
  paidAt: string
  shippedAt: string
  deliveredAt: string
  reviewedAt: string
  updatedAt?: string
}

export type OmsOrderItem = {
  id: number
  orderId: number
  orderLineId: number
  productImageUrl: string
  productName: string
  brandName: string
  unitPrice: number
  quantity: number
  skuId: number
  permissionGroupId: number
  promotionName: string
  promotionDiscountAmount: number
  couponDiscountAmount: number
  lineTotalAmount: number
  giftIntegration: number
  giftGrowth: number
  skuAttributes: string
}

export type OmsOrderOperateHistory = {
  id: number
  orderId: number
  changedByAdminId: string
  createdAt: string
  toStatus: number
  note: string
}

export type OrderQueryParam = PageParam & {
  orderNumber?: string
  receiverKeyword?: string
  status?: number
  orderType?: number
  createdAt?: string
}

export type OmsOrderDeliveryParam = {
  orderId: number
  carrierName?: string
  trackingNumber?: string
}

export type OmsOrderDetail = OmsOrder & {
  orderItemList: OmsOrderItem[]
  historyList: OmsOrderOperateHistory[]
}

export type OmsReceiverInfoParam = {
  orderId: number
  recipientName?: string
  recipientPhone?: string
  shippingPostalCode?: string
  shippingAddressLine?: string
  status: number
}

export type OmsMoneyInfoParam = {
  orderId: number
  shippingAmount: number
  manualAdjustmentAmount: number
  status: number
}
