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
  promotionPrice?: number;
  lockStock: number;
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
  return request.get<unknown, { data: PmsPortalProductDetail }>(`/product/detail/${id}`);
};

export const searchProducts = async (keyword: string, pageNum: number = 0, pageSize: number = 20) => {
  const res = await request.get<unknown, { data: { list: any[] } }>('/api/search/esProduct/search', {
    baseURL: '/', // reset baseURL so it doesn't prepend /api/portal
    params: { keyword, pageNum, pageSize }
  });
  if (res && res.data && res.data.list) {
    res.data.list = res.data.list.map(item => ({
      ...item,
      primaryImageUrl: item.pic || item.primaryImageUrl,
      listPrice: item.price !== undefined ? item.price : item.listPrice,
      unitsSold: item.sale !== undefined ? item.sale : item.unitsSold,
      onHandQty: item.stock !== undefined ? item.stock : item.onHandQty,
    }));
  }
  return res as unknown as { data: { list: PmsProduct[] } };
};
