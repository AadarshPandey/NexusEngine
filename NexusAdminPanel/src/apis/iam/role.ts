import type { PageParam, PageResponse } from '@/types/common'
import type { ConsoleMenu, Permission, Role } from '@/types/iam'
import http from '@/utils/http'

export function getRoleListAPI(params: PageParam & { keyword?: string }) {
  const queryParams: Record<string, any> = { ...params }
  if (queryParams.pageNum !== undefined) {
    queryParams.page = (queryParams.pageNum || 1) - 1
    delete queryParams.pageNum
  }
  if (queryParams.pageSize !== undefined) {
    queryParams.size = queryParams.pageSize
    delete queryParams.pageSize
  }
  return http<PageResponse<Role>>({
    url: '/api/admin/iam/roles',
    method: 'get',
    params: queryParams,
  })
}

export function getRoleListAllAPI() {
  return http<Role[]>({
    url: '/api/admin/iam/roles/all',
    method: 'get',
  })
}

export function roleCreateAPI(data: any) {
  const payload = {
    name: data.name,
    description: data.description ?? data.customerComment,
    sort: data.sort ?? data.displayOrder ?? 0,
    status: data.status !== undefined ? Boolean(data.status) : (data.isActive !== undefined ? Boolean(data.isActive) : true),
  }
  return http<number>({
    url: '/api/admin/iam/roles',
    method: 'post',
    data: payload,
  })
}

export function roleUpdateByIdAPI(id: number, data: any) {
  const payload = {
    name: data.name,
    description: data.description ?? data.customerComment,
    sort: data.sort ?? data.displayOrder ?? 0,
    status: data.status !== undefined ? Boolean(data.status) : (data.isActive !== undefined ? Boolean(data.isActive) : true),
  }
  return http<number>({
    url: `/api/admin/iam/roles/${id}`,
    method: 'put',
    data: payload,
  })
}

export function roleDeleteBatchAPI(params: { ids: string }) {
  return http<number>({
    url: '/api/admin/iam/roles',
    method: 'delete',
    params,
  })
}

export function roleUpdateStatusByIdAPI(id: number, params: { status: number }) {
  return http<number>({
    url: `/api/admin/iam/roles/${id}/status`,
    method: 'patch',
    params,
  })
}

export function roleListMenuByRoleIdAPI(roleId: number) {
  return http<ConsoleMenu[]>({
    url: `/api/admin/iam/roles/${roleId}/menus`,
    method: 'get',
  })
}

export function roleListResourceById(roleId: number) {
  return http<Permission[]>({
    url: `/api/admin/iam/roles/${roleId}/permissions`,
    method: 'get',
  })
}

export function roleAllocMenuAPI(params: { roleId: number; menuIds: string }) {
  return http<number>({
    url: '/api/admin/iam/roles/menus',
    method: 'put',
    params,
  })
}

export function roleAllocResourceAPI(params: { roleId: number; resourceIds: string }) {
  return http<number>({
    url: '/api/admin/iam/roles/permissions',
    method: 'put',
    params,
  })
}

export const roleUpdateStatusAPI = roleUpdateStatusByIdAPI
export const roleDeleteByIdsAPI = roleDeleteBatchAPI
