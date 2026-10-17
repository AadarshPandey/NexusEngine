import type { OrderPolicy } from '@/types/ordering'
import http from '@/utils/http'

export function getOrderSettingByIdAPI(id: number) {
  return http<OrderPolicy>({
    url: `/api/admin/ordering/policies/${id}`,
    method: 'get',
  })
}

export function orderSettingUpdateByIdAPI(id: number, data: OrderPolicy) {
  return http<number>({
    url: `/api/admin/ordering/policies/${id}`,
    method: 'put',
    data,
  })
}
