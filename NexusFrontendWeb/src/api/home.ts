import request from '../utils/request';

export interface PmsProduct {
  id: number;
  brandId: number;
  permissionGroupId: number;
  name: string;
  primaryImageUrl: string;
  productCode: string;
  deleteStatus: number;
  isPublished: number;
  isNewArrival: number;
  isFeatured: number;
  approvalStatus: number;
  displayOrder: number;
  unitsSold: number;
  listPrice: number;
  salePrice: number;
  promotionType?: number;
  subTitle: string;
  customerComment: string;
  onHandQty: number;
  galleryList?: { url: string; displayOrder: number }[];
}


export interface PmsProductCategory {
  id: number;
  parentReviewId: number;
  name: string;
  depth: number;
  quantity: number;
  unitOfMeasure: string;
  isInNavigation: number;
  isVisible: number;
  displayOrder: number;
  iconUrl: string;
  searchKeywords: string;
}

export interface HomeContentResult {
  advertiseList: unknown[];
  brandList: unknown[];
  homeFlashPromotion: unknown;
  newProductList: PmsProduct[];
  hotProductList: PmsProduct[];
}

export const fetchHomeContent = () => {
  return request.get<unknown, { data: HomeContentResult }>('/home/body');
};

export const fetchRecommendProductList = (pageSize: number = 4, pageNum: number = 1) => {
  return request.get<unknown, { data: PmsProduct[] }>('/home/recommendProductList', {
    params: { pageSize, pageNum },
  });
};

export const fetchHotProductList = (pageSize: number = 6, pageNum: number = 1) => {
  return request.get<unknown, { data: PmsProduct[] }>('/home/hotProductList', {
    params: { pageSize, pageNum },
  });
};

export const fetchNewProductList = (pageSize: number = 6, pageNum: number = 1) => {
  return request.get<unknown, { data: PmsProduct[] }>('/home/newProductList', {
    params: { pageSize, pageNum },
  });
};
