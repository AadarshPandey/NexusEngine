/**
 * Catalog Bounded Context Type Definitions
 * Aligned with schema_redesign.md (catalog schema) and BackendMigrationPlan.md
 */

export interface Product {
  id?: number
  brandId?: number
  categoryId?: number
  productTypeId?: number
  name: string
  slug?: string
  primaryImageUrl?: string
  productCode: string
  isPublished?: boolean | number
  isNewArrival?: boolean | number
  isFeatured?: boolean | number
  approvalStatus?: number // 0: PENDING, 1: APPROVED, 2: REJECTED
  unitsSold?: number
  listPrice?: number
  salePrice?: number
  price?: number
  originalPrice?: number
  subTitle?: string
  description?: string
  searchKeywords?: string | string[]
  brandName?: string
  categoryName?: string
  productSkuList?: ProductSku[]
  skuStockList?: ProductSku[]
  mediaGallery?: ProductMedia[]
  attributeValues?: ProductAttributeValue[]
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
  lowStockThreshold?: number
  imageUrl?: string
  pic?: string
  unitsSold?: number
  sale?: number
  skuAttributes?: Record<string, any> | string
  version?: number
}

export interface Brand {
  id?: number
  name: string
  slug?: string
  logoUrl?: string
  bannerUrl?: string
  brandStory?: string
  firstLetter?: string
  displayOrder?: number
  isVisible?: boolean | number
  isFactoryAuthorised?: boolean | number
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
  iconUrl?: string
  isInNavigation?: boolean | number
  isVisible?: boolean | number
  displayOrder?: number
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
  name: string
  selectionMode?: number // 0: NONE, 1: SINGLE, 2: MULTI
  inputMode?: number // 0: FREE_TEXT, 1: PICK_LIST
  allowedValues?: string[] | string
  displayOrder?: number
  kind?: number // 0: SPEC, 1: PARAM
}

export interface ProductAttributeValue {
  id?: number
  productId?: number
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

// Backward-compatible aliases
export type PmsProduct = Product
export type PmsBrand = Brand
export type PmsSkuStock = ProductSku
export type PmsProductCategory = Category
export type PmsProductAttribute = AttributeDefinition
export type PmsProductAttributeValue = ProductAttributeValue
