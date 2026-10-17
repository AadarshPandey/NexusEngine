import type { PageResponse } from '@/types/common'
import type { Permission, ResourceQueryParam } from '@/types/iam'
import http from '@/utils/http'

export function getResourceListAPI(params: ResourceQueryParam) {
  const queryParams: Record<string, any> = { ...params }
  if (queryParams.pageNum !== undefined) {
    queryParams.page = (queryParams.pageNum || 1) - 1
    delete queryParams.pageNum
  }
  if (queryParams.pageSize !== undefined) {
    queryParams.size = queryParams.pageSize
    delete queryParams.pageSize
  }
  if (queryParams.permissionGroupId !== undefined && queryParams.categoryId === undefined) {
    queryParams.categoryId = queryParams.permissionGroupId
    delete queryParams.permissionGroupId
  }
  return http<PageResponse<Permission>>({
    url: '/api/admin/iam/permissions',
    method: 'get',
    params: queryParams,
  })
}

export function fetchAllResourceList() {
  return http<Permission[]>({
    url: '/api/admin/iam/permissions/all',
    method: 'get',
  })
}

export function getResourceByIdAPI(id: number) {
  return http<Permission>({
    url: `/api/admin/iam/permissions/${id}`,
    method: 'get',
  })
}

export function resourceCreateAPI(data: any) {
  return http<number>({
    url: '/api/admin/iam/permissions',
    method: 'post',
    data,
  })
}

export function resourceUpdateAPI(id: number, data: any) {
  return http<number>({
    url: `/api/admin/iam/permissions/${id}`,
    method: 'put',
    data,
  })
}

export function resourceDeleteByIdAPI(id: number) {
  return http<number>({
    url: `/api/admin/iam/permissions/${id}`,
    method: 'delete',
  })
}
