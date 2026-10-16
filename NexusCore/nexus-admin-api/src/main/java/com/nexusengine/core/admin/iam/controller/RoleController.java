package com.nexusengine.core.admin.iam.controller;

import com.nexusengine.core.admin.iam.service.RoleService;
import com.nexusengine.core.common.exception.Asserts;
import com.nexusengine.core.domain.iam.entity.ConsoleMenu;
import com.nexusengine.core.domain.iam.entity.Permission;
import com.nexusengine.core.domain.iam.entity.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "IAM - Roles", description = "Admin role management APIs")
@RequestMapping("/api/admin/iam/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @Operation(summary = "Create role")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Integer create(@Valid @RequestBody Role role) {
        int count = roleService.create(role);
        if (count <= 0) {
            Asserts.fail("Failed to create role");
        }
        return count;
    }

    @Operation(summary = "Update role")
    @PutMapping("/{id}")
    public Integer update(@PathVariable("id") Long id, @Valid @RequestBody Role role) {
        int count = roleService.update(id, role);
        if (count <= 0) {
            Asserts.fail("Failed to update role");
        }
        return count;
    }

    @Operation(summary = "Delete roles")
    @DeleteMapping
    public Integer delete(@RequestParam("ids") List<Long> ids) {
        int count = roleService.delete(ids);
        if (count <= 0) {
            Asserts.fail("Failed to delete roles");
        }
        return count;
    }

    @Operation(summary = "List all roles")
    @GetMapping("/all")
    public List<Role> listAll() {
        return roleService.list();
    }

    @Operation(summary = "List roles with pagination")
    @GetMapping
    public Page<Role> list(
            @RequestParam(value = "keyword", required = false) String keyword,
            @PageableDefault(size = 5) Pageable pageable) {
        return roleService.list(
                keyword, pageable.getPageSize(), pageable.getPageNumber() + 1);
    }

    @Operation(summary = "Update role status")
    @PatchMapping("/{id}/status")
    public Integer updateStatus(
            @PathVariable("id") Long id, @RequestParam(value = "status") Integer status) {
        Role role = new Role();
        role.setStatus(status != null && status == 1);
        int count = roleService.update(id, role);
        if (count <= 0) {
            Asserts.fail("Failed to update role status");
        }
        return count;
    }

    @Operation(summary = "List menus for role")
    @GetMapping("/{roleId}/menus")
    public List<ConsoleMenu> listMenu(@PathVariable("roleId") Long roleId) {
        return roleService.listMenu(roleId);
    }

    @Operation(summary = "List permissions for role")
    @GetMapping("/{roleId}/permissions")
    public List<Permission> listResource(@PathVariable("roleId") Long roleId) {
        return roleService.listResource(roleId);
    }

    @Operation(summary = "Assign menus to role")
    @PutMapping("/menus")
    public Integer allocMenu(
            @RequestParam("roleId") Long roleId, @RequestParam("menuIds") List<Long> menuIds) {
        return roleService.allocMenu(roleId, menuIds);
    }

    @Operation(summary = "Assign permissions to role")
    @PutMapping("/permissions")
    public Integer allocResource(
            @RequestParam("roleId") Long roleId,
            @RequestParam("resourceIds") List<Long> resourceIds) {
        return roleService.allocResource(roleId, resourceIds);
    }
}
