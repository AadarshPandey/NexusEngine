/**
 * Ordering Context API Client
 * Aligned with ADR 0003 and modular monolith domain model
 */

import request from '../utils/request';
import type { CustomerOrder, EnrichedCartItem, OrderLine, ReturnRequest } from '../types/ordering';
import type { MemberAddress } from '../types/customer';
import type { Coupon } from '../types/promotion';

export type { CustomerOrder, EnrichedCartItem, OrderLine, ReturnRequest };

export interface ConfirmOrderCalcAmount {
  totalAmount: number;
  freightAmount: number;
  promotionAmount: number;
  payAmount: number;
}

export interface CouponHistoryDetail {
  id: number;
  couponId: number;
  coupon: Coupon;
  categoryRelationList?: { productCategoryId: number }[];
  productRelationList?: { productId: number }[];
}

export interface ConfirmOrderResult {
  cartPromotionItemList: EnrichedCartItem[];
  memberReceiveAddressList: MemberAddress[];
  couponHistoryDetailList: CouponHistoryDetail[];
  calcAmount: ConfirmOrderCalcAmount;
}

export interface OrderParam {
  memberReceiveAddressId: number;
  couponId?: number;
  payType: number;
  cartIds?: number[];
}

export interface GenerateOrderResult {
  order: CustomerOrder;
}

export const getCart = () => {
  return request.get<unknown, { data: EnrichedCartItem[] }>('/cart/list/promotion');
};

export const fetchCartList = getCart;

export const addItemToCart = (data: { productId?: number; productSkuId?: number; skuId?: number; quantity: number; price?: number }) => {
  return request.post<unknown, { data: unknown }>('/cart/add', {
    productId: data.productId,
    productSkuId: data.skuId || data.productSkuId,
    quantity: data.quantity,
    price: data.price,
  });
};

export const addToCart = addItemToCart;

export const removeCartItems = (cartIds: number[]) => {
  return request.post<unknown, unknown>('/cart/delete', null, { params: { ids: cartIds.join(',') } });
};

export const deleteCartItem = removeCartItems;

export const updateItemQuantity = (cartId: number, quantity: number) => {
  return request.get<unknown, unknown>('/cart/update/quantity', { params: { id: cartId, quantity } });
};

export const updateCartItemQuantity = updateItemQuantity;

export const generateConfirmOrder = (cartIds: number[]) => {
  return request.post<unknown, { data: ConfirmOrderResult }>('/order/generateConfirmOrder', cartIds);
};

export const createOrder = (orderParam: OrderParam) => {
  return request.post<unknown, { data: GenerateOrderResult }>('/order/generateOrder', orderParam);
};

export const generateOrder = createOrder;

export const getOrders = (status: number = -1, pageNum: number = 1, pageSize: number = 10) => {
  return request.get<unknown, { data: { list: CustomerOrder[] } }>('/order/list', {
    params: { status, pageNum, pageSize },
  });
};

export const fetchOrderList = getOrders;

export const cancelUserOrder = (orderId: number) => {
  return request.post<unknown, { data: unknown }>(`/order/cancelUserOrder?orderId=${orderId}`);
};

export const submitReturnApply = (data: unknown) => {
  return request.post<unknown, { data: unknown }>('/returnApply/create', data);
};

export const getReturnReasons = () => {
  return request.get<unknown, { data: { id: number; name: string }[] }>('/returnApply/reasons');
};

// Backward-compatible aliases
export type OmsCartItem = EnrichedCartItem;
export type OmsOrderDetail = CustomerOrder;
export type OmsOrderItem = OrderLine;
export type OmsOrderReturnApply = ReturnRequest;
export type SmsCouponHistoryDetail = CouponHistoryDetail;
