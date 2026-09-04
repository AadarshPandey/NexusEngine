import type { PageParam } from './common'

export type SmsHomeAdvertise = {
  /** ID */
  id?: number
  name?: string
  type?: number
  pic?: string
  startTime?: string
  endTime?: string
  status?: number
  clickCount?: number
  orderCount?: number
  url?: string
  note?: string
  sort?: number
}

export type HomeAdvertiseQueryParam = PageParam & {
  name?: string
  type?: number
  endTime?: string
}
