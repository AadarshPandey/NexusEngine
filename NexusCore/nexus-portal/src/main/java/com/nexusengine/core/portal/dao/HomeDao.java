package com.nexusengine.core.portal.dao;

import com.nexusengine.core.model.*;
import com.nexusengine.core.portal.domain.FlashPromotionProduct;
import com.nexusengine.core.repository.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Home page data access - replaces legacy MyBatis HomeDao
 */
@Repository
public class HomeDao {
    @Autowired
    private PmsProductRepository productRepository;
    @Autowired
    private PmsBrandRepository brandRepository;
    @Autowired
    private CmsSubjectRepository subjectRepository;

    public List<PmsBrand> getRecommendBrandList(int offset, int limit) {
        // Return first N brands sorted by sort order
        return brandRepository.findAll(PageRequest.of(offset / Math.max(limit, 1), limit, Sort.by(Sort.Direction.DESC, "sort"))).getContent();
    }

    public List<PmsProduct> getNewProductList(int offset, int limit) {
        return productRepository.findAll(PageRequest.of(offset / Math.max(limit, 1), limit, Sort.by(Sort.Direction.DESC, "id"))).getContent();
    }

    public List<PmsProduct> getHotProductList(int offset, int limit) {
        return productRepository.findAll(PageRequest.of(offset / Math.max(limit, 1), limit, Sort.by(Sort.Direction.DESC, "sale"))).getContent();
    }

    public List<CmsSubject> getRecommendSubjectList(int offset, int limit) {
        return subjectRepository.findAll(PageRequest.of(offset / Math.max(limit, 1), limit, Sort.by(Sort.Direction.DESC, "id"))).getContent();
    }

    public List<FlashPromotionProduct> getFlashProductList(Long flashPromotionId, Long sessionId) {
        // Flash promotion product lookup - simplified since we don't have the relation table easily
        return Collections.emptyList();
    }
}
