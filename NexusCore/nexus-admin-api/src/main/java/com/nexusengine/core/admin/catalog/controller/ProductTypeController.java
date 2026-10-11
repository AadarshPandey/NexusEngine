package com.nexusengine.core.admin.catalog.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RestController;

import com.nexusengine.core.admin.catalog.service.ProductTypeService;
import com.nexusengine.core.admin.catalog.dto.ProductTypeWithAttributes;
import com.nexusengine.core.common.api.CommonPage;
import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.domain.catalog.entity.ProductType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Represents the ProductTypeController component.
 * Provides core functionality and operations for ProductTypeController.
 */
@RestController
@Tag(name = "ProductTypeController", description = "Pms product attribute category controller APIs")
@RequestMapping("/productAttribute/category")
@RequiredArgsConstructor
public class ProductTypeController {
    private final ProductTypeService productAttributeCategoryService;

    @Operation(summary = "Create Operation")
    @PostMapping("/create")

    public CommonResult create(@RequestParam("name") String name) {
        int count = productAttributeCategoryService.create(name);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @Operation(summary = "Update Operation")
    @PostMapping("/update/{id}")

    public CommonResult update(@PathVariable("id") Long id, @RequestParam("name") String name) {
        int count = productAttributeCategoryService.update(id, name);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @Operation(summary = "Delete Operation")
    @GetMapping("/delete/{id}")

    public CommonResult delete(@PathVariable("id") Long id) {
        int count = productAttributeCategoryService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @Operation(summary = "Get item Operation")
    @GetMapping("/{id}")

    public CommonResult<ProductType> getItem(@PathVariable("id") Long id) {
        ProductType productAttributeCategory = productAttributeCategoryService.getItem(id);
        return CommonResult.success(productAttributeCategory);
    }

    @Operation(summary = "Get list Operation")
    @GetMapping("/list")

    public CommonResult<CommonPage<ProductType>> getList(@RequestParam(defaultValue = "5") Integer pageSize, @RequestParam(defaultValue = "1") Integer pageNum) {
        org.springframework.data.domain.Page<ProductType> productAttributeCategoryList = productAttributeCategoryService.getList(pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(productAttributeCategoryList));
    }

    @Operation(summary = "Get list with attr Operation")
    @GetMapping("/list/withAttr")

    public CommonResult<List<ProductTypeWithAttributes>> getListWithAttr() {
        List<ProductTypeWithAttributes> productAttributeCategoryResultList = productAttributeCategoryService.getListWithAttr();
        return CommonResult.success(productAttributeCategoryResultList);
    }
}
