import type { PageParam } from './common'
import type { CustomerOrder, OrderLine, OrderStatusHistory, ReturnRequest, ReturnReason, MerchantAddress, OrderPolicy } from './ordering'

export type { CustomerOrder, OrderLine, OrderStatusHistory, ReturnRequest, ReturnReason, MerchantAddress, OrderPolicy } from './ordering'

export type OmsOrder = {
  id: number
  memberId?: number
  memberUsername?: string
  couponId?: number | null
  orderNumber?: string
  orderSn?: string
  createdAt: string
  subtotalAmount: number
  totalAmount?: number
  grandTotalAmount: number
  payAmount?: number
  shippingAmount: number
  freightAmount?: number
  promotionDiscountAmount?: number
  promotionAmount?: number
  couponDiscountAmount?: number
  couponAmount?: number
  manualAdjustmentAmount: number
  discountAmount?: number
  paymentMethod: number
  payType?: number
  status: number
  orderType: number
  carrierName?: string
  deliveryCompany?: string
  trackingNumber?: string
  deliverySn?: string
  recipientName?: string
  receiverName?: string
  recipientPhone?: string
  receiverPhone?: string
  shippingPostalCode?: string
  receiverPostCode?: string
  shippingAddressLine?: string
  receiverDetailAddress?: string
  note?: string
  isReceiptConfirmed?: boolean | number
  confirmStatus?: number
  deletedAt?: number | string | null
  deleteStatus?: number
  useIntegration?: number
  paidAt: string
  paymentTime?: string
  shippedAt: string
  deliveryTime?: string
  deliveredAt: string
  receiveTime?: string
  reviewedAt: string
  commentTime?: string
  updatedAt?: string
  modifyTime?: string
}

export type OmsOrderItem = {
  id: number
  orderId: number
  orderLineId?: number
  productId?: number
  productImageUrl?: string
  productPic?: string
  productName: string
  brandName?: string
  productBrand?: string
  unitPrice: number
  productPrice?: number
  quantity: number
  productQuantity?: number
  skuId?: number
  productSkuId?: number
  permissionGroupId?: number
  productCategoryId?: number
  promotionName?: string
  promotionDiscountAmount?: number
  promotionAmount?: number
  couponDiscountAmount?: number
  couponAmount?: number
  lineTotalAmount: number
  realAmount?: number
  giftIntegration?: number
  giftGrowth?: number
  skuAttributes?: string | Record<string, any>
}

export type OmsOrderOperateHistory = {
  id: number
  orderId: number
  changedByAdminId?: string
  operateMan?: string
  createdAt: string
  createTime?: string
  toStatus: number
  orderStatus?: number
  note: string
}

export type OrderQueryParam = PageParam & {
  orderNumber?: string
  orderSn?: string
  receiverKeyword?: string
  recipientKeyword?: string
  status?: number
  orderType?: number
  createdAt?: string
}

export type OmsOrderDeliveryParam = {
  orderId: number
  carrierName?: string
  deliveryCompany?: string
  trackingNumber?: string
  deliverySn?: string
}

export type OmsOrderDetail = OmsOrder & {
  orderItemList: OmsOrderItem[]
  orderLines?: OmsOrderItem[]
  historyList: OmsOrderOperateHistory[]
}

export type OmsReceiverInfoParam = {
  orderId: number
  recipientName?: string
  receiverName?: string
  recipientPhone?: string
  receiverPhone?: string
  shippingPostalCode?: string
  receiverPostCode?: string
  shippingAddressLine?: string
  receiverDetailAddress?: string
  status: number
}

export type OmsMoneyInfoParam = {
  orderId: number
  shippingAmount: number
  freightAmount?: number
  manualAdjustmentAmount: number
  discountAmount?: number
  status: number
}
