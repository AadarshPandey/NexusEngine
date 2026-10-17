import type { PageParam, PageResponse } from '@/types/common'
import type { ReturnApplyStatusParam, ReturnRequest } from '@/types/ordering'
import http from '@/utils/http'

export function getReturnApplyListAPI(params: PageParam & Record<string, any>) {
  const queryParams: Record<string, any> = { ...params }
  if (queryParams.pageNum !== undefined) {
    queryParams.page = (queryParams.pageNum || 1) - 1
    delete queryParams.pageNum
  }
  if (queryParams.pageSize !== undefined) {
    queryParams.size = queryParams.pageSize
    delete queryParams.pageSize
  }
  return http<PageResponse<ReturnRequest>>({
    url: '/api/admin/ordering/return-requests',
    method: 'get',
    params: queryParams,
  })
}

export function getReturnApplyByIdAPI(id: number) {
  return http<ReturnRequest>({
    url: `/api/admin/ordering/return-requests/${id}`,
    method: 'get',
  })
}

export function returnApplyDeleteByIdsAPI(params: { ids: string }) {
  return http<number>({
    url: '/api/admin/ordering/return-requests',
    method: 'delete',
    params,
  })
}

export function returnApplyUpdateStatusAPI(id: number, data: ReturnApplyStatusParam) {
  return http<number>({
    url: `/api/admin/ordering/return-requests/${id}/status`,
    method: 'patch',
    data,
  })
}
