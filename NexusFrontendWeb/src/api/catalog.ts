/**
 * Catalog Context API Client
 * Aligned with ADR 0003 and modular monolith domain model
 */

import request from '../utils/request';
import type { Product, ProductSku, Brand, Category } from '../types/catalog';
import type { Banner, Coupon } from '../types/promotion';

export type { Product, ProductSku, Brand, Category };

export interface ProductDetailResult {
  product: Product;
  brand?: Brand;
  productAttributeList?: any[];
  productAttributeValueList?: any[];
  skuStockList?: ProductSku[];
  productVolumeDiscountList?: any[];
  productSpendDiscountList?: any[];
  couponList?: Coupon[];
  flashSaleActive?: boolean;
}

export interface HomeContentResult {
  banners: Banner[];
  advertiseList?: Banner[];
  brandList?: Brand[];
  homeFlashPromotion?: unknown;
  newProductList: Product[];
  hotProductList: Product[];
  recommendProductList?: Product[];
}

export const getProductDetail = (id: number) => {
  return request.get<unknown, { data: ProductDetailResult }>(`/product/detail/${id}`);
};

export const fetchProductDetail = getProductDetail;

export const getCategoryList = (parentId: number = 0) => {
  return request.get<unknown, { data: Category[] }>(`/home/productCateList/${parentId}`);
};

export const fetchHomeContent = () => {
  return request.get<unknown, { data: HomeContentResult }>('/home/body');
};

export const getRecommendProducts = (pageSize: number = 4, pageNum: number = 1) => {
  return request.get<unknown, { data: Product[] }>('/home/recommendProductList', {
    params: { pageSize, pageNum },
  });
};

export const fetchRecommendProductList = getRecommendProducts;

export const getHotProducts = (pageSize: number = 6, pageNum: number = 1) => {
  return request.get<unknown, { data: Product[] }>('/home/hotProductList', {
    params: { pageSize, pageNum },
  });
};

export const fetchHotProductList = getHotProducts;

export const getNewProducts = (pageSize: number = 6, pageNum: number = 1) => {
  return request.get<unknown, { data: Product[] }>('/home/newProductList', {
    params: { pageSize, pageNum },
  });
};

export const fetchNewProductList = getNewProducts;

export const searchProducts = async (keyword: string, pageNum: number = 0, pageSize: number = 20) => {
  const res = await request.get<unknown, { data: { list: any[] } }>('/api/search/esProduct/search', {
    baseURL: '/', // Search endpoint sits at /api/search
    params: { keyword, pageNum, pageSize }
  });
  if (res && res.data && res.data.list) {
    res.data.list = res.data.list.map(item => ({
      ...item,
      primaryImageUrl: item.primaryImageUrl || item.pic || '',
      listPrice: item.listPrice !== undefined ? item.listPrice : (item.price ?? 0),
      salePrice: item.salePrice !== undefined ? item.salePrice : item.price,
      unitsSold: item.unitsSold !== undefined ? item.unitsSold : (item.sale ?? 0),
      onHandQty: item.onHandQty !== undefined ? item.onHandQty : (item.stock ?? 0),
      productCode: item.productCode || item.productSn || '',
    }));
  }
  return res as unknown as { data: { list: Product[] } };
};

// Backward-compatible type aliases
export type PmsPortalProductDetail = ProductDetailResult;
export type PmsSkuStock = ProductSku;
export type PmsProduct = Product;
