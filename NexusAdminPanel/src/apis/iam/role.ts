import type { CommonPage, PageParam } from '@/types/common'
import type { ConsoleMenu, Permission, Role } from '@/types/iam'
import http from '@/utils/http'

export function getRoleListAPI(params: PageParam & { keyword?: string }) {
  const queryParams: Record<string, any> = { ...params }
  if (queryParams.pageNum !== undefined) {
    queryParams.page = (queryParams.pageNum || 1) - 1
  }
  if (queryParams.pageSize !== undefined) {
    queryParams.size = queryParams.pageSize
  }
  return http<CommonPage<Role>>({
    url: '/api/admin/iam/roles',
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

export function getRoleListAllAPI() {
  return http<Role[]>({
    url: '/api/admin/iam/roles/all',
    method: 'get',
  }).then((res: any) => ({ data: res }))
}

export function roleCreateAPI(data: { name: string; description?: string; status?: number; isActive?: boolean | number; sort?: number; displayOrder?: number }) {
  const payload = {
    name: data.name,
    description: data.description,
    displayOrder: data.displayOrder ?? data.sort ?? 0,
    isActive: data.isActive !== undefined ? Boolean(data.isActive) : (data.status === 1),
  }
  return http({
    url: '/api/admin/iam/roles',
    method: 'post',
    data: payload,
  })
}

export function roleUpdateByIdAPI(id: number, data: any) {
  const payload = {
    name: data.name,
    description: data.description,
    displayOrder: data.displayOrder ?? data.sort ?? 0,
    isActive: data.isActive !== undefined ? Boolean(data.isActive) : (data.status === 1),
  }
  return http({
    url: `/api/admin/iam/roles/${id}`,
    method: 'put',
    data: payload,
  })
}

export function roleDeleteBatchAPI(params: { ids: string }) {
  return http({
    url: '/api/admin/iam/roles',
    method: 'delete',
    params,
  })
}

export function roleUpdateStatusByIdAPI(id: number, params: { status: number }) {
  return http({
    url: `/api/admin/iam/roles/${id}/status`,
    method: 'patch',
    params,
  })
}

export function roleListMenuByRoleIdAPI(roleId: number) {
  return http<ConsoleMenu[]>({
    url: `/api/admin/iam/roles/${roleId}/menus`,
    method: 'get',
  }).then((res: any) => ({ data: res }))
}

export function roleListResourceById(roleId: number) {
  return http<Permission[]>({
    url: `/api/admin/iam/roles/${roleId}/permissions`,
    method: 'get',
  }).then((res: any) => ({ data: res }))
}

export function roleAllocMenuAPI(params: { roleId: number; menuIds: string }) {
  return http({
    url: '/api/admin/iam/roles/menus',
    method: 'put',
    params,
  })
}

export function roleAllocResourceAPI(params: { roleId: number; resourceIds: string }) {
  return http({
    url: '/api/admin/iam/roles/permissions',
    method: 'put',
    params,
  })
}

export const roleUpdateStatusAPI = roleUpdateStatusByIdAPI
export const roleDeleteByIdsAPI = roleDeleteBatchAPI

