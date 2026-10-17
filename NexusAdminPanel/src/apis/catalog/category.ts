import type { PageParam, PageResponse } from '@/types/common'
import type { Category, CategoryParam, CategoryWithChildrenItem } from '@/types/catalog'
import http from '@/utils/http'

export function getProductCategoryListAPI(parentId: number, params: PageParam) {
  const queryParams: Record<string, any> = { ...params }
  if (queryParams.pageNum !== undefined) {
    queryParams.page = (queryParams.pageNum || 1) - 1
    delete queryParams.pageNum
  }
  if (queryParams.pageSize !== undefined) {
    queryParams.size = queryParams.pageSize
    delete queryParams.pageSize
  }
  return http<PageResponse<Category>>({
    url: `/api/admin/catalog/categories/parent/${parentId}`,
    method: 'get',
    params: queryParams,
  })
}

export function getProductCategoryListWithChildrenAPI() {
  return http<CategoryWithChildrenItem[]>({
    url: '/api/admin/catalog/categories/tree',
    method: 'get',
  })
}

export function getProductCategoryByIdAPI(id: number) {
  return http<Category>({
    url: `/api/admin/catalog/categories/${id}`,
    method: 'get',
  })
}

export function productCategoryCreateAPI(data: CategoryParam) {
  return http<number>({
    url: '/api/admin/catalog/categories',
    method: 'post',
    data,
  })
}

export function productCategoryUpdateByIdAPI(id: number, data: CategoryParam) {
  return http<number>({
    url: `/api/admin/catalog/categories/${id}`,
    method: 'put',
    data,
  })
}

export function productCategoryDeleteByIdAPI(id: number) {
  return http<number>({
    url: `/api/admin/catalog/categories/${id}`,
    method: 'delete',
  })
}

export function productCategoryUpdateNavStatusAPI(params: { ids: string; isInNavigation?: number; navStatus?: number }) {
  return http<number>({
    url: '/api/admin/catalog/categories/navigation-status',
    method: 'patch',
    params: {
      ids: params.ids,
      isInNavigation: params.isInNavigation ?? params.navStatus,
    },
  })
}

export function productCategoryUpdateShowStatusAPI(params: { ids: string; isVisible?: number; showStatus?: number }) {
  return http<number>({
    url: '/api/admin/catalog/categories/visibility',
    method: 'patch',
    params: {
      ids: params.ids,
      isVisible: params.isVisible ?? params.showStatus,
    },
  })
}
