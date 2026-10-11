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
  return request.get<unknown, { data: { list: ProductReview[] } }>(`/review/product/${productId}`);
};

export const getReviewReplies = (reviewId: number) => {
  return request.get<unknown, { data: ProductReview[] }>(`/review/${reviewId}/replies`);
};

export const submitReview = (param: CreateReviewParam) => {
  return request.post<unknown, { data: unknown }>('/review/create', param);
};

export const likeReview = (reviewId: number) => {
  return request.post<unknown, unknown>(`/review/${reviewId}/like`);
};

export const dislikeReview = (reviewId: number) => {
  return request.post<unknown, unknown>(`/review/${reviewId}/dislike`);
};

export const uploadReviewAttachment = (file: File) => {
  const formData = new FormData();
  formData.append('file', file);
  return request.post<unknown, { data: { url: string } }>('/review/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });
};
