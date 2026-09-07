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

export const searchProducts = (keyword: string, pageNum: number = 1, pageSize: number = 20) => {
  return request.get<any, { data: { list: PmsProduct[] } }>('/product/search', {
    params: { keyword, pageNum, pageSize }
  });
};
