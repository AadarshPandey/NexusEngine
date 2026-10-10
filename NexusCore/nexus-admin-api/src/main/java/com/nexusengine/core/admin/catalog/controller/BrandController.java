package com.nexusengine.core.admin.catalog.controller;

import lombok.RequiredArgsConstructor;

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
import com.nexusengine.core.admin.catalog.dto.*;
import com.nexusengine.core.admin.catalog.service.*;
import com.nexusengine.core.admin.analytics.service.*;
import com.nexusengine.core.admin.ordering.dto.*;
import com.nexusengine.core.admin.ordering.service.*;
import com.nexusengine.core.admin.promotion.dto.*;
import com.nexusengine.core.admin.promotion.service.*;
import com.nexusengine.core.admin.ai.service.*;
import com.nexusengine.core.admin.infra.dto.*;
import com.nexusengine.core.admin.infra.service.*;
import com.nexusengine.core.admin.iam.dto.*;
import com.nexusengine.core.admin.iam.service.*;
import com.nexusengine.core.common.api.CommonPage;
import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.domain.catalog.entity.Brand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Represents the BrandController component.
 * Provides core functionality and operations for BrandController.
 */
@RestController("adminBrandController")
@Tag(name = "BrandController", description = "Pms brand controller APIs")
@RequestMapping("/brand")
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;

    @Operation(summary =  "Get list Operation")
    @GetMapping("/listAll")

    public CommonResult<CommonPage<Brand>> getList() {
        return CommonResult.success(CommonPage.restPage(brandService.listAllBrand()));
    }

    @Operation(summary =  "Create Operation")
    @PostMapping("/create")

    public CommonResult create(@Validated @RequestBody BrandParam brand) {
        CommonResult commonResult;
        int count = brandService.createBrand(brand);
        if (count == 1) {
            commonResult = CommonResult.success(count);
        } else {
            commonResult = CommonResult.failed();
        }
        return commonResult;
    }

    @Operation(summary =  "Update Operation")
    @PostMapping("/update/{id}")

    public CommonResult update(@PathVariable("id") Long id,
                               @Validated @RequestBody BrandParam brandParam) {
        CommonResult commonResult;
        int count = brandService.updateBrand(id, brandParam);
        if (count == 1) {
            commonResult = CommonResult.success(count);
        } else {
            commonResult = CommonResult.failed();
        }
        return commonResult;
    }

    @Operation(summary =  "Delete Operation")
    @GetMapping("/delete/{id}")

    public CommonResult delete(@PathVariable("id") Long id) {
        int count = brandService.deleteBrand(id);
        if (count == 1) {
            return CommonResult.success(null);
        } else {
            return CommonResult.failed();
        }
    }

    @Operation(summary =  "Get list Operation")
    @GetMapping("/list")

    public CommonResult<CommonPage<Brand>> getList(@RequestParam(value = "keyword", required = false) String keyword,
                                                      @RequestParam(value = "isVisible",required = false) Integer isVisible,
                                                      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                      @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        org.springframework.data.domain.Page<Brand> brandList = brandService.listBrand(keyword,isVisible,pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(brandList));
    }

    @Operation(summary =  "Get item Operation")
    @GetMapping("/{id}")

    public CommonResult<Brand> getItem(@PathVariable("id") Long id) {
        return CommonResult.success(brandService.getBrand(id));
    }

    @Operation(summary =  "Delete batch Operation")
    @PostMapping("/delete/batch")

    public CommonResult deleteBatch(@RequestParam("ids") List<Long> ids) {
        int count = brandService.deleteBrand(ids);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @PostMapping("/update/isVisible")

    public CommonResult updateIsVisible(@RequestParam("ids") List<Long> ids,
                                   @RequestParam("isVisible") Integer isVisible) {
        int count = brandService.updateIsVisible(ids, isVisible);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }
}
