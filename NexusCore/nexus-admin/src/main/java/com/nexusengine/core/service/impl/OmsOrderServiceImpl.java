package com.nexusengine.core.service.impl;

import com.nexusengine.core.dto.*;
import com.nexusengine.core.repository.OmsOrderRepository;
import com.nexusengine.core.repository.OmsOrderItemRepository;
import com.nexusengine.core.repository.OmsOrderOperateHistoryRepository;
import com.nexusengine.core.model.OmsOrder;
import com.nexusengine.core.model.OmsOrderItem;
import com.nexusengine.core.model.OmsOrderOperateHistory;
import com.nexusengine.core.service.OmsOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@lombok.RequiredArgsConstructor
public class OmsOrderServiceImpl implements OmsOrderService {
    private final OmsOrderRepository orderRepository;
    private final OmsOrderItemRepository orderItemRepository;
    private final OmsOrderOperateHistoryRepository orderOperateHistoryRepository;

    private final com.nexusengine.core.service.UmsAdminService adminService;

    @Override
    public List<OmsOrder> list(OmsOrderQueryParam queryParam, Integer pageSize, Integer pageNum) {
        String username = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        com.nexusengine.core.model.UmsAdmin admin = adminService.getAdminByUsername(username);
        
        return orderRepository.findAll(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "createTime"));
    }

    @Override
    public int delivery(List<OmsOrderDeliveryParam> deliveryParamList) {
        int count = 0;
        for (OmsOrderDeliveryParam param : deliveryParamList) {
            OmsOrder order = orderRepository.findById(param.orderId()).orElse(null);
            if (order != null) {
                order.setDeliverySn(param.deliverySn());
                order.setDeliveryCompany(param.deliveryCompany());
                order.setDeliveryTime(java.time.LocalDateTime.now());
                order.setStatus(2);
                orderRepository.save(order);
                count++;
            }
        }
        List<OmsOrderOperateHistory> historyList = deliveryParamList.stream().map(p -> {
            OmsOrderOperateHistory h = new OmsOrderOperateHistory();
            h.setOrderId(p.orderId());
            h.setCreateTime(java.time.LocalDateTime.now());
            h.setOperateMan("Admin");
            h.setOrderStatus(2);
            h.setNote("Delivery completed");
            return h;
        }).collect(Collectors.toList());
        orderOperateHistoryRepository.saveAll(historyList);
        return count;
    }

    @Override
    public int close(List<Long> ids, String note) {
        List<OmsOrder> orders = orderRepository.findAllById(ids);
        for (OmsOrder order : orders) {
            order.setStatus(4);
            orderRepository.save(order);
        }
        List<OmsOrderOperateHistory> historyList = ids.stream().map(id -> {
            OmsOrderOperateHistory h = new OmsOrderOperateHistory();
            h.setOrderId(id);
            h.setCreateTime(java.time.LocalDateTime.now());
            h.setOperateMan("Admin");
            h.setOrderStatus(4);
            h.setNote("Order closed: " + note);
            return h;
        }).collect(Collectors.toList());
        orderOperateHistoryRepository.saveAll(historyList);
        return orders.size();
    }

    @Override
    public int delete(List<Long> ids) {
        List<OmsOrder> orders = orderRepository.findAllById(ids);
        for (OmsOrder order : orders) {
            order.setDeleteStatus(1);
            orderRepository.save(order);
        }
        return orders.size();
    }

    @Override
    public OmsOrderDetail detail(Long id) {
        OmsOrder order = orderRepository.findById(id).orElse(null);
        if (order == null) return null;
        return new OmsOrderDetail(order, orderItemRepository.findByOrderId(id), orderOperateHistoryRepository.findByOrderId(id));
    }

    @Override
    public int updateReceiverInfo(OmsReceiverInfoParam receiverInfoParam) {
        OmsOrder order = orderRepository.findById(receiverInfoParam.orderId()).orElse(null);
        if (order == null) return 0;
        order.setReceiverName(receiverInfoParam.receiverName());
        order.setReceiverPhone(receiverInfoParam.receiverPhone());
        order.setReceiverPostCode(receiverInfoParam.receiverPostCode());
        order.setReceiverDetailAddress(receiverInfoParam.receiverDetailAddress());

        order.setModifyTime(java.time.LocalDateTime.now());
        orderRepository.save(order);
        OmsOrderOperateHistory h = new OmsOrderOperateHistory();
        h.setOrderId(receiverInfoParam.orderId());
        h.setCreateTime(java.time.LocalDateTime.now());
        h.setOperateMan("Admin");
        h.setOrderStatus(receiverInfoParam.status());
        h.setNote("Modified receiver info");
        orderOperateHistoryRepository.save(h);
        return 1;
    }

    @Override
    public int updateMoneyInfo(OmsMoneyInfoParam moneyInfoParam) {
        OmsOrder order = orderRepository.findById(moneyInfoParam.orderId()).orElse(null);
        if (order == null) return 0;
        order.setFreightAmount(moneyInfoParam.freightAmount());
        order.setDiscountAmount(moneyInfoParam.discountAmount());
        order.setModifyTime(java.time.LocalDateTime.now());
        orderRepository.save(order);
        OmsOrderOperateHistory h = new OmsOrderOperateHistory();
        h.setOrderId(moneyInfoParam.orderId());
        h.setCreateTime(java.time.LocalDateTime.now());
        h.setOperateMan("Admin");
        h.setOrderStatus(moneyInfoParam.status());
        h.setNote("Modified fee info");
        orderOperateHistoryRepository.save(h);
        return 1;
    }

    @Override
    public int updateNote(Long id, String note, Integer status) {
        OmsOrder order = orderRepository.findById(id).orElse(null);
        if (order == null) return 0;
        order.setNote(note);
        order.setModifyTime(java.time.LocalDateTime.now());
        orderRepository.save(order);
        OmsOrderOperateHistory h = new OmsOrderOperateHistory();
        h.setOrderId(id);
        h.setCreateTime(java.time.LocalDateTime.now());
        h.setOperateMan("Admin");
        h.setOrderStatus(status);
        h.setNote("Modified note: " + note);
        orderOperateHistoryRepository.save(h);
        return 1;
    }

    @Override
    public int updateStatus(List<Long> ids, Integer status, String note) {
        List<OmsOrder> orders = orderRepository.findAllById(ids);
        for (OmsOrder order : orders) {
            order.setStatus(status);
            order.setModifyTime(java.time.LocalDateTime.now());
            orderRepository.save(order);
        }
        List<OmsOrderOperateHistory> historyList = ids.stream().map(id -> {
            OmsOrderOperateHistory h = new OmsOrderOperateHistory();
            h.setOrderId(id);
            h.setCreateTime(java.time.LocalDateTime.now());
            h.setOperateMan("Admin");
            h.setOrderStatus(status);
            h.setNote(note != null ? note : "Status updated by Admin");
            return h;
        }).collect(Collectors.toList());
        orderOperateHistoryRepository.saveAll(historyList);
        return orders.size();
    }
}
