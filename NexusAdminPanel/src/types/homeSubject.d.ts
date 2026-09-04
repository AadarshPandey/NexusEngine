import type { PageParam } from './common'

/**
 */
export type SmsHomeRecommendSubject = {
  /** ID */
  id?: number
  subjectId: number
  subjectName: string
  recommendStatus?: number
  sort?: number
}

export type HomeSubjectQueryParam = PageParam & {
  subjectName?: string
  recommendStatus?: number
}
