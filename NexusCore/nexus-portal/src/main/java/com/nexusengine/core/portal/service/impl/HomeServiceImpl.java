package com.nexusengine.core.portal.service.impl;

import com.nexusengine.core.model.*;
import com.nexusengine.core.portal.dao.HomeDao;
import com.nexusengine.core.portal.domain.HomeContentResult;
import com.nexusengine.core.portal.domain.HomeFlashPromotion;
import com.nexusengine.core.portal.domain.FlashPromotionProduct;
import com.nexusengine.core.portal.service.HomeService;
import com.nexusengine.core.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Home page content Service implementation - Purged of legacy CMS/SMS dependencies
 */
@Service
@lombok.RequiredArgsConstructor
@org.springframework.transaction.annotation.Transactional(readOnly = true)
public class HomeServiceImpl implements HomeService {
    private final PromotionalBannerRepository advertiseRepository;
    private final HomeDao homeDao;
    private final PmsProductRepository productRepository;
    private final PmsProductCategoryRepository productCategoryRepository;
    private final com.nexusengine.core.portal.service.PmsProductSemanticSearchService semanticSearchService;
    private final SmsFlashPromotionRepository flashPromotionRepository;
    private final SmsFlashPromotionSessionRepository flashPromotionSessionRepository;
    private final SmsFlashPromotionProductRelationRepository flashPromotionProductRelationRepository;

    private void initializeMediaList(List<PmsProduct> products) {
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
        java.util.List<com.nexusengine.core.model.PromotionalBanner> resAdvertiseList = null; java.util.List<com.nexusengine.core.model.PmsBrand> resBrandList = null; java.util.List<com.nexusengine.core.model.PmsProduct> resNewProductList = null; java.util.List<com.nexusengine.core.model.PmsProduct> resHotProductList = null; java.util.List<com.nexusengine.core.model.PmsProduct> resAiRecommendProductList = new java.util.ArrayList<>();
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
            List<SmsFlashPromotion> promotions = flashPromotionRepository.findByStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(1, today, today);
            if (!promotions.isEmpty()) {
                SmsFlashPromotion promotion = promotions.get(0);
                
                // Find active session right now
                java.util.Optional<SmsFlashPromotionSession> currentSessionOpt = flashPromotionSessionRepository.findFirstByStatusAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(1, currentTime, currentTime);
                
                if (currentSessionOpt.isPresent()) {
                    SmsFlashPromotionSession currentSession = currentSessionOpt.get();
                    
                    // Set times based on today + session time
                    today = java.time.LocalDate.now();
                    hfpStartTime = (java.time.LocalDateTime.of(today, currentSession.getStartTime()));
                    hfpEndTime = (java.time.LocalDateTime.of(today, currentSession.getEndTime()));
                    
                    // Fetch products
                    List<SmsFlashPromotionProductRelation> relations = flashPromotionProductRelationRepository.findByFlashPromotionIdAndFlashPromotionSessionId(promotion.getId(), currentSession.getId());
                    List<FlashPromotionProduct> flashProductList = new java.util.ArrayList<>();
                    for (SmsFlashPromotionProductRelation relation : relations) {
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
    public List<PmsProduct> recommendProductList(Integer pageSize, Integer pageNum) {
        List<PmsProduct> list = productRepository.findAll(PageRequest.of(pageNum, pageSize)).getContent();
        initializeMediaList(list);
        return list;
    }

    @Override
    public List<PmsProductCategory> getProductCateList(Long parentId) {
        if (parentId != null && parentId == 0L) {
            return productCategoryRepository.findByParentIdIsNullOrderBySortDesc(org.springframework.data.domain.PageRequest.of(0, 100)).getContent();
        }
        return productCategoryRepository.findByParentIdOrderBySortDesc(parentId, org.springframework.data.domain.PageRequest.of(0, 100)).getContent();
    }

    @Override
    public List<PmsProduct> hotProductList(Integer pageNum, Integer pageSize) {
        int offset = pageSize * (pageNum - 1);
        List<PmsProduct> list = homeDao.getHotProductList(offset, pageSize);
        initializeMediaList(list);
        return list;
    }

    @Override
    public List<PmsProduct> newProductList(Integer pageNum, Integer pageSize) {
        int offset = pageSize * (pageNum - 1);
        List<PmsProduct> list = homeDao.getNewProductList(offset, pageSize);
        initializeMediaList(list);
        return list;
    }

    private List<PromotionalBanner> getHomeAdvertiseList() {
        return advertiseRepository.findByStatusOrderBySortDesc(1);
    }
}
