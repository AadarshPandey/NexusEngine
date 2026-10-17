import type { DashboardInfo } from '@/types/analytics'
import http from '@/utils/http'

export type { DashboardInfo } from '@/types/analytics'

export function getDashboardInfoAPI() {
  return http<DashboardInfo>({
    url: '/api/admin/analytics/dashboard',
    method: 'get',
  })
}
