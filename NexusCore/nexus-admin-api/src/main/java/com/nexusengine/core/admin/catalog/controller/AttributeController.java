package com.nexusengine.core.admin.catalog.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RestController;

import com.nexusengine.core.admin.catalog.dto.AttributeInfo;
import com.nexusengine.core.admin.catalog.dto.AttributeParam;
import com.nexusengine.core.admin.catalog.service.AttributeService;
import com.nexusengine.core.common.api.CommonPage;
import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.domain.catalog.entity.AttributeDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Represents the AttributeController component.
 * Provides core functionality and operations for AttributeController.
 */
@RestController
@Tag(name = "AttributeController", description = "Pms product attribute controller APIs")
@RequestMapping("/productAttribute")
@RequiredArgsConstructor
public class AttributeController {
    private final AttributeService productAttributeService;

    @Operation(summary = "Get list Operation")
    @Parameters({@Parameter(name = "type", description = "0 for attribute, 1 for parameter", required = true,in = ParameterIn.QUERY, schema = @Schema(type = "integer"))})
    @GetMapping("/list/{cid}")

    public CommonResult<CommonPage<AttributeDefinition>> getList(@PathVariable("cid") Long cid,
                                                                 @RequestParam(value = "type") Integer type,
                                                                 @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                                 @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        org.springframework.data.domain.Page<AttributeDefinition> productAttributeList = productAttributeService.getList(cid, type, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(productAttributeList));
    }

    @Operation(summary = "Create Operation")
    @PostMapping("/create")

    public CommonResult create(@RequestBody AttributeParam productAttributeParam) {
        int count = productAttributeService.create(productAttributeParam);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @Operation(summary = "Update Operation")
    @PostMapping("/update/{id}")

    public CommonResult update(@PathVariable("id") Long id, @RequestBody AttributeParam productAttributeParam) {
        int count = productAttributeService.update(id, productAttributeParam);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @Operation(summary = "Get item Operation")
    @GetMapping("/{id}")

    public CommonResult<AttributeDefinition> getItem(@PathVariable("id") Long id) {
        AttributeDefinition productAttribute = productAttributeService.getItem(id);
        return CommonResult.success(productAttribute);
    }

    @Operation(summary = "Delete Operation")
    @PostMapping("/delete")

    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        int count = productAttributeService.delete(ids);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @Operation(summary = "Get attr info Operation")
    @GetMapping("/attrInfo/{productCategoryId}")

    public CommonResult<List<AttributeInfo>> getAttrInfo(@PathVariable("productCategoryId") Long productCategoryId) {
        List<AttributeInfo> productAttrInfoList = productAttributeService.getProductAttrInfo(productCategoryId);
        return CommonResult.success(productAttrInfoList);
    }
}
