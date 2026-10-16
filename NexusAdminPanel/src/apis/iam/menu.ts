import type { CommonPage, PageParam } from '@/types/common'
import type { ConsoleMenu, ConsoleMenuNode } from '@/types/iam'
import http from '@/utils/http'

export function getMenuListByParentIdAPI(parentId: number, params: PageParam) {
  const queryParams: Record<string, any> = { ...params }
  if (queryParams.pageNum !== undefined) {
    queryParams.page = (queryParams.pageNum || 1) - 1
  }
  if (queryParams.pageSize !== undefined) {
    queryParams.size = queryParams.pageSize
  }
  return http<CommonPage<ConsoleMenu>>({
    url: `/api/admin/iam/menus/parent/${parentId}`,
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

export function getMenuTreeListAPI() {
  return http<ConsoleMenuNode[]>({
    url: '/api/admin/iam/menus/tree',
    method: 'get',
  }).then((res: any) => ({ data: res }))
}

export function getMenuByIdAPI(id: number) {
  return http<ConsoleMenu>({
    url: `/api/admin/iam/menus/${id}`,
    method: 'get',
  }).then((res: any) => ({ data: res }))
}

export function menuCreateAPI(data: any) {
  const payload = {
    ...data,
    displayOrder: data.displayOrder ?? data.sort ?? 0,
    isHidden: data.isHidden !== undefined ? Boolean(data.isHidden) : (data.hidden === 1),
  }
  return http({
    url: '/api/admin/iam/menus',
    method: 'post',
    data: payload,
  })
}

export function menuUpdateByIdAPI(id: number, data: any) {
  const payload = {
    ...data,
    displayOrder: data.displayOrder ?? data.sort ?? 0,
    isHidden: data.isHidden !== undefined ? Boolean(data.isHidden) : (data.hidden === 1),
  }
  return http({
    url: `/api/admin/iam/menus/${id}`,
    method: 'put',
    data: payload,
  })
}

export function deleteMenuByIdAPI(id: number) {
  return http({
    url: `/api/admin/iam/menus/${id}`,
    method: 'delete',
  })
}

export function menuUpdateHiddenByIdAPI(id: number, params: { hidden: number }) {
  return http({
    url: `/api/admin/iam/menus/${id}/visibility`,
    method: 'patch',
    params: {
      hidden: params.hidden,
    },
  })
}
