package com.nexusengine.core.portal.catalog.dao;

import com.nexusengine.core.domain.catalog.entity.Brand;
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
import com.nexusengine.core.domain.catalog.entity.Product;
import com.nexusengine.core.domain.catalog.repository.BrandRepository;
import com.nexusengine.core.domain.catalog.repository.ProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;


/**
 * Home page data access - replaces legacy MyBatis HomeDao
 */
@Repository
public class HomeDao {
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;

    public HomeDao(ProductRepository productRepository, BrandRepository brandRepository) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
    }

    public List<Brand> getRecommendBrandList(int offset, int limit) {
        // Return first N brands sorted by id since sort column was removed
        return brandRepository.findAll(PageRequest.of(offset / Math.max(limit, 1), limit, Sort.by(Sort.Direction.DESC, "id"))).getContent();
    }

    public List<Product> getNewProductList(int offset, int limit) {
        return productRepository.findAll(PageRequest.of(offset / Math.max(limit, 1), limit, Sort.by(Sort.Direction.DESC, "id"))).getContent();
    }

    public List<Product> getHotProductList(int offset, int limit) {
        return productRepository.findAll(PageRequest.of(offset / Math.max(limit, 1), limit, Sort.by(Sort.Direction.DESC, "sale"))).getContent();
    }

    public List<FlashPromotionProduct> getFlashProductList(Long flashPromotionId, Long sessionId) {
        // Flash promotion product lookup - simplified since we don't have the relation table easily
        return Collections.emptyList();
    }
}
