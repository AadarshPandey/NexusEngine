package com.nexusengine.core.portal.controller;

import org.springframework.web.bind.annotation.RestController;
import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.model.PmsProduct;
import com.nexusengine.core.model.PmsProductCategory;
import com.nexusengine.core.portal.domain.HomeContentResult;
import com.nexusengine.core.portal.service.HomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Represents the HomeController component.
 * Provides core functionality and operations for HomeController.
 */
@RestController
@Tag(name = "HomeController", description = "Home controller APIs")
@RequestMapping("/portal/home")
public class HomeController {
    @Autowired
    private HomeService homeService;

    @Operation(summary = "Content Operation")
    @RequestMapping(value = "/content", method = RequestMethod.GET)

    public CommonResult<HomeContentResult> content() {
        HomeContentResult contentResult = homeService.content();
        return CommonResult.success(contentResult);
    }

    @Operation(summary = "Recommend product list Operation")
    @RequestMapping(value = "/recommendProductList", method = RequestMethod.GET)

    public CommonResult<List<PmsProduct>> recommendProductList(@RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize,
                                                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<PmsProduct> productList = homeService.recommendProductList(pageSize, pageNum);
        return CommonResult.success(productList);
    }

    @Operation(summary = "Get product cate list Operation")
    @RequestMapping(value = "/productCateList/{parentId}", method = RequestMethod.GET)

    public CommonResult<List<PmsProductCategory>> getProductCateList(@PathVariable Long parentId) {
        List<PmsProductCategory> productCategoryList = homeService.getProductCateList(parentId);
        return CommonResult.success(productCategoryList);
    }

    @Operation(summary = "Get subject list Operation")
    @RequestMapping(value = "/subjectList", method = RequestMethod.GET)

                                                         @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize,
                                                         @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        return CommonResult.success(subjectList);
    }

    @Operation(summary = "Hot product list Operation")
    @RequestMapping(value = "/hotProductList", method = RequestMethod.GET)

    public CommonResult<List<PmsProduct>> hotProductList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                         @RequestParam(value = "pageSize", defaultValue = "6") Integer pageSize) {
        List<PmsProduct> productList = homeService.hotProductList(pageNum,pageSize);
        return CommonResult.success(productList);
    }

    @Operation(summary = "New product list Operation")
    @RequestMapping(value = "/newProductList", method = RequestMethod.GET)

    public CommonResult<List<PmsProduct>> newProductList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                         @RequestParam(value = "pageSize", defaultValue = "6") Integer pageSize) {
        List<PmsProduct> productList = homeService.newProductList(pageNum,pageSize);
        return CommonResult.success(productList);
    }
}
