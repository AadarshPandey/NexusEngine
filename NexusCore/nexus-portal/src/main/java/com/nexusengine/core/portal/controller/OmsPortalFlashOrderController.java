package com.nexusengine.core.portal.controller;

import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.portal.service.FlashSaleOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Tag(name = "OmsPortalFlashOrderController", description = "Flash sale order controller APIs")
@RequestMapping("/portal/flash-order")
public class OmsPortalFlashOrderController {

    @Autowired
    private FlashSaleOrderService flashSaleOrderService;

    @Operation(summary = "Generate Flash Sale Order (High Concurrency)")
    @RequestMapping(value = "/generate", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult generateFlashOrder(@RequestParam Long productId,
                                           @RequestParam Long flashPromotionId,
                                           @RequestParam Long flashPromotionSessionId,
                                           @RequestParam(defaultValue = "1") Integer quantity) {
        return flashSaleOrderService.generateFlashOrder(productId, flashPromotionId, flashPromotionSessionId, quantity);
    }
}
