import type { CommonPage, PageParam } from '@/types/common'
import type { PmsProduct, Product, ProductParam, ProductQueryParam } from '@/types/catalog'
import http from '@/utils/http'

export function getProductListAPI(params: ProductQueryParam) {
  const queryParams: Record<string, any> = { ...params }
  if (queryParams.pageNum !== undefined) {
    queryParams.page = (queryParams.pageNum || 1) - 1
  }
  if (queryParams.pageSize !== undefined) {
    queryParams.size = queryParams.pageSize
  }
  return http<CommonPage<Product>>({
    url: '/api/admin/catalog/products',
    method: 'get',
    params: queryParams,
  }).then((res: any) => {
    // Normalise Spring Page to CommonPage structure { list, total }
    if (res && res.content && res.list === undefined) {
      res.list = res.content
      res.total = res.totalElements ?? res.content.length
    }
    return { data: res }
  })
}

export function getSimpleProductListAPI(keyword?: string) {
  return http<Product[]>({
    url: '/api/admin/catalog/products/simple',
    method: 'get',
    params: keyword ? { keyword } : undefined,
  }).then((res: any) => ({ data: res }))
}

export function getPruductUpdateInfoAPI(id: number) {
  return http<ProductParam>({
    url: `/api/admin/catalog/products/${id}`,
    method: 'get',
  }).then((res: any) => ({ data: res }))
}

export function productCreateAPI(data: ProductParam) {
  return http({
    url: '/api/admin/catalog/products',
    method: 'post',
    data,
  })
}

export function productUpdateByIdAPI(id: number, data: ProductParam) {
  return http({
    url: `/api/admin/catalog/products/${id}`,
    method: 'put',
    data,
  })
}

export function productUpdatePublishStatusAPI(params: { ids: string; isPublished: number }) {
  return http({
    url: '/api/admin/catalog/products/publish-status',
    method: 'patch',
    params: {
      ids: params.ids,
      publishStatus: params.isPublished,
    },
  })
}

export function productUpdateNewStatusAPI(params: { ids: string; isNewArrival: number }) {
  return http({
    url: '/api/admin/catalog/products/new-arrival-status',
    method: 'patch',
    params: {
      ids: params.ids,
      newStatus: params.isNewArrival,
    },
  })
}

export function productUpdateRecommendStatusAPI(params: { ids: string; isFeatured: number }) {
  return http({
    url: '/api/admin/catalog/products/featured-status',
    method: 'patch',
    params: {
      ids: params.ids,
      recommendStatus: params.isFeatured,
    },
  })
}

export function productUpdateDeleteStatusAPI(params: { ids: string; deletedAt?: number; deleteStatus?: number }) {
  return http({
    url: '/api/admin/catalog/products',
    method: 'delete',
    params: {
      ids: params.ids,
      deleteStatus: params.deleteStatus ?? params.deletedAt ?? 1,
    },
  })
}

export function productUpdateApprovalStatusAPI(params: { ids: string; approvalStatus: number; detail?: string; comment?: string }) {
  return http({
    url: '/api/admin/catalog/products/approval',
    method: 'patch',
    params: {
      ids: params.ids,
      verifyStatus: params.approvalStatus,
      detail: params.detail || params.comment || '',
    },
  })
}

export function getProductAuditLogsAPI(id: number) {
  return http({
    url: `/api/admin/catalog/products/${id}/audit-logs`,
    method: 'get',
  }).then((res: any) => ({ data: res }))
}

export function getProductApprovalHistoryAPI(id: number) {
  return http({
    url: `/api/admin/catalog/products/${id}/approval-history`,
    method: 'get',
  }).then((res: any) => ({ data: res }))
}
