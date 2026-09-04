package com.nexusengine.core.portal.service.impl;

import com.nexusengine.core.model.*;
import com.nexusengine.core.portal.dao.HomeDao;
import com.nexusengine.core.portal.domain.FlashPromotionProduct;
import com.nexusengine.core.portal.domain.HomeContentResult;
import com.nexusengine.core.portal.domain.HomeFlashPromotion;
import com.nexusengine.core.portal.service.HomeService;
import com.nexusengine.core.portal.util.DateUtil;
import com.nexusengine.core.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * Home page content Service implementation
 */
@Service
public class HomeServiceImpl implements HomeService {
    @Autowired
    private SmsHomeAdvertiseRepository advertiseRepository;
    @Autowired
    private HomeDao homeDao;
    @Autowired
    private SmsFlashPromotionRepository flashPromotionRepository;
    @Autowired
    private SmsFlashPromotionSessionRepository promotionSessionRepository;
    @Autowired
    private PmsProductRepository productRepository;
    @Autowired
    private PmsProductCategoryRepository productCategoryRepository;
    @Autowired
    private CmsSubjectRepository subjectRepository;
    @Autowired
    private com.nexusengine.core.portal.service.PmsProductSemanticSearchService semanticSearchService;

    @Override
    public HomeContentResult content() {
        HomeContentResult result = new HomeContentResult();
        result.setAdvertiseList(getHomeAdvertiseList());
        result.setBrandList(homeDao.getRecommendBrandList(0, 6));
        result.setHomeFlashPromotion(getHomeFlashPromotion());
        result.setNewProductList(homeDao.getNewProductList(0, 4));
        result.setHotProductList(homeDao.getHotProductList(0, 4));
        result.setSubjectList(homeDao.getRecommendSubjectList(0, 4));
        try {
            // Default generic context for anonymous users on homepage, or we could fetch member browsing history
            result.setAiRecommendProductList(semanticSearchService.semanticSearch(null, "latest trending electronics smartphones laptops"));
        } catch (Exception e) {
            // Gracefully degrade if AI service is not configured or down
            result.setAiRecommendProductList(new java.util.ArrayList<>());
        }
        return result;
    }

    @Override
    public List<PmsProduct> recommendProductList(Integer pageSize, Integer pageNum) {
        return productRepository.findAll(PageRequest.of(pageNum, pageSize)).getContent();
    }

    @Override
    public List<PmsProductCategory> getProductCateList(Long parentId) {
        return productCategoryRepository.findByParentIdOrderBySortDesc(parentId);
    }

    @Override
    public List<CmsSubject> getSubjectList(Long cateId, Integer pageSize, Integer pageNum) {
        if (cateId != null) {
            return subjectRepository.findByCategoryId(cateId, PageRequest.of(pageNum, pageSize));
        }
        return subjectRepository.findAll(PageRequest.of(pageNum, pageSize)).getContent();
    }

    @Override
    public List<PmsProduct> hotProductList(Integer pageNum, Integer pageSize) {
        int offset = pageSize * (pageNum - 1);
        return homeDao.getHotProductList(offset, pageSize);
    }

    @Override
    public List<PmsProduct> newProductList(Integer pageNum, Integer pageSize) {
        int offset = pageSize * (pageNum - 1);
        return homeDao.getNewProductList(offset, pageSize);
    }

    private HomeFlashPromotion getHomeFlashPromotion() {
        HomeFlashPromotion homeFlashPromotion = new HomeFlashPromotion();
        Date now = new Date();
        SmsFlashPromotion flashPromotion = getFlashPromotion(now);
        if (flashPromotion != null) {
            SmsFlashPromotionSession flashPromotionSession = getFlashPromotionSession(now);
            if (flashPromotionSession != null) {
                homeFlashPromotion.setStartTime(flashPromotionSession.getStartTime());
                homeFlashPromotion.setEndTime(flashPromotionSession.getEndTime());
                SmsFlashPromotionSession nextSession = getNextFlashPromotionSession(homeFlashPromotion.getStartTime());
                if (nextSession != null) {
                    homeFlashPromotion.setNextStartTime(nextSession.getStartTime());
                    homeFlashPromotion.setNextEndTime(nextSession.getEndTime());
                }
                List<FlashPromotionProduct> flashProductList = homeDao.getFlashProductList(flashPromotion.getId(), flashPromotionSession.getId());
                homeFlashPromotion.setProductList(flashProductList);
            }
        }
        return homeFlashPromotion;
    }

    private SmsFlashPromotionSession getNextFlashPromotionSession(Date date) {
        List<SmsFlashPromotionSession> sessions = promotionSessionRepository.findByStartTimeGreaterThanOrderByStartTimeAsc(date);
        if (!CollectionUtils.isEmpty(sessions)) {
            return sessions.get(0);
        }
        return null;
    }

    private List<SmsHomeAdvertise> getHomeAdvertiseList() {
        return advertiseRepository.findByTypeAndStatusOrderBySortDesc(1, 1);
    }

    private SmsFlashPromotion getFlashPromotion(Date date) {
        Date currDate = DateUtil.getDate(date);
        List<SmsFlashPromotion> list = flashPromotionRepository.findByStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(1, currDate, currDate);
        if (!CollectionUtils.isEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    private SmsFlashPromotionSession getFlashPromotionSession(Date date) {
        Date currTime = DateUtil.getTime(date);
        List<SmsFlashPromotionSession> sessions = promotionSessionRepository.findByStartTimeLessThanEqualAndEndTimeGreaterThanEqual(currTime, currTime);
        if (!CollectionUtils.isEmpty(sessions)) {
            return sessions.get(0);
        }
        return null;
    }
}
