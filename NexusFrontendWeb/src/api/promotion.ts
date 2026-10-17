/**
 * Promotion Context API Client
 * Aligned with schema_redesign.md and FrontendMigrationPlan.md
 */

import request from '../utils/request';
import type { Coupon, FlashSale, PromotionalBanner } from '../types/promotion';

export const getAvailableCoupons = (useStatus: number = 0) => {
  return request.get<unknown, { data: Coupon[] }>('/promotion/coupons', { params: { useStatus } });
};

export const claimCoupon = (couponId: number) => {
  return request.post<unknown, { data: unknown }>(`/promotion/coupons/${couponId}/claim`);
};

export const getMemberCoupons = (useStatus: number = 0) => {
  return request.get<unknown, { data: Coupon[] }>('/promotion/coupons', { params: { useStatus } });
};

export const getCouponsByProduct = (productId: number) => {
  return request.get<unknown, { data: Coupon[] }>(`/promotion/coupons/product/${productId}`);
};

export const getFlashSaleDetails = (flashSaleId: number) => {
  return request.get<unknown, { data: FlashSale }>(`/promotion/flash-sales/${flashSaleId}`);
};

export const getPromotionalBanners = () => {
  return request.get<unknown, { data: PromotionalBanner[] }>('/home');
};
