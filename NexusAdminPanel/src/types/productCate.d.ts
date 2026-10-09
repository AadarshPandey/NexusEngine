export type PmsProductCategory = {
  /** ID */
  id?: number
  parentReviewId: number
  name: string
  depth?: number
  quantity?: number
  unitOfMeasure?: string
  isInNavigation: number
  isVisible: number
  displayOrder?: number
  iconUrl?: string
  searchKeywords?: string
  customerComment?: string
  productAttributeIdList?: number[]
}

export type PmsProductCategoryExt = PmsProductCategory & {
  children?: PmsProductCategory[]
}
