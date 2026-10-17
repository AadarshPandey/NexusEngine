import type { PageResponse } from '@/types/common'
import type {
  CustomerOrder,
  OrderDeliveryParam,
  OrderMoneyInfoParam,
  OrderQueryParams,
  OrderReceiverInfoParam,
} from '@/types/ordering'
import http from '@/utils/http'

export function getOrderListAPI(params: OrderQueryParams) {
  const queryParams: Record<string, any> = { ...params }
  if (queryParams.pageNum !== undefined) {
    queryParams.page = (queryParams.pageNum || 1) - 1
    delete queryParams.pageNum
  }
  if (queryParams.pageSize !== undefined) {
    queryParams.size = queryParams.pageSize
    delete queryParams.pageSize
  }
  return http<CustomerOrder[]>({
    url: '/api/admin/ordering/orders',
    method: 'get',
    params: queryParams,
  })
}

export function getOrderDetailByIdAPI(id: number) {
  return http<CustomerOrder>({
    url: `/api/admin/ordering/orders/${id}`,
    method: 'get',
  })
}

export const getOrderDetailAPI = getOrderDetailByIdAPI

export function orderDeliveryAPI(data: OrderDeliveryParam[]) {
  return http<number>({
    url: '/api/admin/ordering/orders/delivery',
    method: 'post',
    data,
  })
}

export function orderCloseBatchAPI(params: { ids: string; note: string }) {
  return http<number>({
    url: '/api/admin/ordering/orders/cancel',
    method: 'patch',
    params,
  })
}

export function orderDeleteBatchAPI(params: { ids: string }) {
  return http<number>({
    url: '/api/admin/ordering/orders',
    method: 'delete',
    params,
  })
}

export function orderUpdateReceiverInfoAPI(data: OrderReceiverInfoParam) {
  return http<number>({
    url: '/api/admin/ordering/orders/receiver-info',
    method: 'patch',
    data,
  })
}

export function orderUpdateMoneyInfoAPI(data: OrderMoneyInfoParam) {
  return http<number>({
    url: '/api/admin/ordering/orders/money-info',
    method: 'patch',
    data,
  })
}

export function orderUpdateNoteAPI(params: { id: number; note: string; status: number }) {
  return http<number>({
    url: '/api/admin/ordering/orders/note',
    method: 'patch',
    params,
  })
}

export function orderUpdateStatusAPI(params: { ids: string; status: number; note?: string }) {
  return http<number>({
    url: '/api/admin/ordering/orders/status',
    method: 'patch',
    params,
  })
}
