import request from '../utils/request';
import type { OmsCartItem } from './cart';
import type { UmsMemberReceiveAddress } from './member';

export interface OmsOrderItem {
  id: number;
  orderId: number;
  orderSn: string;
  orderLineId: number;
  productImageUrl: string;
  productName: string;
  brandName: string;
  productCode: string;
  unitPrice: number;
  quantity: number;
  skuId: number;
  productSkuCode: string;
  permissionGroupId: number;
  sp1: string;
  sp2: string;
  sp3: string;
  promotionName: string;
  promotionAmount: number;
  couponDiscountAmount: number;
  lineTotalAmount: number;
  giftIntegration: number;
  giftGrowth: number;
  skuAttributes: string;
}

export interface OmsOrderReturnApply {
  id: number;
  orderId: number;
  returnAddressId: number;
  orderLineId: number;
  orderSn: string;
  createdAt: string;
  refundAmount: number;
  contactName: string;
  contactPhone: string;
  status: number;
  commentTime: string;
  productImageUrl: string;
  productName: string;
  brandName: string;
  productCode: string;
  skuAttributes: string;
  quantity: number;
  unitPrice: number;
  unitRefundAmount: number;
  reasonId: string;
  customerComment: string;
  evidenceUrls: string;
  reviewNote: string;
  reviewedByAdminId: string;
  receivedByAdminId: string;
  receiveTime: string;
  receiptNote: string;
}

export interface SmsCoupon {
  id: number;
  type: number;
  name: string;
  discountValue: number;
  minSpendAmount: number;
  applicabilityScope: number;
  endsAt: string;
    maxDiscountAmount?: number;
}

export interface SmsCouponProductRelation {
  id: number;
  couponId: number;
  orderLineId: number;
}

export interface SmsCouponProductCategoryRelation {
  id: number;
  couponId: number;
  permissionGroupId: number;
}

export interface SmsCouponHistoryDetail {
  id: number;
  couponId: number;
  memberId: number;
  couponCode: string;
  memberNickname: string;
  acquisitionChannel: number;
  createdAt: string;
  status: number;
  redeemedAt: string;
  orderId: number;
  orderSn: string;
  coupon: SmsCoupon;
  productRelationList?: SmsCouponProductRelation[];
  categoryRelationList?: SmsCouponProductCategoryRelation[];
}

export interface ConfirmOrderResult {
  cartPromotionItemList: OmsCartItem[];
  memberReceiveAddressList: UmsMemberReceiveAddress[];
  couponHistoryDetailList: SmsCouponHistoryDetail[];
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
  totalAmount: number;
  payAmount: number;
  freightAmount: number;
  promotionAmount: number;
  payType: number;
  status: number;
  orderType: number;
  deliveryCompany: string;
  deliverySn: string;
  receiverName: string;
  receiverPhone: string;
  receiverPostCode: string;
  receiverDetailAddress: string;
    confirmStatus: number;
  deleteStatus: number;
  useIntegration: number;
  paymentTime: string;
  deliveryTime: string;
  receiveTime: string;
  commentTime: string;
  modifyTime: string;
  orderItemList: OmsOrderItem[];
  returnApplyList?: OmsOrderReturnApply[];
}

export const submitReturnApply = (data: unknown) => {
  return request.post<unknown, { data: unknown }>('/returnApply/create', data);
};

export const getReturnReasons = () => {
  return request.get<unknown, { data: { id: number, name: string }[] }>('/returnApply/reasons');
};

export const generateConfirmOrder = (cartIds: number[]) => {
  return request.post<unknown, { data: ConfirmOrderResult }>('/order/generateConfirmOrder', cartIds);
};

export interface GenerateOrderResult {
  order: OmsOrderDetail;
}

export const generateOrder = (orderParam: OrderParam) => {
  return request.post<unknown, { data: GenerateOrderResult }>('/order/generateOrder', orderParam);
};

export const fetchOrderList = (status: number = -1, pageNum: number = 1, pageSize: number = 5) => {
  return request.get<unknown, { data: { list: OmsOrderDetail[] } }>('/order/list', {
    params: { status, pageNum, pageSize }
  });
};

export const paySuccess = (orderId: number, payType: number = 1) => {
  return request.post<unknown, { data: unknown }>(`/order/paySuccess?orderId=${orderId}&payType=${payType}`);
};

export const createRazorpayOrder = (orderId: number) => {
  return request.post<unknown, { data: { razorpayOrderId: string, keyId: string, discountValue: string } }>(`/order/createRazorpayOrder?orderId=${orderId}`);
};

export const verifyRazorpayPayment = (orderId: number, razorpayPaymentId: string, razorpayOrderId: string, razorpaySignature: string) => {
  return request.post<unknown, { data: unknown }>(`/order/verifyRazorpayPayment?orderId=${orderId}&razorpayPaymentId=${razorpayPaymentId}&razorpayOrderId=${razorpayOrderId}&razorpaySignature=${razorpaySignature}`);
};
