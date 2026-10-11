/**
 * Engagement Bounded Context Type Definitions (Reviews, UGC, Media)
 * Aligned with schema_redesign.md (engagement schema) and BackendMigrationPlan.md
 */

export interface ProductReview {
  id: number
  productId: number
  memberId: number
  authorDisplayName?: string
  memberNickName?: string
  authorAvatarUrl?: string
  memberIcon?: string
  rating: number // 1 - 5
  body: string
  content?: string
  helpfulCount?: number
  likeCount?: number
  moderationStatus?: number
  mediaUrls?: string[]
  pics?: string[]
  createdAt: string
  updatedAt?: string
}

export interface ReviewMedia {
  id?: number
  reviewId: number
  mediaType: 'IMAGE' | 'VIDEO'
  url: string
  thumbnailUrl?: string
  displayOrder?: number
}

export type PmsReview = ProductReview
export type PmsReviewAttachment = ReviewMedia
