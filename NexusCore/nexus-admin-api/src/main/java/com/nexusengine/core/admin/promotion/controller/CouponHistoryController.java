package com.nexusengine.core.admin.promotion.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RestController;

import com.nexusengine.core.admin.promotion.service.CouponHistoryService;
import com.nexusengine.core.common.api.CommonPage;
import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.domain.promotion.entity.MemberCoupon;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/**
 * Represents the CouponHistoryController component.
 * Provides core functionality and operations for CouponHistoryController.
 */
@RestController
@Tag(name = "CouponHistoryController", description = "Sms coupon history controller APIs")
@RequestMapping("/couponHistory")
@RequiredArgsConstructor
public class CouponHistoryController {
    private final CouponHistoryService historyService;

    @Operation(summary = "List Operation")
    @GetMapping("/list")

    public CommonResult<CommonPage<MemberCoupon>> list(@RequestParam(value = "couponId", required = false) Long couponId,
                                                           @RequestParam(value = "useStatus", required = false) Integer useStatus,
                                                           @RequestParam(value = "orderSn", required = false) String orderSn,
                                                           @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                           @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<MemberCoupon> historyList = historyService.list(couponId, useStatus, orderSn, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(historyList));
    }
}
