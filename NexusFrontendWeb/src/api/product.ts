import request from '../utils/request';
import type { PmsProduct } from './home';

export interface PmsBrand {
  id: number;
  name: string;
  firstLetter: string;
  sort: number;
  factoryStatus: number;
  showStatus: number;
  productCount: number;
  productCommentCount: number;
  logo: string;
  bigPic: string;
  brandStory: string;
}

export interface PmsProductAttribute {
  id: number;
  productAttributeCategoryId: number;
  name: string;
  selectType: number;
  inputType: number;
  inputList: string;
  sort: number;
  filterType: number;
  searchType: number;
  relatedStatus: number;
  handAddStatus: number;
  type: number;
}

export interface PmsProductAttributeValue {
  id: number;
  productId: number;
  productAttributeId: number;
  value: string;
}

export interface PmsSkuStock {
  id: number;
  productId: number;
  skuCode: string;
  price: number;
  stock: number;
  lowStock: number;
  pic: string;
  sale: number;
  promotionPrice: number;
  lockStock: number;
  spData: string;
}

export interface SmsCoupon {
  id: number;
  type: number;
  name: string;
  platform: number;
  count: number;
  amount: number;
  perLimit: number;
  minPoint: number;
  startTime: string;
  endTime: string;
  useType: number;
  note: string;
  publishCount: number;
  useCount: number;
  receiveCount: number;
  enableTime: string;
  code: string;
}

export interface PmsPortalProductDetail {
  product: PmsProduct;
  brand: PmsBrand;
  productAttributeList: PmsProductAttribute[];
  productAttributeValueList: PmsProductAttributeValue[];
  skuStockList: PmsSkuStock[];
  couponList: SmsCoupon[];
}

export const fetchProductDetail = (id: number) => {
  return request.get<unknown, { data: PmsPortalProductDetail }>(`/product/detail/${id}`);
};

export const searchProducts = (keyword: string, pageNum: number = 1, pageSize: number = 20) => {
  return request.get<unknown, { data: { list: PmsProduct[] } }>('/product/search', {
    params: { keyword, pageNum, pageSize }
  });
};
