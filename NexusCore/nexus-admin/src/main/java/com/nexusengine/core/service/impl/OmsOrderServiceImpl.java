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
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OmsOrderServiceImpl implements OmsOrderService {
    @Autowired
    private OmsOrderRepository orderRepository;
    @Autowired
    private OmsOrderItemRepository orderItemRepository;
    @Autowired
    private OmsOrderOperateHistoryRepository orderOperateHistoryRepository;

    @Autowired
    private com.nexusengine.core.service.UmsAdminService adminService;

    @Override
    public List<OmsOrder> list(OmsOrderQueryParam queryParam, Integer pageSize, Integer pageNum) {
        String username = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        com.nexusengine.core.model.UmsAdmin admin = adminService.getAdminByUsername(username);
        Long vendorId = admin != null ? admin.getVendorId() : null;
        if (vendorId != null) {
            return orderRepository.findByVendorId(vendorId);
        }
        return orderRepository.findAll();
    }

    @Override
    public int delivery(List<OmsOrderDeliveryParam> deliveryParamList) {
        int count = 0;
        for (OmsOrderDeliveryParam param : deliveryParamList) {
            OmsOrder order = orderRepository.findById(param.getOrderId()).orElse(null);
            if (order != null) {
                order.setDeliverySn(param.getDeliverySn());
                order.setDeliveryCompany(param.getDeliveryCompany());
                order.setDeliveryTime(new Date());
                order.setStatus(2);
                orderRepository.save(order);
                count++;
            }
        }
        List<OmsOrderOperateHistory> historyList = deliveryParamList.stream().map(p -> {
            OmsOrderOperateHistory h = new OmsOrderOperateHistory();
            h.setOrderId(p.getOrderId());
            h.setCreateTime(new Date());
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
            h.setCreateTime(new Date());
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
        OmsOrderDetail detail = new OmsOrderDetail();
        BeanUtils.copyProperties(order, detail);
        detail.setOrderItemList(orderItemRepository.findByOrderId(id));
        detail.setHistoryList(orderOperateHistoryRepository.findByOrderId(id));
        return detail;
    }

    @Override
    public int updateReceiverInfo(OmsReceiverInfoParam receiverInfoParam) {
        OmsOrder order = orderRepository.findById(receiverInfoParam.getOrderId()).orElse(null);
        if (order == null) return 0;
        order.setReceiverName(receiverInfoParam.getReceiverName());
        order.setReceiverPhone(receiverInfoParam.getReceiverPhone());
        order.setReceiverPostCode(receiverInfoParam.getReceiverPostCode());
        order.setReceiverDetailAddress(receiverInfoParam.getReceiverDetailAddress());
        order.setReceiverProvince(receiverInfoParam.getReceiverProvince());
        order.setReceiverCity(receiverInfoParam.getReceiverCity());
        order.setReceiverRegion(receiverInfoParam.getReceiverRegion());
        order.setModifyTime(new Date());
        orderRepository.save(order);
        OmsOrderOperateHistory h = new OmsOrderOperateHistory();
        h.setOrderId(receiverInfoParam.getOrderId());
        h.setCreateTime(new Date());
        h.setOperateMan("Admin");
        h.setOrderStatus(receiverInfoParam.getStatus());
        h.setNote("Modified receiver info");
        orderOperateHistoryRepository.save(h);
        return 1;
    }

    @Override
    public int updateMoneyInfo(OmsMoneyInfoParam moneyInfoParam) {
        OmsOrder order = orderRepository.findById(moneyInfoParam.getOrderId()).orElse(null);
        if (order == null) return 0;
        order.setFreightAmount(moneyInfoParam.getFreightAmount());
        order.setDiscountAmount(moneyInfoParam.getDiscountAmount());
        order.setModifyTime(new Date());
        orderRepository.save(order);
        OmsOrderOperateHistory h = new OmsOrderOperateHistory();
        h.setOrderId(moneyInfoParam.getOrderId());
        h.setCreateTime(new Date());
        h.setOperateMan("Admin");
        h.setOrderStatus(moneyInfoParam.getStatus());
        h.setNote("Modified fee info");
        orderOperateHistoryRepository.save(h);
        return 1;
    }

    @Override
    public int updateNote(Long id, String note, Integer status) {
        OmsOrder order = orderRepository.findById(id).orElse(null);
        if (order == null) return 0;
        order.setNote(note);
        order.setModifyTime(new Date());
        orderRepository.save(order);
        OmsOrderOperateHistory h = new OmsOrderOperateHistory();
        h.setOrderId(id);
        h.setCreateTime(new Date());
        h.setOperateMan("Admin");
        h.setOrderStatus(status);
        h.setNote("Modified note: " + note);
        orderOperateHistoryRepository.save(h);
        return 1;
    }
}
