package com.nexusengine.core.portal.promotion.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RestController;

import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.domain.promotion.entity.Coupon;
import com.nexusengine.core.domain.promotion.entity.MemberCoupon;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.nexusengine.core.portal.ordering.dto.CartPromotionItem;
import com.nexusengine.core.portal.ordering.service.CartService;
import com.nexusengine.core.portal.promotion.dto.CouponHistoryDetail;
import com.nexusengine.core.portal.promotion.service.MemberCouponService;
import com.nexusengine.core.portal.customer.service.MemberService;


/**
 * Represents the CouponController component.
 * Provides core functionality and operations for CouponController.
 */
@RestController("portalCouponController")
@Tag(name = "CouponController", description = "Ums member coupon controller APIs")
@RequestMapping("/portal/member/coupon")
@RequiredArgsConstructor
public class CouponController {
    private final MemberCouponService memberCouponService;
    private final CartService cartItemService;
    private final MemberService memberService;

    @Operation(summary = "Add Operation")
    @PostMapping("/add/{couponId}")

    public CommonResult add(@PathVariable("couponId") Long couponId) {
        memberCouponService.add(couponId);
        return CommonResult.success(null,"Success");
    }

    @Operation(summary = "API Operation")
    @Parameter(name = "useStatus", description = "Description",
            in = ParameterIn.QUERY,schema = @Schema(type = "integer",allowableValues = {"0","1","2"}))
    @GetMapping("/listHistory")

    public CommonResult<List<MemberCoupon>> listHistory(@RequestParam(value = "useStatus", required = false) Integer useStatus) {
        List<MemberCoupon> couponHistoryList = memberCouponService.listHistory(useStatus);
        return CommonResult.success(couponHistoryList);
    }

    @Operation(summary = "API Operation")
    @Parameter(name = "useStatus", description = "Description",
            in = ParameterIn.QUERY,schema = @Schema(type = "integer",allowableValues = {"0","1","2"}))
    @GetMapping("/list")

    public CommonResult<List<Coupon>> list(@RequestParam(value = "useStatus", required = false) Integer useStatus) {
        List<Coupon> couponList = memberCouponService.list(useStatus);
        return CommonResult.success(couponList);
    }

    @Operation(summary = "API Operation")
    @Parameter(name = "type", description = "Description",
            in = ParameterIn.PATH,schema = @Schema(type = "integer",defaultValue = "1",allowableValues = {"0","1"}))
    @GetMapping("/list/cart/{type}")

    public CommonResult<List<CouponHistoryDetail>> listCart(@PathVariable("type") Integer type) {
        List<CartPromotionItem> cartPromotionItemList = cartItemService.listPromotion(memberService.getCurrentMember().getId(), null);
        List<CouponHistoryDetail> couponHistoryList = memberCouponService.listCart(cartPromotionItemList, type);
        return CommonResult.success(couponHistoryList);
    }

    @Operation(summary = "List by product Operation")
    @GetMapping("/listByProduct/{productId}")

    public CommonResult<List<Coupon>> listByProduct(@PathVariable("productId") Long productId) {
        List<Coupon> couponHistoryList = memberCouponService.listByProduct(productId);
        return CommonResult.success(couponHistoryList);
    }
}
