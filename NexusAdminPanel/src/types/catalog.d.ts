/**
 * Catalog Bounded Context Type Definitions
 * Aligned with schema_redesign.md (catalog schema) and BackendMigrationPlan.md
 */

import type { PageParam } from './common'

export interface Product {
  id?: number
  brandId?: number
  categoryId?: number
  productCategoryId?: number
  productTypeId?: number
  productAttributeCategoryId?: number
  name: string
  slug?: string
  primaryImageUrl?: string
  pic?: string
  productCode: string
  productSn?: string
  isPublished?: boolean | number
  publishStatus?: number
  isNewArrival?: boolean | number
  newStatus?: number
  isFeatured?: boolean | number
  recommendStatus?: number
  approvalStatus?: number // 0: PENDING, 1: APPROVED, 2: REJECTED
  verifyStatus?: number
  unitsSold?: number
  sale?: number
  listPrice?: number
  salePrice?: number
  price?: number
  originalPrice?: number
  subTitle?: string
  description?: string
  searchKeywords?: string | string[]
  keywords?: string
  albumPics?: string
  brandName?: string
  categoryName?: string
  productSkuList?: ProductSku[]
  skuStockList?: ProductSku[]
  mediaGallery?: ProductMedia[]
  attributeValues?: ProductAttributeValue[]
  onHandQty?: number
  stock?: number
  createdAt?: string
  updatedAt?: string
  deletedAt?: string | number | null
}

export interface ProductSku {
  id?: number
  productId?: number
  skuCode: string
  listPrice?: number
  salePrice?: number
  price?: number
  promotionPrice?: number
  onHandQty?: number
  reservedQty?: number
  availableQty?: number
  stock?: number
  lowStockThreshold?: number
  lowStock?: number
  imageUrl?: string
  pic?: string
  unitsSold?: number
  sale?: number
  skuAttributes?: Record<string, any> | string
  spData?: string
  version?: number
}

export interface Brand {
  id?: number
  name: string
  slug?: string
  logoUrl?: string
  logo?: string
  bannerUrl?: string
  brandStory?: string
  firstLetter?: string
  displayOrder?: number
  sort?: number
  isVisible?: boolean | number
  showStatus?: number
  isFactoryAuthorised?: boolean | number
  factoryStatus?: number
  productCount?: number
  createdAt?: string
  updatedAt?: string
}

export interface Category {
  id?: number
  parentId?: number
  name: string
  slug?: string
  depth?: number
  level?: number
  iconUrl?: string
  icon?: string
  isInNavigation?: boolean | number
  navStatus?: number
  isVisible?: boolean | number
  showStatus?: number
  displayOrder?: number
  sort?: number
  productCount?: number
  quantity?: number
  productUnit?: string
  description?: string
  children?: Category[]
}

export interface ProductType {
  id?: number
  name: string
  attributeCount?: number
  paramCount?: number
}

export interface AttributeDefinition {
  id?: number
  productTypeId?: number
  productAttributeCategoryId?: number
  name: string
  selectionMode?: number // 0: NONE, 1: SINGLE, 2: MULTI
  selectType?: number
  inputMode?: number // 0: FREE_TEXT, 1: PICK_LIST
  inputType?: number
  allowedValues?: string[] | string
  inputList?: string
  displayOrder?: number
  sort?: number
  kind?: number // 0: SPEC, 1: PARAM
  type?: number
  filterType?: number
  searchType?: number
  relatedStatus?: number
  handAddStatus?: number
}

export interface ProductAttributeValue {
  id?: number
  productId?: number
  productAttributeId?: number
  attributeId?: number
  value?: string
}

export interface ProductMedia {
  id?: number
  productId?: number
  mediaType: 'IMAGE' | 'VIDEO'
  url: string
  displayOrder?: number
}

export type ProductQueryParam = PageParam & {
  keyword?: string
  productCode?: string
  productSn?: string
  categoryId?: number
  productCategoryId?: number
  brandId?: number
  isPublished?: boolean | number
  publishStatus?: number
  approvalStatus?: number
  verifyStatus?: number
  isFeatured?: boolean | number
  recommendStatus?: number
  isNewArrival?: boolean | number
  newStatus?: number
}

export interface ProductParam extends Partial<Product> {
  productAttributeValueList?: ProductAttributeValue[]
  productVolumeDiscountList?: Array<{ minQuantity: number; discountRate?: number; listPrice?: number }>
  productSpendDiscountList?: Array<{ minSpendAmount?: number; manualAdjustmentAmount?: number }>
  memberPriceList?: Array<{ orderLineId?: number; memberPrice?: number }>
  cateParentId?: number
  [key: string]: any
}

export interface BrandParam {
  id?: number
  name: string
  slug?: string
  logoUrl?: string
  logo?: string
  bannerUrl?: string
  brandStory?: string
  firstLetter?: string
  displayOrder?: number
  sort?: number
  isVisible?: boolean | number
  showStatus?: number
  isFactoryAuthorised?: boolean | number
  factoryStatus?: number
}

export interface CategoryParam {
  id?: number
  parentId?: number
  name: string
  slug?: string
  depth?: number
  iconUrl?: string
  icon?: string
  isInNavigation?: boolean | number
  navStatus?: number
  isVisible?: boolean | number
  showStatus?: number
  displayOrder?: number
  sort?: number
  productAttributeIdList?: number[]
  productUnit?: string
  description?: string
}

export interface CategoryWithChildrenItem extends Category {
  children?: CategoryWithChildrenItem[]
}

export interface AttributeParam {
  id?: number
  productTypeId?: number
  productAttributeCategoryId?: number
  name: string
  selectionMode?: number
  selectType?: number
  inputMode?: number
  inputType?: number
  allowedValues?: string[] | string
  inputList?: string
  displayOrder?: number
  sort?: number
  kind?: number
  type?: number
  filterType?: number
  searchType?: number
  relatedStatus?: number
  handAddStatus?: number
}

export interface AttributeInfo {
  attributeId: number
  productAttributeId?: number
  attributeCategoryId?: number
  productAttributeCategoryId?: number
}

export interface ProductTypeWithAttributes extends ProductType {
  attributeList?: AttributeDefinition[]
  productAttributeList?: AttributeDefinition[]
}

// Backward-compatible aliases
export type PmsProduct = Product
export type PmsProductParam = ProductParam
export type PmsBrand = Brand
export type PmsSkuStock = ProductSku
export type PmsProductCategory = Category
export type PmsProductCategoryExt = CategoryWithChildrenItem
export type PmsProductAttribute = AttributeDefinition
export type PmsProductAttributeCategory = ProductType
export type PmsProductAttributeCategoryExt = ProductTypeWithAttributes
export type PmsProductAttributeValue = ProductAttributeValue
export type ProductAttrInfo = AttributeInfo

