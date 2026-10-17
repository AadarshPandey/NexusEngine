/**
 * Engagement Context API Client
 * Aligned with schema_redesign.md and FrontendMigrationPlan.md
 */

import request from '../utils/request';
import type { ProductReview, ReviewMedia } from '../types/engagement';
export type { ProductReview, ReviewMedia };

export interface CreateReviewParam {
  productId: number;
  parentReviewId?: number | null;
  rating: number;
  content: string;
  attachmentList?: { mediaType: string; mediaUrl: string; sortOrder: number }[];
}

export const getReviewsByProductId = (productId: number) => {
  return request.get<unknown, { data: { list: ProductReview[] } }>(`/engagement/reviews/product/${productId}`);
};

export const getReviewReplies = (reviewId: number) => {
  return request.get<unknown, { data: ProductReview[] }>(`/engagement/reviews/${reviewId}/replies`);
};

export const submitReview = (param: CreateReviewParam) => {
  return request.post<unknown, { data: unknown }>('/engagement/reviews', param);
};

export const likeReview = (reviewId: number) => {
  return request.post<unknown, unknown>(`/engagement/reviews/${reviewId}/like`);
};

export const dislikeReview = (reviewId: number) => {
  return request.post<unknown, unknown>(`/engagement/reviews/${reviewId}/dislike`);
};

export const uploadReviewAttachment = (file: File) => {
  const formData = new FormData();
  formData.append('file', file);
  return request.post<unknown, { data: { url: string } }>('/engagement/reviews/media', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });
};
