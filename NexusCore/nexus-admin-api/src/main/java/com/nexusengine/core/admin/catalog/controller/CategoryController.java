package com.nexusengine.core.admin.catalog.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RestController;

import com.nexusengine.core.admin.catalog.dto.CategoryParam;
import com.nexusengine.core.admin.catalog.service.CategoryService;
import com.nexusengine.core.admin.catalog.dto.CategoryWithChildrenItem;
import com.nexusengine.core.common.api.CommonPage;
import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.domain.catalog.entity.Category;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Represents the CategoryController component.
 * Provides core functionality and operations for CategoryController.
 */
@RestController
@Tag(name = "CategoryController", description = "Pms product category controller APIs")
@RequestMapping("/productCategory")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService productCategoryService;

    @Operation(summary = "Create Operation")
    @PostMapping("/create")

    public CommonResult create(@Validated @RequestBody CategoryParam productCategoryParam) {
        int count = productCategoryService.create(productCategoryParam);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @Operation(summary = "Update Operation")
    @PostMapping("/update/{id}")

    public CommonResult update(@PathVariable("id") Long id,
                         @Validated
                         @RequestBody CategoryParam productCategoryParam) {
        int count = productCategoryService.update(id, productCategoryParam);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @Operation(summary = "Get list Operation")
    @GetMapping("/list/{parentId}")

    public CommonResult<CommonPage<Category>> getList(@PathVariable("parentId") Long parentId,
                                                                @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                                @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        org.springframework.data.domain.Page<Category> productCategoryList = productCategoryService.getList(parentId, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(productCategoryList));
    }

    @Operation(summary = "Get item Operation")
    @GetMapping("/{id}")

    public CommonResult<Category> getItem(@PathVariable("id") Long id) {
        Category productCategory = productCategoryService.getItem(id);
        return CommonResult.success(productCategory);
    }

    @Operation(summary = "Delete Operation")
    @PostMapping("/delete/{id}")

    public CommonResult delete(@PathVariable("id") Long id) {
        int count = productCategoryService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @Operation(summary = "Update nav status Operation")
    @PostMapping("/update/isInNavigation")

    public CommonResult updateNavStatus(@RequestParam("ids") List<Long> ids, @RequestParam("isInNavigation") Integer navStatus) {
        int count = productCategoryService.updateNavStatus(ids, navStatus);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @Operation(summary = "Update show status Operation")
    @PostMapping("/update/isVisible")

    public CommonResult updateShowStatus(@RequestParam("ids") List<Long> ids, @RequestParam("isVisible") Integer showStatus) {
        int count = productCategoryService.updateShowStatus(ids, showStatus);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @Operation(summary = "List with children Operation")
    @GetMapping("/list/withChildren")

    public CommonResult<List<CategoryWithChildrenItem>> listWithChildren() {
        List<CategoryWithChildrenItem> list = productCategoryService.listWithChildren();
        return CommonResult.success(list);
    }
}
