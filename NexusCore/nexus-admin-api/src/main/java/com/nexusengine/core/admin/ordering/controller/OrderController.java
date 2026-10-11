package com.nexusengine.core.admin.ordering.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RestController;

import com.nexusengine.core.admin.ordering.dto.OrderDeliveryParam;
import com.nexusengine.core.admin.ordering.dto.OrderDetail;
import com.nexusengine.core.admin.ordering.dto.OrderMoneyInfoParam;
import com.nexusengine.core.admin.ordering.dto.OrderQueryParam;
import com.nexusengine.core.admin.ordering.dto.OrderReceiverInfoParam;
import com.nexusengine.core.admin.ordering.service.OrderService;
import com.nexusengine.core.common.api.CommonPage;
import com.nexusengine.core.common.api.CommonResult;


import com.nexusengine.core.domain.ordering.entity.CustomerOrder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Represents the OrderController component.
 * Provides core functionality and operations for OrderController.
 */
@RestController("adminOrderController")
@Tag(name = "OrderController", description = "Oms order controller APIs")
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "List Operation")
    @GetMapping("/list")

    public CommonResult<CommonPage<CustomerOrder>> list(OrderQueryParam queryParam,
                                                   @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                   @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<CustomerOrder> orderList = orderService.list(queryParam, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(orderList));
    }

    @Operation(summary = "Delivery Operation")
    @PostMapping("/update/delivery")

    public CommonResult delivery(@RequestBody List<OrderDeliveryParam> deliveryParamList) {
        int count = orderService.delivery(deliveryParamList);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "Close Operation")
    @PostMapping("/update/close")

    public CommonResult close(@RequestParam("ids") List<Long> ids, @RequestParam("note") String note) {
        int count = orderService.close(ids, note);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "Delete Operation")
    @PostMapping("/delete")

    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        int count = orderService.delete(ids);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "Detail Operation")
    @GetMapping("/{id}")

    public CommonResult<OrderDetail> detail(@PathVariable("id") Long id) {
        OrderDetail orderDetailResult = orderService.detail(id);
        return CommonResult.success(orderDetailResult);
    }

    @Operation(summary = "Update receiver info Operation")
    @PostMapping("/update/receiverInfo")

    public CommonResult updateReceiverInfo(@RequestBody OrderReceiverInfoParam receiverInfoParam) {
        int count = orderService.updateReceiverInfo(receiverInfoParam);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "Update receiver info Operation")
    @PostMapping("/update/moneyInfo")

    public CommonResult updateReceiverInfo(@RequestBody OrderMoneyInfoParam moneyInfoParam) {
        int count = orderService.updateMoneyInfo(moneyInfoParam);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "Update note Operation")
    @PostMapping("/update/note")

    public CommonResult updateNote(@RequestParam("id") Long id,
                                   @RequestParam("note") String note,
                                   @RequestParam("status") Integer status) {
        int count = orderService.updateNote(id, note, status);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "Update Order Status Operation")
    @PostMapping("/update/status")

    public CommonResult updateStatus(@RequestParam("ids") List<Long> ids,
                                     @RequestParam("status") Integer status,
                                     @RequestParam(value = "note", required = false) String note) {
        int count = orderService.updateStatus(ids, status, note);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}
