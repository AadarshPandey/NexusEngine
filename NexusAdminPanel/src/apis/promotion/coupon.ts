import type { PageParam, PageResponse } from '@/types/common'
import type { Coupon, CouponParam, CouponQueryParams, MemberCoupon } from '@/types/promotion'
import http from '@/utils/http'

export function getCouponListAPI(params: CouponQueryParams) {
  const queryParams: Record<string, any> = { ...params }
  if (queryParams.pageNum !== undefined) {
    queryParams.page = (queryParams.pageNum || 1) - 1
    delete queryParams.pageNum
  }
  if (queryParams.pageSize !== undefined) {
    queryParams.size = queryParams.pageSize
    delete queryParams.pageSize
  }
  return http<PageResponse<Coupon>>({
    url: '/api/admin/promotion/coupons',
    method: 'get',
    params: queryParams,
  })
}

export function getCouponByIdAPI(id: number) {
  return http<CouponParam>({
    url: `/api/admin/promotion/coupons/${id}`,
    method: 'get',
  })
}

export function couponCreateAPI(data: CouponParam) {
  return http<number>({
    url: '/api/admin/promotion/coupons',
    method: 'post',
    data,
  })
}

export function couponUpdateByIdAPI(id: number, data: CouponParam) {
  return http<number>({
    url: `/api/admin/promotion/coupons/${id}`,
    method: 'put',
    data,
  })
}

export function couponDeleteByIdAPI(id: number) {
  return http<number>({
    url: `/api/admin/promotion/coupons/${id}`,
    method: 'delete',
  })
}

export function getCouponHistoryListAPI(params: PageParam & { couponId?: number; useStatus?: number; orderSn?: string }) {
  const queryParams: Record<string, any> = { ...params }
  if (queryParams.pageNum !== undefined) {
    queryParams.page = (queryParams.pageNum || 1) - 1
    delete queryParams.pageNum
  }
  if (queryParams.pageSize !== undefined) {
    queryParams.size = queryParams.pageSize
    delete queryParams.pageSize
  }
  return http<PageResponse<MemberCoupon>>({
    url: '/api/admin/promotion/coupon-claims',
    method: 'get',
    params: queryParams,
  })
}
