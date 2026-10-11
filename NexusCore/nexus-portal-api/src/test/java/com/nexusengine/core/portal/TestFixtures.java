package com.nexusengine.core.portal;

import com.nexusengine.core.domain.ordering.entity.OrderLine;
import com.nexusengine.core.domain.ordering.entity.CustomerOrder;
import com.nexusengine.core.domain.customer.entity.Member;
import com.nexusengine.core.domain.ordering.entity.CartItem;
import com.nexusengine.core.domain.catalog.entity.ProductSku;
import com.nexusengine.core.domain.promotion.entity.Coupon;

import java.math.BigDecimal;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.nexusengine.core.portal.customer.dto.MemberDetails;

public final class TestFixtures {

    public static Member member() {
        Member member = new Member();
        member.setId(1L);
        member.setUsername("testuser");
        member.setPassword("encodedPassword");
        member.setContactPhone("1234567890");
        member.setStatus(1);
        member.setPointsBalance(100);
        return member;
    }

    public static void setAuthenticatedMember(Member member) {
        MemberDetails memberDetails = new MemberDetails(member);
        UsernamePasswordAuthenticationToken authentication = 
            new UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public static void clearAuthentication() {
        SecurityContextHolder.clearContext();
    }

    public static Coupon coupon(Integer type, BigDecimal amount, BigDecimal minOrderAmount, Integer limit) {
        Coupon coupon = new Coupon();
        coupon.setId(10L);
        coupon.setName("Test Coupon");
        coupon.setType(type); // 0=fixed, 1=percentage, 2=free shipping
        coupon.setAmount(amount);
        coupon.setMinOrderAmount(minOrderAmount);
        coupon.setPerLimit(limit);
        coupon.setCount(100);
        coupon.setStartTime(OffsetDateTime.now().minusSeconds(10));
        coupon.setEndTime(OffsetDateTime.now().plusDays(1));
        coupon.setUseType(0);
        return coupon;
    }

    public static CartItem cartItem(Long productId, Long skuId, BigDecimal price, Integer quantity) {
        CartItem item = new CartItem();
        item.setId(100L);
        item.setProductId(productId);
        item.setProductSkuId(skuId);
        item.setPrice(price);
        item.setQuantity(quantity);
        item.setDeleteStatus(0);
        return item;
    }

    public static OrderLine orderItem(Long productId, BigDecimal price, Integer quantity) {
        OrderLine item = new OrderLine();
        item.setProductId(productId);
        item.setProductPrice(price);
        item.setProductQuantity(quantity);
        return item;
    }

    public static CustomerOrder order(Long memberId) {
        CustomerOrder order = new CustomerOrder();
        order.setId(1000L);
        order.setMemberId(memberId);
        
        order.setTotalAmount(new BigDecimal("500.00"));
        order.setPayAmount(new BigDecimal("500.00"));
        order.setStatus(0);
        order.setDeleteStatus(0);
        order.setCreateTime(OffsetDateTime.now());
        return order;
    }

    public static ProductSku skuStock(Long id, Long productId, Integer stock, BigDecimal price) {
        ProductSku skuStock = new ProductSku();
        skuStock.setId(id);
        skuStock.setProductId(productId);
        skuStock.setOnHandQty(stock);
        skuStock.setReservedQty(0);
        skuStock.setSalePrice(price);
        skuStock.setListPrice(price);
        return skuStock;
    }
}
