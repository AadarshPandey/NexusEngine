import type { PageResponse } from '@/types/common'
import type { Banner, HomeAdvertiseQueryParam } from '@/types/promotion'
import http from '@/utils/http'

export function getHomeAdvertiseListAPI(params: HomeAdvertiseQueryParam) {
  const queryParams: Record<string, any> = { ...params }
  if (queryParams.pageNum !== undefined) {
    queryParams.page = (queryParams.pageNum || 1) - 1
    delete queryParams.pageNum
  }
  if (queryParams.pageSize !== undefined) {
    queryParams.size = queryParams.pageSize
    delete queryParams.pageSize
  }
  return http<PageResponse<Banner>>({
    url: '/api/admin/promotion/banners',
    method: 'get',
    params: queryParams,
  })
}

export function getHomeAdvertiseByIdAPI(id: number) {
  return http<Banner>({
    url: `/api/admin/promotion/banners/${id}`,
    method: 'get',
  })
}

export function homeAdvertiseCreateAPI(data: Banner) {
  return http<number>({
    url: '/api/admin/promotion/banners',
    method: 'post',
    data,
  })
}

export function homeAdvertiseUpdateAPI(id: number, data: Banner) {
  return http<number>({
    url: `/api/admin/promotion/banners/${id}`,
    method: 'put',
    data,
  })
}

export function homeAdvertiseDeleteBatchAPI(params: { ids: string }) {
  return http<number>({
    url: '/api/admin/promotion/banners',
    method: 'delete',
    params,
  })
}

export function homeAdvertiseUpdateStatusAPI(idOrParams: number | { id: number; status: number }, params?: { status: number }) {
  const id = typeof idOrParams === 'number' ? idOrParams : idOrParams.id
  const status = typeof idOrParams === 'number' ? params?.status : idOrParams.status
  return http<number>({
    url: `/api/admin/promotion/banners/${id}/status`,
    method: 'patch',
    params: { status },
  })
}

export const deleteHomeAdvertiseAPI = homeAdvertiseDeleteBatchAPI
