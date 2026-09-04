import request from '../utils/request';
import type { OmsCartItem } from './cart';

export interface ConfirmOrderResult {
  cartPromotionItemList: OmsCartItem[];
  memberReceiveAddressList: any[];
  calcAmount: {
    totalAmount: number;
    freightAmount: number;
    promotionAmount: number;
    payAmount: number;
  };
}

export interface OrderParam {
  memberReceiveAddressId: number;
  couponId?: number;
  useIntegration?: number;
  payType: number;
  cartIds?: number[];
}

export interface OmsOrderDetail {
  id: number;
  orderSn: string;
  createTime: string;
  memberUsername: string;
  totalAmount: number;
  payAmount: number;
  freightAmount: number;
  promotionAmount: number;
  payType: number;
  sourceType: number;
  status: number;
  orderType: number;
  deliveryCompany: string;
  deliverySn: string;
  autoConfirmDay: number;
  integration: number;
  growth: number;
  promotionInfo: string;
  billType: number;
  billHeader: string;
  billContent: string;
  billReceiverPhone: string;
  billReceiverEmail: string;
  receiverName: string;
  receiverPhone: string;
  receiverPostCode: string;
  receiverProvince: string;
  receiverCity: string;
  receiverRegion: string;
  receiverDetailAddress: string;
  note: string;
  confirmStatus: number;
  deleteStatus: number;
  useIntegration: number;
  paymentTime: string;
  deliveryTime: string;
  receiveTime: string;
  commentTime: string;
  modifyTime: string;
}

export const generateConfirmOrder = (cartIds: number[]) => {
  return request.post<any, { data: ConfirmOrderResult }>('/order/generateConfirmOrder', cartIds);
};

export const generateOrder = (orderParam: OrderParam) => {
  return request.post<any, { data: any }>('/order/generateOrder', orderParam);
};

export const fetchOrderList = (status: number = -1, pageNum: number = 1, pageSize: number = 5) => {
  return request.get<any, { data: { list: OmsOrderDetail[] } }>('/order/list', {
    params: { status, pageNum, pageSize }
  });
};

export const paySuccess = (orderId: number, payType: number = 1) => {
  return request.post<any, { data: any }>(`/order/paySuccess?orderId=${orderId}&payType=${payType}`);
};

export const createRazorpayOrder = (orderId: number) => {
  return request.post<any, { data: { razorpayOrderId: string, keyId: string, amount: string } }>(`/order/createRazorpayOrder?orderId=${orderId}`);
};

export const verifyRazorpayPayment = (orderId: number, razorpayPaymentId: string, razorpayOrderId: string, razorpaySignature: string) => {
  return request.post<any, { data: any }>(`/order/verifyRazorpayPayment?orderId=${orderId}&razorpayPaymentId=${razorpayPaymentId}&razorpayOrderId=${razorpayOrderId}&razorpaySignature=${razorpaySignature}`);
};
