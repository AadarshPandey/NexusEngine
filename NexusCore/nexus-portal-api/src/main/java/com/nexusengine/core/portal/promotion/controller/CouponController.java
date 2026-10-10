package com.nexusengine.core.portal.promotion.controller;

import lombok.RequiredArgsConstructor;
import com.nexusengine.core.portal.catalog.dto.*;
import com.nexusengine.core.portal.catalog.service.*;
import com.nexusengine.core.portal.catalog.dao.*;
import com.nexusengine.core.portal.payment.service.*;
import com.nexusengine.core.portal.ordering.dto.*;
import com.nexusengine.core.portal.ordering.service.*;
import com.nexusengine.core.portal.ordering.dao.*;
import com.nexusengine.core.portal.customer.dto.*;
import com.nexusengine.core.portal.customer.service.*;
import com.nexusengine.core.portal.promotion.dto.*;
import com.nexusengine.core.portal.promotion.service.*;
import com.nexusengine.core.portal.promotion.strategy.*;
import com.nexusengine.core.portal.ai.service.*;
import com.nexusengine.core.portal.home.dto.*;
import com.nexusengine.core.portal.home.service.*;
import com.nexusengine.core.portal.engagement.dto.*;
import com.nexusengine.core.portal.engagement.service.*;

import org.springframework.web.bind.annotation.RestController;

import com.nexusengine.core.domain.catalog.entity.*;
import com.nexusengine.core.domain.ordering.entity.*;
import com.nexusengine.core.domain.customer.entity.*;
import com.nexusengine.core.domain.promotion.entity.*;
import com.nexusengine.core.domain.iam.entity.*;
import com.nexusengine.core.domain.catalog.repository.*;
import com.nexusengine.core.domain.ordering.repository.*;
import com.nexusengine.core.domain.customer.repository.*;
import com.nexusengine.core.domain.promotion.repository.*;
import com.nexusengine.core.domain.iam.repository.*;
import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.domain.promotion.entity.Coupon;
import com.nexusengine.core.domain.promotion.entity.MemberCoupon;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Represents the CouponController component.
 * Provides core functionality and operations for CouponController.
 */
@RestController
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
