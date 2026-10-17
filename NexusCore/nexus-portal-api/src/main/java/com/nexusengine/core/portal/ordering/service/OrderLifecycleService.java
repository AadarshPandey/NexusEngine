package com.nexusengine.core.portal.ordering.service;

import com.nexusengine.core.common.exception.Asserts;
import com.nexusengine.core.domain.customer.entity.Member;
import com.nexusengine.core.domain.ordering.entity.CustomerOrder;
import com.nexusengine.core.domain.ordering.entity.OrderLine;
import com.nexusengine.core.domain.ordering.entity.OrderPolicy;
import com.nexusengine.core.domain.ordering.entity.ReturnRequest;
import com.nexusengine.core.domain.ordering.repository.CustomerOrderRepository;
import com.nexusengine.core.domain.ordering.repository.OrderLineRepository;
import com.nexusengine.core.domain.ordering.repository.OrderPolicyRepository;
import com.nexusengine.core.domain.ordering.repository.ReturnRequestRepository;
import com.nexusengine.core.domain.payment.entity.PaymentTransaction;
import com.nexusengine.core.domain.payment.repository.PaymentTransactionRepository;
import com.nexusengine.core.domain.promotion.entity.MemberCoupon;
import com.nexusengine.core.domain.promotion.repository.MemberCouponRepository;
import com.nexusengine.core.portal.catalog.service.StockReservationService;
import com.nexusengine.core.portal.customer.service.MemberService;
import com.nexusengine.core.portal.ordering.dao.OrderDao;
import com.nexusengine.core.portal.ordering.dto.OrderDetail;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * Service managing order lifecycle operations: payment, cancellation, confirmation, completion, and retrieval.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderLifecycleService {

    private final CustomerOrderRepository orderRepository;
    private final OrderLineRepository orderItemRepository;
    private final ReturnRequestRepository returnApplyRepository;
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final MemberCouponRepository couponHistoryRepository;
    private final OrderPolicyRepository orderSettingRepository;
    private final MemberService memberService;
    private final OrderDao orderDao;
    private final StockReservationService stockReservationService;
    private final RedissonClient redissonClient;

    @Transactional
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
            tx.setCreateTime(OffsetDateTime.now());
            tx.setAmount(order != null && order.getPayAmount() != null ? order.getPayAmount() : BigDecimal.ZERO);
            try {
                paymentTransactionRepository.saveAndFlush(tx);
            } catch (DataIntegrityViolationException e) {
                log.warn("Duplicate payment replay prevented by DB constraint: {}", paymentId);
                Asserts.fail("Payment transaction already processed");
            }
        }
        return paySuccess(orderId, payType);
    }

    @Transactional
    public Integer paySuccess(Long orderId, Integer payType) {
        RLock lock = redissonClient.getLock("order_pay_lock:" + orderId);
        try {
            if (lock.tryLock(5, 10, TimeUnit.SECONDS)) {
                CustomerOrder order = orderRepository.findById(orderId).orElse(null);
                if (order == null || order.getStatus() != 0) {
                    return 0; // Only pay if unpaid
                }

                order.setStatus(1);
                order.setPaymentTime(OffsetDateTime.now());
                order.setPayType(payType);
                orderRepository.save(order);
                orderRepository.flush();

                OrderDetail orderDetail = orderDao.getDetail(orderId);
                if (orderDetail != null && !CollectionUtils.isEmpty(orderDetail.orderItemList())) {
                    stockReservationService.deductStockOnPaymentBatch(orderDetail.orderItemList());
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

    @Transactional
    public void paySuccessByOrderSn(String orderSn, Integer payType) {
        CustomerOrder order = orderRepository.findByOrderSn(orderSn);
        if (order != null && order.getStatus() == 0 && order.getDeletedAt() == null) {
            paySuccess(order.getId(), payType);
        }
    }

    public Integer cancelTimeOutOrder() {
        RLock lock = redissonClient.getLock("cancel_timeout_order_lock");
        try {
            if (lock.tryLock(0, 30, TimeUnit.SECONDS)) {
                int count = 0;
                OrderPolicy orderSetting = orderSettingRepository.findById(1L).orElse(null);
                if (orderSetting == null) return 0;
                List<OrderDetail> timeOutOrders = orderDao.getTimeOutOrders(orderSetting.getPaymentTimeoutMin());
                if (CollectionUtils.isEmpty(timeOutOrders)) return count;
                List<Long> ids = timeOutOrders.stream().map(detail -> detail.customerOrder().getId()).collect(Collectors.toList());
                orderDao.updateOrderStatus(ids, 4);
                for (OrderDetail timeOutOrder : timeOutOrders) {
                    stockReservationService.releaseReservedStockBatch(timeOutOrder.orderItemList());
                    updateCouponStatus(timeOutOrder.customerOrder().getCouponId(), timeOutOrder.customerOrder().getMemberId(), 0);
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

    @Transactional
    public void cancelOrder(Long orderId) {
        List<CustomerOrder> cancelOrderList = orderRepository.findByIdAndStatusAndDeletedAtIsNull(orderId, 0);
        if (CollectionUtils.isEmpty(cancelOrderList)) return;
        CustomerOrder cancelOrder = cancelOrderList.get(0);

        Member currentMember = memberService.getCurrentMember();
        if (currentMember != null && !cancelOrder.getMemberId().equals(currentMember.getId())) {
            throw new RuntimeException("Unauthorized to cancel this order");
        }
        cancelOrder.setStatus(4);
        orderRepository.save(cancelOrder);
        List<OrderLine> orderItemList = orderItemRepository.findByOrderId(orderId);
        if (!CollectionUtils.isEmpty(orderItemList)) {
            stockReservationService.releaseReservedStockBatch(orderItemList);
        }
        updateCouponStatus(cancelOrder.getCouponId(), cancelOrder.getMemberId(), 0);
    }

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
        order.setConfirmStatus(true);
        order.setReceiveTime(OffsetDateTime.now());
        orderRepository.save(order);
    }

    public Page<OrderDetail> list(Integer status, Integer pageNum, Integer pageSize) {
        Member member = memberService.getCurrentMember();
        PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize);
        Page<CustomerOrder> orderPage;
        if (status == null || status == -1) {
            orderPage = orderRepository.findByMemberIdAndDeletedAtIsNullOrderByCreateTimeDesc(
                    member.getId(), pageRequest);
        } else {
            orderPage = orderRepository.findByMemberIdAndStatusAndDeletedAtIsNullOrderByCreateTimeDesc(
                    member.getId(), status, pageRequest);
        }
        List<CustomerOrder> orderList = orderPage.getContent();
        if (orderList.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageRequest, orderPage.getTotalElements());
        }
        List<Long> orderIds = orderList.stream().map(CustomerOrder::getId).collect(Collectors.toList());
        List<OrderLine> allOrderItems = orderItemRepository.findByOrderIdIn(orderIds);
        List<ReturnRequest> allApplies = returnApplyRepository.findByOrderIdIn(orderIds);
        List<PaymentTransaction> allTxns = paymentTransactionRepository.findByOrderIdInOrderByCreateTimeAsc(orderIds);

        Map<Long, List<OrderLine>> itemMap = allOrderItems.stream().collect(Collectors.groupingBy(OrderLine::getOrderId));
        Map<Long, List<ReturnRequest>> applyMap = allApplies.stream().collect(Collectors.groupingBy(ReturnRequest::getOrderId));
        Map<Long, List<PaymentTransaction>> txnMap = allTxns.stream().collect(Collectors.groupingBy(PaymentTransaction::getOrderId));

        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (CustomerOrder customerOrder : orderList) {
            OrderDetail orderDetail = new OrderDetail(
                customerOrder,
                itemMap.getOrDefault(customerOrder.getId(), Collections.emptyList()),
                applyMap.getOrDefault(customerOrder.getId(), Collections.emptyList()),
                txnMap.getOrDefault(customerOrder.getId(), Collections.emptyList())
            );
            orderDetailList.add(orderDetail);
        }
        return new PageImpl<>(orderDetailList, pageRequest, orderPage.getTotalElements());
    }

    public OrderDetail detail(Long orderId) {
        return orderDao.getDetail(orderId);
    }

    public void deleteOrder(Long orderId) {
        Member member = memberService.getCurrentMember();
        CustomerOrder order = orderRepository.findById(orderId).orElse(null);
        if (order == null || !member.getId().equals(order.getMemberId())) {
            Asserts.fail("Cannot delete other's order");
        }
        if (order.getStatus() == 3 || order.getStatus() == 4) {
            order.setDeletedAt(Instant.now());
            orderRepository.save(order);
        } else {
            Asserts.fail("Can only delete completed or cancelled orders");
        }
    }

    public int autoConfirmShippedOrders() {
        OrderPolicy orderSetting = orderSettingRepository.findById(1L).orElse(null);
        int days = (orderSetting != null && orderSetting.getAutoConfirmReceiptDays() != null)
                ? orderSetting.getAutoConfirmReceiptDays()
                : 7;
        return orderDao.autoConfirmShippedOrders(days);
    }

    private void updateCouponStatus(Long couponId, Long memberId, Integer useStatus) {
        if (couponId == null) return;
        List<MemberCoupon> histories = couponHistoryRepository.findByMemberIdAndUseStatus(memberId, useStatus == 0 ? 1 : 0);
        List<MemberCoupon> filtered = histories.stream()
                .filter(h -> h.getCouponId().equals(couponId)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(filtered)) {
            MemberCoupon couponHistory = filtered.get(0);
            couponHistory.setUseTime(useStatus == 1 ? OffsetDateTime.now() : null);
            couponHistory.setUseStatus(useStatus);
            if (useStatus == 0) {
                couponHistory.setOrderId(null);
                couponHistory.setOrderSn(null);
            }
            couponHistoryRepository.save(couponHistory);
        }
    }
}
