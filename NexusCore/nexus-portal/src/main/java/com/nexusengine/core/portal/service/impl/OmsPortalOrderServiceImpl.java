package com.nexusengine.core.portal.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import cn.hutool.core.collection.CollUtil;
import com.nexusengine.core.common.api.CommonPage;
import com.nexusengine.core.common.exception.Asserts;
import com.nexusengine.core.common.service.RedisService;
import com.nexusengine.core.model.*;
import com.nexusengine.core.portal.component.CancelOrderSender;
import com.nexusengine.core.portal.dao.PortalOrderDao;
import com.nexusengine.core.portal.domain.*;
import com.nexusengine.core.portal.service.*;
import com.nexusengine.core.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Portal order management Service implementation
 */
@Service
@Transactional
@lombok.extern.slf4j.Slf4j
@lombok.RequiredArgsConstructor
public class OmsPortalOrderServiceImpl implements OmsPortalOrderService {
    private final OmsPaymentTransactionRepository paymentTransactionRepository;
    private final ObjectMapper objectMapper;

    private final UmsMemberService memberService;
    private final OmsCartItemService cartItemService;
    private final UmsMemberReceiveAddressService memberReceiveAddressService;
    private final UmsMemberCouponService memberCouponService;
    private final PmsSkuStockRepository skuStockRepository;
    private final SmsCouponHistoryRepository couponHistoryRepository;
    private final OmsOrderRepository orderRepository;
    private final OmsOrderItemRepository orderItemRepository;
    private final RedisService redisService;
    @Value("${redis.key.orderId}")
    private String REDIS_KEY_ORDER_ID;
    @Value("${redis.database}")
    private String REDIS_DATABASE;
    private final PortalOrderDao portalOrderDao;
    private final OmsOrderSettingRepository orderSettingRepository;
    private final PmsProductRepository productRepository;
    private final org.redisson.api.RedissonClient redissonClient;
    private final com.nexusengine.core.repository.OutboxEventRepository outboxEventRepository;
    private final com.nexusengine.core.repository.OmsOrderReturnApplyRepository returnApplyRepository;

    @Override
    public ConfirmOrderResult generateConfirmOrder(List<Long> cartIds) {
        ConfirmOrderResult result = new ConfirmOrderResult();
        UmsMember currentMember = memberService.getCurrentMember();
        List<CartPromotionItem> cartPromotionItemList = cartItemService.listPromotion(currentMember.getId(), cartIds);
        result.setCartPromotionItemList(cartPromotionItemList);
        List<UmsMemberReceiveAddress> memberReceiveAddressList = memberReceiveAddressService.list();
        result.setMemberReceiveAddressList(memberReceiveAddressList);
        List<SmsCouponHistoryDetail> couponHistoryDetailList = memberCouponService.listCart(cartPromotionItemList, 1);
        result.setCouponHistoryDetailList(couponHistoryDetailList);
        result.setMemberIntegration(currentMember.getRewardPoints());
        ConfirmOrderResult.CalcAmount calcAmount = calcCartAmount(cartPromotionItemList);
        result.setCalcAmount(calcAmount);
        return result;
    }

    private final org.springframework.transaction.PlatformTransactionManager transactionManager;

    @Override
    public Map<String, Object> generateOrder(OrderParam orderParam) {
        List<OmsOrderItem> orderItemList = new ArrayList<>();
        if (orderParam.getMemberReceiveAddressId() == null) {
            Asserts.fail("Please select a shipping address");
        }
        UmsMember currentMember = memberService.getCurrentMember();
        List<CartPromotionItem> cartPromotionItemList = cartItemService.listPromotion(currentMember.getId(), orderParam.getCartIds());
        
        // Fix 2: Sort by product ID to prevent deadlocks
        cartPromotionItemList.sort(Comparator.comparing(CartPromotionItem::getProductId));

        // REDISSON DISTRIBUTED LOCK
        List<org.redisson.api.RLock> productLocks = new ArrayList<>();
        for (CartPromotionItem item : cartPromotionItemList) {
            productLocks.add(redissonClient.getLock("lock:product:stock:" + item.getProductId()));
        }
        org.redisson.api.RLock multiLock = redissonClient.getMultiLock(productLocks.toArray(new org.redisson.api.RLock[0]));
        boolean isLocked = false;
        try {
            isLocked = multiLock.tryLock(10, 30, java.util.concurrent.TimeUnit.SECONDS);
            if (!isLocked) {
                Asserts.fail("High traffic, please try again later.");
            }
            
            // Fix 1: Execute DB logic inside the lock using programmatic transaction
            org.springframework.transaction.support.TransactionTemplate transactionTemplate = new org.springframework.transaction.support.TransactionTemplate(transactionManager);
            return transactionTemplate.execute(status -> {
                for (CartPromotionItem cartPromotionItem : cartPromotionItemList) {
            OmsOrderItem orderItem = new OmsOrderItem();
            orderItem.setProductId(cartPromotionItem.getProductId());
            orderItem.setProductName(cartPromotionItem.getProductName());
            orderItem.setProductPic(cartPromotionItem.getProductPic());
            orderItem.setProductAttr(cartPromotionItem.getProductAttr());
            orderItem.setProductBrand(cartPromotionItem.getProductBrand());
            orderItem.setProductSn(cartPromotionItem.getProductSn());
            orderItem.setProductPrice(cartPromotionItem.getPrice());
            orderItem.setProductQuantity(cartPromotionItem.getQuantity());
            orderItem.setProductSkuId(cartPromotionItem.getProductSkuId());
            orderItem.setProductSkuCode(cartPromotionItem.getProductSkuCode());
            orderItem.setProductCategoryId(cartPromotionItem.getProductCategoryId());
            orderItem.setPromotionAmount(cartPromotionItem.getReduceAmount());
            orderItem.setPromotionName(cartPromotionItem.getPromotionMessage());
            orderItem.setGiftIntegration(cartPromotionItem.getRewardPoints());
            orderItem.setGiftGrowth(cartPromotionItem.getExperiencePoints());
            orderItemList.add(orderItem);
        }
            for (CartPromotionItem item : cartPromotionItemList) {
                
                if (item.getProductSkuId() != null) {
                    PmsSkuStock skuStock = skuStockRepository.findById(item.getProductSkuId()).orElse(null);
                    if (skuStock == null || (skuStock.getStock() - (skuStock.getLockStock() == null ? 0 : skuStock.getLockStock())) < item.getQuantity()) {
                        Asserts.fail("Insufficient stock for SKU");
                    }
                }
            }
        if (orderParam.getCouponId() == null) {
            for (OmsOrderItem orderItem : orderItemList) {
                orderItem.setCouponAmount(new BigDecimal(0));
            }
        } else {
            SmsCouponHistoryDetail couponHistoryDetail = getUseCoupon(cartPromotionItemList, orderParam.getCouponId());
            if (couponHistoryDetail == null) {
                Asserts.fail("Coupon not available");
            }
            handleCouponAmount(orderItemList, couponHistoryDetail);
        }
        if (orderParam.getUseIntegration() == null || orderParam.getUseIntegration().equals(0)) {
            for (OmsOrderItem orderItem : orderItemList) {
                orderItem.setIntegrationAmount(new BigDecimal(0));
            }
        } else {
            BigDecimal totalAmount = calcTotalAmount(orderItemList);
            BigDecimal integrationAmount = getUseIntegrationAmount(orderParam.getUseIntegration(), totalAmount, currentMember, orderParam.getCouponId() != null);
            if (integrationAmount.compareTo(new BigDecimal(0)) == 0) {
                Asserts.fail("Integration points not applicable");
            } else {
                for (OmsOrderItem orderItem : orderItemList) {
                    BigDecimal perAmount = orderItem.getProductPrice().divide(totalAmount, 3, RoundingMode.HALF_EVEN).multiply(integrationAmount);
                    orderItem.setIntegrationAmount(perAmount);
                }
            }
        }
        handleRealAmount(orderItemList);
        lockStock(cartPromotionItemList);
        OmsOrder order = new OmsOrder();
        order.setDiscountAmount(new BigDecimal(0));
        order.setTotalAmount(calcTotalAmount(orderItemList));
        order.setFreightAmount(new BigDecimal(0));
        order.setPromotionAmount(calcPromotionAmount(orderItemList));
        order.setPromotionInfo(getOrderPromotionInfo(orderItemList));
        if (orderParam.getCouponId() == null) {
            order.setCouponAmount(new BigDecimal(0));
        } else {
            order.setCouponId(orderParam.getCouponId());
            order.setCouponAmount(calcCouponAmount(orderItemList));
        }
        if (orderParam.getUseIntegration() == null) {
            order.setRewardPoints(0);
            order.setIntegrationAmount(new BigDecimal(0));
        } else {
            order.setRewardPoints(orderParam.getUseIntegration());
            order.setIntegrationAmount(calcIntegrationAmount(orderItemList));
        }
        order.setPayAmount(calcPayAmount(order));
        order.setMemberId(currentMember.getId());
        order.setCreateTime(new Date());
        order.setMemberUsername(currentMember.getUsername());
        order.setPayType(orderParam.getPayType());
        order.setSourceType(1);
        order.setStatus(0);
        order.setOrderType(0);
        if (!orderItemList.isEmpty()) {
            PmsProduct product = productRepository.findById(orderItemList.get(0).getProductId()).orElse(null);
            if (product != null) {
                order.setVendorId(product.getVendorId());
            }
        }
        UmsMemberReceiveAddress address = memberReceiveAddressService.getItem(orderParam.getMemberReceiveAddressId());
        if (address == null) {
            Asserts.fail("Invalid shipping address");
        }
        order.setReceiverName(address.getName());
        order.setReceiverPhone(address.getPhoneNumber());
        order.setReceiverPostCode(address.getPostCode());
        order.setReceiverProvince(address.getProvince());
        order.setReceiverCity(address.getCity());
        order.setReceiverRegion(address.getRegion());
        order.setReceiverDetailAddress(address.getDetailAddress());
        order.setConfirmStatus(0);
        order.setDeleteStatus(0);
        order.setRewardPoints(calcGifIntegration(orderItemList));
        order.setExperiencePoints(calcGiftGrowth(orderItemList));
        order.setOrderSn(generateOrderSn(order));
        OmsOrderSetting orderSetting = orderSettingRepository.findById(1L).orElse(null);
        if (orderSetting != null) {
            order.setAutoConfirmDay(orderSetting.getConfirmOvertime());
        }
        orderRepository.save(order);
        for (OmsOrderItem orderItem : orderItemList) {
            orderItem.setOrderId(order.getId());
            orderItem.setOrderSn(order.getOrderSn());
        }
        orderItemRepository.saveAll(orderItemList);
        if (orderParam.getCouponId() != null) {
            updateCouponStatus(orderParam.getCouponId(), currentMember.getId(), 1);
        }
        if (orderParam.getUseIntegration() != null) {
            order.setUseIntegration(orderParam.getUseIntegration());
            if (currentMember.getRewardPoints() == null) {
                currentMember.setRewardPoints(0);
            }
            memberService.updateIntegration(currentMember.getId(), currentMember.getRewardPoints() - orderParam.getUseIntegration());
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
        
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Asserts.fail("Order creation interrupted");
            return null;
        } finally {
            if (isLocked) {
                multiLock.unlock();
            }
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public Integer paySuccess(Long orderId, Integer payType) {
        org.redisson.api.RLock lock = redissonClient.getLock("order_pay_lock:" + orderId);
        try {
            if (lock.tryLock(5, 10, java.util.concurrent.TimeUnit.SECONDS)) {
                OmsOrder order = orderRepository.findById(orderId).orElse(null);
                if (order == null || order.getStatus() != 0) return 0; // Only pay if unpaid
                
                order.setStatus(1);
                order.setPaymentTime(new Date());
                order.setPayType(payType);
                orderRepository.save(order);
                orderRepository.flush(); // ensure commit

                OmsOrderDetail orderDetail = portalOrderDao.getDetail(orderId);
                if (orderDetail != null && !CollectionUtils.isEmpty(orderDetail.getOrderItemList())) {
                    return portalOrderDao.updateSkuStock(orderDetail.getOrderItemList());
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
                OmsOrderSetting orderSetting = orderSettingRepository.findById(1L).orElse(null);
                if (orderSetting == null) return 0;
                List<OmsOrderDetail> timeOutOrders = portalOrderDao.getTimeOutOrders(orderSetting.getNormalOrderOvertime());
                if (CollectionUtils.isEmpty(timeOutOrders)) return count;
                List<Long> ids = timeOutOrders.stream().map(OmsOrderDetail::getId).collect(Collectors.toList());
                portalOrderDao.updateOrderStatus(ids, 4);
                for (OmsOrderDetail timeOutOrder : timeOutOrders) {
                    portalOrderDao.releaseSkuStockLock(timeOutOrder.getOrderItemList());
                    updateCouponStatus(timeOutOrder.getCouponId(), timeOutOrder.getMemberId(), 0);
                    if (timeOutOrder.getUseIntegration() != null) {
                        UmsMember member = memberService.getById(timeOutOrder.getMemberId());
                        memberService.updateIntegration(timeOutOrder.getMemberId(), member.getRewardPoints() + timeOutOrder.getUseIntegration());
                    }
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
        List<OmsOrder> cancelOrderList = orderRepository.findByIdAndStatusAndDeleteStatus(orderId, 0, 0);
        if (CollectionUtils.isEmpty(cancelOrderList)) return;
        OmsOrder cancelOrder = cancelOrderList.get(0);
        
        UmsMember currentMember = memberService.getCurrentMember();
        if (currentMember != null && !cancelOrder.getMemberId().equals(currentMember.getId())) {
            // Internal scheduled tasks might have no current member, but if one exists, it must match.
            throw new RuntimeException("Unauthorized to cancel this order");
        }
        cancelOrder.setStatus(4);
        orderRepository.save(cancelOrder);
        List<OmsOrderItem> orderItemList = orderItemRepository.findByOrderId(orderId);
        if (!CollectionUtils.isEmpty(orderItemList)) {
            portalOrderDao.releaseSkuStockLock(orderItemList);
        }
        updateCouponStatus(cancelOrder.getCouponId(), cancelOrder.getMemberId(), 0);
        if (cancelOrder.getUseIntegration() != null) {
            UmsMember member = memberService.getById(cancelOrder.getMemberId());
            memberService.updateIntegration(cancelOrder.getMemberId(), member.getRewardPoints() + cancelOrder.getUseIntegration());
        }
    }



    @Override
    public void sendDelayMessageCancelOrder(Long orderId) {
        OmsOrderSetting orderSetting = orderSettingRepository.findById(1L).orElse(null);
        if (orderSetting == null) return;
        
        long delayTimes = orderSetting.getNormalOrderOvertime() * 60 * 1000;
        com.nexusengine.core.model.OutboxEvent event = new com.nexusengine.core.model.OutboxEvent();
        event.setAggregateType("Order");
        event.setAggregateId(String.valueOf(orderId));
        event.setType("CancelOrder");
        event.setPayload(String.valueOf(delayTimes));
        event.setStatus("PENDING");
        event.setCreatedAt(new Date());
        
        outboxEventRepository.save(event);
    }

    @Override
    public void confirmReceiveOrder(Long orderId) {
        UmsMember member = memberService.getCurrentMember();
        OmsOrder order = orderRepository.findById(orderId).orElse(null);
        if (order == null || !member.getId().equals(order.getMemberId())) {
            Asserts.fail("Cannot confirm other's order");
        }
        if (order.getStatus() != 2) {
            Asserts.fail("Order has not been shipped");
        }
        order.setStatus(3);
        order.setConfirmStatus(1);
        order.setReceiveTime(new Date());
        orderRepository.save(order);
    }

    @Override
    public CommonPage<OmsOrderDetail> list(Integer status, Integer pageNum, Integer pageSize) {
        UmsMember member = memberService.getCurrentMember();
        Page<OmsOrder> orderPage;
        if (status == null || status == -1) {
            orderPage = orderRepository.findByMemberIdAndDeleteStatusOrderByCreateTimeDesc(
                    member.getId(), 0, PageRequest.of(pageNum - 1, pageSize));
        } else {
            orderPage = orderRepository.findByMemberIdAndStatusAndDeleteStatusOrderByCreateTimeDesc(
                    member.getId(), status, 0, PageRequest.of(pageNum - 1, pageSize));
        }
        CommonPage<OmsOrder> commonOrderPage = CommonPage.restPage(orderPage);
        CommonPage<OmsOrderDetail> resultPage = new CommonPage<>();
        resultPage.setPageNum(commonOrderPage.getPageNum());
        resultPage.setPageSize(commonOrderPage.getPageSize());
        resultPage.setTotal(commonOrderPage.getTotal());
        resultPage.setTotalPage(commonOrderPage.getTotalPage());
        List<OmsOrder> orderList = orderPage.getContent();
        if (CollUtil.isEmpty(orderList)) return resultPage;
        List<Long> orderIds = orderList.stream().map(OmsOrder::getId).collect(Collectors.toList());
        List<OmsOrderItem> allOrderItems = orderItemRepository.findByOrderIdIn(orderIds);
        List<OmsOrderReturnApply> allApplies = returnApplyRepository.findByOrderIdIn(orderIds);

        Map<Long, List<OmsOrderItem>> itemMap = allOrderItems.stream().collect(Collectors.groupingBy(OmsOrderItem::getOrderId));
        Map<Long, List<OmsOrderReturnApply>> applyMap = allApplies.stream().collect(Collectors.groupingBy(OmsOrderReturnApply::getOrderId));

        List<OmsOrderDetail> orderDetailList = new ArrayList<>();
        for (OmsOrder omsOrder : orderList) {
            OmsOrderDetail orderDetail = new OmsOrderDetail();
            BeanUtil.copyProperties(omsOrder, orderDetail);
            orderDetail.setOrderItemList(itemMap.getOrDefault(omsOrder.getId(), Collections.emptyList()));
            orderDetail.setReturnApplyList(applyMap.getOrDefault(omsOrder.getId(), Collections.emptyList()));
            orderDetailList.add(orderDetail);
        }
        resultPage.setList(orderDetailList);
        return resultPage;
    }

    @Override
    public OmsOrderDetail detail(Long orderId) {
        return portalOrderDao.getDetail(orderId);
    }

    @Override
    public void deleteOrder(Long orderId) {
        UmsMember member = memberService.getCurrentMember();
        OmsOrder order = orderRepository.findById(orderId).orElse(null);
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
        OmsOrder order = orderRepository.findByOrderSn(orderSn);
        if (order != null && order.getStatus() == 0 && order.getDeleteStatus() == 0) {
            paySuccess(order.getId(), payType);
        }
    }

    private String generateOrderSn(OmsOrder order) {
        StringBuilder sb = new StringBuilder();
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String key = REDIS_DATABASE + ":" + REDIS_KEY_ORDER_ID + date;
        Long increment = redisService.incr(key, 1);
        sb.append(date);
        sb.append(String.format("%02d", order.getSourceType()));
        sb.append(String.format("%02d", order.getPayType()));
        String incrementStr = increment.toString();
        if (incrementStr.length() <= 6) {
            sb.append(String.format("%06d", increment));
        } else {
            sb.append(incrementStr);
        }
        return sb.toString();
    }

    private void deleteCartItemList(List<CartPromotionItem> cartPromotionItemList, UmsMember currentMember) {
        List<Long> ids = cartPromotionItemList.stream().map(CartPromotionItem::getId).collect(Collectors.toList());
        cartItemService.delete(currentMember.getId(), ids);
    }

    private Integer calcGiftGrowth(List<OmsOrderItem> orderItemList) {
        int sum = 0;
        for (OmsOrderItem orderItem : orderItemList) {
            if (orderItem.getGiftGrowth() != null) {
                sum += orderItem.getGiftGrowth() * orderItem.getProductQuantity();
            }
        }
        return sum;
    }

    private Integer calcGifIntegration(List<OmsOrderItem> orderItemList) {
        int sum = 0;
        for (OmsOrderItem orderItem : orderItemList) {
            if (orderItem.getGiftIntegration() != null) {
                sum += orderItem.getGiftIntegration() * orderItem.getProductQuantity();
            }
        }
        return sum;
    }

    private void updateCouponStatus(Long couponId, Long memberId, Integer useStatus) {
        if (couponId == null) return;
        List<SmsCouponHistory> histories = couponHistoryRepository.findByMemberIdAndUseStatus(memberId, useStatus == 0 ? 1 : 0);
        List<SmsCouponHistory> filtered = histories.stream()
                .filter(h -> h.getCouponId().equals(couponId)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(filtered)) {
            SmsCouponHistory couponHistory = filtered.get(0);
            couponHistory.setUseTime(new Date());
            couponHistory.setUseStatus(useStatus);
            couponHistoryRepository.save(couponHistory);
        }
    }

    private void handleRealAmount(List<OmsOrderItem> orderItemList) {
        for (OmsOrderItem orderItem : orderItemList) {
            BigDecimal realAmount = orderItem.getProductPrice()
                    .subtract(orderItem.getPromotionAmount())
                    .subtract(orderItem.getCouponAmount())
                    .subtract(orderItem.getIntegrationAmount());
            orderItem.setRealAmount(realAmount);
        }
    }

    private String getOrderPromotionInfo(List<OmsOrderItem> orderItemList) {
        StringBuilder sb = new StringBuilder();
        for (OmsOrderItem orderItem : orderItemList) {
            sb.append(orderItem.getPromotionName()).append(";");
        }
        String result = sb.toString();
        return result.endsWith(";") ? result.substring(0, result.length() - 1) : result;
    }

    private BigDecimal calcPayAmount(OmsOrder order) {
        return order.getTotalAmount().add(order.getFreightAmount())
                .subtract(order.getPromotionAmount()).subtract(order.getCouponAmount()).subtract(order.getIntegrationAmount());
    }

    private BigDecimal calcIntegrationAmount(List<OmsOrderItem> orderItemList) {
        BigDecimal amount = new BigDecimal(0);
        for (OmsOrderItem item : orderItemList) {
            if (item.getIntegrationAmount() != null) amount = amount.add(item.getIntegrationAmount().multiply(new BigDecimal(item.getProductQuantity())));
        }
        return amount;
    }

    private BigDecimal calcCouponAmount(List<OmsOrderItem> orderItemList) {
        BigDecimal amount = new BigDecimal(0);
        for (OmsOrderItem item : orderItemList) {
            if (item.getCouponAmount() != null) amount = amount.add(item.getCouponAmount().multiply(new BigDecimal(item.getProductQuantity())));
        }
        return amount;
    }

    private BigDecimal calcPromotionAmount(List<OmsOrderItem> orderItemList) {
        BigDecimal amount = new BigDecimal(0);
        for (OmsOrderItem item : orderItemList) {
            if (item.getPromotionAmount() != null) amount = amount.add(item.getPromotionAmount().multiply(new BigDecimal(item.getProductQuantity())));
        }
        return amount;
    }

    private BigDecimal getUseIntegrationAmount(Integer useIntegration, BigDecimal totalAmount, UmsMember currentMember, boolean hasCoupon) {
        return new BigDecimal(0);
    }

    private void handleCouponAmount(List<OmsOrderItem> orderItemList, SmsCouponHistoryDetail couponHistoryDetail) {
        SmsCoupon coupon = couponHistoryDetail.getCoupon();
        if (coupon.getUseType().equals(0)) {
            calcPerCouponAmount(orderItemList, coupon);
        } else if (coupon.getUseType().equals(1)) {
            calcPerCouponAmount(getCouponOrderItemByRelation(couponHistoryDetail, orderItemList, 0), coupon);
        } else if (coupon.getUseType().equals(2)) {
            calcPerCouponAmount(getCouponOrderItemByRelation(couponHistoryDetail, orderItemList, 1), coupon);
        }
    }

    private void calcPerCouponAmount(List<OmsOrderItem> orderItemList, SmsCoupon coupon) {
        BigDecimal totalAmount = calcTotalAmount(orderItemList);
        for (OmsOrderItem orderItem : orderItemList) {
            BigDecimal couponAmount = orderItem.getProductPrice().divide(totalAmount, 3, RoundingMode.HALF_EVEN).multiply(coupon.getAmount());
            orderItem.setCouponAmount(couponAmount);
        }
    }

    private List<OmsOrderItem> getCouponOrderItemByRelation(SmsCouponHistoryDetail detail, List<OmsOrderItem> orderItemList, int type) {
        List<OmsOrderItem> result = new ArrayList<>();
        if (type == 0) {
            List<Long> categoryIds = detail.getCategoryRelationList().stream().map(SmsCouponProductCategoryRelation::getProductCategoryId).collect(Collectors.toList());
            for (OmsOrderItem item : orderItemList) {
                if (categoryIds.contains(item.getProductCategoryId())) result.add(item);
                else item.setCouponAmount(new BigDecimal(0));
            }
        } else {
            List<Long> productIds = detail.getProductRelationList().stream().map(SmsCouponProductRelation::getProductId).collect(Collectors.toList());
            for (OmsOrderItem item : orderItemList) {
                if (productIds.contains(item.getProductId())) result.add(item);
                else item.setCouponAmount(new BigDecimal(0));
            }
        }
        return result;
    }

    private SmsCouponHistoryDetail getUseCoupon(List<CartPromotionItem> cartPromotionItemList, Long couponId) {
        List<SmsCouponHistoryDetail> list = memberCouponService.listCart(cartPromotionItemList, 1);
        for (SmsCouponHistoryDetail detail : list) {
            if (detail.getCoupon().getId().equals(couponId)) return detail;
        }
        return null;
    }

    private BigDecimal calcTotalAmount(List<OmsOrderItem> orderItemList) {
        BigDecimal total = new BigDecimal("0");
        for (OmsOrderItem item : orderItemList) {
            total = total.add(item.getProductPrice().multiply(new BigDecimal(item.getProductQuantity())));
        }
        return total;
    }

    private void lockStock(List<CartPromotionItem> cartPromotionItemList) {
        for (CartPromotionItem item : cartPromotionItemList) {
            if (item.getProductSkuId() != null) {
                PmsSkuStock skuStock = skuStockRepository.findById(item.getProductSkuId()).orElse(null);
                if (skuStock != null) {
                    skuStock.setLockStock(skuStock.getLockStock() + item.getQuantity());
                    skuStockRepository.save(skuStock);
                }
            }
            
        }
    }

    private boolean hasStock(List<CartPromotionItem> cartPromotionItemList) {
        for (CartPromotionItem item : cartPromotionItemList) {
            if (item.getRealStock() == null || item.getRealStock() <= 0 || item.getRealStock() < item.getQuantity()) {
                return false;
            }
        }
        return true;
    }

    private ConfirmOrderResult.CalcAmount calcCartAmount(List<CartPromotionItem> cartPromotionItemList) {
        ConfirmOrderResult.CalcAmount calcAmount = new ConfirmOrderResult.CalcAmount();
        calcAmount.setFreightAmount(new BigDecimal(0));
        BigDecimal totalAmount = new BigDecimal("0");
        BigDecimal promotionAmount = new BigDecimal("0");
        for (CartPromotionItem item : cartPromotionItemList) {
            totalAmount = totalAmount.add(item.getPrice().multiply(new BigDecimal(item.getQuantity())));
            promotionAmount = promotionAmount.add(item.getReduceAmount().multiply(new BigDecimal(item.getQuantity())));
        }
        calcAmount.setTotalAmount(totalAmount);
        calcAmount.setPromotionAmount(promotionAmount);
        calcAmount.setPayAmount(totalAmount.subtract(promotionAmount));
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
            
            if (transactionId == null || transactionId.isEmpty()) return;
            
            // Idempotency check: Does this transaction already exist?
            if (paymentTransactionRepository.findByTransactionId(transactionId).isPresent()) {
                log.info("Idempotent Webhook: Transaction {} already processed. Ignoring.", transactionId);
                return;
            }
            
            Long orderId = null;
            try {
                orderId = Long.parseLong(orderIdStr);
            } catch (Exception e) {
                log.warn("Could not parse order_id from webhook payload: {}", orderIdStr);
            }
            
            // 1. Record the transaction immediately to prevent concurrent duplicate processing
            OmsPaymentTransaction tx = new OmsPaymentTransaction();
            tx.setTransactionId(transactionId);
            tx.setOrderId(orderId != null ? orderId : -1L);
            tx.setTransactionType("PAYMENT");
            tx.setAmount(new java.math.BigDecimal(paymentEntity.path("amount").asInt()).divide(new java.math.BigDecimal("100")));
            tx.setStatus("SUCCESS");
            tx.setWebhookPayload(payload);
            tx.setCreateTime(new java.util.Date());
            paymentTransactionRepository.save(tx);
            
            // 2. Update the Order status
            if (orderId != null) {
                com.nexusengine.core.model.OmsOrder dbOrder = orderRepository.findById(orderId).orElse(null);
                if (dbOrder != null && dbOrder.getStatus() == 0) { // 0 is unpaid
                    dbOrder.setPaymentId(transactionId);
                    dbOrder.setStatus(1); // 1 is pending shipment
                    dbOrder.setPaymentTime(new java.util.Date());
                    orderRepository.save(dbOrder);
                    
                    // The cancelOrder event should be removed from DLQ or ignored since status != 0
                }
            }
            
        } catch (Exception e) {
            log.error("Error processing webhook payload", e);
            throw new RuntimeException("Webhook processing error", e);
        }
    }

}
