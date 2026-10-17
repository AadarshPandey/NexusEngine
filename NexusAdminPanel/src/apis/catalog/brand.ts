import type { PageParam, PageResponse } from '@/types/common'
import type { Brand, BrandParam } from '@/types/catalog'
import http from '@/utils/http'

export function getBrandListAPI(params: PageParam & { keyword?: string; isVisible?: number; showStatus?: number }) {
  const queryParams: Record<string, any> = { ...params }
  if (queryParams.pageNum !== undefined) {
    queryParams.page = (queryParams.pageNum || 1) - 1
    delete queryParams.pageNum
  }
  if (queryParams.pageSize !== undefined) {
    queryParams.size = queryParams.pageSize
    delete queryParams.pageSize
  }
  if (queryParams.showStatus !== undefined && queryParams.isVisible === undefined) {
    queryParams.isVisible = queryParams.showStatus
    delete queryParams.showStatus
  }
  return http<PageResponse<Brand>>({
    url: '/api/admin/catalog/brands',
    method: 'get',
    params: queryParams,
  })
}

export function getBrandListAllAPI() {
  return http<Brand[]>({
    url: '/api/admin/catalog/brands/all',
    method: 'get',
  })
}

export function getBrandByIdAPI(id: number) {
  return http<Brand>({
    url: `/api/admin/catalog/brands/${id}`,
    method: 'get',
  })
}

export function brandCreateAPI(data: BrandParam) {
  return http<number>({
    url: '/api/admin/catalog/brands',
    method: 'post',
    data,
  })
}

export function brandUpdateByIdAPI(id: number, data: BrandParam) {
  return http<number>({
    url: `/api/admin/catalog/brands/${id}`,
    method: 'put',
    data,
  })
}

export function brandDeleteByIdAPI(id: number) {
  return http<number>({
    url: `/api/admin/catalog/brands/${id}`,
    method: 'delete',
  })
}

export function brandDeleteBatchAPI(ids: string | number[]) {
  const idsStr = Array.isArray(ids) ? ids.join(',') : ids
  return http<number>({
    url: '/api/admin/catalog/brands/batch',
    method: 'delete',
    params: { ids: idsStr },
  })
}

export function brandUpdateShowStatusAPI(params: { ids: string; isVisible: number }) {
  return http<number>({
    url: '/api/admin/catalog/brands/visibility',
    method: 'patch',
    params: {
      ids: params.ids,
      isVisible: params.isVisible,
    },
  })
}

export function brandUpdateFactoryStatusAPI(params: { ids: string; isFactoryAuthorised: number }) {
  return http<number>({
    url: '/api/admin/catalog/brands/visibility',
    method: 'patch',
    params: {
      ids: params.ids,
      isVisible: params.isFactoryAuthorised,
    },
  })
}
