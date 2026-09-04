package com.nexusengine.core.controller;

import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.model.PmsSkuStock;
import com.nexusengine.core.service.PmsSkuStockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/4/27.
 */
@Controller
@Tag(name = "PmsSkuStockController", description = "Pms sku stock controller APIs")
@RequestMapping("/sku")
public class PmsSkuStockController {
    @Autowired
    private PmsSkuStockService skuStockService;

    @Operation(summary = "Get list Operation")
    @RequestMapping(value = "/{pid}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<PmsSkuStock>> getList(@PathVariable Long pid, @RequestParam(value = "keyword",required = false) String keyword) {
        List<PmsSkuStock> skuStockList = skuStockService.getList(pid, keyword);
        return CommonResult.success(skuStockList);
    }
    @Operation(summary = "Update Operation")
    @RequestMapping(value = "/update/{pid}",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable Long pid,@RequestBody List<PmsSkuStock> skuStockList){
        int count = skuStockService.update(pid,skuStockList);
        if(count>0){
            return CommonResult.success(count);
        }else{
            return CommonResult.failed();
        }
    }
}
