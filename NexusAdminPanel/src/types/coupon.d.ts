import type { PageParam } from './common'

export type SmsCoupon = {
  /** ID */
  id?: number
  type: number
  name: string
    minQuantity?: number
  discountValue: number
  perMemberLimit: number
  minSpendAmount?: number
  startsAt?: string
  endsAt?: string
  applicabilityScope: number
        claimedCount?: number
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
  acquisitionChannel?: number
  createdAt?: string
  status?: number
  redeemedAt?: string
  orderId?: number
  orderNumber?: string
}

export type CouponHistoryQueryParam = PageParam & {
  status?: number
  orderNumber?: string
  couponId?: number
}
