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
  recommandStatus?: number
  verifyStatus?: number
  sort?: number
  sale?: number
  price?: number
  promotionPrice?: number
  giftGrowth?: number
  giftPoint?: number
  usePointLimit?: number
  subTitle?: string
  originalPrice?: number
  stock?: number
  lowStock?: number
  unit?: string
  weight?: number
  previewStatus?: number
  serviceIds?: string
  keywords?: string
  note?: string
  albumPics?: string
  detailTitle?: string
  promotionStartTime?: string
  promotionEndTime?: string
  promotionPerLimit?: number
  promotionType?: number
  brandName?: string
  productCategoryName?: string
  description?: string
  detailDesc?: string
  detailHtml?: string
  detailMobileHtml?: string
}

export type ProductQueryParam = PageParam & {
  publishStatus?: number
  verifyStatus?: number
  productSn?: string
  productCategoryId?: number
  brandId?: number
}

export type PmsProductParam = PmsProduct & {
  productLadderList?: PmsProductLadder[]
  productFullReductionList?: PmsProductFullReduction[]
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

export type PmsProductLadder = {
  /** ID */
  id?: number
  productId?: number
  count: number
  discount?: number
  price?: number
}

export type PmsProductFullReduction = {
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
  memberLevelId?: number
  memberPrice?: number
  memberLevelName?: string
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
