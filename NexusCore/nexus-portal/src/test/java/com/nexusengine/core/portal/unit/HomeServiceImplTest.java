package com.nexusengine.core.portal.unit;

import com.nexusengine.core.model.PmsBrand;
import com.nexusengine.core.model.PmsProduct;
import com.nexusengine.core.model.SmsHomeAdvertise;
import com.nexusengine.core.portal.dao.HomeDao;
import com.nexusengine.core.portal.domain.HomeContentResult;
import com.nexusengine.core.portal.service.impl.HomeServiceImpl;
import com.nexusengine.core.repository.SmsHomeAdvertiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class HomeServiceImplTest {
    @InjectMocks
    private HomeServiceImpl homeService;
    @Mock
    private HomeDao homeDao;
    @Mock
    private SmsHomeAdvertiseRepository advertiseRepository;
    @Mock
    private com.nexusengine.core.portal.service.PmsProductSemanticSearchService semanticSearchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void content_Success() {
        when(advertiseRepository.findByTypeAndStatusOrderBySortDesc(1, 1))
                .thenReturn(Collections.singletonList(new SmsHomeAdvertise()));
        when(homeDao.getRecommendBrandList(anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(new PmsBrand()));
        when(homeDao.getNewProductList(anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(new PmsProduct()));
        when(homeDao.getHotProductList(anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(new PmsProduct()));

        HomeContentResult result = homeService.content();

        assertNotNull(result);
        assertFalse(result.getAdvertiseList().isEmpty());
        assertFalse(result.getBrandList().isEmpty());
        assertFalse(result.getNewProductList().isEmpty());
        assertFalse(result.getHotProductList().isEmpty());
        assertNotNull(result.getHomeFlashPromotion());
    }
}
