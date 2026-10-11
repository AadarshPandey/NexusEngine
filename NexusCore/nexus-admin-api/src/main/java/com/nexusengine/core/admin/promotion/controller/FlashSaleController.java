package com.nexusengine.core.admin.promotion.controller;

import com.nexusengine.core.common.api.CommonPage;

import com.nexusengine.core.admin.promotion.service.FlashSaleService;
import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.domain.promotion.entity.FlashSale;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


@RestController
@Tag(name = "FlashSaleController", description = "Flash Promotion Management")
@RequestMapping("/flash")
@RequiredArgsConstructor
public class FlashSaleController {
    private final FlashSaleService flashPromotionService;

    @Operation(summary = "Add flash promotion")
    @PostMapping("/create")
    public CommonResult<Integer> create(@RequestBody FlashSale flashPromotion) {
        int count = flashPromotionService.create(flashPromotion);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "Update flash promotion")
    @PostMapping("/update/{id}")
    public CommonResult<Integer> update(@PathVariable("id") Long id, @RequestBody FlashSale flashPromotion) {
        int count = flashPromotionService.update(id, flashPromotion);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "Delete flash promotion")
    @PostMapping("/delete/{id}")
    public CommonResult<Integer> delete(@PathVariable("id") Long id) {
        int count = flashPromotionService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "Update status")
    @PostMapping("/update/status/{id}")
    public CommonResult<Integer> updateStatus(@PathVariable("id") Long id, @RequestParam("status") Integer status) {
        int count = flashPromotionService.updateStatus(id, status);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "Get flash promotion by ID")
    @GetMapping("/{id}")
    public CommonResult<FlashSale> getItem(@PathVariable("id") Long id) {
        FlashSale flashPromotion = flashPromotionService.getItem(id);
        return CommonResult.success(flashPromotion);
    }

    @Operation(summary = "List flash promotions with pagination")
    @GetMapping("/list")
    public CommonResult<CommonPage<FlashSale>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        Page<FlashSale> flashPromotionList = flashPromotionService.list(keyword, pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(flashPromotionList));
    }
}
