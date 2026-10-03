package com.nexusengine.core.portal.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.nexusengine.core.common.exception.Asserts;
import com.nexusengine.core.model.*;
import com.nexusengine.core.portal.dao.PortalProductDao;
import com.nexusengine.core.portal.domain.CartPromotionItem;
import com.nexusengine.core.portal.domain.SmsCouponHistoryDetail;
import com.nexusengine.core.portal.service.UmsMemberCouponService;
import com.nexusengine.core.portal.service.UmsMemberService;
import com.nexusengine.core.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;
import java.security.SecureRandom;
import java.util.stream.Collectors;

/**
 * Member coupon management Service implementation
 */
@Service
@Transactional
@lombok.RequiredArgsConstructor
public class UmsMemberCouponServiceImpl implements UmsMemberCouponService {
    private final UmsMemberService memberService;
    private final SmsCouponRepository couponRepository;
    private final SmsCouponHistoryRepository couponHistoryRepository;
    private final SmsCouponProductRelationRepository couponProductRelationRepository;
    private final SmsCouponProductCategoryRelationRepository couponProductCategoryRelationRepository;
    private final PmsProductRepository productRepository;
    private final PortalProductDao portalProductDao;

    private final org.redisson.api.RedissonClient redissonClient;

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void add(Long couponId) {
        UmsMember currentMember = memberService.getCurrentMember();
        // Fix 6: Use Redisson lock to prevent claiming race condition
        org.redisson.api.RLock lock = redissonClient.getLock("coupon:claim:" + couponId + ":" + currentMember.getId());
        try {
            if (lock.tryLock(5, 10, java.util.concurrent.TimeUnit.SECONDS)) {
                SmsCoupon coupon = couponRepository.findById(couponId).orElse(null);
                if (coupon == null) {
                    Asserts.fail("Coupon not found");
                }
                if (coupon.getCount() <= 0) {
                    Asserts.fail("Coupon is out of stock");
                }
                LocalDateTime now = java.time.LocalDateTime.now();
                if (now.isBefore(coupon.getStartTime())) {
                    Asserts.fail("Coupon is not yet available");
                }
                long count = couponHistoryRepository.countByCouponIdAndMemberId(couponId, currentMember.getId());
                if (count >= coupon.getPerLimit()) {
                    Asserts.fail("You have already claimed this coupon");
                }
                SmsCouponHistory couponHistory = new SmsCouponHistory();
                couponHistory.setCouponId(couponId);
                couponHistory.setCouponCode(generateCouponCode(currentMember.getId()));
                couponHistory.setCreateTime(now);
                couponHistory.setMemberId(currentMember.getId());
                couponHistory.setMemberNickname(currentMember.getNickname());
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
    public List<SmsCouponHistory> listHistory(Integer useStatus) {
        UmsMember currentMember = memberService.getCurrentMember();
        if (useStatus != null) {
            return couponHistoryRepository.findByMemberIdAndUseStatus(currentMember.getId(), useStatus);
        }
        return couponHistoryRepository.findByMemberId(currentMember.getId());
    }

    @Override
    public List<SmsCouponHistoryDetail> listCart(List<CartPromotionItem> cartItemList, Integer type) {
        UmsMember currentMember = memberService.getCurrentMember();
        LocalDateTime now = java.time.LocalDateTime.now();
        List<SmsCouponHistoryDetail> allList = portalProductDao.getDetailList(currentMember.getId());
        List<SmsCouponHistoryDetail> enableList = new ArrayList<>();
        List<SmsCouponHistoryDetail> disableList = new ArrayList<>();
        for (SmsCouponHistoryDetail couponHistoryDetail : allList) {
            if (couponHistoryDetail.coupon() == null) continue;
            Integer useType = couponHistoryDetail.coupon().getUseType();
            BigDecimal minOrderAmount = couponHistoryDetail.coupon().getMinOrderAmount();
            LocalDateTime endTime = couponHistoryDetail.coupon().getEndTime();
            if (useType.equals(0)) {
                BigDecimal totalAmount = calcTotalAmount(cartItemList);
                if (now.isBefore(endTime) && totalAmount.subtract(minOrderAmount).intValue() >= 0) {
                    enableList.add(couponHistoryDetail);
                } else {
                    disableList.add(couponHistoryDetail);
                }
            } else if (useType.equals(1)) {
                List<Long> productCategoryIds = couponHistoryDetail.categoryRelationList().stream()
                        .map(SmsCouponProductCategoryRelation::getProductCategoryId).collect(Collectors.toList());
                BigDecimal totalAmount = calcTotalAmountByProductCategoryId(cartItemList, productCategoryIds);
                if (now.isBefore(endTime) && totalAmount.intValue() > 0 && totalAmount.subtract(minOrderAmount).intValue() >= 0) {
                    enableList.add(couponHistoryDetail);
                } else {
                    disableList.add(couponHistoryDetail);
                }
            } else if (useType.equals(2)) {
                List<Long> productIds = couponHistoryDetail.productRelationList().stream()
                        .map(SmsCouponProductRelation::getProductId).collect(Collectors.toList());
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
    public List<SmsCoupon> listByProduct(Long productId) {
        return portalProductDao.getAvailableCouponList(productId,
                productRepository.findById(productId).map(PmsProduct::getProductCategoryId).orElse(null));
    }

    @Override
    public List<SmsCoupon> list(Integer useStatus) {
        UmsMember member = memberService.getCurrentMember();
        List<SmsCouponHistory> histories;
        if (useStatus != null) {
            histories = couponHistoryRepository.findByMemberIdAndUseStatus(member.getId(), useStatus);
        } else {
            histories = couponHistoryRepository.findByMemberId(member.getId());
        }
        List<Long> couponIds = histories.stream().map(SmsCouponHistory::getCouponId).distinct().collect(Collectors.toList());
        if (CollUtil.isEmpty(couponIds)) return new ArrayList<>();
        return couponRepository.findAllById(couponIds);
    }

    private BigDecimal calcTotalAmount(List<CartPromotionItem> cartItemList) {
        BigDecimal total = new BigDecimal("0");
        for (CartPromotionItem item : cartItemList) {
            BigDecimal realPrice = item.omsCartItem().getPrice().subtract(item.reduceAmount());
            total = total.add(realPrice.multiply(new BigDecimal(item.omsCartItem().getQuantity())));
        }
        return total;
    }

    private BigDecimal calcTotalAmountByProductCategoryId(List<CartPromotionItem> cartItemList, List<Long> productCategoryIds) {
        BigDecimal total = new BigDecimal("0");
        for (CartPromotionItem item : cartItemList) {
            if (productCategoryIds.contains(item.omsCartItem().getProductCategoryId())) {
                BigDecimal realPrice = item.omsCartItem().getPrice().subtract(item.reduceAmount());
                total = total.add(realPrice.multiply(new BigDecimal(item.omsCartItem().getQuantity())));
            }
        }
        return total;
    }

    private BigDecimal calcTotalAmountByProductId(List<CartPromotionItem> cartItemList, List<Long> productIds) {
        BigDecimal total = new BigDecimal("0");
        for (CartPromotionItem item : cartItemList) {
            if (productIds.contains(item.omsCartItem().getProductId())) {
                BigDecimal realPrice = item.omsCartItem().getPrice().subtract(item.reduceAmount());
                total = total.add(realPrice.multiply(new BigDecimal(item.omsCartItem().getQuantity())));
            }
        }
        return total;
    }
}
