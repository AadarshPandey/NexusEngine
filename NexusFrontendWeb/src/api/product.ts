import request from '../utils/request';
import type { PmsProduct } from './home';

export interface PmsBrand {
  id: number;
  name: string;
  firstLetter: string;
  displayOrder: number;
  factoryStatus: number;
  isVisible: number;
  quantity: number;
  productCommentCount: number;
  logoUrl: string;
  bigPic: string;
  brandStory: string;
}

export interface PmsProductAttribute {
  id: number;
  productTypeId: number;
  name: string;
  selectionMode: number;
  inputMode: number;
  allowedValues: string;
  displayOrder: number;
  filterType: number;
  searchType: number;
  relatedStatus: number;
  handAddStatus: number;
  type: number;
}

export interface PmsProductAttributeValue {
  id: number;
  orderLineId: number;
  attributeId: number;
  value: string;
}

export interface PmsSkuStock {
  id: number;
  orderLineId: number;
  skuCode: string;
  listPrice: number;
  onHandQty: number;
  lowStockThreshold: number;
  primaryImageUrl: string;
  unitsSold: number;
  salePrice: number;
  reservedQty: number;
  spData: string;
}

export interface SmsCoupon {
  id: number;
  type: number;
  name: string;
    minQuantity: number;
  discountValue: number;
  perMemberLimit: number;
  minSpendAmount: number;
  startsAt: string;
  endsAt: string;
  applicabilityScope: number;
        claimedCount: number;
    code: string;
}

export interface PmsPortalProductDetail {
  product: PmsProduct;
  brand: PmsBrand;
  productAttributeList: PmsProductAttribute[];
  productAttributeValueList: PmsProductAttributeValue[];
  skuStockList: PmsSkuStock[];
  couponList: SmsCoupon[];
  flashSaleActive: boolean;
}

export const fetchProductDetail = (id: number) => {
  return request.get<unknown, { data: PmsPortalProductDetail }>(`/product/comment/${id}`);
};

export const searchProducts = (keyword: string, pageNum: number = 1, pageSize: number = 20) => {
  return request.get<unknown, { data: { list: PmsProduct[] } }>('/portal/product/search', {
    params: { keyword, pageNum, pageSize }
  });
};
