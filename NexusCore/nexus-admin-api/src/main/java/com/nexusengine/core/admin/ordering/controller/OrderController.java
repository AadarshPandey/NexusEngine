package com.nexusengine.core.admin.ordering.controller;

import com.nexusengine.core.admin.ordering.dto.OrderDeliveryParam;
import com.nexusengine.core.admin.ordering.dto.OrderDetail;
import com.nexusengine.core.admin.ordering.dto.OrderMoneyInfoParam;
import com.nexusengine.core.admin.ordering.dto.OrderQueryParam;
import com.nexusengine.core.admin.ordering.dto.OrderReceiverInfoParam;
import com.nexusengine.core.admin.ordering.dto.OrderSummaryResponse;
import com.nexusengine.core.admin.ordering.mapper.OrderMapper;
import com.nexusengine.core.admin.ordering.service.OrderService;
import com.nexusengine.core.common.exception.Asserts;
import com.nexusengine.core.common.exception.ResourceNotFoundException;
import com.nexusengine.core.domain.ordering.entity.CustomerOrder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("adminOrderController")
@Tag(name = "Ordering - Orders", description = "Admin order management APIs")
@RequestMapping("/api/admin/ordering/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @Operation(summary = "List orders")
    @GetMapping
    public List<OrderSummaryResponse> list(
            OrderQueryParam queryParam, @PageableDefault(size = 5) Pageable pageable) {
        List<CustomerOrder> orderList =
                orderService.list(
                        queryParam, pageable.getPageSize(), pageable.getPageNumber() + 1);
        return orderMapper.toSummaryList(orderList);
    }

    @Operation(summary = "Ship orders")
    @PostMapping("/delivery")
    public Integer delivery(@Valid @RequestBody List<OrderDeliveryParam> deliveryParamList) {
        int count = orderService.delivery(deliveryParamList);
        if (count <= 0) {
            Asserts.fail("Failed to update order delivery");
        }
        return count;
    }

    @Operation(summary = "Cancel/close orders")
    @PatchMapping("/cancel")
    public Integer close(
            @RequestParam("ids") List<Long> ids, @RequestParam("note") String note) {
        int count = orderService.close(ids, note);
        if (count <= 0) {
            Asserts.fail("Failed to close orders");
        }
        return count;
    }

    @Operation(summary = "Delete orders")
    @DeleteMapping
    public Integer delete(@RequestParam("ids") List<Long> ids) {
        int count = orderService.delete(ids);
        if (count <= 0) {
            Asserts.fail("Failed to delete orders");
        }
        return count;
    }

    @Operation(summary = "Get order detail")
    @GetMapping("/{id}")
    public OrderDetail detail(@PathVariable("id") Long id) {
        OrderDetail orderDetailResult = orderService.detail(id);
        if (orderDetailResult == null) {
            throw new ResourceNotFoundException("Order", id);
        }
        return orderDetailResult;
    }

    @Operation(summary = "Update recipient info")
    @PatchMapping("/receiver-info")
    public Integer updateReceiverInfo(
            @Valid @RequestBody OrderReceiverInfoParam receiverInfoParam) {
        int count = orderService.updateReceiverInfo(receiverInfoParam);
        if (count <= 0) {
            Asserts.fail("Failed to update recipient info");
        }
        return count;
    }

    @Operation(summary = "Update order financial info")
    @PatchMapping("/money-info")
    public Integer updateMoneyInfo(@Valid @RequestBody OrderMoneyInfoParam moneyInfoParam) {
        int count = orderService.updateMoneyInfo(moneyInfoParam);
        if (count <= 0) {
            Asserts.fail("Failed to update order money info");
        }
        return count;
    }

    @Operation(summary = "Update order note")
    @PatchMapping("/note")
    public Integer updateNote(
            @RequestParam("id") Long id,
            @RequestParam("note") String note,
            @RequestParam("status") Integer status) {
        int count = orderService.updateNote(id, note, status);
        if (count <= 0) {
            Asserts.fail("Failed to update order note");
        }
        return count;
    }

    @Operation(summary = "Update order status")
    @PatchMapping("/status")
    public Integer updateStatus(
            @RequestParam("ids") List<Long> ids,
            @RequestParam("status") Integer status,
            @RequestParam(value = "note", required = false) String note) {
        int count = orderService.updateStatus(ids, status, note);
        if (count <= 0) {
            Asserts.fail("Failed to update order status");
        }
        return count;
    }
}
