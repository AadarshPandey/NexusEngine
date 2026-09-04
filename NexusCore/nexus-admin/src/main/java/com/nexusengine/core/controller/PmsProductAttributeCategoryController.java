package com.nexusengine.core.controller;

import com.nexusengine.core.common.api.CommonPage;
import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.dto.PmsProductAttributeCategoryItem;
import com.nexusengine.core.model.PmsProductAttributeCategory;
import com.nexusengine.core.service.PmsProductAttributeCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/4/26.
 */
@Controller
@Tag(name = "PmsProductAttributeCategoryController", description = "Pms product attribute category controller APIs")
@RequestMapping("/productAttribute/category")
public class PmsProductAttributeCategoryController {
    @Autowired
    private PmsProductAttributeCategoryService productAttributeCategoryService;

    @Operation(summary = "Create Operation")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@RequestParam String name) {
        int count = productAttributeCategoryService.create(name);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @Operation(summary = "Update Operation")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable Long id, @RequestParam String name) {
        int count = productAttributeCategoryService.update(id, name);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @Operation(summary = "Delete Operation")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult delete(@PathVariable Long id) {
        int count = productAttributeCategoryService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @Operation(summary = "Get item Operation")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<PmsProductAttributeCategory> getItem(@PathVariable Long id) {
        PmsProductAttributeCategory productAttributeCategory = productAttributeCategoryService.getItem(id);
        return CommonResult.success(productAttributeCategory);
    }

    @Operation(summary = "Get list Operation")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<PmsProductAttributeCategory>> getList(@RequestParam(defaultValue = "5") Integer pageSize, @RequestParam(defaultValue = "1") Integer pageNum) {
        List<PmsProductAttributeCategory> productAttributeCategoryList = productAttributeCategoryService.getList(pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(productAttributeCategoryList));
    }

    @Operation(summary = "Get list with attr Operation")
    @RequestMapping(value = "/list/withAttr", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<PmsProductAttributeCategoryItem>> getListWithAttr() {
        List<PmsProductAttributeCategoryItem> productAttributeCategoryResultList = productAttributeCategoryService.getListWithAttr();
        return CommonResult.success(productAttributeCategoryResultList);
    }
}
