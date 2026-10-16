package com.nexusengine.core.admin.promotion.controller;

import com.nexusengine.core.admin.promotion.dto.CouponParam;
import com.nexusengine.core.admin.promotion.service.CouponService;
import com.nexusengine.core.common.exception.Asserts;
import com.nexusengine.core.common.exception.ResourceNotFoundException;
import com.nexusengine.core.domain.promotion.entity.Coupon;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController("adminCouponController")
@Tag(name = "Promotion - Coupons", description = "Admin coupon management APIs")
@RequestMapping("/api/admin/promotion/coupons")
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;

    @Operation(summary = "Create coupon")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Integer add(@Valid @RequestBody CouponParam couponParam) {
        int count = couponService.create(couponParam);
        if (count <= 0) {
            Asserts.fail("Failed to create coupon");
        }
        return count;
    }

    @Operation(summary = "Delete coupon")
    @DeleteMapping("/{id}")
    public Integer delete(@PathVariable("id") Long id) {
        int count = couponService.delete(id);
        if (count <= 0) {
            Asserts.fail("Failed to delete coupon");
        }
        return count;
    }

    @Operation(summary = "Update coupon")
    @PutMapping("/{id}")
    public Integer update(
            @PathVariable("id") Long id, @Valid @RequestBody CouponParam couponParam) {
        int count = couponService.update(id, couponParam);
        if (count <= 0) {
            Asserts.fail("Failed to update coupon");
        }
        return count;
    }

    @Operation(summary = "List coupons with pagination")
    @GetMapping
    public Page<Coupon> list(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "type", required = false) Integer type,
            @PageableDefault(size = 5) Pageable pageable) {
        return couponService.list(
                name, type, pageable.getPageSize(), pageable.getPageNumber() + 1);
    }

    @Operation(summary = "Get coupon by ID")
    @GetMapping("/{id}")
    public CouponParam getItem(@PathVariable("id") Long id) {
        CouponParam couponParam = couponService.getItem(id);
        if (couponParam == null) {
            throw new ResourceNotFoundException("Coupon", id);
        }
        return couponParam;
    }
}
