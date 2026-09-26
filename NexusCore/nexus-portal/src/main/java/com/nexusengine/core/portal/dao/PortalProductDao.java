package com.nexusengine.core.portal.dao;
import org.springframework.data.domain.PageRequest;

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

    public CartProduct getCartProduct(Long productId) {
        PmsProduct product = productRepository.findById(productId).orElse(null);
        if (product == null) return null;
        List<PmsProductAttribute> attributeList = productAttributeRepository
                .findByProductAttributeCategoryId(product.getProductAttributeCategoryId());
        List<PmsSkuStock> skuStockList = skuStockRepository.findByProductId(productId);
        return new CartProduct(product, attributeList, skuStockList);
    }

    public List<PromotionProduct> getPromotionProductList(List<Long> productIdList) {
        if (productIdList == null || productIdList.isEmpty()) {
            return Collections.emptyList();
        }
        List<PmsProduct> products = productRepository.findAllById(productIdList);
        List<PromotionProduct> result = new ArrayList<>();
        for (PmsProduct product : products) {
            List<PmsSkuStock> skuStockList = skuStockRepository.findByProductId(product.getId());
            List<PmsProductLadder> productLadderList = productLadderRepository.findByProductId(product.getId());
            List<PmsProductFullReduction> productFullReductionList = productFullReductionRepository.findByProductId(product.getId());
            result.add(new PromotionProduct(product, skuStockList, productLadderList, productFullReductionList));
        }
        return result;
    }

    public List<SmsCoupon> getAvailableCouponList(Long productId, Long productCategoryId) {
        List<SmsCoupon> allCoupons = couponRepository.findAll(PageRequest.of(0, 1000)).getContent();
        return allCoupons.stream()
                .filter(coupon -> {
                    if (coupon.getUseType() == null) return false;
                    if (coupon.getUseType() == 0) return true;
                    if (coupon.getUseType() == 1) {
                        List<SmsCouponProductCategoryRelation> relations =
                                couponProductCategoryRelationRepository.findByCouponId(coupon.getId());
                        return relations.stream().anyMatch(r -> r.getProductCategoryId().equals(productCategoryId));
                    }
                    if (coupon.getUseType() == 2) {
                        List<SmsCouponProductRelation> relations =
                                couponProductRelationRepository.findByCouponId(coupon.getId());
                        return relations.stream().anyMatch(r -> r.getProductId().equals(productId));
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }

    public List<SmsCouponHistoryDetail> getDetailList(Long memberId) {
        List<SmsCouponHistory> historyList = couponHistoryRepository.findByMemberId(memberId);
        List<SmsCouponHistoryDetail> result = new ArrayList<>();
        for (SmsCouponHistory history : historyList) {
            SmsCoupon coupon = couponRepository.findById(history.getCouponId()).orElse(null);
            List<SmsCouponProductRelation> pr = new ArrayList<>();
            List<SmsCouponProductCategoryRelation> cr = new ArrayList<>();
            if (coupon != null) {
                pr = couponProductRelationRepository.findByCouponId(coupon.getId());
                cr = couponProductCategoryRelationRepository.findByCouponId(coupon.getId());
            }
            result.add(new SmsCouponHistoryDetail(history, coupon, pr, cr));
        }
        return result;
    }
}
