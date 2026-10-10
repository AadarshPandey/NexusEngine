package com.nexusengine.core.portal.catalog.controller;

import lombok.RequiredArgsConstructor;
import com.nexusengine.core.portal.catalog.dto.*;
import com.nexusengine.core.portal.catalog.service.*;
import com.nexusengine.core.portal.catalog.dao.*;
import com.nexusengine.core.portal.payment.service.*;
import com.nexusengine.core.portal.ordering.dto.*;
import com.nexusengine.core.portal.ordering.service.*;
import com.nexusengine.core.portal.ordering.dao.*;
import com.nexusengine.core.portal.customer.dto.*;
import com.nexusengine.core.portal.customer.service.*;
import com.nexusengine.core.portal.promotion.dto.*;
import com.nexusengine.core.portal.promotion.service.*;
import com.nexusengine.core.portal.promotion.strategy.*;
import com.nexusengine.core.portal.ai.service.*;
import com.nexusengine.core.portal.home.dto.*;
import com.nexusengine.core.portal.home.service.*;
import com.nexusengine.core.portal.engagement.dto.*;
import com.nexusengine.core.portal.engagement.service.*;

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
import com.nexusengine.core.common.api.CommonPage;
import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.domain.catalog.entity.Brand;
import com.nexusengine.core.domain.catalog.entity.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
