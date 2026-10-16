import type { ProductSku } from '@/types/catalog'
import http from '@/utils/http'

export function getSkuStockByPidAPI(pid: number, params?: { keyword?: string }) {
  return http<ProductSku[]>({
    url: `/api/admin/catalog/skus/${pid}`,
    method: 'get',
    params,
  }).then((res: any) => ({ data: res }))
}

export function skuStockUpdateAPI(pid: number, data: ProductSku[]) {
  return http({
    url: `/api/admin/catalog/skus/${pid}`,
    method: 'put',
    data,
  })
}
