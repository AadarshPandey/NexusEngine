package com.nexusengine.core.admin.iam.controller;

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
import com.nexusengine.core.domain.iam.entity.Permission;
import com.nexusengine.core.security.component.DynamicSecurityMetadataSource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Represents the PermissionController component.
 * Provides core functionality and operations for PermissionController.
 */
@RestController
@Tag(name = "PermissionController", description = "Ums resource controller APIs")
@RequestMapping("/resource")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService resourceService;
    private final DynamicSecurityMetadataSource dynamicSecurityMetadataSource;

    @Operation(summary = "Create Operation")
    @PostMapping("/create")

    public CommonResult create(@RequestBody Permission permission) {
        int count = resourceService.create(permission);
        dynamicSecurityMetadataSource.clearDataSource();
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @Operation(summary = "Update Operation")
    @PostMapping("/update/{id}")

    public CommonResult update(@PathVariable("id") Long id,
                               @RequestBody Permission permission) {
        int count = resourceService.update(id, permission);
        dynamicSecurityMetadataSource.clearDataSource();
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @Operation(summary = "Get item Operation")
    @GetMapping("/{id}")

    public CommonResult<Permission> getItem(@PathVariable("id") Long id) {
        Permission permission = resourceService.getItem(id);
        return CommonResult.success(permission);
    }

    @Operation(summary = "Delete Operation")
    @PostMapping("/delete/{id}")

    public CommonResult delete(@PathVariable("id") Long id) {
        int count = resourceService.delete(id);
        dynamicSecurityMetadataSource.clearDataSource();
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @Operation(summary = "List Operation")
    @GetMapping("/list")

    public CommonResult<CommonPage<Permission>> list(@RequestParam(required = false) Long categoryId,
                                                      @RequestParam(required = false) String nameKeyword,
                                                      @RequestParam(required = false) String urlKeyword,
                                                      @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        org.springframework.data.domain.Page<Permission> resourceList = resourceService.list(categoryId,nameKeyword, urlKeyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(resourceList));
    }

    @Operation(summary = "List all Operation")
    @GetMapping("/listAll")

    public CommonResult<List<Permission>> listAll() {
        List<Permission> resourceList = resourceService.listAll();
        return CommonResult.success(resourceList);
    }
}
