import type { CommonPage, PageParam } from '@/types/common'
import type { AdminInfoResponse, AdminUser, AdminUserParam, LoginParam, LoginResult, Role } from '@/types/iam'
import http from '@/utils/http'

export function adminLoginAPI(data: LoginParam) {
  return http<LoginResult>({
    url: '/api/admin/iam/users/login',
    method: 'post',
    data,
  }).then((res: any) => ({ data: res }))
}

export function getAdminInfoAPI() {
  return http<AdminInfoResponse>({
    url: '/api/admin/iam/users/me',
    method: 'get',
  }).then((res: any) => ({ data: res }))
}

export function adminLogoutAPI() {
  return http({
    url: '/api/admin/iam/users/logout',
    method: 'post',
  })
}

export function refreshTokenAPI() {
  return http<LoginResult>({
    url: '/api/admin/iam/users/refresh-token',
    method: 'get',
  }).then((res: any) => ({ data: res }))
}

export function getAdminListAPI(params: PageParam & { keyword?: string }) {
  const queryParams: Record<string, any> = { ...params }
  if (queryParams.pageNum !== undefined) {
    queryParams.page = (queryParams.pageNum || 1) - 1
  }
  if (queryParams.pageSize !== undefined) {
    queryParams.size = queryParams.pageSize
  }
  return http<CommonPage<AdminUser>>({
    url: '/api/admin/iam/users',
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

export function adminRegisterAPI(data: AdminUserParam) {
  return http({
    url: '/api/admin/iam/users/register',
    method: 'post',
    data,
  })
}

export function adminUpdateByIdAPI(id: number, data: AdminUserParam) {
  return http({
    url: `/api/admin/iam/users/${id}`,
    method: 'put',
    data,
  })
}

export function adminUpdatePasswordAPI(data: any) {
  return http({
    url: '/api/admin/iam/users/password',
    method: 'put',
    data,
  })
}

export function adminDeleteByIdAPI(id: number) {
  return http({
    url: `/api/admin/iam/users/${id}`,
    method: 'delete',
  })
}

export function adminUpdateStatusByIdAPI(id: number, params: { status: number }) {
  return http({
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
  return http({
    url: '/api/admin/iam/users/roles',
    method: 'put',
    params: {
      adminId,
      roleIds: roleIdList.join(','),
    },
  })
}

export function adminGetRoleListByAdminIdAPI(adminId: number): Promise<{ data: Role[] }> {
  return http<Role[]>({
    url: `/api/admin/iam/users/${adminId}/roles`,
    method: 'get',
  }).then((res: any) => ({ data: (Array.isArray(res) ? res : res?.data || []) as Role[] }))
}

export const getRoleByAdminIdAPI = adminGetRoleListByAdminIdAPI

