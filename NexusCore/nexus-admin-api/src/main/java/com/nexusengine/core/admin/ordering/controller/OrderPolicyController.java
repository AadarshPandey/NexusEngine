package com.nexusengine.core.admin.ordering.controller;

import com.nexusengine.core.admin.ordering.service.OrderPolicyService;
import com.nexusengine.core.common.exception.Asserts;
import com.nexusengine.core.common.exception.ResourceNotFoundException;
import com.nexusengine.core.domain.ordering.entity.OrderPolicy;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Ordering - Order Policy", description = "Admin order policy management APIs")
@RequestMapping("/orderSetting")
@RequiredArgsConstructor
public class OrderPolicyController {
    private final OrderPolicyService orderSettingService;

    @Operation(summary = "Get order policy by ID")
    @GetMapping("/{id}")
    public OrderPolicy getItem(@PathVariable("id") Long id) {
        OrderPolicy orderSetting = orderSettingService.getItem(id);
        if (orderSetting == null) {
            throw new ResourceNotFoundException("OrderPolicy", id);
        }
        return orderSetting;
    }

    @Operation(summary = "Update order policy")
    @PostMapping("/update/{id}")
    public Integer update(
            @PathVariable("id") Long id, @Valid @RequestBody OrderPolicy orderSetting) {
        int count = orderSettingService.update(id, orderSetting);
        if (count <= 0) {
            Asserts.fail("Failed to update order policy");
        }
        return count;
    }
}
