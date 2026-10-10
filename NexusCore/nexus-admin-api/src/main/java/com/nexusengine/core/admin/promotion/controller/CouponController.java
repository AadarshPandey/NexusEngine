package com.nexusengine.core.admin.promotion.controller;

import lombok.RequiredArgsConstructor;

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
import com.nexusengine.core.admin.catalog.dto.*;
import com.nexusengine.core.admin.catalog.service.*;
import com.nexusengine.core.admin.analytics.service.*;
import com.nexusengine.core.admin.ordering.dto.*;
import com.nexusengine.core.admin.ordering.service.*;
import com.nexusengine.core.admin.promotion.dto.*;
import com.nexusengine.core.admin.promotion.service.*;
import com.nexusengine.core.admin.ai.service.*;
import com.nexusengine.core.admin.infra.dto.*;
import com.nexusengine.core.admin.infra.service.*;
import com.nexusengine.core.admin.iam.dto.*;
import com.nexusengine.core.admin.iam.service.*;
import com.nexusengine.core.common.api.CommonPage;
import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.domain.promotion.entity.Coupon;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Represents the CouponController component.
 * Provides core functionality and operations for CouponController.
 */
@RestController("adminCouponController")
@Tag(name = "CouponController", description = "Sms coupon controller APIs")
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;
    @Operation(summary = "Add Operation")
    @PostMapping("/create")

    public CommonResult add(@RequestBody CouponParam couponParam) {
        int count = couponService.create(couponParam);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "Delete Operation")
    @PostMapping("/delete/{id}")

    public CommonResult delete(@PathVariable("id") Long id) {
        int count = couponService.delete(id);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "Update Operation")
    @PostMapping("/update/{id}")

    public CommonResult update(@PathVariable("id") Long id,@RequestBody CouponParam couponParam) {
        int count = couponService.update(id,couponParam);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "List Operation")
    @GetMapping("/list")

    public CommonResult<CommonPage<Coupon>> list(
            @RequestParam(value = "name",required = false) String name,
            @RequestParam(value = "type",required = false) Integer type,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        org.springframework.data.domain.Page<Coupon> couponPage = couponService.list(name,type,pageSize,pageNum);
        return CommonResult.success(CommonPage.restPage(couponPage));
    }

    @Operation(summary = "Get item Operation")
    @GetMapping("/{id}")

    public CommonResult<CouponParam> getItem(@PathVariable("id") Long id) {
        CouponParam couponParam = couponService.getItem(id);
        return CommonResult.success(couponParam);
    }
}
