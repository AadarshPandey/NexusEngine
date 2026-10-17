package com.nexusengine.core.portal.catalog.dao;

import com.nexusengine.core.domain.promotion.entity.CouponProduct;
import com.nexusengine.core.domain.promotion.repository.CouponCategoryRepository;
import com.nexusengine.core.domain.catalog.entity.ProductSku;
import com.nexusengine.core.domain.promotion.entity.MemberCoupon;
import com.nexusengine.core.domain.catalog.repository.ProductSkuRepository;
import com.nexusengine.core.domain.catalog.entity.Product;
import com.nexusengine.core.domain.catalog.repository.ProductRepository;
import com.nexusengine.core.domain.promotion.entity.Coupon;
import com.nexusengine.core.domain.promotion.repository.CouponRepository;
import com.nexusengine.core.domain.promotion.repository.MemberCouponRepository;
import com.nexusengine.core.domain.promotion.repository.CouponProductRepository;
import com.nexusengine.core.domain.promotion.entity.CouponCategory;
import com.nexusengine.core.domain.catalog.entity.AttributeDefinition;
import com.nexusengine.core.domain.catalog.repository.AttributeDefinitionRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import com.nexusengine.core.portal.ordering.dto.CartProduct;
import com.nexusengine.core.portal.promotion.dto.CouponHistoryDetail;
import com.nexusengine.core.portal.catalog.dto.PromotionProduct;

@Repository
public class ProductDao {
    private final ProductRepository productRepository;
    private final AttributeDefinitionRepository productAttributeRepository;
    private final ProductSkuRepository skuStockRepository;
    private final CouponRepository couponRepository;
    private final CouponProductRepository couponProductRelationRepository;
    private final CouponCategoryRepository couponProductCategoryRelationRepository;
    private final MemberCouponRepository couponHistoryRepository;

    public ProductDao(ProductRepository productRepository,
                      AttributeDefinitionRepository productAttributeRepository,
                      ProductSkuRepository skuStockRepository,
                      CouponRepository couponRepository,
                      CouponProductRepository couponProductRelationRepository,
                      CouponCategoryRepository couponProductCategoryRelationRepository,
                      MemberCouponRepository couponHistoryRepository) {
        this.productRepository = productRepository;
        this.productAttributeRepository = productAttributeRepository;
        this.skuStockRepository = skuStockRepository;
        this.couponRepository = couponRepository;
        this.couponProductRelationRepository = couponProductRelationRepository;
        this.couponProductCategoryRelationRepository = couponProductCategoryRelationRepository;
        this.couponHistoryRepository = couponHistoryRepository;
    }

    public CartProduct getCartProduct(Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) return null;
        List<AttributeDefinition> attributeList = productAttributeRepository
                .findByProductAttributeCategoryId(product.getProductAttributeCategoryId());
        List<ProductSku> skuStockList = skuStockRepository.findByProductId(productId);
        return new CartProduct(product, attributeList, skuStockList);
    }

    public List<PromotionProduct> getPromotionProductList(List<Long> productIdList) {
        if (productIdList == null || productIdList.isEmpty()) {
            return Collections.emptyList();
        }
        List<Product> products = productRepository.findAllById(productIdList);
        List<PromotionProduct> result = new ArrayList<>();
        for (Product product : products) {
            List<ProductSku> skuStockList = skuStockRepository.findByProductId(product.getId());
            result.add(new PromotionProduct(product, skuStockList));
        }
        return result;
    }

    public List<Coupon> getAvailableCouponList(Long productId, Long productCategoryId) {
        List<Coupon> allCoupons = couponRepository.findAll(PageRequest.of(0, 1000)).getContent();
        return allCoupons.stream()
                .filter(coupon -> {
                    if (coupon.getUseType() == null) return false;
                    if (coupon.getUseType() == 0) return true;
                    if (coupon.getUseType() == 1) {
                        List<CouponCategory> relations =
                                couponProductCategoryRelationRepository.findByCouponId(coupon.getId());
                        return relations.stream().anyMatch(r -> r.getProductCategoryId().equals(productCategoryId));
                    }
                    if (coupon.getUseType() == 2) {
                        List<CouponProduct> relations =
                                couponProductRelationRepository.findByCouponId(coupon.getId());
                        return relations.stream().anyMatch(r -> r.getProductId().equals(productId));
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }

    public List<CouponHistoryDetail> getDetailList(Long memberId) {
        List<MemberCoupon> historyList = couponHistoryRepository.findByMemberId(memberId);
        List<CouponHistoryDetail> result = new ArrayList<>();
        for (MemberCoupon history : historyList) {
            Coupon coupon = couponRepository.findById(history.getCouponId()).orElse(null);
            List<CouponProduct> pr = new ArrayList<>();
            List<CouponCategory> cr = new ArrayList<>();
            if (coupon != null) {
                pr = couponProductRelationRepository.findByCouponId(coupon.getId());
                cr = couponProductCategoryRelationRepository.findByCouponId(coupon.getId());
            }
            result.add(new CouponHistoryDetail(history, coupon, pr, cr));
        }
        return result;
    }
}
