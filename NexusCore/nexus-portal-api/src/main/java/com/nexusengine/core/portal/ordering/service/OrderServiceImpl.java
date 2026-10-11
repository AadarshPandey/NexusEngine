package com.nexusengine.core.portal.ordering.service;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import com.nexusengine.core.domain.messaging.repository.OutboxEventRepository;
import com.nexusengine.core.domain.ordering.entity.OrderLine;
import com.nexusengine.core.domain.promotion.entity.MemberCoupon;
import com.nexusengine.core.domain.ordering.repository.OrderLineRepository;
import com.nexusengine.core.domain.messaging.entity.OutboxEvent;
import com.nexusengine.core.domain.ordering.repository.OrderPolicyRepository;
import com.nexusengine.core.domain.customer.entity.MemberAddress;
import com.nexusengine.core.domain.promotion.entity.CouponCategory;
import com.nexusengine.core.domain.payment.repository.PaymentTransactionRepository;
import com.nexusengine.core.domain.promotion.entity.CouponProduct;
import com.nexusengine.core.domain.ordering.repository.CustomerOrderRepository;
import com.nexusengine.core.domain.catalog.repository.ProductSkuRepository;
import com.nexusengine.core.domain.payment.entity.PaymentTransaction;
import com.nexusengine.core.domain.ordering.repository.ReturnRequestRepository;
import com.nexusengine.core.domain.catalog.entity.Product;
import com.nexusengine.core.domain.catalog.repository.ProductRepository;
import com.nexusengine.core.domain.ordering.entity.CustomerOrder;
import com.nexusengine.core.domain.promotion.entity.Coupon;
import com.nexusengine.core.domain.promotion.repository.MemberCouponRepository;
import com.nexusengine.core.domain.customer.entity.Member;
import com.nexusengine.core.domain.ordering.entity.ReturnRequest;
import com.nexusengine.core.domain.ordering.entity.OrderPolicy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.nexusengine.core.common.api.CommonPage;
import com.nexusengine.core.common.exception.Asserts;
import com.nexusengine.core.common.service.RedisService;




import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import com.nexusengine.core.portal.ordering.dto.CartPromotionItem;
import com.nexusengine.core.portal.ordering.dto.ConfirmOrderResult;
import com.nexusengine.core.portal.promotion.strategy.CouponDiscountStrategy;
import com.nexusengine.core.portal.promotion.dto.CouponHistoryDetail;
import com.nexusengine.core.portal.promotion.strategy.CouponStrategyFactory;
import com.nexusengine.core.portal.customer.service.MemberAddressService;
import com.nexusengine.core.portal.promotion.service.MemberCouponService;
import com.nexusengine.core.portal.customer.service.MemberService;
import com.nexusengine.core.portal.ordering.dao.OrderDao;
import com.nexusengine.core.portal.ordering.dto.OrderDetail;
import com.nexusengine.core.portal.ordering.dto.OrderParam;


/**
 * Portal order management Service implementation
 */
@Service("portalOrderServiceImpl")
@Transactional
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final ObjectMapper objectMapper;

    private final MemberService memberService;
    private final CartService cartItemService;
    private final MemberAddressService memberReceiveAddressService;
    private final MemberCouponService memberCouponService;
    private final ProductSkuRepository skuStockRepository;
    private final MemberCouponRepository couponHistoryRepository;
    private final CustomerOrderRepository orderRepository;
    private final OrderLineRepository orderItemRepository;
    private final RedisService redisService;
    @Value("${redis.key.orderId}")
    private String REDIS_KEY_ORDER_ID;
    @Value("${redis.database}")
    private String REDIS_DATABASE;
    private final OrderDao orderDao;
    private final OrderPolicyRepository orderSettingRepository;
    private final ProductRepository productRepository;
    private final org.redisson.api.RedissonClient redissonClient;
    private final OutboxEventRepository outboxEventRepository;
    private final ReturnRequestRepository returnApplyRepository;

    @Override
    public ConfirmOrderResult generateConfirmOrder(List<Long> cartIds) {
        ConfirmOrderResult result = new ConfirmOrderResult();
        Member currentMember = memberService.getCurrentMember();
        List<CartPromotionItem> cartPromotionItemList = cartItemService.listPromotion(currentMember.getId(), cartIds);
        result.setCartPromotionItemList(cartPromotionItemList);
        List<MemberAddress> memberReceiveAddressList = memberReceiveAddressService.list();
        result.setMemberReceiveAddressList(memberReceiveAddressList);
        List<CouponHistoryDetail> couponHistoryDetailList = memberCouponService.listCart(cartPromotionItemList, 1);
        result.setCouponHistoryDetailList(couponHistoryDetailList);
        result.setMemberPoints(currentMember.getPointsBalance());
        ConfirmOrderResult.CalcAmount calcAmount = calcCartAmount(cartPromotionItemList);
        result.setCalcAmount(calcAmount);
        return result;
    }

    private final org.springframework.transaction.PlatformTransactionManager transactionManager;

    @Override
    public Map<String, Object> generateOrder(OrderParam orderParam) {
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
            orderItem.setProductAttr(cartPromotionItem.cartItem().getProductAttr());
            orderItem.setProductBrand(cartPromotionItem.cartItem().getProductBrand());
            orderItem.setProductPrice(cartPromotionItem.cartItem().getPrice());
            orderItem.setProductQuantity(cartPromotionItem.cartItem().getQuantity());
            orderItem.setProductSkuId(cartPromotionItem.cartItem().getProductSkuId());
            orderItem.setProductCategoryId(cartPromotionItem.cartItem().getProductCategoryId());
            orderItem.setPromotionAmount(cartPromotionItem.reduceAmount());
            orderItem.setPromotionName(cartPromotionItem.promotionMessage());

            orderItemList.add(orderItem);
        }

        // Atomic DB stock reservation (ADR 0001) - eliminates check-then-act race condition
        for (CartPromotionItem item : cartPromotionItemList) {
            if (item.cartItem().getProductSkuId() != null) {
                int updated = skuStockRepository.reserveStock(item.cartItem().getProductSkuId(), item.cartItem().getQuantity());
                if (updated == 0) {
                    Asserts.fail("Insufficient stock for SKU: " + item.cartItem().getProductSkuId());
                }
            }
        }

        if (orderParam.couponId() == null) {
            for (OrderLine orderItem : orderItemList) {
                orderItem.setCouponAmount(new BigDecimal(0));
            }
        } else {
            CouponHistoryDetail couponHistoryDetail = getUseCoupon(cartPromotionItemList, orderParam.couponId());
            if (couponHistoryDetail == null) {
                Asserts.fail("Coupon not available");
            }
            handleCouponAmount(orderItemList, couponHistoryDetail);
        }
        if (orderParam.usePoints() == null || orderParam.usePoints().equals(0)) {
            for (OrderLine orderItem : orderItemList) {

            }
        } else {
            BigDecimal totalAmount = calcTotalAmount(orderItemList);
            BigDecimal pointsAmount = getUsePointsAmount(orderParam.usePoints(), totalAmount, currentMember, orderParam.couponId() != null);
            if (pointsAmount.compareTo(new BigDecimal(0)) == 0) {
                Asserts.fail("Points points not applicable");
            } else {
                for (OrderLine orderItem : orderItemList) {
                    BigDecimal perAmount = orderItem.getProductPrice().divide(totalAmount, 3, RoundingMode.HALF_EVEN).multiply(pointsAmount);

                }
            }
        }
        handleRealAmount(orderItemList);
        CustomerOrder order = new CustomerOrder();
        order.setDiscountAmount(new BigDecimal(0));
        order.setTotalAmount(calcTotalAmount(orderItemList));
        BigDecimal originalFreight = calculateFreight(order.getTotalAmount());
        order.setFreightAmount(originalFreight);
        order.setPromotionAmount(calcPromotionAmount(orderItemList));

        if (orderParam.couponId() == null) {
            order.setCouponAmount(new BigDecimal(0));
        } else {
            Coupon coupon = getUseCoupon(cartPromotionItemList, orderParam.couponId()).coupon();
            order.setCouponId(orderParam.couponId());
            if (coupon.getType() != null && coupon.getType() == 2) {
                order.setFreightAmount(BigDecimal.ZERO);
                order.setCouponAmount(originalFreight); // Show the savings
            } else {
                order.setCouponAmount(calcCouponAmount(orderItemList));
            }
        }
        if (orderParam.usePoints() == null) {

        } else {

        }
        order.setPayAmount(calcPayAmount(order));
        order.setMemberId(currentMember.getId());
        order.setCreateTime(java.time.OffsetDateTime.now());

        order.setPayType(orderParam.payType());

        order.setStatus(0);
        order.setOrderType(0);
        if (!orderItemList.isEmpty()) {
            Product product = productRepository.findById(orderItemList.get(0).getProductId()).orElse(null);
            if (product != null) {
            }
        }
        MemberAddress address = memberReceiveAddressService.getItem(orderParam.memberReceiveAddressId());
        if (address == null) {
            Asserts.fail("Invalid shipping address");
        }
        order.setReceiverName(address.getName());
        order.setReceiverPhone(address.getPhoneNumber());
        order.setReceiverPostCode(address.getPostCode());

        order.setReceiverDetailAddress(address.getDetailAddress());
        order.setConfirmStatus(0);
        order.setDeleteStatus(0);

        order.setOrderSn(generateOrderSn(order));
        OrderPolicy orderSetting = orderSettingRepository.findById(1L).orElse(null);
        if (orderSetting != null) {

        }
        orderRepository.save(order);
        for (OrderLine orderItem : orderItemList) {
            orderItem.setOrderId(order.getId());
        }
        orderItemRepository.saveAll(orderItemList);
        if (orderParam.couponId() != null) {
            updateCouponStatus(orderParam.couponId(), currentMember.getId(), 1, order);
        }
        deleteCartItemList(cartPromotionItemList, currentMember);
        sendDelayMessageCancelOrder(order.getId());
        orderRepository.flush();
        orderItemRepository.flush();
        productRepository.flush();
        skuStockRepository.flush();

        Map<String, Object> result = new HashMap<>();
        result.put("order", order);
        result.put("orderItemList", orderItemList);
        return result;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public Integer paySuccess(Long orderId, Integer payType, String paymentId) {
        if (paymentId != null && !paymentId.isBlank()) {
            if (paymentTransactionRepository.findByTransactionId(paymentId).isPresent()) {
                log.warn("Payment replay attempt detected for transaction: {}", paymentId);
                Asserts.fail("Payment transaction already processed");
            }
            CustomerOrder order = orderRepository.findById(orderId).orElse(null);
            PaymentTransaction tx = new PaymentTransaction();
            tx.setTransactionId(paymentId);
            tx.setOrderId(orderId);
            tx.setTransactionType("PAYMENT");
            tx.setStatus("SUCCESS");
            tx.setCreateTime(java.time.OffsetDateTime.now());
            tx.setAmount(order != null && order.getPayAmount() != null ? order.getPayAmount() : java.math.BigDecimal.ZERO);
            try {
                paymentTransactionRepository.saveAndFlush(tx);
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                log.warn("Duplicate payment replay prevented by DB constraint: {}", paymentId);
                Asserts.fail("Payment transaction already processed");
            }
        }
        return paySuccess(orderId, payType);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public Integer paySuccess(Long orderId, Integer payType) {
        org.redisson.api.RLock lock = redissonClient.getLock("order_pay_lock:" + orderId);
        try {
            if (lock.tryLock(5, 10, java.util.concurrent.TimeUnit.SECONDS)) {
                CustomerOrder order = orderRepository.findById(orderId).orElse(null);
                if (order == null || order.getStatus() != 0) return 0; // Only pay if unpaid

                order.setStatus(1);
                order.setPaymentTime(java.time.OffsetDateTime.now());
                order.setPayType(payType);
                orderRepository.save(order);
                orderRepository.flush(); // ensure commit

                OrderDetail orderDetail = orderDao.getDetail(orderId);
                if (orderDetail != null && !CollectionUtils.isEmpty(orderDetail.orderItemList())) {
                    return orderDao.updateSkuStock(orderDetail.orderItemList());
                }
                return 1;
            } else {
                throw new RuntimeException("Could not acquire lock for order payment");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Payment processing interrupted", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    public Integer cancelTimeOutOrder() {
        org.redisson.api.RLock lock = redissonClient.getLock("cancel_timeout_order_lock");
        try {
            if (lock.tryLock(0, 30, java.util.concurrent.TimeUnit.SECONDS)) {
                int count = 0;
                OrderPolicy orderSetting = orderSettingRepository.findById(1L).orElse(null);
                if (orderSetting == null) return 0;
                List<OrderDetail> timeOutOrders = orderDao.getTimeOutOrders(orderSetting.getPaymentTimeoutMin());
                if (CollectionUtils.isEmpty(timeOutOrders)) return count;
                List<Long> ids = timeOutOrders.stream().map(detail -> detail.customerOrder().getId()).collect(Collectors.toList());
                orderDao.updateOrderStatus(ids, 4);
                for (OrderDetail timeOutOrder : timeOutOrders) {
                    orderDao.releaseSkuStockLock(timeOutOrder.orderItemList());
                    updateCouponStatus(timeOutOrder.customerOrder().getCouponId(), timeOutOrder.customerOrder().getMemberId(), 0, null);

                }
                return timeOutOrders.size();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return 0;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void cancelOrder(Long orderId) {
        List<CustomerOrder> cancelOrderList = orderRepository.findByIdAndStatusAndDeleteStatusIsNull(orderId, 0);
        if (CollectionUtils.isEmpty(cancelOrderList)) return;
        CustomerOrder cancelOrder = cancelOrderList.get(0);

        Member currentMember = memberService.getCurrentMember();
        if (currentMember != null && !cancelOrder.getMemberId().equals(currentMember.getId())) {
            // Internal scheduled tasks might have no current member, but if one exists, it must match.
            throw new RuntimeException("Unauthorized to cancel this order");
        }
        cancelOrder.setStatus(4);
        orderRepository.save(cancelOrder);
        List<OrderLine> orderItemList = orderItemRepository.findByOrderId(orderId);
        if (!CollectionUtils.isEmpty(orderItemList)) {
            orderDao.releaseSkuStockLock(orderItemList);
        }
        updateCouponStatus(cancelOrder.getCouponId(), cancelOrder.getMemberId(), 0, null);

    }



    @Override
    public void sendDelayMessageCancelOrder(Long orderId) {
        OrderPolicy orderSetting = orderSettingRepository.findById(1L).orElse(null);
        long timeoutMin = (orderSetting != null && orderSetting.getPaymentTimeoutMin() != null)
                ? orderSetting.getPaymentTimeoutMin()
                : 30L;
        if (orderSetting == null) {
            log.warn("Order policy record not found in database; using default {}-minute timeout for order {}", timeoutMin, orderId);
        }

        long delayTimes = timeoutMin * 60 * 1000;
        OutboxEvent event = new OutboxEvent();
        event.setAggregateType("Order");
        event.setAggregateId(String.valueOf(orderId));
        event.setType("CancelOrder");
        event.setPayload(String.valueOf(delayTimes));
        event.setStatus("PENDING");
        event.setCreatedAt(java.time.OffsetDateTime.now());

        outboxEventRepository.save(event);
    }

    @Override
    public void confirmReceiveOrder(Long orderId) {
        Member member = memberService.getCurrentMember();
        CustomerOrder order = orderRepository.findById(orderId).orElse(null);
        if (order == null || !member.getId().equals(order.getMemberId())) {
            Asserts.fail("Cannot confirm other's order");
        }
        if (order.getStatus() != 2) {
            Asserts.fail("Order has not been shipped");
        }
        order.setStatus(3);
        order.setConfirmStatus(1);
        order.setReceiveTime(java.time.OffsetDateTime.now());
        orderRepository.save(order);
    }

    @Override
    public CommonPage<OrderDetail> list(Integer status, Integer pageNum, Integer pageSize) {
        Member member = memberService.getCurrentMember();
        Page<CustomerOrder> orderPage;
        if (status == null || status == -1) {
            orderPage = orderRepository.findByMemberIdAndDeleteStatusIsNullOrderByCreateTimeDesc(
                    member.getId(), PageRequest.of(pageNum - 1, pageSize));
        } else {
            orderPage = orderRepository.findByMemberIdAndStatusAndDeleteStatusIsNullOrderByCreateTimeDesc(
                    member.getId(), status, PageRequest.of(pageNum - 1, pageSize));
        }
        CommonPage<CustomerOrder> commonOrderPage = CommonPage.restPage(orderPage);
        CommonPage<OrderDetail> resultPage = new CommonPage<>();
        resultPage.setPageNum(commonOrderPage.getPageNum());
        resultPage.setPageSize(commonOrderPage.getPageSize());
        resultPage.setTotal(commonOrderPage.getTotal());
        resultPage.setTotalPage(commonOrderPage.getTotalPage());
        List<CustomerOrder> orderList = orderPage.getContent();
        if (orderList.isEmpty()) {
            resultPage.setList(new ArrayList<>());
            return resultPage;
        }
        List<Long> orderIds = orderList.stream().map(CustomerOrder::getId).collect(Collectors.toList());
        List<OrderLine> allOrderItems = orderItemRepository.findByOrderIdIn(orderIds);
        List<ReturnRequest> allApplies = returnApplyRepository.findByOrderIdIn(orderIds);

        Map<Long, List<OrderLine>> itemMap = allOrderItems.stream().collect(Collectors.groupingBy(OrderLine::getOrderId));
        Map<Long, List<ReturnRequest>> applyMap = allApplies.stream().collect(Collectors.groupingBy(ReturnRequest::getOrderId));

        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (CustomerOrder customerOrder : orderList) {
            OrderDetail orderDetail = new OrderDetail(customerOrder, itemMap.getOrDefault(customerOrder.getId(), Collections.emptyList()), applyMap.getOrDefault(customerOrder.getId(), Collections.emptyList()));
            orderDetailList.add(orderDetail);
        }
        resultPage.setList(orderDetailList);
        return resultPage;
    }

    @Override
    public OrderDetail detail(Long orderId) {
        return orderDao.getDetail(orderId);
    }

    @Override
    public void deleteOrder(Long orderId) {
        Member member = memberService.getCurrentMember();
        CustomerOrder order = orderRepository.findById(orderId).orElse(null);
        if (order == null || !member.getId().equals(order.getMemberId())) {
            Asserts.fail("Cannot delete other's order");
        }
        if (order.getStatus() == 3 || order.getStatus() == 4) {
            order.setDeleteStatus(1);
            orderRepository.save(order);
        } else {
            Asserts.fail("Can only delete completed or cancelled orders");
        }
    }

    @Override
    public void paySuccessByOrderSn(String orderSn, Integer payType) {
        CustomerOrder order = orderRepository.findByOrderSn(orderSn);
        if (order != null && order.getStatus() == 0 && order.getDeleteStatus() == 0) {
            paySuccess(order.getId(), payType);
        }
    }

    private String generateOrderSn(CustomerOrder order) {
        StringBuilder sb = new StringBuilder();
        String date = java.time.OffsetDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
        String key = REDIS_DATABASE + ":" + REDIS_KEY_ORDER_ID + date;
        Long increment = redisService.incr(key, 1);
        sb.append(date);
        sb.append("01");
        sb.append(String.format("%02d", order.getPayType()));
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

    private Integer calcEarnedPoints(List<OrderLine> orderItemList) {
        int sum = 0;
        for (OrderLine orderItem : orderItemList) {

        }
        return sum;
    }

    private void updateCouponStatus(Long couponId, Long memberId, Integer useStatus, CustomerOrder order) {
        if (couponId == null) return;
        List<MemberCoupon> histories = couponHistoryRepository.findByMemberIdAndUseStatus(memberId, useStatus == 0 ? 1 : 0);
        List<MemberCoupon> filtered = histories.stream()
                .filter(h -> h.getCouponId().equals(couponId)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(filtered)) {
            MemberCoupon couponHistory = filtered.get(0);
            couponHistory.setUseTime(useStatus == 1 ? java.time.OffsetDateTime.now() : (java.time.OffsetDateTime) null);
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
                    .subtract(orderItem.getPromotionAmount())
                    .subtract(orderItem.getCouponAmount())
                    ;
            orderItem.setRealAmount(realAmount);
        }
    }

    private String getOrderPromotionInfo(List<OrderLine> orderItemList) {
        StringBuilder sb = new StringBuilder();
        for (OrderLine orderItem : orderItemList) {
            sb.append(orderItem.getPromotionName()).append(";");
        }
        String result = sb.toString();
        return result.endsWith(";") ? result.substring(0, result.length() - 1) : result;
    }

    private BigDecimal calcPayAmount(CustomerOrder order) {
        return order.getTotalAmount().add(order.getFreightAmount())
                .subtract(order.getPromotionAmount()).subtract(order.getCouponAmount());
    }

    private BigDecimal calcPointsAmount(List<OrderLine> orderItemList) {
        BigDecimal amount = new BigDecimal(0);
        for (OrderLine item : orderItemList) {

        }
        return amount;
    }

    private BigDecimal calcCouponAmount(List<OrderLine> orderItemList) {
        BigDecimal amount = new BigDecimal(0);
        for (OrderLine item : orderItemList) {
            if (item.getCouponAmount() != null) amount = amount.add(item.getCouponAmount().multiply(new BigDecimal(item.getProductQuantity())));
        }
        return amount;
    }

    private BigDecimal calcPromotionAmount(List<OrderLine> orderItemList) {
        BigDecimal amount = new BigDecimal(0);
        for (OrderLine item : orderItemList) {
            if (item.getPromotionAmount() != null) amount = amount.add(item.getPromotionAmount().multiply(new BigDecimal(item.getProductQuantity())));
        }
        return amount;
    }

    private BigDecimal getUsePointsAmount(Integer usePoints, BigDecimal totalAmount, Member currentMember, boolean hasCoupon) {
        return new BigDecimal(0);
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

        CouponDiscountStrategy strategy =
            CouponStrategyFactory.getStrategy(coupon.getType());

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
                if (categoryIds.contains(item.getProductCategoryId())) result.add(item);
                else item.setCouponAmount(new BigDecimal(0));
            }
        } else {
            List<Long> productIds = detail.productRelationList().stream().map(CouponProduct::getProductId).collect(Collectors.toList());
            for (OrderLine item : orderItemList) {
                if (productIds.contains(item.getProductId())) result.add(item);
                else item.setCouponAmount(new BigDecimal(0));
            }
        }
        return result;
    }

    private CouponHistoryDetail getUseCoupon(List<CartPromotionItem> cartPromotionItemList, Long couponId) {
        List<CouponHistoryDetail> list = memberCouponService.listCart(cartPromotionItemList, 1);
        for (CouponHistoryDetail detail : list) {
            if (detail.coupon().getId().equals(couponId)) return detail;
        }
        return null;
    }

    private BigDecimal calcTotalAmount(List<OrderLine> orderItemList) {
        BigDecimal total = new BigDecimal("0");
        for (OrderLine item : orderItemList) {
            total = total.add(item.getProductPrice().multiply(new BigDecimal(item.getProductQuantity())));
        }
        return total;
    }


    private boolean hasStock(List<CartPromotionItem> cartPromotionItemList) {
        for (CartPromotionItem item : cartPromotionItemList) {
            if (item.realStock() == null || item.realStock() <= 0 || item.realStock() < item.cartItem().getQuantity()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Calculates freight amount based on total order value.
     * Tiered pricing:
     *   - Orders under ₹5,000:        ₹149 freight
     *   - Orders ₹5,000 – ₹19,999:    ₹99 freight
     *   - Orders ₹20,000 – ₹49,999:   ₹49 freight
     *   - Orders ₹50,000+:            ₹199 freight (heavy / bulky items)
     */
    private BigDecimal calculateFreight(BigDecimal totalAmount) {
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

    private ConfirmOrderResult.CalcAmount calcCartAmount(List<CartPromotionItem> cartPromotionItemList) {
        ConfirmOrderResult.CalcAmount calcAmount = new ConfirmOrderResult.CalcAmount();
        BigDecimal totalAmount = new BigDecimal("0");
        BigDecimal promotionAmount = new BigDecimal("0");
        for (CartPromotionItem item : cartPromotionItemList) {
            totalAmount = totalAmount.add(item.cartItem().getPrice().multiply(new BigDecimal(item.cartItem().getQuantity())));
            promotionAmount = promotionAmount.add(item.reduceAmount().multiply(new BigDecimal(item.cartItem().getQuantity())));
        }
        calcAmount.setTotalAmount(totalAmount);
        calcAmount.setPromotionAmount(promotionAmount);
        BigDecimal freightAmount = calculateFreight(totalAmount);
        calcAmount.setFreightAmount(freightAmount);
        calcAmount.setPayAmount(totalAmount.subtract(promotionAmount).add(freightAmount));
        return calcAmount;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void handlePaymentWebhook(String payload, String signature) {
        try {
            JsonNode root = objectMapper.readTree(payload);
            String event = root.path("event").asText();

            // Only handle payment.captured for now
            if (!"payment.captured".equals(event)) {
                return;
            }

            JsonNode paymentEntity = root.path("payload").path("payment").path("entity");
            String transactionId = paymentEntity.path("id").asText();
            String orderIdStr = paymentEntity.path("notes").path("order_id").asText();

            Long orderId = null;
            if (orderIdStr != null && !orderIdStr.isBlank()) {
                try {
                    orderId = Long.parseLong(orderIdStr);
                } catch (NumberFormatException e) {
                    log.warn("Could not parse order_id from webhook payload: {}", orderIdStr);
                }
            }

            // 1. Record the transaction immediately with saveAndFlush; unique DB constraint prevents concurrent duplicates
            PaymentTransaction tx = new PaymentTransaction();
            tx.setTransactionId(transactionId);
            tx.setOrderId(orderId != null ? orderId : -1L);
            tx.setTransactionType("PAYMENT");
            tx.setAmount(new java.math.BigDecimal(paymentEntity.path("amount").asInt()).divide(new java.math.BigDecimal("100")));
            tx.setStatus("SUCCESS");
            tx.setWebhookPayload(payload);
            tx.setCreateTime(java.time.OffsetDateTime.now());
            try {
                paymentTransactionRepository.saveAndFlush(tx);
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                log.info("Idempotent Webhook: Transaction {} already processed. Skipping duplicate.", transactionId);
                return;
            }

            // 2. Update the Order status and deduct inventory atomically
            if (orderId != null) {
                CustomerOrder dbOrder = orderRepository.findById(orderId).orElse(null);
                if (dbOrder != null && dbOrder.getStatus() == 0) { // 0 is unpaid
                    dbOrder.setStatus(1); // 1 is pending shipment
                    dbOrder.setPaymentTime(java.time.OffsetDateTime.now());
                    orderRepository.save(dbOrder);

                    OrderDetail orderDetail = orderDao.getDetail(orderId);
                    if (orderDetail != null && !CollectionUtils.isEmpty(orderDetail.orderItemList())) {
                        orderDao.updateSkuStock(orderDetail.orderItemList());
                    }
                }
            }

        } catch (Exception e) {
            log.error("Error processing webhook payload", e);
            throw new RuntimeException("Webhook processing error", e);
        }
    }

}
