export type PmsProductCategory = {
  /** ID */
  id?: number
  parentId: number
  name: string
  depth?: number
  quantity?: number
  isInNavigation: number
  isVisible: number
  iconUrl?: string
  productAttributeIdList?: number[]
}

export type PmsProductCategoryExt = PmsProductCategory & {
  children?: PmsProductCategory[]
}
