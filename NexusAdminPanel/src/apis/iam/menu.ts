import type { PageParam, PageResponse } from '@/types/common'
import type { ConsoleMenu, ConsoleMenuNode } from '@/types/iam'
import http from '@/utils/http'

export function getMenuListByParentIdAPI(parentId: number, params: PageParam) {
  const queryParams: Record<string, any> = { ...params }
  if (queryParams.pageNum !== undefined) {
    queryParams.page = (queryParams.pageNum || 1) - 1
    delete queryParams.pageNum
  }
  if (queryParams.pageSize !== undefined) {
    queryParams.size = queryParams.pageSize
    delete queryParams.pageSize
  }
  return http<PageResponse<ConsoleMenu>>({
    url: `/api/admin/iam/menus/parent/${parentId}`,
    method: 'get',
    params: queryParams,
  })
}

export function getMenuTreeListAPI() {
  return http<ConsoleMenuNode[]>({
    url: '/api/admin/iam/menus/tree',
    method: 'get',
  })
}

export function getMenuByIdAPI(id: number) {
  return http<ConsoleMenu>({
    url: `/api/admin/iam/menus/${id}`,
    method: 'get',
  })
}

export function menuCreateAPI(data: any) {
  const payload = {
    parentId: data.parentId ?? data.parentReviewId ?? 0,
    title: data.title,
    name: data.name ?? data.routeName,
    icon: data.icon ?? data.iconUrl,
    sort: data.sort ?? data.displayOrder ?? 0,
    hidden: data.hidden !== undefined ? Boolean(data.hidden) : Boolean(data.isHidden),
  }
  return http<number>({
    url: '/api/admin/iam/menus',
    method: 'post',
    data: payload,
  })
}

export function menuUpdateByIdAPI(id: number, data: any) {
  const payload = {
    parentId: data.parentId ?? data.parentReviewId ?? 0,
    title: data.title,
    name: data.name ?? data.routeName,
    icon: data.icon ?? data.iconUrl,
    sort: data.sort ?? data.displayOrder ?? 0,
    hidden: data.hidden !== undefined ? Boolean(data.hidden) : Boolean(data.isHidden),
  }
  return http<number>({
    url: `/api/admin/iam/menus/${id}`,
    method: 'put',
    data: payload,
  })
}

export function deleteMenuByIdAPI(id: number) {
  return http<number>({
    url: `/api/admin/iam/menus/${id}`,
    method: 'delete',
  })
}

export function menuUpdateHiddenByIdAPI(id: number, params: { hidden: number }) {
  return http<number>({
    url: `/api/admin/iam/menus/${id}/visibility`,
    method: 'patch',
    params: {
      hidden: params.hidden,
    },
  })
}
