import type { PageParam, PageResponse } from '@/types/common'
import type { AttributeDefinition, AttributeInfo, AttributeParam } from '@/types/catalog'
import http from '@/utils/http'

export function getProductAttributeListAPI(cid: number, params: PageParam & { type: number }) {
  const queryParams: Record<string, any> = { ...params }
  if (queryParams.pageNum !== undefined) {
    queryParams.page = (queryParams.pageNum || 1) - 1
    delete queryParams.pageNum
  }
  if (queryParams.pageSize !== undefined) {
    queryParams.size = queryParams.pageSize
    delete queryParams.pageSize
  }
  return http<PageResponse<AttributeDefinition>>({
    url: `/api/admin/catalog/attributes/product-type/${cid}`,
    method: 'get',
    params: queryParams,
  })
}

export function getProductAttributeInfoByCategoryIdAPI(productCategoryId: number) {
  return http<AttributeInfo[]>({
    url: `/api/admin/catalog/attributes/category-info/${productCategoryId}`,
    method: 'get',
  })
}

export function getProductAttributeByIdAPI(id: number) {
  return http<AttributeDefinition>({
    url: `/api/admin/catalog/attributes/${id}`,
    method: 'get',
  })
}

export function productAttributeCreateAPI(data: AttributeParam) {
  return http<number>({
    url: '/api/admin/catalog/attributes',
    method: 'post',
    data,
  })
}

export function productAttributeUpdateAPI(id: number, data: AttributeParam) {
  return http<number>({
    url: `/api/admin/catalog/attributes/${id}`,
    method: 'put',
    data,
  })
}

export function productAttributeDeleteByIds(params: { ids: string }) {
  return http<number>({
    url: '/api/admin/catalog/attributes',
    method: 'delete',
    params,
  })
}
