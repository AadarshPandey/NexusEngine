/**
 * Promotion Context API Client
 * Aligned with schema_redesign.md and FrontendMigrationPlan.md
 */

import request from '../utils/request';
import type { Coupon, FlashSale, PromotionalBanner } from '../types/promotion';

export const getAvailableCoupons = () => {
  return request.get<unknown, { data: Coupon[] }>('/coupon/list');
};

export const claimCoupon = (couponId: number) => {
  return request.post<unknown, { data: unknown }>(`/coupon/add/${couponId}`);
};

export const getMemberCoupons = (useStatus: number = 0) => {
  return request.get<unknown, { data: Coupon[] }>(`/coupon/listMemberCoupon/${useStatus}`);
};

export const getFlashSaleDetails = (flashSaleId: number) => {
  return request.get<unknown, { data: FlashSale }>(`/flashPromotion/${flashSaleId}`);
};

export const getPromotionalBanners = () => {
  return request.get<unknown, { data: PromotionalBanner[] }>('/home/advertiseList');
};
