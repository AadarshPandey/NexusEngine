package com.nexusengine.core.portal.home.service;

import lombok.RequiredArgsConstructor;

import com.nexusengine.core.domain.promotion.entity.Banner;
import com.nexusengine.core.domain.catalog.entity.Product;
import com.nexusengine.core.domain.catalog.repository.ProductRepository;
import com.nexusengine.core.domain.catalog.entity.Category;
import com.nexusengine.core.domain.catalog.entity.Brand;
import com.nexusengine.core.domain.catalog.repository.CategoryRepository;
import com.nexusengine.core.domain.promotion.repository.BannerRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;
import com.nexusengine.core.portal.home.dto.HomeContentResult;
import com.nexusengine.core.portal.catalog.dao.HomeDao;

/**
 * Home page content Service implementation - Purged of legacy CMS/SMS dependencies
 */
@Service
@RequiredArgsConstructor
@org.springframework.transaction.annotation.Transactional(readOnly = true)
public class HomeServiceImpl implements HomeService {
    private final BannerRepository advertiseRepository;
    private final HomeDao homeDao;
    private final ProductRepository productRepository;
    private final CategoryRepository productCategoryRepository;
    private final com.nexusengine.core.portal.customer.service.MemberService memberService;
    private final com.nexusengine.core.domain.customer.repository.MemberCategoryInterestRepository memberCategoryInterestRepository;

    private void initializeMediaList(List<Product> products) {
        if (products != null) {
            products.forEach(p -> {
                if (p.getGalleryList() != null) {
                    org.hibernate.Hibernate.initialize(p.getGalleryList());
                }
            });
        }
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    @Override
    public HomeContentResult content() {
        java.util.List<Banner> resAdvertiseList = getHomeAdvertiseList();
        java.util.List<Brand> resBrandList = homeDao.getRecommendBrandList(0, 6);
        java.util.List<Product> resNewProductList = homeDao.getNewProductList(0, 4);
        java.util.List<Product> resHotProductList = homeDao.getHotProductList(0, 4);
        java.util.List<Product> resAiRecommendProductList = java.util.List.of();

        initializeMediaList(resNewProductList);
        initializeMediaList(resHotProductList);

        return new HomeContentResult(resAdvertiseList, resBrandList, resNewProductList, resHotProductList, resAiRecommendProductList);
    }

    @Override
    public List<Product> recommendProductList(Integer pageSize, Integer pageNum) {
        try {
            var currentMember = memberService.getCurrentMember();
            if (currentMember != null) {
                var interests = memberCategoryInterestRepository.findByMemberId(currentMember.getId());
                if (interests != null && !interests.isEmpty()) {
                    List<Long> catIds = interests.stream().map(com.nexusengine.core.domain.customer.entity.MemberCategoryInterest::getProductCategoryId).toList();
                    List<Product> interestProducts = productRepository.findByProductCategoryIdIn(catIds, PageRequest.of(pageNum, pageSize)).getContent();
                    if (!interestProducts.isEmpty()) {
                        initializeMediaList(interestProducts);
                        return interestProducts;
                    }
                }
            }
        } catch (Exception ignored) {
        }
        List<Product> list = productRepository.findAll(PageRequest.of(pageNum, pageSize)).getContent();
        initializeMediaList(list);
        return list;
    }

    @Override
    public List<Category> getProductCateList(Long parentId) {
        if (parentId != null && parentId == 0L) {
            return productCategoryRepository.findByParentIdIsNull(org.springframework.data.domain.PageRequest.of(0, 100)).getContent();
        }
        return productCategoryRepository.findByParentId(parentId, org.springframework.data.domain.PageRequest.of(0, 100)).getContent();
    }

    @Override
    public List<Product> hotProductList(Integer pageNum, Integer pageSize) {
        int offset = pageSize * (pageNum - 1);
        List<Product> list = homeDao.getHotProductList(offset, pageSize);
        initializeMediaList(list);
        return list;
    }

    @Override
    public List<Product> newProductList(Integer pageNum, Integer pageSize) {
        int offset = pageSize * (pageNum - 1);
        List<Product> list = homeDao.getNewProductList(offset, pageSize);
        initializeMediaList(list);
        return list;
    }

    private List<Banner> getHomeAdvertiseList() {
        return advertiseRepository.findByStatusOrderByDisplayOrderDesc(true);
    }
}
