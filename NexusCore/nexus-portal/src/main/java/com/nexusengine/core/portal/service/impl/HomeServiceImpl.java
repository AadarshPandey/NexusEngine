package com.nexusengine.core.portal.service.impl;

import com.nexusengine.core.model.*;
import com.nexusengine.core.portal.dao.HomeDao;
import com.nexusengine.core.portal.domain.HomeContentResult;
import com.nexusengine.core.portal.domain.HomeFlashPromotion;
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
    private final SmsHomeAdvertiseRepository advertiseRepository;
    private final HomeDao homeDao;
    private final PmsProductRepository productRepository;
    private final PmsProductCategoryRepository productCategoryRepository;
    private final com.nexusengine.core.portal.service.PmsProductSemanticSearchService semanticSearchService;

    private void initializeMediaList(List<PmsProduct> products) {
        if (products != null) {
            products.forEach(p -> {
                if (p.getMediaList() != null) {
                    p.getMediaList().size();
                }
            });
        }
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    @Override
    public HomeContentResult content() {
        HomeContentResult result = new HomeContentResult();
        result.setAdvertiseList(getHomeAdvertiseList());
        result.setBrandList(homeDao.getRecommendBrandList(0, 6));
        result.setNewProductList(homeDao.getNewProductList(0, 4));
        result.setHotProductList(homeDao.getHotProductList(0, 4));
        
        initializeMediaList(result.getNewProductList());
        initializeMediaList(result.getHotProductList());
        
        // Return an empty FlashPromotion object to prevent frontend crashes
        result.setHomeFlashPromotion(new HomeFlashPromotion());
        
        try {
            result.setAiRecommendProductList(semanticSearchService.semanticSearch(null, "latest trending electronics smartphones laptops"));
            initializeMediaList(result.getAiRecommendProductList());
        } catch (Exception e) {
            result.setAiRecommendProductList(new java.util.ArrayList<>());
        }
        return result;
    }

    @Override
    public List<PmsProduct> recommendProductList(Integer pageSize, Integer pageNum) {
        List<PmsProduct> list = productRepository.findAll(PageRequest.of(pageNum, pageSize)).getContent();
        initializeMediaList(list);
        return list;
    }

    @Override
    public List<PmsProductCategory> getProductCateList(Long parentId) {
        return productCategoryRepository.findByParentIdOrderBySortDesc(parentId);
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

    private List<SmsHomeAdvertise> getHomeAdvertiseList() {
        return advertiseRepository.findByTypeAndStatusOrderBySortDesc(1, 1);
    }
}
