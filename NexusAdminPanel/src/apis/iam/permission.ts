import type { CommonPage, PageParam } from '@/types/common'
import type { Permission, ResourceQueryParam } from '@/types/iam'
import http from '@/utils/http'

export function getResourceListAPI(params: ResourceQueryParam) {
  const queryParams: Record<string, any> = { ...params }
  if (queryParams.pageNum !== undefined) {
    queryParams.page = (queryParams.pageNum || 1) - 1
  }
  if (queryParams.pageSize !== undefined) {
    queryParams.size = queryParams.pageSize
  }
  return http<CommonPage<Permission>>({
    url: '/api/admin/iam/permissions',
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

export function fetchAllResourceList() {
  return http<Permission[]>({
    url: '/api/admin/iam/permissions/all',
    method: 'get',
  }).then((res: any) => ({ data: res }))
}

export function getResourceByIdAPI(id: number) {
  return http<Permission>({
    url: `/api/admin/iam/permissions/${id}`,
    method: 'get',
  }).then((res: any) => ({ data: res }))
}

export function resourceCreateAPI(data: any) {
  return http({
    url: '/api/admin/iam/permissions',
    method: 'post',
    data,
  })
}

export function resourceUpdateAPI(id: number, data: any) {
  return http({
    url: `/api/admin/iam/permissions/${id}`,
    method: 'put',
    data,
  })
}

export function resourceDeleteByIdAPI(id: number) {
  return http({
    url: `/api/admin/iam/permissions/${id}`,
    method: 'delete',
  })
}
