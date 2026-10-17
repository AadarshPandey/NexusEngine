package com.nexusengine.core.portal.ordering.service;

import com.nexusengine.core.common.exception.Asserts;
import com.nexusengine.core.domain.catalog.repository.ProductRepository;
import com.nexusengine.core.domain.customer.entity.Member;
import com.nexusengine.core.domain.customer.entity.MemberAddress;
import com.nexusengine.core.domain.customer.entity.MemberCategoryInterest;
import com.nexusengine.core.domain.customer.repository.MemberCategoryInterestRepository;
import com.nexusengine.core.domain.messaging.entity.OutboxEvent;
import com.nexusengine.core.domain.messaging.repository.OutboxEventRepository;
import com.nexusengine.core.domain.ordering.entity.CustomerOrder;
import com.nexusengine.core.domain.ordering.entity.OrderLine;
import com.nexusengine.core.domain.ordering.entity.OrderPolicy;
import com.nexusengine.core.domain.ordering.repository.CustomerOrderRepository;
import com.nexusengine.core.domain.ordering.repository.OrderLineRepository;
import com.nexusengine.core.domain.ordering.repository.OrderPolicyRepository;
import com.nexusengine.core.domain.promotion.entity.Coupon;
import com.nexusengine.core.domain.promotion.entity.CouponCategory;
import com.nexusengine.core.domain.promotion.entity.CouponProduct;
import com.nexusengine.core.domain.promotion.entity.MemberCoupon;
import com.nexusengine.core.domain.promotion.repository.MemberCouponRepository;
import com.nexusengine.core.portal.ordering.dto.CartPromotionItem;
import com.nexusengine.core.portal.catalog.service.StockReservationService;
import com.nexusengine.core.portal.customer.service.MemberAddressService;
import com.nexusengine.core.portal.customer.service.MemberService;
import com.nexusengine.core.portal.ordering.dto.ConfirmOrderResult;
import com.nexusengine.core.portal.ordering.dto.OrderConfirmationResponse;
import com.nexusengine.core.portal.ordering.dto.OrderParam;
import com.nexusengine.core.portal.promotion.dto.CouponHistoryDetail;
import com.nexusengine.core.portal.promotion.service.MemberCouponService;
import com.nexusengine.core.portal.promotion.strategy.CouponDiscountStrategy;
import com.nexusengine.core.portal.promotion.strategy.CouponStrategyFactory;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * Service managing order checkout: validation, amount calculation, atomic stock reservation, and creation.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CheckoutService {

    private final MemberService memberService;
    private final CartService cartItemService;
    private final MemberAddressService memberReceiveAddressService;
    private final MemberCouponService memberCouponService;
    private final StockReservationService stockReservationService;
    private final MemberCouponRepository couponHistoryRepository;
    private final CustomerOrderRepository orderRepository;
    private final OrderLineRepository orderItemRepository;
    private final OrderPolicyRepository orderSettingRepository;
    private final ProductRepository productRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final MemberCategoryInterestRepository memberCategoryInterestRepository;
    private final StringRedisTemplate stringRedisTemplate;

    @Value("${redis.key.orderId:ordering:orderId:}")
    private String redisKeyOrderId;

    @Value("${redis.database:nexus}")
    private String redisDatabase;

    public ConfirmOrderResult generateConfirmOrder(List<Long> cartIds) {
        ConfirmOrderResult result = new ConfirmOrderResult();
        Member currentMember = memberService.getCurrentMember();
        List<CartPromotionItem> cartPromotionItemList = cartItemService.listPromotion(currentMember.getId(), cartIds);
        result.setCartPromotionItemList(cartPromotionItemList);
        List<MemberAddress> memberReceiveAddressList = memberReceiveAddressService.list();
        result.setMemberReceiveAddressList(memberReceiveAddressList);
        List<CouponHistoryDetail> couponHistoryDetailList = memberCouponService.listCart(cartPromotionItemList, 1);
        result.setCouponHistoryDetailList(couponHistoryDetailList);
        result.setMemberPoints(0);
        ConfirmOrderResult.CalcAmount calcAmount = calcCartAmount(cartPromotionItemList);
        result.setCalcAmount(calcAmount);
        return result;
    }

    @Transactional
    public OrderConfirmationResponse generateOrder(OrderParam orderParam) {
        List<OrderLine> orderItemList = new ArrayList<>();
        if (orderParam.memberReceiveAddressId() == null) {
            Asserts.fail("Please select a shipping address");
        }
        Member currentMember = memberService.getCurrentMember();
        List<CartPromotionItem> cartPromotionItemList = cartItemService.listPromotion(currentMember.getId(), orderParam.cartIds());

        // Sort by product ID to prevent deadlocks across database rows
        cartPromotionItemList.sort(Comparator.comparing(item -> item.cartItem().getProductId()));

        for (CartPromotionItem cartPromotionItem : cartPromotionItemList) {
            OrderLine orderItem = new OrderLine();
            orderItem.setProductId(cartPromotionItem.cartItem().getProductId());
            orderItem.setProductName(cartPromotionItem.cartItem().getProductName());
            orderItem.setProductPic(cartPromotionItem.cartItem().getProductPic());
            orderItem.setProductBrand(cartPromotionItem.cartItem().getProductBrand());
            orderItem.setProductPrice(cartPromotionItem.cartItem().getPrice());
            orderItem.setProductQuantity(cartPromotionItem.cartItem().getQuantity());
            orderItem.setProductSkuId(cartPromotionItem.cartItem().getProductSkuId());
            orderItem.setProductCategoryId(cartPromotionItem.cartItem().getProductCategoryId());
            orderItemList.add(orderItem);
        }

        // Atomic DB stock reservation (ADR 0001) - eliminates check-then-act race condition
        for (CartPromotionItem item : cartPromotionItemList) {
            if (item.cartItem().getProductSkuId() != null) {
                stockReservationService.reserveStock(item.cartItem().getProductSkuId(), item.cartItem().getQuantity());
            }
        }

        if (orderParam.couponId() == null) {
            for (OrderLine orderItem : orderItemList) {
                orderItem.setCouponAmount(BigDecimal.ZERO);
            }
        } else {
            CouponHistoryDetail couponHistoryDetail = getUseCoupon(cartPromotionItemList, orderParam.couponId());
            if (couponHistoryDetail == null) {
                Asserts.fail("Coupon not available");
            }
            handleCouponAmount(orderItemList, couponHistoryDetail);
        }

        handleRealAmount(orderItemList);
        CustomerOrder order = new CustomerOrder();
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setTotalAmount(calcTotalAmount(orderItemList));
        BigDecimal originalFreight = calculateFreight(order.getTotalAmount());
        order.setFreightAmount(originalFreight);

        if (orderParam.couponId() == null) {
            order.setCouponAmount(BigDecimal.ZERO);
        } else {
            Coupon coupon = getUseCoupon(cartPromotionItemList, orderParam.couponId()).coupon();
            order.setCouponId(orderParam.couponId());
            if (coupon.getType() != null && coupon.getType() == 2) {
                order.setFreightAmount(BigDecimal.ZERO);
                order.setCouponAmount(originalFreight);
            } else {
                order.setCouponAmount(calcCouponAmount(orderItemList));
            }
        }

        order.setPayAmount(calcPayAmount(order));
        order.setMemberId(currentMember.getId());
        order.setCreateTime(OffsetDateTime.now());
        order.setPayType(orderParam.payType());
        order.setStatus(0);

        MemberAddress address = memberReceiveAddressService.getItem(orderParam.memberReceiveAddressId());
        if (address == null) {
            Asserts.fail("Invalid shipping address");
        }
        order.setReceiverName(address.getName());
        order.setReceiverPhone(address.getPhoneNumber());
        order.setReceiverPostCode(address.getPostCode());
        order.setReceiverDetailAddress(address.getDetailAddress());
        order.setConfirmStatus(false);
        order.setOrderSn(generateOrderSn(order));

        orderRepository.save(order);
        for (OrderLine orderItem : orderItemList) {
            orderItem.setOrderId(order.getId());
        }
        orderItemRepository.saveAll(orderItemList);

        if (orderParam.couponId() != null) {
            updateCouponStatus(orderParam.couponId(), currentMember.getId(), 1, order);
        }

        deleteCartItemList(cartPromotionItemList, currentMember);

        if (currentMember != null) {
            cartPromotionItemList.stream()
                .map(item -> item.cartItem() != null ? item.cartItem().getProductCategoryId() : null)
                .filter(Objects::nonNull)
                .distinct()
                .forEach(catId -> {
                    try {
                        if (!memberCategoryInterestRepository.existsByMemberIdAndProductCategoryId(currentMember.getId(), catId)) {
                            MemberCategoryInterest interest = new MemberCategoryInterest();
                            interest.setMemberId(currentMember.getId());
                            interest.setProductCategoryId(catId);
                            memberCategoryInterestRepository.save(interest);
                        }
                    } catch (Exception ex) {
                        log.debug("Category interest record skipped: {}", ex.getMessage());
                    }
                });
        }

        sendDelayMessageCancelOrder(order.getId());
        orderRepository.flush();
        orderItemRepository.flush();

        return new OrderConfirmationResponse(order, orderItemList);
    }

    public void sendDelayMessageCancelOrder(Long orderId) {
        OrderPolicy orderSetting = orderSettingRepository.findById(1L).orElse(null);
        long timeoutMin = 30L;
        if (orderSetting != null && orderSetting.getPaymentTimeoutMin() != null) {
            timeoutMin = orderSetting.getPaymentTimeoutMin();
        } else {
            log.warn("Order policy record not found in database; using default {}-minute timeout for order {}", timeoutMin, orderId);
        }

        long delayTimes = timeoutMin * 60 * 1000;
        OutboxEvent event = new OutboxEvent();
        event.setAggregateType("Order");
        event.setAggregateId(String.valueOf(orderId));
        event.setType("CancelOrder");
        event.setPayload(String.valueOf(delayTimes));
        event.setStatus("PENDING");
        event.setCreatedAt(OffsetDateTime.now());
        outboxEventRepository.save(event);
    }

    public String generateOrderSn(CustomerOrder order) {
        StringBuilder sb = new StringBuilder();
        String date = OffsetDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String key = redisDatabase + ":" + redisKeyOrderId + date;
        Long increment = null;
        try {
            increment = stringRedisTemplate.opsForValue().increment(key, 1);
        } catch (Exception e) {
            log.warn("Redis unavailable for order SN generation; using timestamp fallback: {}", e.getMessage());
        }
        if (increment == null) {
            increment = System.currentTimeMillis() % 1000000;
        }
        sb.append(date);
        sb.append("01");
        sb.append(String.format("%02d", order.getPayType() != null ? order.getPayType() : 0));
        String incrementStr = increment.toString();
        if (incrementStr.length() <= 6) {
            sb.append(String.format("%06d", increment));
        } else {
            sb.append(incrementStr);
        }
        return sb.toString();
    }

    private void deleteCartItemList(List<CartPromotionItem> cartPromotionItemList, Member currentMember) {
        List<Long> ids = cartPromotionItemList.stream().map(item -> item.cartItem().getId()).collect(Collectors.toList());
        cartItemService.delete(currentMember.getId(), ids);
    }

    private void updateCouponStatus(Long couponId, Long memberId, Integer useStatus, CustomerOrder order) {
        if (couponId == null) return;
        List<MemberCoupon> histories = couponHistoryRepository.findByMemberIdAndUseStatus(memberId, useStatus == 0 ? 1 : 0);
        List<MemberCoupon> filtered = histories.stream()
                .filter(h -> h.getCouponId().equals(couponId)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(filtered)) {
            MemberCoupon couponHistory = filtered.get(0);
            couponHistory.setUseTime(useStatus == 1 ? OffsetDateTime.now() : null);
            couponHistory.setUseStatus(useStatus);
            if (useStatus == 1 && order != null) {
                couponHistory.setOrderId(order.getId());
                couponHistory.setOrderSn(order.getOrderSn());
            } else if (useStatus == 0) {
                couponHistory.setOrderId(null);
                couponHistory.setOrderSn(null);
            }
            couponHistoryRepository.save(couponHistory);
        }
    }

    private void handleRealAmount(List<OrderLine> orderItemList) {
        for (OrderLine orderItem : orderItemList) {
            BigDecimal realAmount = orderItem.getProductPrice()
                    .subtract(orderItem.getCouponAmount() != null ? orderItem.getCouponAmount() : BigDecimal.ZERO);
            orderItem.setRealAmount(realAmount);
        }
    }

    private BigDecimal calcPayAmount(CustomerOrder order) {
        return order.getTotalAmount().add(order.getFreightAmount())
                .subtract(order.getCouponAmount() != null ? order.getCouponAmount() : BigDecimal.ZERO);
    }

    private BigDecimal calcCouponAmount(List<OrderLine> orderItemList) {
        BigDecimal amount = BigDecimal.ZERO;
        for (OrderLine item : orderItemList) {
            if (item.getCouponAmount() != null) {
                amount = amount.add(item.getCouponAmount().multiply(new BigDecimal(item.getProductQuantity())));
            }
        }
        return amount;
    }

    private void handleCouponAmount(List<OrderLine> orderItemList, CouponHistoryDetail couponHistoryDetail) {
        Coupon coupon = couponHistoryDetail.coupon();
        List<OrderLine> eligibleItems;
        if (coupon.getUseType().equals(0)) {
            eligibleItems = orderItemList;
        } else if (coupon.getUseType().equals(1)) {
            eligibleItems = getCouponOrderItemByRelation(couponHistoryDetail, orderItemList, 0);
        } else if (coupon.getUseType().equals(2)) {
            eligibleItems = getCouponOrderItemByRelation(couponHistoryDetail, orderItemList, 1);
        } else {
            eligibleItems = new ArrayList<>();
        }

        CouponDiscountStrategy strategy = CouponStrategyFactory.getStrategy(coupon.getType());
        BigDecimal totalDiscount = strategy.calculateDiscount(eligibleItems, coupon);

        if (totalDiscount.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal totalAmount = calcTotalAmount(eligibleItems);
            for (OrderLine orderItem : eligibleItems) {
                BigDecimal ratio = orderItem.getProductPrice().multiply(new BigDecimal(orderItem.getProductQuantity())).divide(totalAmount, 6, RoundingMode.HALF_EVEN);
                BigDecimal itemDiscount = totalDiscount.multiply(ratio).setScale(2, RoundingMode.HALF_EVEN);
                orderItem.setCouponAmount(itemDiscount);
            }
        }
    }

    private List<OrderLine> getCouponOrderItemByRelation(CouponHistoryDetail detail, List<OrderLine> orderItemList, int type) {
        List<OrderLine> result = new ArrayList<>();
        if (type == 0) {
            List<Long> categoryIds = detail.categoryRelationList().stream().map(CouponCategory::getProductCategoryId).collect(Collectors.toList());
            for (OrderLine item : orderItemList) {
                if (categoryIds.contains(item.getProductCategoryId())) {
                    result.add(item);
                } else {
                    item.setCouponAmount(BigDecimal.ZERO);
                }
            }
        } else {
            List<Long> productIds = detail.productRelationList().stream().map(CouponProduct::getProductId).collect(Collectors.toList());
            for (OrderLine item : orderItemList) {
                if (productIds.contains(item.getProductId())) {
                    result.add(item);
                } else {
                    item.setCouponAmount(BigDecimal.ZERO);
                }
            }
        }
        return result;
    }

    private CouponHistoryDetail getUseCoupon(List<CartPromotionItem> cartPromotionItemList, Long couponId) {
        List<CouponHistoryDetail> list = memberCouponService.listCart(cartPromotionItemList, 1);
        for (CouponHistoryDetail detail : list) {
            if (detail.coupon().getId().equals(couponId)) {
                return detail;
            }
        }
        return null;
    }

    private BigDecimal calcTotalAmount(List<OrderLine> orderItemList) {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderLine item : orderItemList) {
            total = total.add(item.getProductPrice().multiply(new BigDecimal(item.getProductQuantity())));
        }
        return total;
    }

    public BigDecimal calculateFreight(BigDecimal totalAmount) {
        if (totalAmount.compareTo(new BigDecimal("50000")) >= 0) {
            return new BigDecimal("199");
        } else if (totalAmount.compareTo(new BigDecimal("20000")) >= 0) {
            return new BigDecimal("49");
        } else if (totalAmount.compareTo(new BigDecimal("5000")) >= 0) {
            return new BigDecimal("99");
        } else {
            return new BigDecimal("149");
        }
    }

    public ConfirmOrderResult.CalcAmount calcCartAmount(List<CartPromotionItem> cartPromotionItemList) {
        ConfirmOrderResult.CalcAmount calcAmount = new ConfirmOrderResult.CalcAmount();
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal promotionAmount = BigDecimal.ZERO;
        for (CartPromotionItem item : cartPromotionItemList) {
            totalAmount = totalAmount.add(item.cartItem().getPrice().multiply(new BigDecimal(item.cartItem().getQuantity())));
            promotionAmount = promotionAmount.add(item.reduceAmount().multiply(new BigDecimal(item.cartItem().getQuantity())));
        }
        calcAmount.setSubtotalAmount(totalAmount);
        calcAmount.setTotalAmount(totalAmount);
        calcAmount.setPromotionAmount(promotionAmount);
        BigDecimal freightAmount = calculateFreight(totalAmount);
        calcAmount.setShippingAmount(freightAmount);
        calcAmount.setFreightAmount(freightAmount);
        BigDecimal grandTotal = totalAmount.subtract(promotionAmount).add(freightAmount);
        calcAmount.setGrandTotalAmount(grandTotal);
        calcAmount.setPayAmount(grandTotal);
        return calcAmount;
    }
}
