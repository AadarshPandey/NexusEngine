package com.nexusengine.core.portal;

import com.nexusengine.core.model.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.ArrayList;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.nexusengine.core.portal.domain.MemberDetails;

public final class TestFixtures {

    public static UmsMember member() {
        UmsMember member = new UmsMember();
        member.setId(1L);
        member.setUsername("testuser");
        member.setPassword("encodedPassword");
        member.setPhone("1234567890");
        member.setStatus(1);
        member.setPoints(100);
        return member;
    }

    public static void setAuthenticatedMember(UmsMember member) {
        MemberDetails memberDetails = new MemberDetails(member);
        UsernamePasswordAuthenticationToken authentication = 
            new UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public static void clearAuthentication() {
        SecurityContextHolder.clearContext();
    }

    public static SmsCoupon coupon(Integer type, BigDecimal amount, BigDecimal minPoint, Integer limit) {
        SmsCoupon coupon = new SmsCoupon();
        coupon.setId(10L);
        coupon.setName("Test Coupon");
        coupon.setType(type); // 0=fixed, 1=percentage, 2=free shipping
        coupon.setAmount(amount);
        coupon.setMinPoint(minPoint);
        coupon.setPerLimit(limit);
        coupon.setCount(100);
        coupon.setStartTime(new Date(System.currentTimeMillis() - 10000));
        coupon.setEndTime(new Date(System.currentTimeMillis() + 86400000));
        coupon.setUseType(0);
        return coupon;
    }

    public static OmsCartItem cartItem(Long productId, Long skuId, BigDecimal price, Integer quantity) {
        OmsCartItem item = new OmsCartItem();
        item.setId(100L);
        item.setProductId(productId);
        item.setProductSkuId(skuId);
        item.setPrice(price);
        item.setQuantity(quantity);
        item.setDeleteStatus(0);
        return item;
    }

    public static OmsOrderItem orderItem(Long productId, BigDecimal price, Integer quantity) {
        OmsOrderItem item = new OmsOrderItem();
        item.setProductId(productId);
        item.setProductPrice(price);
        item.setProductQuantity(quantity);
        return item;
    }

    public static OmsOrder order(Long memberId) {
        OmsOrder order = new OmsOrder();
        order.setId(1000L);
        order.setMemberId(memberId);
        order.setMemberUsername("testuser");
        order.setTotalAmount(new BigDecimal("500.00"));
        order.setPayAmount(new BigDecimal("500.00"));
        order.setStatus(0);
        order.setDeleteStatus(0);
        order.setCreateTime(new Date());
        return order;
    }

    public static PmsSkuStock skuStock(Long id, Long productId, Integer stock, BigDecimal price) {
        PmsSkuStock skuStock = new PmsSkuStock();
        skuStock.setId(id);
        skuStock.setProductId(productId);
        skuStock.setStock(stock);
        skuStock.setLockStock(0);
        skuStock.setPrice(price);
        return skuStock;
    }
}
