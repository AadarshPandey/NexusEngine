export type SmsHomeBrand = {
  /** ID */
  id?: number
  brandId: number
  brandName: string
  recommendStatus?: number
  sort?: number
}

export type HomeBrandQueryParam = PageParam & {
  brandName?: string
  recommendStatus?: number
}
