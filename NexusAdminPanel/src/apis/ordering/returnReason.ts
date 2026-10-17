import type { PageParam, PageResponse } from '@/types/common'
import type { ReturnReason } from '@/types/ordering'
import http from '@/utils/http'

export function getReturnReasonListAPI(params: PageParam) {
  const queryParams: Record<string, any> = { ...params }
  if (queryParams.pageNum !== undefined) {
    queryParams.page = (queryParams.pageNum || 1) - 1
    delete queryParams.pageNum
  }
  if (queryParams.pageSize !== undefined) {
    queryParams.size = queryParams.pageSize
    delete queryParams.pageSize
  }
  return http<PageResponse<ReturnReason>>({
    url: '/api/admin/ordering/return-reasons',
    method: 'get',
    params: queryParams,
  })
}

export function getReturnReasonByIdAPI(id: number) {
  return http<ReturnReason>({
    url: `/api/admin/ordering/return-reasons/${id}`,
    method: 'get',
  })
}

export function returnReasonCreateAPI(data: { name?: string; label?: string; sort?: number; displayOrder?: number; status?: number; isActive?: boolean | number }) {
  const payload = {
    label: data.label || data.name || '',
    displayOrder: data.displayOrder ?? data.sort ?? 0,
    isActive: data.isActive !== undefined ? Boolean(data.isActive) : (data.status === 1),
  }
  return http<number>({
    url: '/api/admin/ordering/return-reasons',
    method: 'post',
    data: payload,
  })
}

export function returnReasonUpdateAPI(id: number, data: any) {
  const payload = {
    label: data.label || data.name || '',
    displayOrder: data.displayOrder ?? data.sort ?? 0,
    isActive: data.isActive !== undefined ? Boolean(data.isActive) : (data.status === 1),
  }
  return http<number>({
    url: `/api/admin/ordering/return-reasons/${id}`,
    method: 'put',
    data: payload,
  })
}

export function returnReasonDeleteByIdsAPI(params: { ids: string }) {
  return http<number>({
    url: '/api/admin/ordering/return-reasons',
    method: 'delete',
    params,
  })
}

export function returnReasonUpdateStatusAPI(params: { ids: string; status: number }) {
  return http<number>({
    url: '/api/admin/ordering/return-reasons/status',
    method: 'patch',
    params,
  })
}
