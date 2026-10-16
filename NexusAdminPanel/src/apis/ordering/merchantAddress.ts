import type { MerchantAddress } from '@/types/ordering'
import http from '@/utils/http'

export function getCompanyAddressListAllAPI() {
  return http<MerchantAddress[]>({
    url: '/api/admin/ordering/merchant-addresses',
    method: 'get',
  }).then((res: any) => ({ data: res }))
}
