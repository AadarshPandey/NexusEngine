import type { CommonPage, PageParam } from '@/types/common'
import type {
  FlashProductQueryParam,
  FlashSale,
  FlashSaleItem,
  FlashSaleSlot,
} from '@/types/promotion'
import http from '@/utils/http'

export function getFlashListAPI(params: PageParam & { keyword?: string }) {
  const queryParams: Record<string, any> = { ...params }
  if (queryParams.pageNum !== undefined) {
    queryParams.page = (queryParams.pageNum || 1) - 1
  }
  if (queryParams.pageSize !== undefined) {
    queryParams.size = queryParams.pageSize
  }
  return http<CommonPage<FlashSale>>({
    url: '/api/admin/promotion/flash-sales',
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

export function getFlashByIdAPI(id: number) {
  return http<FlashSale>({
    url: `/api/admin/promotion/flash-sales/${id}`,
    method: 'get',
  }).then((res: any) => ({ data: res }))
}

export function flashCreateAPI(data: any) {
  return http({
    url: '/api/admin/promotion/flash-sales',
    method: 'post',
    data,
  })
}

export function flashUpdateByIdAPI(id: number, data: any) {
  return http({
    url: `/api/admin/promotion/flash-sales/${id}`,
    method: 'put',
    data,
  })
}

export function flashDeleteByIdAPI(id: number) {
  return http({
    url: `/api/admin/promotion/flash-sales/${id}`,
    method: 'delete',
  })
}

export function flashUpdateStatusByIdAPI(id: number, params: { status: number }) {
  return http({
    url: `/api/admin/promotion/flash-sales/${id}/status`,
    method: 'patch',
    params,
  })
}

export function getFlashSessionListAPI() {
  return http<FlashSaleSlot[]>({
    url: '/api/admin/promotion/flash-sale-slots',
    method: 'get',
  }).then((res: any) => {
    if (Array.isArray(res)) return { data: res }
    if (res && res.content) return { data: res.content }
    return { data: res?.list || [] }
  })
}

export function getFlashSessionSelectListAPI() {
  return http<any[]>({
    url: '/api/admin/promotion/flash-sale-slots/select-list',
    method: 'get',
  }).then((res: any) => ({ data: res }))
}

export function flashSessionCreateAPI(data: any) {
  return http({
    url: '/api/admin/promotion/flash-sale-slots',
    method: 'post',
    data,
  })
}

export function flashSessionUpdateByIdAPI(id: number, data: any) {
  return http({
    url: `/api/admin/promotion/flash-sale-slots/${id}`,
    method: 'put',
    data,
  })
}

export function flashSessionDeleteByIdAPI(id: number) {
  return http({
    url: `/api/admin/promotion/flash-sale-slots/${id}`,
    method: 'delete',
  })
}

export function flashSessionUpdateStatusByIdAPI(id: number, params: { status: number }) {
  return http({
    url: `/api/admin/promotion/flash-sale-slots/${id}/status`,
    method: 'patch',
    params,
  })
}

export function getFlashProductRelationListAPI(params: FlashProductQueryParam) {
  const queryParams: Record<string, any> = { ...params }
  if (queryParams.pageNum !== undefined) {
    queryParams.page = (queryParams.pageNum || 1) - 1
  }
  if (queryParams.pageSize !== undefined) {
    queryParams.size = queryParams.pageSize
  }
  return http<CommonPage<FlashSaleItem>>({
    url: '/api/admin/promotion/flash-sale-slots/select-list',
    method: 'get',
    params: queryParams,
  }).then((res: any) => {
    if (Array.isArray(res)) return { data: { list: res, total: res.length } }
    if (res && res.content && res.list === undefined) {
      res.list = res.content
      res.total = res.totalElements ?? res.content.length
    }
    return { data: res || { list: [], total: 0 } }
  })
}
