export type ProductAttrInfo = {
  attributeId: number
  attributeCategoryId: number
}

export type PmsProductAttribute = {
  /** ID */
  id?: number
  productAttributeCategoryId?: number
  name: string
  selectType?: number
  inputType?: number
  inputList?: string
  sort?: number
  filterType?: number
  searchType?: number
  relatedStatus?: number
  handAddStatus?: number
  type?: number
}

export type PmsProductAttributeCategory = {
  id?: number
  name: string
  attributeCount?: number
  paramCount?: number
}

export type PmsProductAttributeCategoryExt = PmsProductAttributeCategory & {
  productAttributeList?: PmsProductAttribute[]
}

export type PmsProductAttributeValue = {
  /** ID */
  id?: number
  productId?: number
  productAttributeId?: number
  value?: string
}

export interface ProductAttrVo extends Pick<
  PmsProductAttribute,
  'id' | 'name' | 'handAddStatus' | 'inputList'
> {
  values?: string[]
  options?: string[]
}

export interface ProductParamVo extends Pick<
  PmsProductAttribute,
  'id' | 'name' | 'inputType' | 'inputList'
> {
  value?: string
}

export type ProductAttrPicVo = {
  name?: string
  pic?: string
}
