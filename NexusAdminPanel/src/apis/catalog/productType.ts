import type { PageParam, PageResponse } from '@/types/common'
import type { ProductType, ProductTypeWithAttributes } from '@/types/catalog'
import http from '@/utils/http'

export function getProductAttributeCategoryListAPI(params?: PageParam) {
  const queryParams: Record<string, any> = { ...params }
  if (queryParams.pageNum !== undefined) {
    queryParams.page = (queryParams.pageNum || 1) - 1
    delete queryParams.pageNum
  }
  if (queryParams.pageSize !== undefined) {
    queryParams.size = queryParams.pageSize
    delete queryParams.pageSize
  }
  return http<PageResponse<ProductType>>({
    url: '/api/admin/catalog/product-types',
    method: 'get',
    params: queryParams,
  })
}

export function getProductAttributeCategoryListWithAttrAPI() {
  return http<ProductTypeWithAttributes[]>({
    url: '/api/admin/catalog/product-types/with-attributes',
    method: 'get',
  })
}

export function getProductAttributeCategoryByIdAPI(id: number) {
  return http<ProductType>({
    url: `/api/admin/catalog/product-types/${id}`,
    method: 'get',
  })
}

export function productAttributeCategoryCreateAPI(params: string | { name: string }) {
  const name = typeof params === 'string' ? params : params.name
  return http<number>({
    url: '/api/admin/catalog/product-types',
    method: 'post',
    params: { name },
  })
}

export function productAttributeCategoryUpdateAPI(id: number, params: string | { name: string }) {
  const name = typeof params === 'string' ? params : params.name
  return http<number>({
    url: `/api/admin/catalog/product-types/${id}`,
    method: 'put',
    params: { name },
  })
}

export function productAttributeCategoryDeleteById(id: number) {
  return http<number>({
    url: `/api/admin/catalog/product-types/${id}`,
    method: 'delete',
  })
}
