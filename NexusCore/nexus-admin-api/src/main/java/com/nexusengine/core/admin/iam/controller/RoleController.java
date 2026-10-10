package com.nexusengine.core.admin.iam.controller;

import lombok.RequiredArgsConstructor;

import com.nexusengine.core.domain.iam.entity.ConsoleMenu;
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
import com.nexusengine.core.domain.iam.entity.Role;
import com.nexusengine.core.domain.iam.entity.Permission;

import org.springframework.web.bind.annotation.RestController;
import com.nexusengine.core.common.api.CommonPage;
import com.nexusengine.core.common.api.CommonResult;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Represents the RoleController component.
 * Provides core functionality and operations for RoleController.
 */
@RestController
@Tag(name = "RoleController", description = "Ums role controller APIs")
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @Operation(summary = "Create Operation")
    @PostMapping("/create")

    public CommonResult create(@RequestBody Role role) {
        int count = roleService.create(role);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "Update Operation")
    @PostMapping("/update/{id}")

    public CommonResult update(@PathVariable("id") Long id, @RequestBody Role role) {
        int count = roleService.update(id, role);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "Delete Operation")
    @PostMapping("/delete")

    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        int count = roleService.delete(ids);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "List all Operation")
    @GetMapping("/listAll")

    public CommonResult<List<Role>> listAll() {
        List<Role> roleList = roleService.list();
        return CommonResult.success(roleList);
    }

    @Operation(summary = "List Operation")
    @GetMapping("/list")

    public CommonResult<CommonPage<Role>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                  @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        org.springframework.data.domain.Page<Role> roleList = roleService.list(keyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(roleList));
    }

    @Operation(summary = "Update status Operation")
    @PostMapping("/updateStatus/{id}")

    public CommonResult updateStatus(@PathVariable("id") Long id, @RequestParam(value = "status") Integer status) {
        Role role = new Role();
        role.setStatus(status);
        int count = roleService.update(id, role);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "List menu Operation")
    @GetMapping("/listMenu/{roleId}")

    public CommonResult<List<ConsoleMenu>> listMenu(@PathVariable("roleId") Long roleId) {
        List<ConsoleMenu> roleList = roleService.listMenu(roleId);
        return CommonResult.success(roleList);
    }

    @Operation(summary = "List resource Operation")
    @GetMapping("/listResource/{roleId}")

    public CommonResult<List<Permission>> listResource(@PathVariable("roleId") Long roleId) {
        List<Permission> roleList = roleService.listResource(roleId);
        return CommonResult.success(roleList);
    }

    @Operation(summary = "Alloc menu Operation")
    @PostMapping("/allocMenu")

    public CommonResult allocMenu(@RequestParam("roleId") Long roleId, @RequestParam("menuIds") List<Long> menuIds) {
        int count = roleService.allocMenu(roleId, menuIds);
        return CommonResult.success(count);
    }

    @Operation(summary = "Alloc resource Operation")
    @PostMapping("/allocResource")

    public CommonResult allocResource(@RequestParam("roleId") Long roleId, @RequestParam("resourceIds") List<Long> resourceIds) {
        int count = roleService.allocResource(roleId, resourceIds);
        return CommonResult.success(count);
    }

}
