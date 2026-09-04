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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Member coupon management Service implementation
 */
@Service
public class UmsMemberCouponServiceImpl implements UmsMemberCouponService {
    @Autowired
    private UmsMemberService memberService;
    @Autowired
    private SmsCouponRepository couponRepository;
    @Autowired
    private SmsCouponHistoryRepository couponHistoryRepository;
    @Autowired
    private SmsCouponProductRelationRepository couponProductRelationRepository;
    @Autowired
    private SmsCouponProductCategoryRelationRepository couponProductCategoryRelationRepository;
    @Autowired
    private PmsProductRepository productRepository;
    @Autowired
    private PortalProductDao portalProductDao;

    @Override
    public void add(Long couponId) {
        UmsMember currentMember = memberService.getCurrentMember();
        SmsCoupon coupon = couponRepository.findById(couponId).orElse(null);
        if (coupon == null) {
            Asserts.fail("Coupon not found");
        }
        if (coupon.getCount() <= 0) {
            Asserts.fail("Coupon is out of stock");
        }
        Date now = new Date();
        if (now.before(coupon.getEnableTime())) {
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
        coupon.setCount(coupon.getCount() - 1);
        coupon.setReceiveCount(coupon.getReceiveCount() == null ? 1 : coupon.getReceiveCount() + 1);
        couponRepository.save(coupon);
    }

    private String generateCouponCode(Long memberId) {
        StringBuilder sb = new StringBuilder();
        Long currentTimeMillis = System.currentTimeMillis();
        String timeMillisStr = currentTimeMillis.toString();
        sb.append(timeMillisStr.substring(timeMillisStr.length() - 8));
        for (int i = 0; i < 4; i++) {
            sb.append(new Random().nextInt(10));
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
        Date now = new Date();
        List<SmsCouponHistoryDetail> allList = portalProductDao.getDetailList(currentMember.getId());
        List<SmsCouponHistoryDetail> enableList = new ArrayList<>();
        List<SmsCouponHistoryDetail> disableList = new ArrayList<>();
        for (SmsCouponHistoryDetail couponHistoryDetail : allList) {
            if (couponHistoryDetail.getCoupon() == null) continue;
            Integer useType = couponHistoryDetail.getCoupon().getUseType();
            BigDecimal minPoint = couponHistoryDetail.getCoupon().getMinPoint();
            Date endTime = couponHistoryDetail.getCoupon().getEndTime();
            if (useType.equals(0)) {
                BigDecimal totalAmount = calcTotalAmount(cartItemList);
                if (now.before(endTime) && totalAmount.subtract(minPoint).intValue() >= 0) {
                    enableList.add(couponHistoryDetail);
                } else {
                    disableList.add(couponHistoryDetail);
                }
            } else if (useType.equals(1)) {
                List<Long> productCategoryIds = couponHistoryDetail.getCategoryRelationList().stream()
                        .map(SmsCouponProductCategoryRelation::getProductCategoryId).collect(Collectors.toList());
                BigDecimal totalAmount = calcTotalAmountByProductCategoryId(cartItemList, productCategoryIds);
                if (now.before(endTime) && totalAmount.intValue() > 0 && totalAmount.subtract(minPoint).intValue() >= 0) {
                    enableList.add(couponHistoryDetail);
                } else {
                    disableList.add(couponHistoryDetail);
                }
            } else if (useType.equals(2)) {
                List<Long> productIds = couponHistoryDetail.getProductRelationList().stream()
                        .map(SmsCouponProductRelation::getProductId).collect(Collectors.toList());
                BigDecimal totalAmount = calcTotalAmountByProductId(cartItemList, productIds);
                if (now.before(endTime) && totalAmount.intValue() > 0 && totalAmount.subtract(minPoint).intValue() >= 0) {
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
            BigDecimal realPrice = item.getPrice().subtract(item.getReduceAmount());
            total = total.add(realPrice.multiply(new BigDecimal(item.getQuantity())));
        }
        return total;
    }

    private BigDecimal calcTotalAmountByProductCategoryId(List<CartPromotionItem> cartItemList, List<Long> productCategoryIds) {
        BigDecimal total = new BigDecimal("0");
        for (CartPromotionItem item : cartItemList) {
            if (productCategoryIds.contains(item.getProductCategoryId())) {
                BigDecimal realPrice = item.getPrice().subtract(item.getReduceAmount());
                total = total.add(realPrice.multiply(new BigDecimal(item.getQuantity())));
            }
        }
        return total;
    }

    private BigDecimal calcTotalAmountByProductId(List<CartPromotionItem> cartItemList, List<Long> productIds) {
        BigDecimal total = new BigDecimal("0");
        for (CartPromotionItem item : cartItemList) {
            if (productIds.contains(item.getProductId())) {
                BigDecimal realPrice = item.getPrice().subtract(item.getReduceAmount());
                total = total.add(realPrice.multiply(new BigDecimal(item.getQuantity())));
            }
        }
        return total;
    }
}
