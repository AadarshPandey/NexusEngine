package com.nexusengine.core.admin.iam.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RestController;

import com.nexusengine.core.admin.iam.service.PermissionGroupService;
import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.domain.iam.entity.PermissionGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Represents the PermissionGroupController component.
 * Provides core functionality and operations for PermissionGroupController.
 */
@RestController
@Tag(name = "PermissionGroupController", description = "Ums resource category controller APIs")
@RequestMapping("/resourceCategory")
@RequiredArgsConstructor
public class PermissionGroupController {
    private final PermissionGroupService resourceCategoryService;

    @Operation(summary = "List all Operation")
    @GetMapping("/listAll")

    public CommonResult<List<PermissionGroup>> listAll() {
        List<PermissionGroup> resourceList = resourceCategoryService.listAll();
        return CommonResult.success(resourceList);
    }

    @Operation(summary = "Create Operation")
    @PostMapping("/create")

    public CommonResult create(@RequestBody PermissionGroup permissionGroup) {
        int count = resourceCategoryService.create(permissionGroup);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @Operation(summary = "Update Operation")
    @PostMapping("/update/{id}")

    public CommonResult update(@PathVariable("id") Long id,
                               @RequestBody PermissionGroup permissionGroup) {
        int count = resourceCategoryService.update(id, permissionGroup);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @Operation(summary = "Delete Operation")
    @PostMapping("/delete/{id}")

    public CommonResult delete(@PathVariable("id") Long id) {
        int count = resourceCategoryService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }
}
