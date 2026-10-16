package com.nexusengine.core.portal.ordering.controller;

import com.nexusengine.core.portal.ordering.dto.FlashOrderResponse;
import com.nexusengine.core.portal.ordering.service.FlashSaleOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Storefront - Flash Orders", description = "Storefront flash sale order APIs")
@RequestMapping("/portal/flash-order")
@RequiredArgsConstructor
public class FlashOrderController {

    private final FlashSaleOrderService flashSaleOrderService;

    @Operation(summary = "Generate Flash Sale Order (High Concurrency)")
    @PostMapping("/generate")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public FlashOrderResponse generateFlashOrder(
            @RequestParam("productId") Long productId,
            @RequestParam("flashPromotionId") Long flashPromotionId,
            @RequestParam("flashPromotionSessionId") Long flashPromotionSessionId,
            @RequestParam(defaultValue = "1") Integer quantity) {
        return flashSaleOrderService.generateFlashOrder(
                productId, flashPromotionId, flashPromotionSessionId, quantity);
    }
}
