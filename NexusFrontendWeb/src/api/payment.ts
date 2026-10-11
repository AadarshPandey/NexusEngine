/**
 * Payment Context API Client
 * Aligned with schema_redesign.md and FrontendMigrationPlan.md
 */

import request from '../utils/request';

export interface RazorpayOrderData {
  razorpayOrderId: string;
  keyId: string;
  amount: string;
}

export const createRazorpayOrder = (orderId: number) => {
  return request.post<unknown, { data: RazorpayOrderData }>(`/order/createRazorpayOrder?orderId=${orderId}`);
};

export const verifyRazorpayPayment = (
  orderId: number,
  razorpayPaymentId: string,
  razorpayOrderId: string,
  razorpaySignature: string
) => {
  return request.post<unknown, { data: unknown }>(
    `/order/verifyRazorpayPayment?orderId=${orderId}&razorpayPaymentId=${razorpayPaymentId}&razorpayOrderId=${razorpayOrderId}&razorpaySignature=${razorpaySignature}`
  );
};

export const notifyPaymentSuccess = (orderId: number, payType: number = 2) => {
  return request.post<unknown, { data: unknown }>(`/order/paySuccess?orderId=${orderId}&payType=${payType}`);
};
