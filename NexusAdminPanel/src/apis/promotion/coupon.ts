import type { CommonPage, PageParam } from '@/types/common'
import type { Coupon, CouponParam, CouponQueryParams, MemberCoupon } from '@/types/promotion'
import http from '@/utils/http'

export function getCouponListAPI(params: CouponQueryParams) {
  const queryParams: Record<string, any> = { ...params }
  if (queryParams.pageNum !== undefined) {
    queryParams.page = (queryParams.pageNum || 1) - 1
  }
  if (queryParams.pageSize !== undefined) {
    queryParams.size = queryParams.pageSize
  }
  return http<CommonPage<Coupon>>({
    url: '/api/admin/promotion/coupons',
    method: 'get',
    params: queryParams,
  }).then((res: any) => {
    if (res && res.content && res.list === undefined) {
      res.list = res.content
      res.total = res.totalElements ?? res.content.length
    }
    return { data: res }
  })
}

export function getCouponByIdAPI(id: number) {
  return http<CouponParam>({
    url: `/api/admin/promotion/coupons/${id}`,
    method: 'get',
  }).then((res: any) => ({ data: res }))
}

export function couponCreateAPI(data: CouponParam) {
  return http({
    url: '/api/admin/promotion/coupons',
    method: 'post',
    data,
  })
}

export function couponUpdateByIdAPI(id: number, data: CouponParam) {
  return http({
    url: `/api/admin/promotion/coupons/${id}`,
    method: 'put',
    data,
  })
}

export function couponDeleteByIdAPI(id: number) {
  return http({
    url: `/api/admin/promotion/coupons/${id}`,
    method: 'delete',
  })
}

export function getCouponHistoryListAPI(params: PageParam & { couponId?: number; useStatus?: number; orderSn?: string }) {
  const queryParams: Record<string, any> = { ...params }
  if (queryParams.pageNum !== undefined) {
    queryParams.page = (queryParams.pageNum || 1) - 1
  }
  if (queryParams.pageSize !== undefined) {
    queryParams.size = queryParams.pageSize
  }
  return http<CommonPage<MemberCoupon>>({
    url: '/api/admin/promotion/coupon-claims',
    method: 'get',
    params: queryParams,
  }).then((res: any) => {
    if (res && res.content && res.list === undefined) {
      res.list = res.content
      res.total = res.totalElements ?? res.content.length
    }
    return { data: res }
  })
}
