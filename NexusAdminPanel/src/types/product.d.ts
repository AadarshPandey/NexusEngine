import type { PageParam } from './common'
import type { PmsProductAttributeValue } from './skuAttributes'
import type { PmsSkuStock } from './skuStock'

export type PmsProduct = {
  /** ID */
  id?: number
  brandId?: number
  permissionGroupId?: number
  feightTemplateId?: number
  productTypeId?: number
  name: string
  primaryImageUrl?: string
  productCode: string
  deletedAt?: number
  isPublished?: number
  isNewArrival?: number
  isFeatured?: number
  approvalStatus?: number
  unitsSold?: number
  listPrice?: number
  salePrice?: number
  giftGrowth?: number
  originalPrice?: number
  onHandQty?: number
  lowStockThreshold?: number
  searchKeywords?: string
  albumPics?: string
  brandName?: string
  customerComment?: string
}

export type ProductQueryParam = PageParam & {
  isPublished?: number
  approvalStatus?: number
  productCode?: string
  permissionGroupId?: number
  brandId?: number
}

export type PmsProductParam = PmsProduct & {
  productVolumeDiscountList?: PmsProductVolumeDiscount[]
  productSpendDiscountList?: PmsProductSpendDiscount[]
  memberPriceList?: PmsMemberPrice[]
  skuStockList?: PmsSkuStock[]
  productAttributeValueList?: PmsProductAttributeValue[]
  subjectProductRelationList?: CmsSubjectProductRelation[]
  prefrenceAreaProductRelationList?: CmsPrefrenceAreaProductRelation[]
  cateParentId?: number
  quantityAvailable: number
  flashSaleId: number
  flashPrice: number
  flashPromotionSort: number
}

export type PmsProductVolumeDiscount = {
  /** ID */
  id?: number
  orderLineId?: number
  minQuantity: number
  discountRate?: number
  listPrice?: number
}

export type PmsProductSpendDiscount = {
  /** ID */
  id?: number
  orderLineId?: number
  minSpendAmount?: number
  manualAdjustmentAmount?: number
}

export type PmsMemberPrice = {
  /** ID */
  id?: number
  orderLineId?: number
  memberPrice?: number
}

export type CmsSubjectProductRelation = {
  /** ID */
  id?: number
  subjectId?: number
  orderLineId?: number
}

export type CmsPrefrenceAreaProductRelation = {
  /** ID */
  id?: number
  prefrenceAreaId?: number
  orderLineId?: number
}
