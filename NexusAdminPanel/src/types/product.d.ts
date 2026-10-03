import type { PageParam } from './common'
import type { PmsProductAttributeValue } from './productAttr'
import type { PmsSkuStock } from './skuStock'

export type PmsProduct = {
  /** ID */
  id?: number
  brandId?: number
  productCategoryId?: number
  feightTemplateId?: number
  productAttributeCategoryId?: number
  name: string
  pic?: string
  productSn: string
  deleteStatus?: number
  publishStatus?: number
  newStatus?: number
  recommendStatus?: number
  verifyStatus?: number
  sale?: number
  price?: number
  promotionPrice?: number
  giftGrowth?: number
  originalPrice?: number
  stock?: number
  lowStock?: number
  keywords?: string
  albumPics?: string
  brandName?: string
  description?: string
}

export type ProductQueryParam = PageParam & {
  publishStatus?: number
  verifyStatus?: number
  productSn?: string
  productCategoryId?: number
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
  flashPromotionCount: number
  flashPromotionId: number
  flashPromotionPrice: number
  flashPromotionSort: number
}

export type PmsProductVolumeDiscount = {
  /** ID */
  id?: number
  productId?: number
  count: number
  discount?: number
  price?: number
}

export type PmsProductSpendDiscount = {
  /** ID */
  id?: number
  productId?: number
  fullPrice?: number
  reducePrice?: number
}

export type PmsMemberPrice = {
  /** ID */
  id?: number
  productId?: number
  memberPrice?: number
}

export type CmsSubjectProductRelation = {
  /** ID */
  id?: number
  subjectId?: number
  productId?: number
}

export type CmsPrefrenceAreaProductRelation = {
  /** ID */
  id?: number
  prefrenceAreaId?: number
  productId?: number
}
