package com.nexusengine.core.portal.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.nexusengine.core.model.*;
import com.nexusengine.core.portal.dao.PortalProductDao;
import com.nexusengine.core.portal.domain.PmsPortalProductDetail;
import com.nexusengine.core.portal.domain.PmsProductCategoryNode;
import com.nexusengine.core.portal.service.PmsPortalProductService;
import com.nexusengine.core.repository.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Portal product management Service implementation
 */
@Service
@lombok.RequiredArgsConstructor
@org.springframework.transaction.annotation.Transactional(readOnly = true)
public class PmsPortalProductServiceImpl implements PmsPortalProductService {
    private final PmsProductRepository productRepository;
    private final PmsProductCategoryRepository productCategoryRepository;
    private final PmsBrandRepository brandRepository;
    private final PmsProductAttributeRepository productAttributeRepository;
    private final PmsProductAttributeValueRepository productAttributeValueRepository;
    private final PmsSkuStockRepository skuStockRepository;
    private final PmsProductVolumeDiscountRepository productVolumeDiscountRepository;
    private final PmsProductSpendDiscountRepository productSpendDiscountRepository;
    private final PortalProductDao portalProductDao;
    private final SmsFlashPromotionRepository flashPromotionRepository;

    private void initializeMediaList(List<PmsProduct> products) {
        if (products != null) {
            products.forEach(p -> {
                if (p.getGalleryList() != null) {
                    org.hibernate.Hibernate.initialize(p.getGalleryList());
                }
            });
        }
    }

    @Override
    public List<PmsProduct> search(String keyword, Long brandId, Long productCategoryId, Integer pageNum, Integer pageSize, Integer sort) {
        Sort sortOrder;
        switch (sort) {
            case 1: sortOrder = Sort.by(Sort.Direction.DESC, "id"); break;
            case 2: sortOrder = Sort.by(Sort.Direction.DESC, "sale"); break;
            case 3: sortOrder = Sort.by(Sort.Direction.ASC, "price"); break;
            case 4: sortOrder = Sort.by(Sort.Direction.DESC, "price"); break;
            default: sortOrder = Sort.by(Sort.Direction.DESC, "id"); break;
        }
        // Use Specification for dynamic queries
        List<PmsProduct> list = productRepository.findAll((root, query, cb) -> {
            var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();
            predicates.add(cb.equal(root.get("deleteStatus"), 0));
            predicates.add(cb.equal(root.get("publishStatus"), 1));
            if (StrUtil.isNotEmpty(keyword)) {
                String likeKeyword = "%" + keyword.toLowerCase() + "%";
                predicates.add(cb.or(
                    cb.like(cb.lower(root.get("name")), likeKeyword),
                    cb.like(cb.lower(root.get("keywords")), likeKeyword),
                    cb.like(cb.lower(root.get("brandName")), likeKeyword),
                    cb.like(cb.lower(root.get("productSn")), likeKeyword)
                ));
            }
            if (brandId != null) {
                predicates.add(cb.equal(root.get("brandId"), brandId));
            }
            if (productCategoryId != null) {
                predicates.add(cb.equal(root.get("productCategoryId"), productCategoryId));
            }
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        }, PageRequest.of(pageNum - 1, pageSize, sortOrder)).getContent();
        initializeMediaList(list);
        return list;
    }

    @Override
    public List<PmsProductCategoryNode> categoryTreeList() {
        List<PmsProductCategory> allList = productCategoryRepository.findAll(PageRequest.of(0, 1000)).getContent();
        return allList.stream()
                .filter(item -> item.getParentId().equals(0L))
                .map(item -> covert(item, allList))
                .collect(Collectors.toList());
    }

    @Override
    public PmsPortalProductDetail detail(Long id) {
        PmsProduct product = productRepository.findById(id).orElse(null);
        if (product != null && product.getGalleryList() != null) {
            org.hibernate.Hibernate.initialize(product.getGalleryList());
        }
        if (product == null) return new PmsPortalProductDetail(null, null, null, null, null, null, null, null, null);
        
        PmsBrand brand = null;
        if (product.getBrandId() != null) {
            brand = brandRepository.findById(product.getBrandId()).orElse(null);
        }
        
        List<PmsProductAttribute> productAttributeList = productAttributeRepository
                .findByProductAttributeCategoryId(product.getProductAttributeCategoryId());
                
        List<PmsProductAttributeValue> valueList = null;
        if (CollUtil.isNotEmpty(productAttributeList)) {
            List<Long> attributeIds = productAttributeList.stream().map(PmsProductAttribute::getId).collect(Collectors.toList());
            valueList = productAttributeValueRepository.findByProductIdAndProductAttributeIdIn(product.getId(), attributeIds);
        }
        
        List<PmsSkuStock> skuStockList = skuStockRepository.findByProductId(product.getId());
        
        List<PmsProductVolumeDiscount> productVolumeDiscountList = null;
        if (product.getPromotionType() != null && product.getPromotionType() == 3) {
            productVolumeDiscountList = productVolumeDiscountRepository.findByProductId(product.getId());
        }
        
        List<PmsProductSpendDiscount> productSpendDiscountList = null;
        if (product.getPromotionType() != null && product.getPromotionType() == 4) {
            productSpendDiscountList = productSpendDiscountRepository.findByProductId(product.getId());
        }
        
        List<com.nexusengine.core.model.SmsCoupon> couponList = portalProductDao.getAvailableCouponList(product.getId(), product.getProductCategoryId());
        
        java.time.LocalDate today = java.time.LocalDate.now();
        List<SmsFlashPromotion> promotions = flashPromotionRepository.findByStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(1, today, today);
        Boolean flashSaleActive = !promotions.isEmpty();
        
        return new PmsPortalProductDetail(product, brand, productAttributeList, valueList, skuStockList, productVolumeDiscountList, productSpendDiscountList, couponList, flashSaleActive);
    }

    private PmsProductCategoryNode covert(PmsProductCategory item, List<PmsProductCategory> allList) {
        List<PmsProductCategoryNode> children = allList.stream()
                .filter(subItem -> subItem.getParentId().equals(item.getId()))
                .map(subItem -> covert(subItem, allList)).collect(Collectors.toList());
        return new PmsProductCategoryNode(item, children);
    }
}
