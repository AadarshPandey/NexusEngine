import type { CommonPage, PageParam } from '@/types/common'
import type { AttributeDefinition, AttributeInfo, AttributeParam } from '@/types/catalog'
import http from '@/utils/http'

export function getProductAttributeListAPI(cid: number, params: PageParam & { type: number }) {
  const queryParams: Record<string, any> = { ...params }
  if (queryParams.pageNum !== undefined) {
    queryParams.page = (queryParams.pageNum || 1) - 1
  }
  if (queryParams.pageSize !== undefined) {
    queryParams.size = queryParams.pageSize
  }
  return http<CommonPage<AttributeDefinition>>({
    url: `/api/admin/catalog/attributes/product-type/${cid}`,
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

export function getProductAttributeInfoByCategoryIdAPI(productCategoryId: number) {
  return http<AttributeInfo[]>({
    url: `/api/admin/catalog/attributes/category-info/${productCategoryId}`,
    method: 'get',
  }).then((res: any) => ({ data: res }))
}

export function getProductAttributeByIdAPI(id: number) {
  return http<AttributeDefinition>({
    url: `/api/admin/catalog/attributes/${id}`,
    method: 'get',
  }).then((res: any) => ({ data: res }))
}

export function productAttributeCreateAPI(data: AttributeParam) {
  return http({
    url: '/api/admin/catalog/attributes',
    method: 'post',
    data,
  })
}

export function productAttributeUpdateAPI(id: number, data: AttributeParam) {
  return http({
    url: `/api/admin/catalog/attributes/${id}`,
    method: 'put',
    data,
  })
}

export function productAttributeDeleteByIds(params: { ids: string }) {
  return http({
    url: '/api/admin/catalog/attributes',
    method: 'delete',
    params,
  })
}
