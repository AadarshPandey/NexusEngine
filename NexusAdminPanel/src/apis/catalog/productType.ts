import type { CommonPage, PageParam } from '@/types/common'
import type { ProductType, ProductTypeWithAttributes } from '@/types/catalog'
import http from '@/utils/http'

export function getProductAttributeCategoryListAPI(params?: PageParam) {
  const queryParams: Record<string, any> = { ...params }
  if (queryParams.pageNum !== undefined) {
    queryParams.page = (queryParams.pageNum || 1) - 1
  }
  if (queryParams.pageSize !== undefined) {
    queryParams.size = queryParams.pageSize
  }
  return http<CommonPage<ProductType>>({
    url: '/api/admin/catalog/product-types',
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

export function getProductAttributeCategoryListWithAttrAPI() {
  return http<ProductTypeWithAttributes[]>({
    url: '/api/admin/catalog/product-types/with-attributes',
    method: 'get',
  }).then((res: any) => ({ data: res }))
}

export function getProductAttributeCategoryByIdAPI(id: number) {
  return http<ProductType>({
    url: `/api/admin/catalog/product-types/${id}`,
    method: 'get',
  }).then((res: any) => ({ data: res }))
}

export function productAttributeCategoryCreateAPI(params: string | { name: string }) {
  const name = typeof params === 'string' ? params : params.name
  return http({
    url: '/api/admin/catalog/product-types',
    method: 'post',
    params: { name },
  })
}

export function productAttributeCategoryUpdateAPI(id: number, params: string | { name: string }) {
  const name = typeof params === 'string' ? params : params.name
  return http({
    url: `/api/admin/catalog/product-types/${id}`,
    method: 'put',
    params: { name },
  })
}

export function productAttributeCategoryDeleteById(id: number) {
  return http({
    url: `/api/admin/catalog/product-types/${id}`,
    method: 'delete',
  })
}
