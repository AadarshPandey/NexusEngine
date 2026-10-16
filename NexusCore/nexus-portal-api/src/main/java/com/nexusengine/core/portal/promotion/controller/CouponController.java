package com.nexusengine.core.portal.promotion.controller;

import com.nexusengine.core.domain.promotion.entity.Coupon;
import com.nexusengine.core.domain.promotion.entity.MemberCoupon;
import com.nexusengine.core.portal.customer.service.MemberService;
import com.nexusengine.core.portal.ordering.dto.CartPromotionItem;
import com.nexusengine.core.portal.ordering.service.CartService;
import com.nexusengine.core.portal.promotion.dto.CouponHistoryDetail;
import com.nexusengine.core.portal.promotion.service.MemberCouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController("portalCouponController")
@Tag(name = "Storefront - Coupons", description = "Storefront member coupon management APIs")
@RequestMapping("/api/store/promotion/coupons")
@RequiredArgsConstructor
public class CouponController {
    private final MemberCouponService memberCouponService;
    private final CartService cartItemService;
    private final MemberService memberService;

    @Operation(summary = "Claim coupon")
    @PostMapping("/{couponId}/claim")
    @ResponseStatus(HttpStatus.CREATED)
    public void add(@PathVariable("couponId") Long couponId) {
        memberCouponService.add(couponId);
    }

    @Operation(summary = "List member coupon history")
    @Parameter(
            name = "useStatus",
            description = "Coupon use status",
            in = ParameterIn.QUERY,
            schema = @Schema(type = "integer", allowableValues = {"0", "1", "2"}))
    @GetMapping("/history")
    public List<MemberCoupon> listHistory(
            @RequestParam(value = "useStatus", required = false) Integer useStatus) {
        return memberCouponService.listHistory(useStatus);
    }

    @Operation(summary = "List member coupons")
    @Parameter(
            name = "useStatus",
            description = "Coupon use status",
            in = ParameterIn.QUERY,
            schema = @Schema(type = "integer", allowableValues = {"0", "1", "2"}))
    @GetMapping
    public List<Coupon> list(
            @RequestParam(value = "useStatus", required = false) Integer useStatus) {
        return memberCouponService.list(useStatus);
    }

    @Operation(summary = "List applicable coupons for cart")
    @Parameter(
            name = "type",
            description = "0: unavailable, 1: available",
            in = ParameterIn.PATH,
            schema = @Schema(type = "integer", defaultValue = "1", allowableValues = {"0", "1"}))
    @GetMapping("/cart/{type}")
    public List<CouponHistoryDetail> listCart(@PathVariable("type") Integer type) {
        List<CartPromotionItem> cartPromotionItemList =
                cartItemService.listPromotion(memberService.getCurrentMember().getId(), null);
        return memberCouponService.listCart(cartPromotionItemList, type);
    }

    @Operation(summary = "List coupons by product")
    @GetMapping("/product/{productId}")
    public List<Coupon> listByProduct(@PathVariable("productId") Long productId) {
        return memberCouponService.listByProduct(productId);
    }
}
