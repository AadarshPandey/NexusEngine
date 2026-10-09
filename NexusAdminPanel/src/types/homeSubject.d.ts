import type { PageParam } from './common'

/**
 */
export type SmsHomeRecommendSubject = {
  /** ID */
  id?: number
  subjectId: number
  subjectName: string
  isFeatured?: number
  displayOrder?: number
}

export type HomeSubjectQueryParam = PageParam & {
  subjectName?: string
  isFeatured?: number
}
