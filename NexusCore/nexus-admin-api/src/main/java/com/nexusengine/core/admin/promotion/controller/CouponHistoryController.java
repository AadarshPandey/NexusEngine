package com.nexusengine.core.admin.promotion.controller;

import com.nexusengine.core.admin.promotion.service.CouponHistoryService;
import com.nexusengine.core.domain.promotion.entity.MemberCoupon;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Promotion - Coupon History", description = "Admin coupon claim history APIs")
@RequestMapping("/couponHistory")
@RequiredArgsConstructor
public class CouponHistoryController {
    private final CouponHistoryService historyService;

    @Operation(summary = "List coupon claim history")
    @GetMapping("/list")
    public List<MemberCoupon> list(
            @RequestParam(value = "couponId", required = false) Long couponId,
            @RequestParam(value = "useStatus", required = false) Integer useStatus,
            @RequestParam(value = "orderSn", required = false) String orderSn,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        return historyService.list(couponId, useStatus, orderSn, pageSize, pageNum);
    }
}
