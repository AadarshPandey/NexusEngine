import type { CommonPage, PageParam } from '@/types/common'
import type { ReturnReason } from '@/types/ordering'
import http from '@/utils/http'

export function getReturnReasonListAPI(params: PageParam) {
  const queryParams: Record<string, any> = { ...params }
  if (queryParams.pageNum !== undefined) {
    queryParams.page = (queryParams.pageNum || 1) - 1
  }
  if (queryParams.pageSize !== undefined) {
    queryParams.size = queryParams.pageSize
  }
  return http<CommonPage<ReturnReason>>({
    url: '/api/admin/ordering/return-reasons',
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

export function getReturnReasonByIdAPI(id: number) {
  return http<ReturnReason>({
    url: `/api/admin/ordering/return-reasons/${id}`,
    method: 'get',
  }).then((res: any) => ({ data: res }))
}

export function returnReasonCreateAPI(data: { name?: string; label?: string; sort?: number; displayOrder?: number; status?: number; isActive?: boolean | number }) {
  const payload = {
    label: data.label || data.name || '',
    displayOrder: data.displayOrder ?? data.sort ?? 0,
    isActive: data.isActive !== undefined ? Boolean(data.isActive) : (data.status === 1),
  }
  return http({
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
  return http({
    url: `/api/admin/ordering/return-reasons/${id}`,
    method: 'put',
    data: payload,
  })
}

export function returnReasonDeleteByIdsAPI(params: { ids: string }) {
  return http({
    url: '/api/admin/ordering/return-reasons',
    method: 'delete',
    params,
  })
}

export function returnReasonUpdateStatusAPI(params: { ids: string; status: number }) {
  return http({
    url: '/api/admin/ordering/return-reasons/status',
    method: 'patch',
    params,
  })
}
