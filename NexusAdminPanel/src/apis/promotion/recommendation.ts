import type { CommonPage } from '@/types/common'
import type {
  CmsPrefrenceArea,
  CmsSubject,
  HomeBrandItem,
  HomeBrandQueryParam,
  HomeNewItem,
  HomeSubjectItem,
  HomeSubjectQueryParam,
  NewProductQueryParam,
  RecommendProduct,
  RecommendProductQueryParam,
} from '@/types/promotion'
import http from '@/utils/http'

// Home Brand recommendations
export function getHomeBrandListAPI(params: HomeBrandQueryParam) {
  return http<CommonPage<HomeBrandItem>>({
    url: '/home/brand/list',
    method: 'get',
    params,
  }).then((res: any) => ({ data: res }))
}

export function homeBrandUpdateRecommendStatusAPI(params: { ids: string; isFeatured?: number; recommendStatus?: number }) {
  return http({
    url: '/home/brand/update/isFeatured',
    method: 'post',
    params,
  })
}

export function homeBrandDeleteByIdsAPI(params: { ids: string }) {
  return http({
    url: '/home/brand/delete',
    method: 'post',
    params,
  })
}

export function homeBrandCreateAPI(data: HomeBrandItem[]) {
  return http({
    url: '/home/brand/create',
    method: 'post',
    data,
  })
}

export function homeBrandUpdateSortAPI(params: { id: number; displayOrder?: number; sort?: number }) {
  return http({
    url: `/home/brand/update/displayOrder/${params.id}`,
    method: 'post',
    params,
  })
}

// Home Recommend Product (Hot)
export function getHomeRecommendProductListAPI(params: RecommendProductQueryParam) {
  return http<CommonPage<RecommendProduct>>({
    url: '/home/recommendProduct/list',
    method: 'get',
    params,
  }).then((res: any) => ({ data: res }))
}

export function homeRecommendProductUpdateRecommendStatusAPI(params: { ids: string; isFeatured?: number; recommendStatus?: number }) {
  return http({
    url: '/home/recommendProduct/update/isFeatured',
    method: 'post',
    params,
  })
}

export function homeRecommendProductDeleteByIdsAPI(params: { ids: string }) {
  return http({
    url: '/home/recommendProduct/delete',
    method: 'post',
    params,
  })
}

export function homeRecommendProductCreateAPI(data: RecommendProduct[]) {
  return http({
    url: '/home/recommendProduct/create',
    method: 'post',
    data,
  })
}

export function homeRecommendProductUpdateSortByIdAPI(params: { id: number; displayOrder?: number; sort?: number }) {
  return http({
    url: `/home/recommendProduct/update/displayOrder/${params.id}`,
    method: 'post',
    params,
  })
}

// Home New Product
export function getHomeNewProductListAPI(params: NewProductQueryParam) {
  return http<CommonPage<HomeNewItem>>({
    url: '/home/newProduct/list',
    method: 'get',
    params,
  }).then((res: any) => ({ data: res }))
}

export function homeNewProductUpdateRecommendStatusAPI(params: { ids: string; isFeatured?: number; recommendStatus?: number }) {
  return http({
    url: '/home/newProduct/update/isFeatured',
    method: 'post',
    params,
  })
}

export function homeNewProductDeleteByIdsAPI(params: { ids: string }) {
  return http({
    url: '/home/newProduct/delete',
    method: 'post',
    params,
  })
}

export function homeNewProductCreateAPI(data: HomeNewItem[]) {
  return http({
    url: '/home/newProduct/create',
    method: 'post',
    data,
  })
}

export function homeNewProductUpdateSortByIdAPI(params: { id: number; displayOrder?: number; sort?: number }) {
  return http({
    url: `/home/newProduct/update/displayOrder/${params.id}`,
    method: 'post',
    params,
  })
}

// Home Subject
export function getHomeSubjectListAPI(params: HomeSubjectQueryParam) {
  return http<CommonPage<HomeSubjectItem>>({
    url: '/home/subject/list',
    method: 'get',
    params,
  }).then((res: any) => ({ data: res }))
}

export function homeSubjectUpdateRecommendStatusAPI(params: { ids: string; isFeatured?: number; recommendStatus?: number }) {
  return http({
    url: '/home/subject/update/isFeatured',
    method: 'post',
    params,
  })
}

export function homeSubjectDeleteByIdsAPI(params: { ids: string }) {
  return http({
    url: '/home/subject/delete',
    method: 'post',
    params,
  })
}

export function homeSubjectCreateAPI(data: HomeSubjectItem[]) {
  return http({
    url: '/home/subject/create',
    method: 'post',
    data,
  })
}

export function homeSubjectUpdateSortByIdAPI(params: { id: number; displayOrder?: number; sort?: number }) {
  return http({
    url: `/home/subject/update/displayOrder/${params.id}`,
    method: 'post',
    params,
  })
}

// Subject & Preference Area generic lookups
export function getSubjectListAPI(params?: any) {
  return http<CommonPage<CmsSubject>>({
    url: '/subject/list',
    method: 'get',
    params,
  }).then((res: any) => ({ data: res }))
}

export function getSubjectListAllAPI() {
  return http<CmsSubject[]>({
    url: '/subject/listAll',
    method: 'get',
  }).then((res: any) => ({ data: res }))
}

export function getPrefrenceAreaListAllAPI() {
  return http<CmsPrefrenceArea[]>({
    url: '/prefrenceArea/listAll',
    method: 'get',
  }).then((res: any) => ({ data: res }))
}

export const getHomeRecommendSubjectListAPI = getHomeSubjectListAPI
export const homeRecommendSubjectUpdateRecommendStatusAPI = homeSubjectUpdateRecommendStatusAPI
export const homeRecommendSubjectDeleteByIdsAPI = homeSubjectDeleteByIdsAPI
export const homeRecommendSubjectUpdateSortAPI = homeSubjectUpdateSortByIdAPI
export const homeRecommendSubjectCreateAPI = homeSubjectCreateAPI

