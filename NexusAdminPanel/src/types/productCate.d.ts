export type PmsProductCategory = {
  /** ID */
  id?: number
  parentId: number
  name: string
  level?: number
  productCount?: number
  productUnit?: string
  navStatus: number
  showStatus: number
  sort?: number
  icon?: string
  keywords?: string
  description?: string
  productAttributeIdList?: number[]
}

export type PmsProductCategoryExt = PmsProductCategory & {
  children?: PmsProductCategory[]
}
