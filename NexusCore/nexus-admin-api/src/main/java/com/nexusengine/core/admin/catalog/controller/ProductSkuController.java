package com.nexusengine.core.admin.catalog.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RestController;

import com.nexusengine.core.admin.catalog.service.ProductSkuService;
import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.domain.catalog.entity.ProductSku;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Represents the ProductSkuController component.
 * Provides core functionality and operations for ProductSkuController.
 */
@RestController
@Tag(name = "ProductSkuController", description = "Pms sku stock controller APIs")
@RequestMapping("/sku")
@RequiredArgsConstructor
public class ProductSkuController {
    private final ProductSkuService skuStockService;

    @Operation(summary = "Get list Operation")
    @GetMapping("/{pid}")

    public CommonResult<List<ProductSku>> getList(@PathVariable("pid") Long pid, @RequestParam(value = "keyword",required = false) String keyword) {
        List<ProductSku> skuStockList = skuStockService.getList(pid, keyword);
        return CommonResult.success(skuStockList);
    }
    @Operation(summary = "Update Operation")
    @PostMapping("/update/{pid}")

    public CommonResult update(@PathVariable("pid") Long pid,@RequestBody List<ProductSku> skuStockList){
        int count = skuStockService.update(pid,skuStockList);
        if(count>0){
            return CommonResult.success(count);
        }else{
            return CommonResult.failed();
        }
    }
}
