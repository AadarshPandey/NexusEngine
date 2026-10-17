import type { PageParam, PageResponse } from '@/types/common'
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
    delete queryParams.pageNum
  }
  if (queryParams.pageSize !== undefined) {
    queryParams.size = queryParams.pageSize
    delete queryParams.pageSize
  }
  return http<PageResponse<FlashSale>>({
    url: '/api/admin/promotion/flash-sales',
    method: 'get',
    params: queryParams,
  })
}

export function getFlashByIdAPI(id: number) {
  return http<FlashSale>({
    url: `/api/admin/promotion/flash-sales/${id}`,
    method: 'get',
  })
}

export function flashCreateAPI(data: any) {
  return http<number>({
    url: '/api/admin/promotion/flash-sales',
    method: 'post',
    data,
  })
}

export function flashUpdateByIdAPI(id: number, data: any) {
  return http<number>({
    url: `/api/admin/promotion/flash-sales/${id}`,
    method: 'put',
    data,
  })
}

export function flashDeleteByIdAPI(id: number) {
  return http<number>({
    url: `/api/admin/promotion/flash-sales/${id}`,
    method: 'delete',
  })
}

export function flashUpdateStatusByIdAPI(id: number, params: { status: number }) {
  return http<number>({
    url: `/api/admin/promotion/flash-sales/${id}/status`,
    method: 'patch',
    params,
  })
}

export function getFlashSessionListAPI() {
  return http<FlashSaleSlot[]>({
    url: '/api/admin/promotion/flash-sale-slots',
    method: 'get',
  })
}

export function getFlashSessionSelectListAPI(flashPromotionId?: number) {
  return http<FlashSaleSlot[]>({
    url: '/api/admin/promotion/flash-sale-slots/selectable',
    method: 'get',
    params: flashPromotionId ? { flashPromotionId } : undefined,
  })
}

export function flashSessionCreateAPI(data: any) {
  return http<FlashSaleSlot>({
    url: '/api/admin/promotion/flash-sale-slots',
    method: 'post',
    data,
  })
}

export function flashSessionUpdateByIdAPI(id: number, data: any) {
  return http<FlashSaleSlot>({
    url: `/api/admin/promotion/flash-sale-slots/${id}`,
    method: 'put',
    data,
  })
}

export function flashSessionDeleteByIdAPI(id: number) {
  return http<void>({
    url: `/api/admin/promotion/flash-sale-slots/${id}`,
    method: 'delete',
  })
}

export function flashSessionUpdateStatusByIdAPI(id: number, params: { status: number }) {
  return http<FlashSaleSlot>({
    url: `/api/admin/promotion/flash-sale-slots/${id}/status`,
    method: 'patch',
    params,
  })
}

export function getFlashProductRelationListAPI(params: FlashProductQueryParam) {
  const queryParams: Record<string, any> = { ...params }
  if (queryParams.pageNum !== undefined) {
    queryParams.page = (queryParams.pageNum || 1) - 1
    delete queryParams.pageNum
  }
  if (queryParams.pageSize !== undefined) {
    queryParams.size = queryParams.pageSize
    delete queryParams.pageSize
  }
  return http<PageResponse<FlashSaleItem>>({
    url: '/api/admin/promotion/flash-sale-slots/items',
    method: 'get',
    params: queryParams,
  })
}
