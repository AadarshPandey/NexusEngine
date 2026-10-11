package com.nexusengine.core.portal.ordering.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RestController;

import com.nexusengine.core.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.nexusengine.core.portal.ordering.service.FlashSaleOrderService;


@RestController
@Tag(name = "FlashOrderController", description = "Flash sale order controller APIs")
@RequestMapping("/portal/flash-order")
@RequiredArgsConstructor
public class FlashOrderController {

    private final FlashSaleOrderService flashSaleOrderService;

    @Operation(summary = "Generate Flash Sale Order (High Concurrency)")
    @PostMapping("/generate")

    public CommonResult generateFlashOrder(@RequestParam("productId") Long productId,
                                           @RequestParam("flashPromotionId") Long flashPromotionId,
                                           @RequestParam("flashPromotionSessionId") Long flashPromotionSessionId,
                                           @RequestParam(defaultValue = "1") Integer quantity) {
        return flashSaleOrderService.generateFlashOrder(productId, flashPromotionId, flashPromotionSessionId, quantity);
    }
}
