import type { PageParam } from './common'
import type { Coupon, MemberCoupon } from './promotion'

export type { Coupon, MemberCoupon } from './promotion'

export type SmsCoupon = {
  /** ID */
  id?: number
  type: number
  discountType?: number
  name: string
  count?: number
  remainingQuantity?: number
  amount: number
  discountValue?: number
  perLimit: number
  perMemberLimit?: number
  minOrderAmount?: number
  minSpendAmount?: number
  startTime?: string
  startsAt?: string
  endTime?: string
  endsAt?: string
  useType: number
  applicabilityScope?: number
  receiveCount?: number
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
  discountType?: number
}

export type SmsCouponHistory = {
  /** ID */
  id?: number
  couponId?: number
  memberId?: number
  couponCode?: string
  memberNickname?: string
  getType?: number
  acquisitionChannel?: number
  createTime?: string
  claimedAt?: string
  useStatus?: number
  status?: number
  redeemedAt?: string
  orderId?: number
  orderSn?: string
  orderNumber?: string
}

export type CouponHistoryQueryParam = PageParam & {
  useStatus?: number
  orderSn?: string
  couponId?: number
}
