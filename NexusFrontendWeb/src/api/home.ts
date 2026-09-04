import request from '../utils/request';

export interface PmsProduct {
  id: number;
  brandId: number;
  productCategoryId: number;
  name: string;
  pic: string;
  productSn: string;
  deleteStatus: number;
  publishStatus: number;
  newStatus: number;
  recommandStatus: number;
  verifyStatus: number;
  sort: number;
  sale: number;
  price: number;
  promotionPrice: number;
  subTitle: string;
  description: string;
  stock: number;
}

export interface CmsSubject {
  id: number;
  categoryId: number;
  title: string;
  pic: string;
  productCount: number;
  recommendStatus: number;
  collectCount: number;
  readCount: number;
  commentCount: number;
}

export interface PmsProductCategory {
  id: number;
  parentId: number;
  name: string;
  level: number;
  productCount: number;
  productUnit: string;
  navStatus: number;
  showStatus: number;
  sort: number;
  icon: string;
  keywords: string;
}

export interface HomeContentResult {
  advertiseList: any[];
  brandList: any[];
  homeFlashPromotion: any;
  newProductList: PmsProduct[];
  hotProductList: PmsProduct[];
  subjectList: CmsSubject[];
}

export const fetchHomeContent = () => {
  return request.get<any, { data: HomeContentResult }>('/home/content');
};

export const fetchRecommendProductList = (pageSize: number = 4, pageNum: number = 1) => {
  return request.get<any, { data: PmsProduct[] }>('/home/recommendProductList', {
    params: { pageSize, pageNum },
  });
};

export const fetchHotProductList = (pageSize: number = 6, pageNum: number = 1) => {
  return request.get<any, { data: PmsProduct[] }>('/home/hotProductList', {
    params: { pageSize, pageNum },
  });
};

export const fetchNewProductList = (pageSize: number = 6, pageNum: number = 1) => {
  return request.get<any, { data: PmsProduct[] }>('/home/newProductList', {
    params: { pageSize, pageNum },
  });
};
