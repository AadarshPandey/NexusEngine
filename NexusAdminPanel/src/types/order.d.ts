import type { PageParam } from './common'

export type OmsOrder = {
  id: number
  memberId?: number
  memberUsername?: string
  couponId?: number
  orderSn?: string
  createTime: string
  totalAmount: number
  payAmount: number
  freightAmount: number
  promotionAmount?: number
  couponAmount?: number
  discountAmount: number
  payType: number
  status: number
  orderType: number
  deliveryCompany?: string
  deliverySn?: string
  receiverName?: string
  receiverPhone?: string
  receiverPostCode?: string
  receiverDetailAddress?: string
  note?: string
  confirmStatus?: number
  deleteStatus?: number
  useIntegration?: number
  paymentTime: string
  deliveryTime: string
  receiveTime: string
  commentTime: string
  modifyTime?: string
}

export type OmsOrderItem = {
  id: number
  orderId: number
  productId: number
  productPic: string
  productName: string
  productBrand: string
  productPrice: number
  productQuantity: number
  productSkuId: number
  productCategoryId: number
  promotionName: string
  promotionAmount: number
  couponAmount: number
  realAmount: number
  giftIntegration: number
  giftGrowth: number
  productAttr: string
}

export type OmsOrderOperateHistory = {
  id: number
  orderId: number
  operateMan: string
  createTime: string
  orderStatus: number
  note: string
}

export type OrderQueryParam = PageParam & {
  orderSn?: string
  receiverKeyword?: string
  status?: number
  orderType?: number
  createTime?: string
}

export type OmsOrderDeliveryParam = {
  orderId: number
  deliveryCompany?: string
  deliverySn?: string
}

export type OmsOrderDetail = OmsOrder & {
  orderItemList: OmsOrderItem[]
  historyList: OmsOrderOperateHistory[]
}

export type OmsReceiverInfoParam = {
  orderId: number
  receiverName?: string
  receiverPhone?: string
  receiverPostCode?: string
  receiverDetailAddress?: string
  status: number
}

export type OmsMoneyInfoParam = {
  orderId: number
  freightAmount: number
  discountAmount: number
  status: number
}
