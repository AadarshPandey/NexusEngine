package com.nexusengine.core.admin.ordering.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RestController;

import com.nexusengine.core.admin.ordering.service.OrderPolicyService;
import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.domain.ordering.entity.OrderPolicy;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;


/**
 * Represents the OrderPolicyController component.
 * Provides core functionality and operations for OrderPolicyController.
 */
@RestController
@Tag(name = "OrderPolicyController", description = "Oms order setting controller APIs")
@RequestMapping("/orderSetting")
@RequiredArgsConstructor
public class OrderPolicyController {
    private final OrderPolicyService orderSettingService;

    @Operation(summary = "Get item Operation")
    @GetMapping("/{id}")

    public CommonResult<OrderPolicy> getItem(@PathVariable("id") Long id) {
        OrderPolicy orderSetting = orderSettingService.getItem(id);
        return CommonResult.success(orderSetting);
    }

    @Operation(summary = "Update Operation")
    @PostMapping("/update/{id}")

    public CommonResult update(@PathVariable("id") Long id, @RequestBody OrderPolicy orderSetting) {
        int count = orderSettingService.update(id,orderSetting);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}
