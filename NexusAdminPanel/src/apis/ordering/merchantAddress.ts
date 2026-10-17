import type { MerchantAddress } from '@/types/ordering'
import http from '@/utils/http'

export function getCompanyAddressListAllAPI() {
  return http<MerchantAddress[]>({
    url: '/api/admin/ordering/merchant-addresses',
    method: 'get',
  })
}

export function createCompanyAddressAPI(data: Partial<MerchantAddress>) {
  return http<MerchantAddress>({
    url: '/api/admin/ordering/merchant-addresses',
    method: 'post',
    data,
  })
}

export function updateCompanyAddressAPI(id: number, data: Partial<MerchantAddress>) {
  return http<MerchantAddress>({
    url: `/api/admin/ordering/merchant-addresses/${id}`,
    method: 'put',
    data,
  })
}

export function deleteCompanyAddressAPI(id: number) {
  return http<number>({
    url: `/api/admin/ordering/merchant-addresses/${id}`,
    method: 'delete',
  })
}

