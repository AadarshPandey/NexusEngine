import http from '@/utils/http';
import type { PageResponse } from '@/types/common';

export interface ReviewModerationDTO {
  id: number;
  productId: number;
  productName: string;
  productPic?: string;
  brandName?: string;
  memberId: number;
  memberUsername?: string;
  memberNickName?: string;
  rating: number;
  content: string;
  status: number; // 0 = Pending, 1 = Approved, 2 = Rejected
  likeCount?: number;
  dislikeCount?: number;
  mediaUrls?: string[];
  createdAt?: string;
}

export interface ReviewQueryParams {
  productId?: number;
  status?: number;
  keyword?: string;
  pageNum?: number;
  pageSize?: number;
  page?: number;
  size?: number;
}

export function getReviewListAPI(params: ReviewQueryParams) {
  return http<PageResponse<ReviewModerationDTO>>({
    url: '/api/admin/engagement/reviews',
    method: 'get',
    params: {
      ...params,
      page: params.pageNum ? params.pageNum - 1 : (params.page ?? 0),
      size: params.pageSize ?? params.size ?? 10,
    },
  });
}

export function updateReviewModerationAPI(id: number, status: number) {
  return http<void>({
    url: `/api/admin/engagement/reviews/${id}/moderation`,
    method: 'put',
    params: { status },
  });
}

export function deleteReviewAPI(id: number) {
  return http<void>({
    url: `/api/admin/engagement/reviews/${id}`,
    method: 'delete',
  });
}
