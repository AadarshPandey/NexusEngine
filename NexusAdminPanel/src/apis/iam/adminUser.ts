import type { PageParam, PageResponse } from '@/types/common'
import type { AdminInfoResponse, AdminUser, AdminUserParam, LoginParam, LoginResult, Role } from '@/types/iam'
import http from '@/utils/http'

export function adminLoginAPI(data: LoginParam) {
  return http<LoginResult>({
    url: '/api/admin/iam/users/login',
    method: 'post',
    data,
  })
}

export function getAdminInfoAPI() {
  return http<AdminInfoResponse>({
    url: '/api/admin/iam/users/me',
    method: 'get',
  })
}

export function adminLogoutAPI() {
  return http<void>({
    url: '/api/admin/iam/users/logout',
    method: 'post',
  })
}

export function refreshTokenAPI() {
  return http<LoginResult>({
    url: '/api/admin/iam/users/refresh-token',
    method: 'get',
  })
}

export function getAdminListAPI(params: PageParam & { keyword?: string }) {
  const queryParams: Record<string, any> = { ...params }
  if (queryParams.pageNum !== undefined) {
    queryParams.page = (queryParams.pageNum || 1) - 1
    delete queryParams.pageNum
  }
  if (queryParams.pageSize !== undefined) {
    queryParams.size = queryParams.pageSize
    delete queryParams.pageSize
  }
  return http<PageResponse<AdminUser>>({
    url: '/api/admin/iam/users',
    method: 'get',
    params: queryParams,
  })
}

export function adminRegisterAPI(data: AdminUserParam) {
  return http<AdminUser>({
    url: '/api/admin/iam/users/register',
    method: 'post',
    data,
  })
}

export function adminUpdateByIdAPI(id: number, data: AdminUserParam) {
  return http<number>({
    url: `/api/admin/iam/users/${id}`,
    method: 'put',
    data,
  })
}

export function adminUpdatePasswordAPI(data: any) {
  return http<number>({
    url: '/api/admin/iam/users/password',
    method: 'patch',
    data,
  })
}

export function adminDeleteByIdAPI(id: number) {
  return http<number>({
    url: `/api/admin/iam/users/${id}`,
    method: 'delete',
  })
}

export function adminUpdateStatusByIdAPI(id: number, params: { status: number }) {
  return http<number>({
    url: `/api/admin/iam/users/${id}/status`,
    method: 'patch',
    params,
  })
}

export function adminRoleUpdateAPI(data: { adminId?: number; adminUserId?: number; roleIds: string | number[] }) {
  const adminId = data.adminId ?? data.adminUserId!
  const roleIdList = Array.isArray(data.roleIds)
    ? data.roleIds
    : String(data.roleIds).split(',').filter(Boolean).map(Number)
  return http<number>({
    url: '/api/admin/iam/users/roles',
    method: 'put',
    params: {
      adminId,
      roleIds: roleIdList.join(','),
    },
  })
}

export function adminGetRoleListByAdminIdAPI(adminId: number) {
  return http<Role[]>({
    url: `/api/admin/iam/users/${adminId}/roles`,
    method: 'get',
  })
}

export const getRoleByAdminIdAPI = adminGetRoleListByAdminIdAPI

export interface AdminLoginEvent {
  id: number
  adminId?: number
  adminUserId?: number
  occurredAt?: string
  createTime?: string
  ipAddress?: string
  ip?: string
}

export function adminGetLoginEventsByAdminIdAPI(adminId: number) {
  return http<AdminLoginEvent[]>({
    url: `/api/admin/iam/users/${adminId}/login-events`,
    method: 'get',
  })
}

export const getLoginEventsByAdminIdAPI = adminGetLoginEventsByAdminIdAPI

