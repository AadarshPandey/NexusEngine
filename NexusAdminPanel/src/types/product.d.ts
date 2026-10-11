import type { PageParam } from './common'
import type { PmsProductAttributeValue } from './skuAttributes'
import type { PmsSkuStock } from './skuStock'
import type { Product, ProductSku, Brand, Category } from './catalog'

export type { Product, ProductSku, Brand, Category } from './catalog'

export type PmsProduct = {
  /** ID */
  id?: number
  brandId?: number
  permissionGroupId?: number
  productCategoryId?: number
  productTypeId?: number
  productAttributeCategoryId?: number
  name: string
  slug?: string
  primaryImageUrl?: string
  pic?: string
  productCode: string
  productSn?: string
  deletedAt?: number | string | null
  deleteStatus?: number
  isPublished?: number
  publishStatus?: number
  isNewArrival?: number
  newStatus?: number
  isFeatured?: number
  recommendStatus?: number
  approvalStatus?: number
  verifyStatus?: number
  unitsSold?: number
  sale?: number
  listPrice?: number
  salePrice?: number
  price?: number
  promotionPrice?: number
  giftGrowth?: number
  originalPrice?: number
  onHandQty?: number
  stock?: number
  lowStockThreshold?: number
  lowStock?: number
  searchKeywords?: string | string[]
  keywords?: string
  albumPics?: string
  brandName?: string
  customerComment?: string
  description?: string
  skuStockList?: PmsSkuStock[]
  productSkuList?: ProductSku[]
}

export type ProductQueryParam = PageParam & {
  isPublished?: number | boolean
  approvalStatus?: number
  productCode?: string
  productSn?: string
  permissionGroupId?: number
  productCategoryId?: number
  brandId?: number
}

export type PmsProductParam = PmsProduct & {
  productVolumeDiscountList?: PmsProductVolumeDiscount[]
  productSpendDiscountList?: PmsProductSpendDiscount[]
  memberPriceList?: PmsMemberPrice[]
  skuStockList?: PmsSkuStock[]
  productSkuList?: ProductSku[]
  productAttributeValueList?: PmsProductAttributeValue[]
  subjectProductRelationList?: CmsSubjectProductRelation[]
  prefrenceAreaProductRelationList?: CmsPrefrenceAreaProductRelation[]
  cateParentId?: number
  quantityAvailable?: number
  flashSaleId?: number
  flashPrice?: number
  flashPromotionSort?: number
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
