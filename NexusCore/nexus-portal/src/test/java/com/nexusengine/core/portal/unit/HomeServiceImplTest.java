package com.nexusengine.core.portal.unit;

import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import com.nexusengine.core.model.*;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import com.nexusengine.core.portal.dao.HomeDao;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import com.nexusengine.core.portal.domain.FlashPromotionProduct;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import com.nexusengine.core.portal.domain.HomeContentResult;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import com.nexusengine.core.repository.*;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import org.junit.jupiter.api.BeforeEach;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import org.junit.jupiter.api.Test;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import org.mockito.InjectMocks;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import org.mockito.Mock;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import org.mockito.junit.jupiter.MockitoExtension;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import org.springframework.data.domain.PageImpl;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import org.springframework.data.domain.PageRequest;

import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import java.util.Collections;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import java.util.Date;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import java.util.List;

import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import static org.mockito.ArgumentMatchers.any;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import static org.mockito.ArgumentMatchers.eq;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HomeServiceImplTest {

    @Mock
    private SmsHomeAdvertiseRepository advertiseRepository;
    @Mock
    private HomeDao homeDao;
    @Mock
    private SmsFlashPromotionRepository flashPromotionRepository;
    @Mock
    private SmsFlashPromotionSessionRepository promotionSessionRepository;
    @Mock
    private PmsProductRepository productRepository;
    @Mock
    private PmsProductCategoryRepository productCategoryRepository;
    @Mock
    private CmsSubjectRepository subjectRepository;
    @Mock
    private com.nexusengine.core.portal.service.PmsProductSemanticSearchService semanticSearchService;

    @InjectMocks
    private HomeServiceImpl homeService;

    @Test
    void content_ReturnsAggregatedData() {
        when(advertiseRepository.findByTypeAndStatusOrderBySortDesc(1, 1))
                .thenReturn(Collections.singletonList(new SmsHomeAdvertise()));
        when(homeDao.getRecommendBrandList(0, 6))
                .thenReturn(Collections.singletonList(new PmsBrand()));
        when(homeDao.getNewProductList(0, 4))
                .thenReturn(Collections.singletonList(new PmsProduct()));
        when(homeDao.getHotProductList(0, 4))
                .thenReturn(Collections.singletonList(new PmsProduct()));
        when(homeDao.getRecommendSubjectList(0, 4))
                .thenReturn(Collections.singletonList(new CmsSubject()));

        // Mock Flash Promotion
        SmsFlashPromotion flashPromotion = new SmsFlashPromotion();
        flashPromotion.setId(1L);
        when(flashPromotionRepository.findByStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(eq(1), any(), any()))
                .thenReturn(Collections.singletonList(flashPromotion));

        SmsFlashPromotionSession session = new SmsFlashPromotionSession();
        session.setId(1L);
        session.setStartTime(new Date());
        session.setEndTime(new Date(System.currentTimeMillis() + 10000));
        when(promotionSessionRepository.findByStartTimeLessThanEqualAndEndTimeGreaterThanEqual(any(), any()))
                .thenReturn(Collections.singletonList(session));

        when(homeDao.getFlashProductList(1L, 1L))
                .thenReturn(Collections.singletonList(new FlashPromotionProduct()));

        HomeContentResult result = homeService.content();

        assertNotNull(result);
        assertEquals(1, result.getAdvertiseList().size());
        assertEquals(1, result.getBrandList().size());
        assertEquals(1, result.getNewProductList().size());
        assertEquals(1, result.getHotProductList().size());
        assertEquals(1, result.getSubjectList().size());
        assertNotNull(result.getHomeFlashPromotion());
        assertEquals(1, result.getHomeFlashPromotion().getProductList().size());
    }

    @Test
    void recommendProductList_ReturnsPagedProducts() {
        when(productRepository.findAll(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(new PmsProduct())));

        List<PmsProduct> result = homeService.recommendProductList(5, 0);

        assertEquals(1, result.size());
    }

    @Test
    void getProductCateList_ReturnsCategories() {
        when(productCategoryRepository.findByParentIdOrderBySortDesc(0L))
                .thenReturn(Collections.singletonList(new PmsProductCategory()));

        List<PmsProductCategory> result = homeService.getProductCateList(0L);

        assertEquals(1, result.size());
    }

    @Test
    void getSubjectList_WithCategoryId_ReturnsSubjects() {
        when(subjectRepository.findByCategoryId(eq(1L), any(PageRequest.class)))
                .thenReturn(Collections.singletonList(new CmsSubject()));

        List<CmsSubject> result = homeService.getSubjectList(1L, 5, 0);

        assertEquals(1, result.size());
    }
}
