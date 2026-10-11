import type { PageParam } from './common'
import type { Banner } from './promotion'

export type { Banner } from './promotion'

export type SmsHomeAdvertise = {
  /** ID */
  id?: number
  name?: string
  title?: string
  type?: number
  placement?: number
  primaryImageUrl?: string
  imageUrl?: string
  pic?: string
  startsAt?: string
  startTime?: string
  endsAt?: string
  endTime?: string
  status?: number
  isActive?: boolean | number
  clickCount?: number
  orderCount?: number
  url?: string
  targetUrl?: string
  note?: string
  internalNote?: string
  displayOrder?: number
  sort?: number
}

export type HomeAdvertiseQueryParam = PageParam & {
  name?: string
  type?: number
  endsAt?: string
}
