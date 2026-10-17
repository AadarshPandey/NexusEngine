import type { PermissionGroup } from '@/types/iam'
import http from '@/utils/http'

export function resourceCategoryListAllAPI() {
  return http<PermissionGroup[]>({
    url: '/api/admin/iam/permission-groups',
    method: 'get',
  })
}

export function resourceCategoryCreateAPI(data: any) {
  return http<number>({
    url: '/api/admin/iam/permission-groups',
    method: 'post',
    data,
  })
}

export function resourceCategoryUpdateByIdAPI(id: number, data: any) {
  return http<number>({
    url: `/api/admin/iam/permission-groups/${id}`,
    method: 'put',
    data,
  })
}

export function resourceCategoryDeleteByIdAPI(id: number) {
  return http<number>({
    url: `/api/admin/iam/permission-groups/${id}`,
    method: 'delete',
  })
}
