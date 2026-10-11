package com.nexusengine.core.portal.home.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RestController;

import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.domain.catalog.entity.Product;
import com.nexusengine.core.domain.catalog.entity.Category;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.nexusengine.core.portal.home.dto.HomeContentResult;
import com.nexusengine.core.portal.home.service.HomeService;


/**
 * Represents the HomeController component.
 * Provides core functionality and operations for HomeController.
 */
@RestController
@Tag(name = "HomeController", description = "Home controller APIs")
@RequestMapping("/portal/home")
@RequiredArgsConstructor
public class HomeController {
    private final HomeService homeService;

    @Operation(summary = "Content Operation")
    @GetMapping("/body")

    public CommonResult<HomeContentResult> content() {
        HomeContentResult contentResult = homeService.content();
        return CommonResult.success(contentResult);
    }

    @Operation(summary = "Recommend product list Operation")
    @GetMapping("/recommendProductList")

    public CommonResult<List<Product>> recommendProductList(@RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize,
                                                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<Product> productList = homeService.recommendProductList(pageSize, pageNum);
        return CommonResult.success(productList);
    }

    @Operation(summary = "Get product cate list Operation")
    @GetMapping("/productCateList/{parentId}")

    public CommonResult<List<Category>> getProductCateList(@PathVariable("parentId") Long parentId) {
        List<Category> productCategoryList = homeService.getProductCateList(parentId);
        return CommonResult.success(productCategoryList);
    }


    @Operation(summary = "Hot product list Operation")
    @GetMapping("/hotProductList")

    public CommonResult<List<Product>> hotProductList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                         @RequestParam(value = "pageSize", defaultValue = "6") Integer pageSize) {
        List<Product> productList = homeService.hotProductList(pageNum,pageSize);
        return CommonResult.success(productList);
    }

    @Operation(summary = "New product list Operation")
    @GetMapping("/newProductList")

    public CommonResult<List<Product>> newProductList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                         @RequestParam(value = "pageSize", defaultValue = "6") Integer pageSize) {
        List<Product> productList = homeService.newProductList(pageNum,pageSize);
        return CommonResult.success(productList);
    }
}
