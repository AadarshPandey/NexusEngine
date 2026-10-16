import type { CommonPage, PageParam } from '@/types/common'
import type { Category, CategoryParam, CategoryWithChildrenItem } from '@/types/catalog'
import http from '@/utils/http'

export function getProductCategoryListAPI(parentId: number, params: PageParam) {
  const queryParams: Record<string, any> = { ...params }
  if (queryParams.pageNum !== undefined) {
    queryParams.page = (queryParams.pageNum || 1) - 1
  }
  if (queryParams.pageSize !== undefined) {
    queryParams.size = queryParams.pageSize
  }
  return http<CommonPage<Category>>({
    url: `/api/admin/catalog/categories/parent/${parentId}`,
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

export function getProductCategoryListWithChildrenAPI() {
  return http<CategoryWithChildrenItem[]>({
    url: '/api/admin/catalog/categories/tree',
    method: 'get',
  }).then((res: any) => ({ data: res }))
}

export function getProductCategoryByIdAPI(id: number) {
  return http<Category>({
    url: `/api/admin/catalog/categories/${id}`,
    method: 'get',
  }).then((res: any) => ({ data: res }))
}

export function productCategoryCreateAPI(data: CategoryParam) {
  return http({
    url: '/api/admin/catalog/categories',
    method: 'post',
    data,
  })
}

export function productCategoryUpdateByIdAPI(id: number, data: CategoryParam) {
  return http({
    url: `/api/admin/catalog/categories/${id}`,
    method: 'put',
    data,
  })
}

export function productCategoryDeleteByIdAPI(id: number) {
  return http({
    url: `/api/admin/catalog/categories/${id}`,
    method: 'delete',
  })
}

export function productCategoryUpdateNavStatusAPI(params: { ids: string; isInNavigation?: number; navStatus?: number }) {
  return http({
    url: '/api/admin/catalog/categories/navigation-status',
    method: 'patch',
    params: {
      ids: params.ids,
      isInNavigation: params.isInNavigation ?? params.navStatus,
    },
  })
}

export function productCategoryUpdateShowStatusAPI(params: { ids: string; isVisible?: number; showStatus?: number }) {
  return http({
    url: '/api/admin/catalog/categories/visibility',
    method: 'patch',
    params: {
      ids: params.ids,
      isVisible: params.isVisible ?? params.showStatus,
    },
  })
}
