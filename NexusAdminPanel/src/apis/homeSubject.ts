import type { CommonPage } from '@/types/common'
import type { HomeSubjectQueryParam, SmsHomeRecommendSubject } from '@/types/homeSubject'
import http from '@/utils/http'

/**
 */
export function getHomeRecommendSubjectListAPI(params: HomeSubjectQueryParam) {
  return http<CommonPage<SmsHomeRecommendSubject>>({
    url: '/home/recommendSubject/list',
    method: 'get',
    params: params,
  })
}

/**
 */
export function homeRecommendSubjectUpdateRecommendStatusAPI(params: {
  ids: string
  isFeatured: number
}) {
  return http({
    url: '/home/recommendSubject/update/isFeatured',
    method: 'post',
    params: params,
  })
}

/**
 */
export function homeRecommendSubjectDeleteByIdsAPI(params: { ids: string }) {
  return http({
    url: '/home/recommendSubject/delete',
    method: 'post',
    params: params,
  })
}

/**
 */
export function homeRecommendSubjectCreateAPI(data: SmsHomeRecommendSubject[]) {
  return http({
    url: '/home/recommendSubject/create',
    method: 'post',
    data: data,
  })
}

/**
 */
export function homeRecommendSubjectUpdateSortAPI(params: { id: number; displayOrder: number }) {
  return http({
    url: '/home/recommendSubject/update/displayOrder/' + params.id,
    method: 'post',
    params: params,
  })
}
