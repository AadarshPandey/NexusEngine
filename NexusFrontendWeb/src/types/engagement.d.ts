/**
 * Engagement Bounded Context Types for Customer Storefront
 * Aligned with ADR 0003 and modular monolith domain model
 */

export interface ProductReview {
  id: number
  productId: number
  memberId?: number
  authorDisplayName: string
  authorAvatarUrl?: string
  rating: number
  body: string
  helpfulCount?: number
  likeCount?: number
  dislikeCount?: number
  userLiked?: boolean
  userDisliked?: boolean
  mediaUrls?: string[]
  attachmentList?: ReviewMedia[]
  replies?: ProductReview[]
  createdAt: string
  // Backward compatibility aliases
  content?: string
  memberNickName?: string
  memberIcon?: string
  pics?: string[]
  createTime?: string
}

export interface ReviewMedia {
  id?: number
  reviewId?: number
  mediaType: 'IMAGE' | 'VIDEO' | string
  mediaUrl?: string
  url?: string
  sortOrder?: number
}
