package com.nexusengine.core.portal.dao;

import com.nexusengine.core.model.*;
import com.nexusengine.core.portal.domain.CartProduct;
import com.nexusengine.core.portal.domain.PromotionProduct;
import com.nexusengine.core.portal.domain.SmsCouponHistoryDetail;
import com.nexusengine.core.repository.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Portal-specific composite data access for products, promotions, and coupons.
 * Replaces the legacy MyBatis XML-based DAO with JPA repository calls.
 */
@Repository
public class PortalProductDao {
    @Autowired
    private PmsProductRepository productRepository;
    @Autowired
    private PmsProductAttributeRepository productAttributeRepository;
    @Autowired
    private PmsSkuStockRepository skuStockRepository;
    @Autowired
    private PmsProductLadderRepository productLadderRepository;
    @Autowired
    private PmsProductFullReductionRepository productFullReductionRepository;
    @Autowired
    private SmsCouponRepository couponRepository;
    @Autowired
    private SmsCouponProductRelationRepository couponProductRelationRepository;
    @Autowired
    private SmsCouponProductCategoryRelationRepository couponProductCategoryRelationRepository;
    @Autowired
    private SmsCouponHistoryRepository couponHistoryRepository;

    /**
     * Get cart product with attributes and SKU stock info
     */
    public CartProduct getCartProduct(Long productId) {
        PmsProduct product = productRepository.findById(productId).orElse(null);
        if (product == null) return null;
        CartProduct cartProduct = new CartProduct();
        BeanUtils.copyProperties(product, cartProduct);
        List<PmsProductAttribute> attributeList = productAttributeRepository
                .findByProductAttributeCategoryId(product.getProductAttributeCategoryId());
        cartProduct.setProductAttributeList(attributeList);
        List<PmsSkuStock> skuStockList = skuStockRepository.findByProductId(productId);
        cartProduct.setSkuStockList(skuStockList);
        return cartProduct;
    }

    /**
     * Get promotion product info including SKU stock, ladder pricing, and full reduction
     */
    public List<PromotionProduct> getPromotionProductList(List<Long> productIdList) {
        if (productIdList == null || productIdList.isEmpty()) {
            return Collections.emptyList();
        }
        List<PmsProduct> products = productRepository.findAllById(productIdList);
        List<PromotionProduct> result = new ArrayList<>();
        for (PmsProduct product : products) {
            PromotionProduct pp = new PromotionProduct();
            BeanUtils.copyProperties(product, pp);
            pp.setSkuStockList(skuStockRepository.findByProductId(product.getId()));
            pp.setProductLadderList(productLadderRepository.findByProductId(product.getId()));
            pp.setProductFullReductionList(productFullReductionRepository.findByProductId(product.getId()));
            result.add(pp);
        }
        return result;
    }

    /**
     * Get available coupons for a product (by product ID or product category ID)
     */
    public List<SmsCoupon> getAvailableCouponList(Long productId, Long productCategoryId) {
        List<SmsCoupon> allCoupons = couponRepository.findAll();
        // Filter coupons that are usable (type=0 for all products, or matching product/category)
        return allCoupons.stream()
                .filter(coupon -> {
                    if (coupon.getUseType() == null) return false;
                    if (coupon.getUseType() == 0) return true; // Universal coupon
                    if (coupon.getUseType() == 1) {
                        // Product-specific coupon
                        List<SmsCouponProductRelation> relations =
                                couponProductRelationRepository.findByCouponId(coupon.getId());
                        return relations.stream().anyMatch(r -> r.getProductId().equals(productId));
                    }
                    if (coupon.getUseType() == 2) {
                        // Category-specific coupon
                        List<SmsCouponProductCategoryRelation> relations =
                                couponProductCategoryRelationRepository.findByCouponId(coupon.getId());
                        return relations.stream().anyMatch(r -> r.getProductCategoryId().equals(productCategoryId));
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }

    /**
     * Get coupon history details with full coupon and relation info
     */
    public List<SmsCouponHistoryDetail> getDetailList(Long memberId) {
        List<SmsCouponHistory> historyList = couponHistoryRepository.findByMemberId(memberId);
        List<SmsCouponHistoryDetail> result = new ArrayList<>();
        for (SmsCouponHistory history : historyList) {
            SmsCouponHistoryDetail detail = new SmsCouponHistoryDetail();
            BeanUtils.copyProperties(history, detail);
            SmsCoupon coupon = couponRepository.findById(history.getCouponId()).orElse(null);
            detail.setCoupon(coupon);
            if (coupon != null) {
                detail.setProductRelationList(
                        couponProductRelationRepository.findByCouponId(coupon.getId()));
                detail.setCategoryRelationList(
                        couponProductCategoryRelationRepository.findByCouponId(coupon.getId()));
            }
            result.add(detail);
        }
        return result;
    }
}
