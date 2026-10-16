import type { CommonPage, PageParam } from '@/types/common'
import type { ReturnApplyStatusParam, ReturnRequest } from '@/types/ordering'
import http from '@/utils/http'

export function getReturnApplyListAPI(params: PageParam & Record<string, any>) {
  const queryParams: Record<string, any> = { ...params }
  if (queryParams.pageNum !== undefined) {
    queryParams.page = (queryParams.pageNum || 1) - 1
  }
  if (queryParams.pageSize !== undefined) {
    queryParams.size = queryParams.pageSize
  }
  return http<CommonPage<ReturnRequest>>({
    url: '/api/admin/ordering/return-requests',
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

export function getReturnApplyByIdAPI(id: number) {
  return http<ReturnRequest>({
    url: `/api/admin/ordering/return-requests/${id}`,
    method: 'get',
  }).then((res: any) => ({ data: res }))
}

export function returnApplyDeleteByIdsAPI(params: { ids: string }) {
  return http({
    url: '/api/admin/ordering/return-requests',
    method: 'delete',
    params,
  })
}

export function returnApplyUpdateStatusAPI(id: number, data: ReturnApplyStatusParam) {
  return http({
    url: `/api/admin/ordering/return-requests/${id}/status`,
    method: 'patch',
    data,
  })
}
