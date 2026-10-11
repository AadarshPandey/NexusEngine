package com.nexusengine.core.portal.catalog.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RestController;

import com.nexusengine.core.common.api.CommonPage;
import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.domain.catalog.entity.Brand;
import com.nexusengine.core.domain.catalog.entity.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.nexusengine.core.portal.catalog.service.BrandService;


/**
 * Represents the BrandController component.
 * Provides core functionality and operations for BrandController.
 */
@RestController("portalBrandController")
@Tag(name = "BrandController", description = "Pms portal brand controller APIs")
@RequestMapping("/portal/brand")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService portalBrandService;

    @Operation(summary = "Recommend list Operation")
    @GetMapping("/recommendList")

    public CommonResult<List<Brand>> recommendList(@RequestParam(value = "pageSize", defaultValue = "6") Integer pageSize,
                                                      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<Brand> brandList = portalBrandService.recommendList(pageNum, pageSize);
        return CommonResult.success(brandList);
    }

    @Operation(summary = "Detail Operation")
    @GetMapping("/detail/{brandId}")

    public CommonResult<Brand> detail(@PathVariable("brandId") Long brandId) {
        Brand brand = portalBrandService.detail(brandId);
        return CommonResult.success(brand);
    }

    @Operation(summary = "Product list Operation")
    @GetMapping("/productList")

    public CommonResult<CommonPage<Product>> productList(@RequestParam("brandId") Long brandId,
                                                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                            @RequestParam(value = "pageSize", defaultValue = "6") Integer pageSize) {
        CommonPage<Product> result = portalBrandService.productList(brandId,pageNum, pageSize);
        return CommonResult.success(result);
    }
}
