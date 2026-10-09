import type { CommonPage } from '@/types/common'
import type { PmsProduct, PmsProductParam, ProductQueryParam } from '@/types/product'
import http from '@/utils/http'

/**
 */
export function getProductListAPI(params: ProductQueryParam) {
  return http<CommonPage<PmsProduct>>({
    url: '/product/list',
    method: 'get',
    params: params,
  })
}

/**
 */
export function productUpdateDeleteStatusAPI(params: { ids: string; deletedAt: number }) {
  return http({
    url: '/product/update/deletedAt',
    method: 'post',
    params: params,
  })
}

/**
 */
export function productUpdateNewStatusAPI(params: { ids: string; isNewArrival: number }) {
  return http({
    url: '/product/update/isNewArrival',
    method: 'post',
    params: params,
  })
}

/**
 */
export function productUpdateRecommendStatusAPI(params: { ids: string; isFeatured: number }) {
  return http({
    url: '/product/update/isFeatured',
    method: 'post',
    params: params,
  })
}

/**
 */
export function productUpdatePublishStatusAPI(params: { ids: string; isPublished: number }) {
  return http({
    url: '/product/update/isPublished',
    method: 'post',
    params: params,
  })
}

/**
 */
export function productCreateAPI(data: PmsProductParam) {
  return http({
    url: '/product/create',
    method: 'post',
    data: data,
  })
}

/**
 */
export function productUpdateByIdAPI(id: number, data: PmsProductParam) {
  return http({
    url: '/product/update/' + id,
    method: 'post',
    data: data,
  })
}

/**
 */
export function getPruductUpdateInfoAPI(id: number) {
  return http<PmsProductParam>({
    url: '/product/updateInfo/' + id,
    method: 'get',
  })
}
