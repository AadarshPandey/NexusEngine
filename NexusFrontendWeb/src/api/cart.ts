import request from '../utils/request';

export interface OmsCartItem {
  id: number;
  productId: number;
  productSkuId: number;
  memberId: number;
  quantity: number;
  price: number;
  productPic: string;
  productName: string;
  productSubTitle: string;
  productSkuCode: string;
  memberNickname: string;
  createDate: string;
  modifyDate: string;
  deleteStatus: number;
  productCategoryId: number;
  productBrand: string;
  productSn: string;
  productAttr: string;
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
