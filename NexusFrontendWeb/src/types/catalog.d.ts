/**
 * Catalog Bounded Context Types for Customer Storefront
 * Aligned with ADR 0003 and modular monolith domain model
 */

export interface Product {
  id: number
  brandId?: number
  categoryId?: number
  name: string
  slug?: string
  primaryImageUrl: string
  productCode: string
  isPublished?: boolean
  isNewArrival?: boolean
  isFeatured?: boolean
  approvalStatus?: number
  unitsSold: number
  price?: number
  listPrice: number
  salePrice?: number
  subTitle?: string
  description?: string
  searchKeywords?: string[] | string
  brandName?: string
  categoryName?: string
  productSkuList?: ProductSku[]
  galleryList?: ProductMedia[]
  attributeValues?: Record<string, any>
  createdAt?: string
  updatedAt?: string
}

export interface ProductSku {
  id: number
  productId: number
  skuCode: string
  listPrice: number
  salePrice?: number
  onHandQty: number
  reservedQty?: number
  availableQty: number
  lowStockThreshold?: number
  imageUrl: string
  unitsSold?: number
  skuAttributes: Record<string, any> | string
  version?: number
}

export interface Brand {
  id: number
  name: string
  slug?: string
  logoUrl: string
  bannerUrl?: string
  brandStory?: string
  firstLetter?: string
  displayOrder?: number
  isVisible?: boolean
}

export interface Category {
  id: number
  parentId?: number
  name: string
  slug?: string
  depth?: number
  iconUrl?: string
  isInNavigation?: boolean
  isVisible?: boolean
  children?: Category[]
}

export interface ProductMedia {
  id?: number
  productId?: number
  url: string
  mediaType?: 'IMAGE' | 'VIDEO'
  displayOrder?: number
}

// Backward-compatible alias
export type PmsProduct = Product
export type PmsProductCategory = Category
