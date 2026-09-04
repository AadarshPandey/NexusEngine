package com.nexusengine.core.controller;

import com.nexusengine.core.common.api.CommonPage;
import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.model.SmsHomeNewProduct;
import com.nexusengine.core.service.SmsHomeNewProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/11/6.
 */
@Controller
@Tag(name = "SmsHomeNewProductController", description = "Sms home new product controller APIs")
@RequestMapping("/home/newProduct")
public class SmsHomeNewProductController {
    @Autowired
    private SmsHomeNewProductService homeNewProductService;

    @Operation(summary = "Create Operation")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@RequestBody List<SmsHomeNewProduct> homeNewProductList) {
        int count = homeNewProductService.create(homeNewProductList);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "Update sort Operation")
    @RequestMapping(value = "/update/sort/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateSort(@PathVariable Long id, Integer sort) {
        int count = homeNewProductService.updateSort(id, sort);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "Delete Operation")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        int count = homeNewProductService.delete(ids);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "Update recommend status Operation")
    @RequestMapping(value = "/update/recommendStatus", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateRecommendStatus(@RequestParam("ids") List<Long> ids, @RequestParam Integer recommendStatus) {
        int count = homeNewProductService.updateRecommendStatus(ids, recommendStatus);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "List Operation")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<SmsHomeNewProduct>> list(@RequestParam(value = "productName", required = false) String productName,
                                                            @RequestParam(value = "recommendStatus", required = false) Integer recommendStatus,
                                                            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<SmsHomeNewProduct> homeNewProductList = homeNewProductService.list(productName, recommendStatus, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(homeNewProductList));
    }
}
