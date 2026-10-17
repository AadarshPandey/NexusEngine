import type { PageParam, PageResponse } from '@/types/common'
import type { Product, ProductParam, ProductQueryParam } from '@/types/catalog'
import http from '@/utils/http'

function toCleanQueryParams(params?: Record<string, any>) {
  const queryParams: Record<string, any> = {}
  if (!params) return queryParams
  for (const [k, v] of Object.entries(params)) {
    if (v !== '' && v !== undefined && v !== null) {
      queryParams[k] = v
    }
  }
  if (queryParams.pageNum !== undefined) {
    queryParams.page = (Number(queryParams.pageNum) || 1) - 1
    delete queryParams.pageNum
  }
  if (queryParams.pageSize !== undefined) {
    queryParams.size = Number(queryParams.pageSize)
    delete queryParams.pageSize
  }
  return queryParams
}

export function getProductListAPI(params: ProductQueryParam) {
  return http<PageResponse<Product>>({
    url: '/api/admin/catalog/products',
    method: 'get',
    params: toCleanQueryParams(params),
  })
}

export function getSimpleProductListAPI(keyword?: string) {
  return http<Product[]>({
    url: '/api/admin/catalog/products/simple',
    method: 'get',
    params: keyword ? { keyword } : undefined,
  })
}

export function getPruductUpdateInfoAPI(id: number) {
  return http<ProductParam>({
    url: `/api/admin/catalog/products/${id}`,
    method: 'get',
  })
}

export function productCreateAPI(data: ProductParam) {
  return http<number>({
    url: '/api/admin/catalog/products',
    method: 'post',
    data,
  })
}

export function productUpdateByIdAPI(id: number, data: ProductParam) {
  return http<number>({
    url: `/api/admin/catalog/products/${id}`,
    method: 'put',
    data,
  })
}

export function productUpdatePublishStatusAPI(params: { ids: string; isPublished: number }) {
  return http<number>({
    url: '/api/admin/catalog/products/publish-status',
    method: 'patch',
    params: {
      ids: params.ids,
      publishStatus: params.isPublished,
    },
  })
}

export function productUpdateNewStatusAPI(params: { ids: string; isNewArrival: number }) {
  return http<number>({
    url: '/api/admin/catalog/products/new-arrival-status',
    method: 'patch',
    params: {
      ids: params.ids,
      newStatus: params.isNewArrival,
    },
  })
}

export function productUpdateRecommendStatusAPI(params: { ids: string; isFeatured: number }) {
  return http<number>({
    url: '/api/admin/catalog/products/featured-status',
    method: 'patch',
    params: {
      ids: params.ids,
      recommendStatus: params.isFeatured,
    },
  })
}

export function productUpdateDeleteStatusAPI(params: { ids: string; deletedAt?: number; deleteStatus?: number }) {
  return http<number>({
    url: '/api/admin/catalog/products',
    method: 'delete',
    params: {
      ids: params.ids,
      deleteStatus: params.deleteStatus ?? params.deletedAt ?? 1,
    },
  })
}

export function productUpdateApprovalStatusAPI(params: { ids: string; approvalStatus: number; detail?: string; comment?: string }) {
  return http<number>({
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
  return http<any[]>({
    url: `/api/admin/catalog/products/${id}/audit-logs`,
    method: 'get',
  })
}

export function getProductApprovalHistoryAPI(id: number) {
  return http<any[]>({
    url: `/api/admin/catalog/products/${id}/approval-history`,
    method: 'get',
  })
}
