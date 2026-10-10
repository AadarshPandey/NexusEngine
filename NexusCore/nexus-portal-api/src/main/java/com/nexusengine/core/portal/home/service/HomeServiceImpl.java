package com.nexusengine.core.portal.home.service;

import lombok.RequiredArgsConstructor;
import com.nexusengine.core.portal.catalog.dto.*;
import com.nexusengine.core.portal.catalog.service.*;
import com.nexusengine.core.portal.catalog.dao.*;
import com.nexusengine.core.portal.payment.service.*;
import com.nexusengine.core.portal.ordering.dto.*;
import com.nexusengine.core.portal.ordering.service.*;
import com.nexusengine.core.portal.ordering.dao.*;
import com.nexusengine.core.portal.customer.dto.*;
import com.nexusengine.core.portal.customer.service.*;
import com.nexusengine.core.portal.promotion.dto.*;
import com.nexusengine.core.portal.promotion.service.*;
import com.nexusengine.core.portal.promotion.strategy.*;
import com.nexusengine.core.portal.ai.service.*;
import com.nexusengine.core.portal.home.dto.*;
import com.nexusengine.core.portal.home.service.*;
import com.nexusengine.core.portal.engagement.dto.*;
import com.nexusengine.core.portal.engagement.service.*;

import com.nexusengine.core.domain.promotion.entity.Banner;
import com.nexusengine.core.domain.promotion.entity.FlashSaleItem;
import com.nexusengine.core.domain.promotion.entity.FlashSaleSlot;
import com.nexusengine.core.domain.promotion.repository.FlashSaleItemRepository;
import com.nexusengine.core.domain.promotion.entity.FlashSale;
import com.nexusengine.core.domain.catalog.entity.Product;
import com.nexusengine.core.domain.catalog.repository.ProductRepository;
import com.nexusengine.core.domain.catalog.entity.Category;
import com.nexusengine.core.domain.promotion.repository.FlashSaleRepository;
import com.nexusengine.core.domain.catalog.entity.Brand;
import com.nexusengine.core.domain.catalog.repository.CategoryRepository;
import com.nexusengine.core.domain.promotion.repository.BannerRepository;
import com.nexusengine.core.domain.promotion.repository.FlashSaleSlotRepository;


import com.nexusengine.core.domain.catalog.entity.*;
import com.nexusengine.core.domain.ordering.entity.*;
import com.nexusengine.core.domain.customer.entity.*;
import com.nexusengine.core.domain.promotion.entity.*;
import com.nexusengine.core.domain.iam.entity.*;
import com.nexusengine.core.domain.catalog.repository.*;
import com.nexusengine.core.domain.ordering.repository.*;
import com.nexusengine.core.domain.customer.repository.*;
import com.nexusengine.core.domain.promotion.repository.*;
import com.nexusengine.core.domain.iam.repository.*;


import com.nexusengine.core.domain.catalog.entity.*;
import com.nexusengine.core.domain.ordering.entity.*;
import com.nexusengine.core.domain.customer.entity.*;
import com.nexusengine.core.domain.promotion.entity.*;
import com.nexusengine.core.domain.iam.entity.*;
import com.nexusengine.core.domain.catalog.repository.*;
import com.nexusengine.core.domain.ordering.repository.*;
import com.nexusengine.core.domain.customer.repository.*;
import com.nexusengine.core.domain.promotion.repository.*;
import com.nexusengine.core.domain.iam.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;


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
    private final SemanticSearchService semanticSearchService;
    private final FlashSaleRepository flashPromotionRepository;
    private final FlashSaleSlotRepository flashPromotionSessionRepository;
    private final FlashSaleItemRepository flashPromotionProductRelationRepository;

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
        java.util.List<Banner> resAdvertiseList = null; java.util.List<Brand> resBrandList = null; java.util.List<Product> resNewProductList = null; java.util.List<Product> resHotProductList = null; java.util.List<Product> resAiRecommendProductList = new java.util.ArrayList<>();
        resAdvertiseList = (getHomeAdvertiseList());
        resBrandList = (homeDao.getRecommendBrandList(0, 6));
        resNewProductList = (homeDao.getNewProductList(0, 4));
        resHotProductList = (homeDao.getHotProductList(0, 4));
        
        initializeMediaList(resNewProductList);
        initializeMediaList(resHotProductList);
        
        // Fix 4: Wire HomeFlashPromotion to query active session
        java.time.LocalDateTime hfpStartTime = null; java.time.LocalDateTime hfpEndTime = null; java.time.LocalDateTime hfpNextStartTime = null; java.time.LocalDateTime hfpNextEndTime = null; java.util.List<FlashPromotionProduct> hfpProductList = new java.util.ArrayList<>();
        try {
            java.time.LocalDateTime now = java.time.LocalDateTime.now();
            java.time.LocalTime currentTime = java.time.LocalTime.now();
            
            // Find active promotion event today
            java.time.LocalDate today = java.time.LocalDate.now();
            List<FlashSale> promotions = flashPromotionRepository.findByStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(1, today, today);
            if (!promotions.isEmpty()) {
                FlashSale promotion = promotions.get(0);
                
                // Find active session right now
                java.util.Optional<FlashSaleSlot> currentSessionOpt = flashPromotionSessionRepository.findFirstByStatusAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(1, currentTime, currentTime);
                
                if (currentSessionOpt.isPresent()) {
                    FlashSaleSlot currentSession = currentSessionOpt.get();
                    
                    // Set times based on today + session time
                    today = java.time.LocalDate.now();
                    hfpStartTime = (java.time.LocalDateTime.of(today, currentSession.getStartTime()));
                    hfpEndTime = (java.time.LocalDateTime.of(today, currentSession.getEndTime()));
                    
                    // Fetch products
                    List<FlashSaleItem> relations = flashPromotionProductRelationRepository.findByFlashPromotionIdAndFlashPromotionSessionId(promotion.getId(), currentSession.getId());
                    List<FlashPromotionProduct> flashProductList = new java.util.ArrayList<>();
                    for (FlashSaleItem relation : relations) {
                        productRepository.findById(relation.getProductId()).ifPresent(p -> {
                            FlashPromotionProduct flashProduct = new FlashPromotionProduct(p, relation.getFlashPromotionPrice(), relation.getFlashPromotionCount(), relation.getFlashPromotionLimit());
                            flashProductList.add(flashProduct);
                        });
                    }
                    hfpProductList = flashProductList;
                }
            }
        } catch (Exception e) {
            // fallback
        }
        
        
        try {
            resAiRecommendProductList = (semanticSearchService.semanticSearch(null, "latest trending electronics smartphones laptops"));
            initializeMediaList(resAiRecommendProductList);
        } catch (Exception e) {
            resAiRecommendProductList = (new java.util.ArrayList<>());
        }
        return new HomeContentResult(resAdvertiseList, resBrandList, new HomeFlashPromotion(hfpStartTime, hfpEndTime, hfpNextStartTime, hfpNextEndTime, hfpProductList), resNewProductList, resHotProductList, resAiRecommendProductList);
    }

    @Override
    public List<Product> recommendProductList(Integer pageSize, Integer pageNum) {
        List<Product> list = productRepository.findAll(PageRequest.of(pageNum, pageSize)).getContent();
        initializeMediaList(list);
        return list;
    }

    @Override
    public List<Category> getProductCateList(Long parentId) {
        if (parentId != null && parentId == 0L) {
            return productCategoryRepository.findByParentIdIsNullOrderBySortDesc(org.springframework.data.domain.PageRequest.of(0, 100)).getContent();
        }
        return productCategoryRepository.findByParentIdOrderBySortDesc(parentId, org.springframework.data.domain.PageRequest.of(0, 100)).getContent();
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
        return advertiseRepository.findByStatusOrderBySortDesc(1);
    }
}
