import type { CommonPage, PageParam } from '@/types/common'
import type { Brand, BrandParam } from '@/types/catalog'
import http from '@/utils/http'

export function getBrandListAPI(params: PageParam & { keyword?: string; isVisible?: number; showStatus?: number }) {
  const queryParams: Record<string, any> = { ...params }
  if (queryParams.pageNum !== undefined) {
    queryParams.page = (queryParams.pageNum || 1) - 1
  }
  if (queryParams.pageSize !== undefined) {
    queryParams.size = queryParams.pageSize
  }
  if (queryParams.showStatus !== undefined && queryParams.isVisible === undefined) {
    queryParams.isVisible = queryParams.showStatus
  }
  return http<CommonPage<Brand>>({
    url: '/api/admin/catalog/brands',
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

export function getBrandListAllAPI() {
  return http<CommonPage<Brand>>({
    url: '/api/admin/catalog/brands/all',
    method: 'get',
  }).then((res: any) => {
    if (res && res.content && res.list === undefined) {
      res.list = res.content
      res.total = res.totalElements ?? res.content.length
    }
    return { data: res }
  })
}

export function getBrandByIdAPI(id: number) {
  return http<Brand>({
    url: `/api/admin/catalog/brands/${id}`,
    method: 'get',
  }).then((res: any) => ({ data: res }))
}

export function brandCreateAPI(data: BrandParam) {
  return http({
    url: '/api/admin/catalog/brands',
    method: 'post',
    data,
  })
}

export function brandUpdateByIdAPI(id: number, data: BrandParam) {
  return http({
    url: `/api/admin/catalog/brands/${id}`,
    method: 'put',
    data,
  })
}

export function brandDeleteByIdAPI(id: number) {
  return http({
    url: `/api/admin/catalog/brands/${id}`,
    method: 'delete',
  })
}

export function brandDeleteBatchAPI(ids: string | number[]) {
  const idsStr = Array.isArray(ids) ? ids.join(',') : ids
  return http({
    url: '/api/admin/catalog/brands/batch',
    method: 'delete',
    params: { ids: idsStr },
  })
}

export function brandUpdateShowStatusAPI(params: { ids: string; isVisible: number }) {
  return http({
    url: '/api/admin/catalog/brands/visibility',
    method: 'patch',
    params: {
      ids: params.ids,
      isVisible: params.isVisible,
    },
  })
}

export function brandUpdateFactoryStatusAPI(params: { ids: string; isFactoryAuthorised: number }) {
  return http({
    url: '/api/admin/catalog/brands/visibility',
    method: 'patch',
    params: {
      ids: params.ids,
      isVisible: params.isFactoryAuthorised,
    },
  })
}
