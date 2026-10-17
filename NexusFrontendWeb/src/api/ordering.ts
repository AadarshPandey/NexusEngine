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
  subtotalAmount: number;
  shippingAmount: number;
  promotionAmount?: number;
  grandTotalAmount: number;
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
  return request.get<unknown, { data: EnrichedCartItem[] }>('/ordering/cart/promotion');
};

export const fetchCartList = getCart;

export const addItemToCart = (data: { productId?: number; productSkuId?: number; skuId?: number; quantity: number; price?: number }) => {
  return request.post<unknown, { data: unknown }>('/ordering/cart', {
    productId: data.productId,
    productSkuId: data.skuId || data.productSkuId,
    quantity: data.quantity,
    price: data.price,
  });
};

export const addToCart = addItemToCart;

export const removeCartItems = (cartIds: number[]) => {
  return request.delete<unknown, unknown>('/ordering/cart', { params: { ids: cartIds.join(',') } });
};

export const deleteCartItem = removeCartItems;

export const updateItemQuantity = (cartId: number, quantity: number) => {
  return request.patch<unknown, unknown>('/ordering/cart/quantity', null, { params: { id: cartId, quantity } });
};

export const updateCartItemQuantity = updateItemQuantity;

export const generateConfirmOrder = (cartIds: number[]) => {
  return request.post<unknown, { data: ConfirmOrderResult }>('/ordering/orders/confirm', cartIds);
};

export const createOrder = (orderParam: OrderParam) => {
  return request.post<unknown, { data: GenerateOrderResult }>('/ordering/orders', orderParam);
};

export const generateOrder = createOrder;

export const getOrders = (status: number = -1, pageNum: number = 1, pageSize: number = 10) => {
  return request.get<unknown, { data: { list: CustomerOrder[] } }>('/ordering/orders', {
    params: { status, pageNum, pageSize },
  });
};

export const fetchOrderList = getOrders;

export const getOrderDetail = (orderId: number) => {
  return request.get<unknown, { data: CustomerOrder }>(`/ordering/orders/${orderId}`);
};

export const cancelUserOrder = (orderId: number) => {
  return request.patch<unknown, { data: unknown }>('/ordering/orders/cancel', null, { params: { orderId } });
};

export const confirmReceiveOrder = (orderId: number) => {
  return request.patch<unknown, { data: unknown }>('/ordering/orders/confirm-receipt', null, { params: { orderId } });
};

export const submitReturnApply = (data: unknown) => {
  return request.post<unknown, { data: unknown }>('/ordering/return-requests', data);
};

export const getReturnReasons = () => {
  return request.get<unknown, { data: { id: number; name: string }[] }>('/ordering/return-requests/reasons');
};
