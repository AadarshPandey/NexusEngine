package com.nexusengine.core.portal.controller;

import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.model.SmsCoupon;
import com.nexusengine.core.model.SmsCouponHistory;
import com.nexusengine.core.portal.domain.CartPromotionItem;
import com.nexusengine.core.portal.domain.SmsCouponHistoryDetail;
import com.nexusengine.core.portal.service.OmsCartItemService;
import com.nexusengine.core.portal.service.UmsMemberCouponService;
import com.nexusengine.core.portal.service.UmsMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/8/29.
 */
@Controller
@Tag(name = "UmsMemberCouponController", description = "Ums member coupon controller APIs")
@RequestMapping("/portal/member/coupon")
public class UmsMemberCouponController {
    @Autowired
    private UmsMemberCouponService memberCouponService;
    @Autowired
    private OmsCartItemService cartItemService;
    @Autowired
    private UmsMemberService memberService;

    @Operation(summary = "Add Operation")
    @RequestMapping(value = "/add/{couponId}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult add(@PathVariable Long couponId) {
        memberCouponService.add(couponId);
        return CommonResult.success(null,"Success");
    }

    @Operation(summary = "API Operation")
    @Parameter(name = "useStatus", description = "Description",
            in = ParameterIn.QUERY,schema = @Schema(type = "integer",allowableValues = {"0","1","2"}))
    @RequestMapping(value = "/listHistory", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<SmsCouponHistory>> listHistory(@RequestParam(value = "useStatus", required = false) Integer useStatus) {
        List<SmsCouponHistory> couponHistoryList = memberCouponService.listHistory(useStatus);
        return CommonResult.success(couponHistoryList);
    }

    @Operation(summary = "API Operation")
    @Parameter(name = "useStatus", description = "Description",
            in = ParameterIn.QUERY,schema = @Schema(type = "integer",allowableValues = {"0","1","2"}))
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<SmsCoupon>> list(@RequestParam(value = "useStatus", required = false) Integer useStatus) {
        List<SmsCoupon> couponList = memberCouponService.list(useStatus);
        return CommonResult.success(couponList);
    }

    @Operation(summary = "API Operation")
    @Parameter(name = "type", description = "Description",
            in = ParameterIn.PATH,schema = @Schema(type = "integer",defaultValue = "1",allowableValues = {"0","1"}))
    @RequestMapping(value = "/list/cart/{type}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<SmsCouponHistoryDetail>> listCart(@PathVariable Integer type) {
        List<CartPromotionItem> cartPromotionItemList = cartItemService.listPromotion(memberService.getCurrentMember().getId(), null);
        List<SmsCouponHistoryDetail> couponHistoryList = memberCouponService.listCart(cartPromotionItemList, type);
        return CommonResult.success(couponHistoryList);
    }

    @Operation(summary = "List by product Operation")
    @RequestMapping(value = "/listByProduct/{productId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<SmsCoupon>> listByProduct(@PathVariable Long productId) {
        List<SmsCoupon> couponHistoryList = memberCouponService.listByProduct(productId);
        return CommonResult.success(couponHistoryList);
    }
}
