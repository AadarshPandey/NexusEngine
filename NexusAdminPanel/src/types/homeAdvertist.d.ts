import type { PageParam } from './common'

export type SmsHomeAdvertise = {
  /** ID */
  id?: number
  name?: string
  type?: number
  primaryImageUrl?: string
  startsAt?: string
  endsAt?: string
  status?: number
  clickCount?: number
  orderCount?: number
  url?: string
  note?: string
  displayOrder?: number
}

export type HomeAdvertiseQueryParam = PageParam & {
  name?: string
  type?: number
  endsAt?: string
}
