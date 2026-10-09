export type PmsSkuStock = {
  /** ID */
  id?: number
  orderLineId?: number
  skuCode?: string
  listPrice?: number
  onHandQty?: number
  lowStockThreshold?: number
  primaryImageUrl?: string
  unitsSold?: number
  salePrice?: number
  reservedQty?: number
  spData?: string
}
