package com.nexusengine.core.admin.ordering.service;

import lombok.RequiredArgsConstructor;

import com.nexusengine.core.domain.iam.entity.AdminUser;

import com.nexusengine.core.admin.iam.service.AdminUserService;
import com.nexusengine.core.admin.ordering.dto.OrderDeliveryParam;
import com.nexusengine.core.admin.ordering.dto.OrderDetail;
import com.nexusengine.core.admin.ordering.dto.OrderMoneyInfoParam;
import com.nexusengine.core.admin.ordering.dto.OrderQueryParam;
import com.nexusengine.core.admin.ordering.dto.OrderReceiverInfoParam;



import com.nexusengine.core.domain.ordering.repository.CustomerOrderRepository;
import com.nexusengine.core.domain.ordering.repository.OrderLineRepository;
import com.nexusengine.core.domain.ordering.repository.OrderStatusHistoryRepository;
import com.nexusengine.core.domain.ordering.entity.CustomerOrder;
import com.nexusengine.core.domain.ordering.entity.OrderLine;
import com.nexusengine.core.domain.ordering.entity.OrderStatusHistory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service("adminOrderServiceImpl")
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final CustomerOrderRepository orderRepository;
    private final OrderLineRepository orderItemRepository;
    private final OrderStatusHistoryRepository orderOperateHistoryRepository;

    private final AdminUserService adminService;

    @Override
    public List<CustomerOrder> list(OrderQueryParam queryParam, Integer pageSize, Integer pageNum) {
        String username = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        AdminUser admin = adminService.getAdminByUsername(username);
        
        return orderRepository.findAll(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "createTime"));
    }

    @Override
    public int delivery(List<OrderDeliveryParam> deliveryParamList) {
        int count = 0;
        for (OrderDeliveryParam param : deliveryParamList) {
            CustomerOrder order = orderRepository.findById(param.orderId()).orElse(null);
            if (order != null) {
                order.setDeliverySn(param.deliverySn());
                order.setDeliveryCompany(param.deliveryCompany());
                order.setDeliveryTime(java.time.OffsetDateTime.now());
                order.setStatus(2);
                orderRepository.save(order);
                count++;
            }
        }
        List<OrderStatusHistory> historyList = deliveryParamList.stream().map(p -> {
            OrderStatusHistory h = new OrderStatusHistory();
            h.setOrderId(p.orderId());
            h.setCreateTime(java.time.OffsetDateTime.now());
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
        List<CustomerOrder> orders = orderRepository.findAllById(ids);
        for (CustomerOrder order : orders) {
            order.setStatus(4);
            orderRepository.save(order);
        }
        List<OrderStatusHistory> historyList = ids.stream().map(id -> {
            OrderStatusHistory h = new OrderStatusHistory();
            h.setOrderId(id);
            h.setCreateTime(java.time.OffsetDateTime.now());
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
        List<CustomerOrder> orders = orderRepository.findAllById(ids);
        for (CustomerOrder order : orders) {
            order.setDeleteStatus(1);
            orderRepository.save(order);
        }
        return orders.size();
    }

    @Override
    public OrderDetail detail(Long id) {
        CustomerOrder order = orderRepository.findById(id).orElse(null);
        if (order == null) return null;
        return new OrderDetail(order, orderItemRepository.findByOrderId(id), orderOperateHistoryRepository.findByOrderId(id));
    }

    @Override
    public int updateReceiverInfo(OrderReceiverInfoParam receiverInfoParam) {
        CustomerOrder order = orderRepository.findById(receiverInfoParam.orderId()).orElse(null);
        if (order == null) return 0;
        order.setReceiverName(receiverInfoParam.receiverName());
        order.setReceiverPhone(receiverInfoParam.receiverPhone());
        order.setReceiverPostCode(receiverInfoParam.receiverPostCode());
        order.setReceiverDetailAddress(receiverInfoParam.receiverDetailAddress());

        order.setModifyTime(java.time.OffsetDateTime.now());
        orderRepository.save(order);
        OrderStatusHistory h = new OrderStatusHistory();
        h.setOrderId(receiverInfoParam.orderId());
        h.setCreateTime(java.time.OffsetDateTime.now());
        h.setOperateMan("Admin");
        h.setOrderStatus(receiverInfoParam.status());
        h.setNote("Modified receiver info");
        orderOperateHistoryRepository.save(h);
        return 1;
    }

    @Override
    public int updateMoneyInfo(OrderMoneyInfoParam moneyInfoParam) {
        CustomerOrder order = orderRepository.findById(moneyInfoParam.orderId()).orElse(null);
        if (order == null) return 0;
        order.setFreightAmount(moneyInfoParam.freightAmount());
        order.setDiscountAmount(moneyInfoParam.discountAmount());
        order.setModifyTime(java.time.OffsetDateTime.now());
        orderRepository.save(order);
        OrderStatusHistory h = new OrderStatusHistory();
        h.setOrderId(moneyInfoParam.orderId());
        h.setCreateTime(java.time.OffsetDateTime.now());
        h.setOperateMan("Admin");
        h.setOrderStatus(moneyInfoParam.status());
        h.setNote("Modified fee info");
        orderOperateHistoryRepository.save(h);
        return 1;
    }

    @Override
    public int updateNote(Long id, String note, Integer status) {
        CustomerOrder order = orderRepository.findById(id).orElse(null);
        if (order == null) return 0;
        order.setNote(note);
        order.setModifyTime(java.time.OffsetDateTime.now());
        orderRepository.save(order);
        OrderStatusHistory h = new OrderStatusHistory();
        h.setOrderId(id);
        h.setCreateTime(java.time.OffsetDateTime.now());
        h.setOperateMan("Admin");
        h.setOrderStatus(status);
        h.setNote("Modified note: " + note);
        orderOperateHistoryRepository.save(h);
        return 1;
    }

    @Override
    public int updateStatus(List<Long> ids, Integer status, String note) {
        List<CustomerOrder> orders = orderRepository.findAllById(ids);
        for (CustomerOrder order : orders) {
            order.setStatus(status);
            order.setModifyTime(java.time.OffsetDateTime.now());
            orderRepository.save(order);
        }
        List<OrderStatusHistory> historyList = ids.stream().map(id -> {
            OrderStatusHistory h = new OrderStatusHistory();
            h.setOrderId(id);
            h.setCreateTime(java.time.OffsetDateTime.now());
            h.setOperateMan("Admin");
            h.setOrderStatus(status);
            h.setNote(note != null ? note : "Status updated by Admin");
            return h;
        }).collect(Collectors.toList());
        orderOperateHistoryRepository.saveAll(historyList);
        return orders.size();
    }
}
