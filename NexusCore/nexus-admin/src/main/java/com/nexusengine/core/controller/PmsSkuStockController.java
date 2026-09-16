package com.nexusengine.core.controller;

import org.springframework.web.bind.annotation.RestController;
import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.model.PmsSkuStock;
import com.nexusengine.core.service.PmsSkuStockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Represents the PmsSkuStockController component.
 * Provides core functionality and operations for PmsSkuStockController.
 */
@RestController
@Tag(name = "PmsSkuStockController", description = "Pms sku stock controller APIs")
@RequestMapping("/sku")
@lombok.RequiredArgsConstructor
public class PmsSkuStockController {
    private final PmsSkuStockService skuStockService;

    @Operation(summary = "Get list Operation")
    @GetMapping("/{pid}")

    public CommonResult<List<PmsSkuStock>> getList(@PathVariable Long pid, @RequestParam(value = "keyword",required = false) String keyword) {
        List<PmsSkuStock> skuStockList = skuStockService.getList(pid, keyword);
        return CommonResult.success(skuStockList);
    }
    @Operation(summary = "Update Operation")
    @PostMapping("/update/{pid}")

    public CommonResult update(@PathVariable Long pid,@RequestBody List<PmsSkuStock> skuStockList){
        int count = skuStockService.update(pid,skuStockList);
        if(count>0){
            return CommonResult.success(count);
        }else{
            return CommonResult.failed();
        }
    }
}
