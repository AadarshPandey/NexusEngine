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
  return request.post<unknown, RazorpayOrderData>(`/ordering/orders/payment/razorpay?orderId=${orderId}`);
};

export const verifyRazorpayPayment = (
  orderId: number,
  razorpayPaymentId: string,
  razorpayOrderId: string,
  razorpaySignature: string
) => {
  return request.post<unknown, string>(
    `/ordering/orders/payment/razorpay/verify?orderId=${orderId}&razorpayPaymentId=${encodeURIComponent(razorpayPaymentId)}&razorpayOrderId=${encodeURIComponent(razorpayOrderId)}&razorpaySignature=${encodeURIComponent(razorpaySignature)}`
  );
};
