import type { CommonPage } from '@/types/common'
import type { RecommendProductQueryParam, SmsHomeRecommendProduct } from '@/types/recommendProduct'
import http from '@/utils/http'

/**
 */
export function getHomeRecommendProductListAPI(params: RecommendProductQueryParam) {
  return http<CommonPage<SmsHomeRecommendProduct>>({
    url: '/home/recommendProduct/list',
    method: 'get',
    params: params,
  })
}

/**
 */
export function homeRecommendProductUpdateRecommendStatusAPI(params: {
  ids: string
  isFeatured: number
}) {
  return http({
    url: '/home/recommendProduct/update/isFeatured',
    method: 'post',
    params: params,
  })
}

/**
 */
export function homeRecommendProductDeleteByIdsAPI(params: { ids: string }) {
  return http({
    url: '/home/recommendProduct/delete',
    method: 'post',
    params: params,
  })
}

/**
 */
export function homeRecommendProductCreateAPI(data: SmsHomeRecommendProduct[]) {
  return http({
    url: '/home/recommendProduct/create',
    method: 'post',
    data: data,
  })
}

/**
 */
export function homeRecommendProductUpdateSortByIdAPI(params: { id: number; displayOrder: number }) {
  return http({
    url: '/home/recommendProduct/update/displayOrder/' + params.id,
    method: 'post',
    params: params,
  })
}
