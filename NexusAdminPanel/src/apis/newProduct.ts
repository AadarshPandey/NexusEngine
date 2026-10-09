import type { CommonPage } from '@/types/common'
import type { NewProductQueryParam, SmsHomeNewProduct } from '@/types/newProduct'
import http from '@/utils/http'

/**
 */
export function getHomeNewProductListAPI(params: NewProductQueryParam) {
  return http<CommonPage<SmsHomeNewProduct>>({
    url: '/home/newProduct/list',
    method: 'get',
    params: params,
  })
}

/**
 */
export function homeNewProductUpdateRecommendStatusAPI(params: {
  ids: string
  isFeatured: number
}) {
  return http({
    url: '/home/newProduct/update/isFeatured',
    method: 'post',
    params: params,
  })
}

/**
 */
export function homeNewProductDeleteByIdsAPI(params: { ids: string }) {
  return http({
    url: '/home/newProduct/delete',
    method: 'post',
    params: params,
  })
}

/**
 */
export function homeNewProductCreateAPI(data: SmsHomeNewProduct[]) {
  return http({
    url: '/home/newProduct/create',
    method: 'post',
    data: data,
  })
}

/**
 */
export function homeNewProductUpdateSortByIdAPI(params: { id: number; displayOrder: number }) {
  return http({
    url: '/home/newProduct/update/displayOrder/' + params.id,
    method: 'post',
    params: params,
  })
}
