import type { PageParam } from './common'

export type SmsCoupon = {
  /** ID */
  id?: number
  type: number
  name: string
    count?: number
  amount: number
  perLimit: number
  minOrderAmount?: number
  startTime?: string
  endTime?: string
  useType: number
        receiveCount?: number
    code?: string
}

export type SmsCouponExt = SmsCoupon & {
  productRelationList?: CouponSelectProductOptionVo[]
  productCategoryRelationList?: CouponProductCateRelationVo[]
}

export type CouponProductCateRelationVo = {
  permissionGroupId?: number
  productCategoryName?: string
  parentCategoryName?: string
}

export type CouponSelectProductOptionVo = {
  orderLineId?: number
  productName: string
  productCode: string
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
  redeemedAt?: string
  orderId?: number
  orderSn?: string
}

export type CouponHistoryQueryParam = PageParam & {
  useStatus?: number
  orderSn?: string
  couponId?: number
}
