import type { CommonPage } from '@/types/common'
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
  }
  if (queryParams.pageSize !== undefined) {
    queryParams.size = queryParams.pageSize
  }
  return http<CustomerOrder[]>({
    url: '/api/admin/ordering/orders',
    method: 'get',
    params: queryParams,
  }).then((res: any) => {
    // If backend returns a raw list, wrap in list/total
    if (Array.isArray(res)) {
      return { data: { list: res, total: res.length } }
    }
    if (res && res.content && res.list === undefined) {
      res.list = res.content
      res.total = res.totalElements ?? res.content.length
    }
    return { data: res }
  })
}

export function getOrderDetailByIdAPI(id: number) {
  return http<CustomerOrder>({
    url: `/api/admin/ordering/orders/${id}`,
    method: 'get',
  }).then((res: any) => ({ data: res }))
}

export function orderDeliveryAPI(data: OrderDeliveryParam[]) {
  return http({
    url: '/api/admin/ordering/orders/delivery',
    method: 'post',
    data,
  })
}

export function orderCloseBatchAPI(params: { ids: string; note: string }) {
  return http({
    url: '/api/admin/ordering/orders/cancel',
    method: 'patch',
    params,
  })
}

export function orderDeleteBatchAPI(params: { ids: string }) {
  return http({
    url: '/api/admin/ordering/orders',
    method: 'delete',
    params,
  })
}

export function orderUpdateReceiverInfoAPI(data: OrderReceiverInfoParam) {
  return http({
    url: '/api/admin/ordering/orders/receiver-info',
    method: 'patch',
    data,
  })
}

export function orderUpdateMoneyInfoAPI(data: OrderMoneyInfoParam) {
  return http({
    url: '/api/admin/ordering/orders/money-info',
    method: 'patch',
    data,
  })
}

export function orderUpdateNoteAPI(params: { id: number; note: string; status: number }) {
  return http({
    url: '/api/admin/ordering/orders/note',
    method: 'patch',
    params,
  })
}

export function orderUpdateStatusAPI(params: { ids: string; status: number; note?: string }) {
  return http({
    url: '/api/admin/ordering/orders/status',
    method: 'patch',
    params,
  })
}
