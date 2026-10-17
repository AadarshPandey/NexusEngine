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
  couponList?: Coupon[];
}

export interface HomeContentResult {
  banners: Banner[];
  advertiseList?: Banner[];
  brandList?: Brand[];
  newProductList: Product[];
  hotProductList: Product[];
  recommendProductList?: Product[];
}

export const getProductDetail = (id: number) => {
  return request.get<unknown, ProductDetailResult>(`/catalog/products/${id}`);
};

export const fetchProductDetail = getProductDetail;

export const getCategoryList = (parentId: number = 0) => {
  return request.get<unknown, Category[]>(`/home/categories/${parentId}`);
};

export const fetchHomeContent = () => {
  return request.get<unknown, HomeContentResult>('/home');
};

export const getRecommendProducts = (pageSize: number = 4, pageNum: number = 1) => {
  return request.get<unknown, Product[]>('/home/recommended-products', {
    params: { size: pageSize, page: pageNum - 1 },
  });
};

export const fetchRecommendProductList = getRecommendProducts;

export const getHotProducts = (pageSize: number = 6, pageNum: number = 1) => {
  return request.get<unknown, Product[]>('/home/hot-products', {
    params: { size: pageSize, page: pageNum - 1 },
  });
};

export const fetchHotProductList = getHotProducts;

export const getNewProducts = (pageSize: number = 6, pageNum: number = 1) => {
  return request.get<unknown, Product[]>('/home/new-products', {
    params: { size: pageSize, page: pageNum - 1 },
  });
};

export const fetchNewProductList = getNewProducts;

export const searchProducts = async (keyword: string, page: number = 0, size: number = 20) => {
  return request.get<unknown, Product[]>('/catalog/products/search', {
    params: { keyword, page, size },
  });
};
