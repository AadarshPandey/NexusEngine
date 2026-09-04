package com.nexusengine.core.portal.unit;

import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import com.nexusengine.core.model.*;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import com.nexusengine.core.portal.dao.PortalProductDao;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import com.nexusengine.core.portal.domain.PmsPortalProductDetail;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import com.nexusengine.core.portal.domain.PmsProductCategoryNode;
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
import org.springframework.data.domain.Page;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import org.springframework.data.domain.PageImpl;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import org.springframework.data.domain.PageRequest;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import org.springframework.data.jpa.domain.Specification;

import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import java.util.Arrays;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import java.util.Collections;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import java.util.List;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import java.util.Optional;

import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import static org.junit.jupiter.api.Assertions.*;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import static org.mockito.ArgumentMatchers.any;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PmsPortalProductServiceImplTest {

    @Mock
    private PmsProductRepository productRepository;
    @Mock
    private PmsProductCategoryRepository productCategoryRepository;
    @Mock
    private PmsBrandRepository brandRepository;
    @Mock
    private PmsProductAttributeRepository productAttributeRepository;
    @Mock
    private PmsProductAttributeValueRepository productAttributeValueRepository;
    @Mock
    private PmsSkuStockRepository skuStockRepository;
    @Mock
    private PmsProductLadderRepository productLadderRepository;
    @Mock
    private PmsProductFullReductionRepository productFullReductionRepository;
    @Mock
    private PortalProductDao portalProductDao;

    @InjectMocks
    private PmsPortalProductServiceImpl productService;

    private PmsProduct testProduct;
    private PmsProductCategory rootCategory;
    private PmsProductCategory childCategory;

    @BeforeEach
    void setUp() {
        testProduct = new PmsProduct();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setBrandId(1L);
        testProduct.setProductCategoryId(1L);
        testProduct.setProductAttributeCategoryId(1L);
        testProduct.setPromotionType(0);

        rootCategory = new PmsProductCategory();
        rootCategory.setId(1L);
        rootCategory.setParentId(0L);
        rootCategory.setName("Root");

        childCategory = new PmsProductCategory();
        childCategory.setId(2L);
        childCategory.setParentId(1L);
        childCategory.setName("Child");
    }

    @Test
    void search_ReturnsProductList() {
        Page<PmsProduct> page = new PageImpl<>(Collections.singletonList(testProduct));
        
        when(productRepository.findAll(any(Specification.class), any(PageRequest.class)))
                .thenReturn(page);

        List<PmsProduct> result = productService.search("Test", 1L, 1L, 1, 10, 1);

        assertFalse(result.isEmpty());
        assertEquals("Test Product", result.get(0).getName());
    }

    @Test
    void categoryTreeList_ReturnsNestedCategories() {
        when(productCategoryRepository.findAll()).thenReturn(Arrays.asList(rootCategory, childCategory));

        List<PmsProductCategoryNode> result = productService.categoryTreeList();

        assertEquals(1, result.size());
        assertEquals("Root", result.get(0).getName());
        assertEquals(1, result.get(0).getChildren().size());
        assertEquals("Child", result.get(0).getChildren().get(0).getName());
    }

    @Test
    void detail_ProductExists_ReturnsFullDetail() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        
        PmsBrand brand = new PmsBrand();
        brand.setId(1L);
        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));

        PmsProductAttribute attribute = new PmsProductAttribute();
        attribute.setId(1L);
        when(productAttributeRepository.findByProductAttributeCategoryId(1L))
                .thenReturn(Collections.singletonList(attribute));

        PmsProductAttributeValue value = new PmsProductAttributeValue();
        when(productAttributeValueRepository.findByProductIdAndProductAttributeIdIn(eq(1L), anyList()))
                .thenReturn(Collections.singletonList(value));

        when(skuStockRepository.findByProductId(1L)).thenReturn(Collections.singletonList(new PmsSkuStock()));
        when(portalProductDao.getAvailableCouponList(1L, 1L)).thenReturn(Collections.singletonList(new SmsCoupon()));

        PmsPortalProductDetail detail = productService.detail(1L);

        assertNotNull(detail.getProduct());
        assertNotNull(detail.getBrand());
        assertFalse(detail.getProductAttributeList().isEmpty());
        assertFalse(detail.getProductAttributeValueList().isEmpty());
        assertFalse(detail.getSkuStockList().isEmpty());
        assertFalse(detail.getCouponList().isEmpty());
    }

    @Test
    void detail_ProductNotFound_ReturnsEmptyDetail() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        PmsPortalProductDetail detail = productService.detail(99L);

        assertNull(detail.getProduct());
    }
}
