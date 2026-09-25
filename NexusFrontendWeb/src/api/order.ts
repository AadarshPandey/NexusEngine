import request from '../utils/request';
import type { OmsCartItem } from './cart';
import type { UmsMemberReceiveAddress } from './member';

export interface OmsOrderItem {
  id: number;
  orderId: number;
  orderSn: string;
  productId: number;
  productPic: string;
  productName: string;
  productBrand: string;
  productSn: string;
  productPrice: number;
  productQuantity: number;
  productSkuId: number;
  productSkuCode: string;
  productCategoryId: number;
  sp1: string;
  sp2: string;
  sp3: string;
  promotionName: string;
  promotionAmount: number;
  couponAmount: number;
  pointsDiscountAmount: number;
  realAmount: number;
  giftIntegration: number;
  giftGrowth: number;
  productAttr: string;
}

export interface OmsOrderReturnApply {
  id: number;
  orderId: number;
  companyAddressId: number;
  productId: number;
  orderSn: string;
  createTime: string;
  memberUsername: string;
  returnAmount: number;
  returnName: string;
  returnPhone: string;
  status: number;
  handleTime: string;
  productPic: string;
  productName: string;
  productBrand: string;
  productAttr: string;
  productCount: number;
  productPrice: number;
  productRealPrice: number;
  reason: string;
  description: string;
  proofPics: string;
  handleNote: string;
  handleMan: string;
  receiveMan: string;
  receiveTime: string;
  receiveNote: string;
}

export interface SmsCoupon {
  id: number;
  type: number;
  name: string;
  amount: number;
  minPoint: number;
  useType: number;
  endTime: string;
  enableTime: string;
  maxDiscountAmount?: number;
}

export interface SmsCouponProductRelation {
  id: number;
  couponId: number;
  productId: number;
  productName: string;
  productSn: string;
}

export interface SmsCouponProductCategoryRelation {
  id: number;
  couponId: number;
  productCategoryId: number;
  productCategoryName: string;
  parentCategoryName: string;
}

export interface SmsCouponHistoryDetail {
  id: number;
  couponId: number;
  memberId: number;
  couponCode: string;
  memberNickname: string;
  getType: number;
  createTime: string;
  useStatus: number;
  useTime: string;
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
  orderItemList: OmsOrderItem[];
  returnApplyList?: OmsOrderReturnApply[];
}

export const submitReturnApply = (data: unknown) => {
  return request.post<unknown, { data: unknown }>('/returnApply/create', data);
};

export const generateConfirmOrder = (cartIds: number[]) => {
  return request.post<unknown, { data: ConfirmOrderResult }>('/order/generateConfirmOrder', cartIds);
};

export const generateOrder = (orderParam: OrderParam) => {
  return request.post<unknown, { data: unknown }>('/order/generateOrder', orderParam);
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
  return request.post<unknown, { data: { razorpayOrderId: string, keyId: string, amount: string } }>(`/order/createRazorpayOrder?orderId=${orderId}`);
};

export const verifyRazorpayPayment = (orderId: number, razorpayPaymentId: string, razorpayOrderId: string, razorpaySignature: string) => {
  return request.post<unknown, { data: unknown }>(`/order/verifyRazorpayPayment?orderId=${orderId}&razorpayPaymentId=${razorpayPaymentId}&razorpayOrderId=${razorpayOrderId}&razorpaySignature=${razorpaySignature}`);
};
