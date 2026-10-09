export type ProductAttrInfo = {
  attributeId: number
  attributeCategoryId: number
}

export type PmsProductAttribute = {
  /** ID */
  id?: number
  productTypeId?: number
  name: string
  selectionMode?: number
  inputMode?: number
  allowedValues?: string
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
  orderLineId?: number
  attributeId?: number
  value?: string
}

export interface ProductAttrVo extends Pick<
  PmsProductAttribute,
> {
  values?: string[]
  options?: string[]
}

export interface ProductParamVo extends Pick<
  PmsProductAttribute,
  'id' | 'name' | 'inputMode' | 'allowedValues'
> {
  value?: string
}

export type ProductAttrPicVo = {
  name?: string
  primaryImageUrl?: string
}
