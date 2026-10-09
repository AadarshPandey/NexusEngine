export type SmsHomeBrand = {
  /** ID */
  id?: number
  brandId: number
  brandName: string
  isFeatured?: number
  displayOrder?: number
}

export type HomeBrandQueryParam = PageParam & {
  brandName?: string
  isFeatured?: number
}
