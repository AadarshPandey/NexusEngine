import request from '../utils/request';
import type { PmsProduct } from './home';

export interface PmsPortalProductDetail {
  product: PmsProduct;
  brand: any;
  productAttributeList: any[];
  productAttributeValueList: any[];
  skuStockList: any[];
  couponList: any[];
}

export const fetchProductDetail = (id: number) => {
  return request.get<any, { data: PmsPortalProductDetail }>(`/product/detail/${id}`);
};
