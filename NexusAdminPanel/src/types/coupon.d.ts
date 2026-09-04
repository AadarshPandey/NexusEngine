import type { PageParam } from './common'

export type SmsCoupon = {
  /** ID */
  id?: number
  type: number
  name: string
  platform: number
  count?: number
  amount: number
  perLimit: number
  minPoint?: number
  startTime?: string
  endTime?: string
  useType: number
  note?: string
  publishCount?: number
  useCount?: number
  receiveCount?: number
  enableTime?: string
  code?: string
  memberLevel?: number
}

export type SmsCouponExt = SmsCoupon & {
  productRelationList?: CouponSelectProductOptionVo[]
  productCategoryRelationList?: CouponProductCateRelationVo[]
}

export type CouponProductCateRelationVo = {
  productCategoryId?: number
  productCategoryName?: string
  parentCategoryName?: string
}

export type CouponSelectProductOptionVo = {
  productId?: number
  productName: string
  productSn: string
}

export type CouponQueryParam = PageParam & {
  name?: string
  type?: number
}

export type SmsCouponHistory = {
  /** ID */
  id?: number
  couponId?: number
  memberId?: number
  couponCode?: string
  memberNickname?: string
  getType?: number
  createTime?: string
  useStatus?: number
  useTime?: string
  orderId?: number
  orderSn?: string
}

export type CouponHistoryQueryParam = PageParam & {
  useStatus?: number
  orderSn?: string
  couponId?: number
}
