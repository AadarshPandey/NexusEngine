import type { PageParam } from './common'

export type OmsOrder = {
  id: number
  memberId?: number
  couponId?: number
  orderSn?: string
  createTime: string
  memberUsername?: string
  totalAmount: number
  payAmount: number
  freightAmount: number
  promotionAmount?: number
  integrationAmount?: number
  couponAmount?: number
  discountAmount: number
  payType: number
  sourceType: number
  status: number
  orderType: number
  deliveryCompany?: string
  deliverySn?: string
  autoConfirmDay?: number
  integration?: number
  growth?: number
  promotionInfo: string
  billType?: number
  billHeader?: string
  billContent?: string
  billReceiverPhone?: string
  billReceiverEmail?: string
  receiverName?: string
  receiverPhone?: string
  receiverPostCode?: string
  receiverProvince: string
  receiverCity?: string
  receiverRegion?: string
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
  orderSn: string
  productId: number
  productPic: string
  productName: string
  productBrand: string
  productSn: string
  productPrice: number
  productQuantity: number
  productSkuId: number
  productSkuCode: string
  productCategoryId: number
  promotionName: string
  promotionAmount: number
  couponAmount: number
  integrationAmount: number
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
  sourceType?: number
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
  receiverProvince?: string
  receiverCity?: string
  receiverRegion?: string
  status: number
}

export type OmsMoneyInfoParam = {
  orderId: number
  freightAmount: number
  discountAmount: number
  status: number
}
