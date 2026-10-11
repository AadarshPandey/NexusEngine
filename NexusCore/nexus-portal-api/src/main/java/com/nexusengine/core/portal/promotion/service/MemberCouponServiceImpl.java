package com.nexusengine.core.portal.promotion.service;

import lombok.RequiredArgsConstructor;

import com.nexusengine.core.domain.promotion.repository.CouponCategoryRepository;
import com.nexusengine.core.domain.promotion.entity.MemberCoupon;
import com.nexusengine.core.domain.promotion.entity.CouponProduct;
import com.nexusengine.core.domain.catalog.entity.Product;
import com.nexusengine.core.domain.catalog.repository.ProductRepository;
import com.nexusengine.core.domain.promotion.entity.Coupon;
import com.nexusengine.core.domain.promotion.repository.CouponRepository;
import com.nexusengine.core.domain.promotion.repository.MemberCouponRepository;
import com.nexusengine.core.domain.promotion.repository.CouponProductRepository;
import com.nexusengine.core.domain.customer.entity.Member;
import com.nexusengine.core.domain.promotion.entity.CouponCategory;

import cn.hutool.core.collection.CollUtil;
import com.nexusengine.core.common.exception.Asserts;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.security.SecureRandom;
import java.util.stream.Collectors;
import com.nexusengine.core.portal.ordering.dto.CartPromotionItem;
import com.nexusengine.core.portal.promotion.dto.CouponHistoryDetail;
import com.nexusengine.core.portal.customer.service.MemberService;
import com.nexusengine.core.portal.catalog.dao.ProductDao;


/**
 * Member coupon management Service implementation
 */
@Service
@Transactional
@RequiredArgsConstructor
public class MemberCouponServiceImpl implements MemberCouponService {
    private final MemberService memberService;
    private final CouponRepository couponRepository;
    private final MemberCouponRepository couponHistoryRepository;
    private final CouponProductRepository couponProductRelationRepository;
    private final CouponCategoryRepository couponProductCategoryRelationRepository;
    private final ProductRepository productRepository;
    private final ProductDao productDao;

    private final org.redisson.api.RedissonClient redissonClient;

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void add(Long couponId) {
        Member currentMember = memberService.getCurrentMember();
        // Fix 6: Use Redisson lock to prevent claiming race condition
        org.redisson.api.RLock lock = redissonClient.getLock("coupon:claim:" + couponId + ":" + currentMember.getId());
        try {
            if (lock.tryLock(5, 10, java.util.concurrent.TimeUnit.SECONDS)) {
                Coupon coupon = couponRepository.findById(couponId).orElse(null);
                if (coupon == null) {
                    Asserts.fail("Coupon not found");
                }
                if (coupon.getCount() <= 0) {
                    Asserts.fail("Coupon is out of stock");
                }
                OffsetDateTime now = java.time.OffsetDateTime.now();
                if (now.isBefore(coupon.getStartTime())) {
                    Asserts.fail("Coupon is not yet available");
                }
                long count = couponHistoryRepository.countByCouponIdAndMemberId(couponId, currentMember.getId());
                if (count >= coupon.getPerLimit()) {
                    Asserts.fail("You have already claimed this coupon");
                }
                MemberCoupon couponHistory = new MemberCoupon();
                couponHistory.setCouponId(couponId);
                couponHistory.setCouponCode(generateCouponCode(currentMember.getId()));
                couponHistory.setCreateTime(now);
                couponHistory.setMemberId(currentMember.getId());
                couponHistory.setMemberNickname(currentMember.getDisplayName());
                couponHistory.setGetType(1);
                couponHistory.setUseStatus(0);
                couponHistoryRepository.save(couponHistory);
                
                // Atomic stock decrement
                int countUpdated = couponRepository.decrementCouponStock(couponId);
                if (countUpdated == 0) {
                    Asserts.fail("Coupon is out of stock");
                }
            } else {
                Asserts.fail("Processing request, please wait");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Asserts.fail("Request interrupted");
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    private String generateCouponCode(Long memberId) {
        StringBuilder sb = new StringBuilder();
        Long currentTimeMillis = System.currentTimeMillis();
        String timeMillisStr = currentTimeMillis.toString();
        sb.append(timeMillisStr.substring(timeMillisStr.length() - 8));
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < 4; i++) {
            sb.append(random.nextInt(10));
        }
        String memberIdStr = memberId.toString();
        if (memberIdStr.length() <= 4) {
            sb.append(String.format("%04d", memberId));
        } else {
            sb.append(memberIdStr.substring(memberIdStr.length() - 4));
        }
        return sb.toString();
    }

    @Override
    public List<MemberCoupon> listHistory(Integer useStatus) {
        Member currentMember = memberService.getCurrentMember();
        if (useStatus != null) {
            return couponHistoryRepository.findByMemberIdAndUseStatus(currentMember.getId(), useStatus);
        }
        return couponHistoryRepository.findByMemberId(currentMember.getId());
    }

    @Override
    public List<CouponHistoryDetail> listCart(List<CartPromotionItem> cartItemList, Integer type) {
        Member currentMember = memberService.getCurrentMember();
        OffsetDateTime now = java.time.OffsetDateTime.now();
        List<CouponHistoryDetail> allList = productDao.getDetailList(currentMember.getId());
        List<CouponHistoryDetail> enableList = new ArrayList<>();
        List<CouponHistoryDetail> disableList = new ArrayList<>();
        for (CouponHistoryDetail couponHistoryDetail : allList) {
            if (couponHistoryDetail.coupon() == null) continue;
            Integer useType = couponHistoryDetail.coupon().getUseType();
            BigDecimal minOrderAmount = couponHistoryDetail.coupon().getMinOrderAmount();
            OffsetDateTime endTime = couponHistoryDetail.coupon().getEndTime();
            if (useType.equals(0)) {
                BigDecimal totalAmount = calcTotalAmount(cartItemList);
                if (now.isBefore(endTime) && totalAmount.subtract(minOrderAmount).intValue() >= 0) {
                    enableList.add(couponHistoryDetail);
                } else {
                    disableList.add(couponHistoryDetail);
                }
            } else if (useType.equals(1)) {
                List<Long> productCategoryIds = couponHistoryDetail.categoryRelationList().stream()
                        .map(CouponCategory::getProductCategoryId).collect(Collectors.toList());
                BigDecimal totalAmount = calcTotalAmountByProductCategoryId(cartItemList, productCategoryIds);
                if (now.isBefore(endTime) && totalAmount.intValue() > 0 && totalAmount.subtract(minOrderAmount).intValue() >= 0) {
                    enableList.add(couponHistoryDetail);
                } else {
                    disableList.add(couponHistoryDetail);
                }
            } else if (useType.equals(2)) {
                List<Long> productIds = couponHistoryDetail.productRelationList().stream()
                        .map(CouponProduct::getProductId).collect(Collectors.toList());
                BigDecimal totalAmount = calcTotalAmountByProductId(cartItemList, productIds);
                if (now.isBefore(endTime) && totalAmount.intValue() > 0 && totalAmount.subtract(minOrderAmount).intValue() >= 0) {
                    enableList.add(couponHistoryDetail);
                } else {
                    disableList.add(couponHistoryDetail);
                }
            }
        }
        return type.equals(1) ? enableList : disableList;
    }

    @Override
    public List<Coupon> listByProduct(Long productId) {
        return productDao.getAvailableCouponList(productId,
                productRepository.findById(productId).map(Product::getProductCategoryId).orElse(null));
    }

    @Override
    public List<Coupon> list(Integer useStatus) {
        Member member = memberService.getCurrentMember();
        List<MemberCoupon> histories;
        if (useStatus != null) {
            histories = couponHistoryRepository.findByMemberIdAndUseStatus(member.getId(), useStatus);
        } else {
            histories = couponHistoryRepository.findByMemberId(member.getId());
        }
        List<Long> couponIds = histories.stream().map(MemberCoupon::getCouponId).distinct().collect(Collectors.toList());
        if (CollUtil.isEmpty(couponIds)) return new ArrayList<>();
        return couponRepository.findAllById(couponIds);
    }

    private BigDecimal calcTotalAmount(List<CartPromotionItem> cartItemList) {
        BigDecimal total = new BigDecimal("0");
        for (CartPromotionItem item : cartItemList) {
            BigDecimal realPrice = item.cartItem().getPrice().subtract(item.reduceAmount());
            total = total.add(realPrice.multiply(new BigDecimal(item.cartItem().getQuantity())));
        }
        return total;
    }

    private BigDecimal calcTotalAmountByProductCategoryId(List<CartPromotionItem> cartItemList, List<Long> productCategoryIds) {
        BigDecimal total = new BigDecimal("0");
        for (CartPromotionItem item : cartItemList) {
            if (productCategoryIds.contains(item.cartItem().getProductCategoryId())) {
                BigDecimal realPrice = item.cartItem().getPrice().subtract(item.reduceAmount());
                total = total.add(realPrice.multiply(new BigDecimal(item.cartItem().getQuantity())));
            }
        }
        return total;
    }

    private BigDecimal calcTotalAmountByProductId(List<CartPromotionItem> cartItemList, List<Long> productIds) {
        BigDecimal total = new BigDecimal("0");
        for (CartPromotionItem item : cartItemList) {
            if (productIds.contains(item.cartItem().getProductId())) {
                BigDecimal realPrice = item.cartItem().getPrice().subtract(item.reduceAmount());
                total = total.add(realPrice.multiply(new BigDecimal(item.cartItem().getQuantity())));
            }
        }
        return total;
    }
}
