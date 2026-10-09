import request from '../utils/request';

export interface OmsCartItem {
  id: number;
  orderLineId: number;
  skuId: number;
  memberId: number;
  quantity: number;
  listPrice: number;
  productImageUrl: string;
  productName: string;
  productSkuCode: string;
  createdAt: string;
  updatedAt: string;
  deletedAt: number;
  permissionGroupId: number;
  brandName: string;
  productCode: string;
  skuAttributes: string;
  realStock?: number;
}

export const fetchCartList = () => {
  return request.get<unknown, { data: OmsCartItem[] }>('/cart/list/promotion');
};

export const addToCart = (data: Partial<OmsCartItem>) => {
  return request.post<unknown, { data: unknown }>('/cart/add', data);
};

export const deleteCartItem = (ids: number[]) => {
  return request.post<unknown, unknown>('/cart/delete', null, { params: { ids: ids.join(',') } });
};

export const updateCartItemQuantity = (id: number, quantity: number) => {
  return request.get<unknown, unknown>('/cart/update/quantity', { params: { id, quantity } });
};
