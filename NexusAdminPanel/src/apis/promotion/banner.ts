import type { CommonPage } from '@/types/common'
import type { Banner, HomeAdvertiseQueryParam } from '@/types/promotion'
import http from '@/utils/http'

export function getHomeAdvertiseListAPI(params: HomeAdvertiseQueryParam) {
  const queryParams: Record<string, any> = { ...params }
  if (queryParams.pageNum !== undefined) {
    queryParams.page = (queryParams.pageNum || 1) - 1
  }
  if (queryParams.pageSize !== undefined) {
    queryParams.size = queryParams.pageSize
  }
  return http<CommonPage<Banner>>({
    url: '/api/admin/promotion/banners',
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

export function getHomeAdvertiseByIdAPI(id: number) {
  return http<Banner>({
    url: `/api/admin/promotion/banners/${id}`,
    method: 'get',
  }).then((res: any) => ({ data: res }))
}

export function homeAdvertiseCreateAPI(data: Banner) {
  return http({
    url: '/api/admin/promotion/banners',
    method: 'post',
    data,
  })
}

export function homeAdvertiseUpdateAPI(id: number, data: Banner) {
  return http({
    url: `/api/admin/promotion/banners/${id}`,
    method: 'put',
    data,
  })
}

export function homeAdvertiseDeleteBatchAPI(params: { ids: string }) {
  return http({
    url: '/api/admin/promotion/banners',
    method: 'delete',
    params,
  })
}

export function homeAdvertiseUpdateStatusAPI(idOrParams: number | { id: number; status: number }, params?: { status: number }) {
  const id = typeof idOrParams === 'number' ? idOrParams : idOrParams.id
  const status = typeof idOrParams === 'number' ? params?.status : idOrParams.status
  return http({
    url: `/api/admin/promotion/banners/${id}/status`,
    method: 'patch',
    params: { status },
  })
}

export const deleteHomeAdvertiseAPI = homeAdvertiseDeleteBatchAPI

