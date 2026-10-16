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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "IAM - Roles", description = "Admin role management APIs")
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @Operation(summary = "Create role")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Integer create(@Valid @RequestBody Role role) {
        int count = roleService.create(role);
        if (count <= 0) {
            Asserts.fail("Failed to create role");
        }
        return count;
    }

    @Operation(summary = "Update role")
    @PostMapping("/update/{id}")
    public Integer update(@PathVariable("id") Long id, @Valid @RequestBody Role role) {
        int count = roleService.update(id, role);
        if (count <= 0) {
            Asserts.fail("Failed to update role");
        }
        return count;
    }

    @Operation(summary = "Delete roles")
    @PostMapping("/delete")
    public Integer delete(@RequestParam("ids") List<Long> ids) {
        int count = roleService.delete(ids);
        if (count <= 0) {
            Asserts.fail("Failed to delete roles");
        }
        return count;
    }

    @Operation(summary = "List all roles")
    @GetMapping("/listAll")
    public List<Role> listAll() {
        return roleService.list();
    }

    @Operation(summary = "List roles with pagination")
    @GetMapping("/list")
    public Page<Role> list(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        return roleService.list(keyword, pageSize, pageNum);
    }

    @Operation(summary = "Update role status")
    @PostMapping("/updateStatus/{id}")
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
    @GetMapping("/listMenu/{roleId}")
    public List<ConsoleMenu> listMenu(@PathVariable("roleId") Long roleId) {
        return roleService.listMenu(roleId);
    }

    @Operation(summary = "List permissions for role")
    @GetMapping("/listResource/{roleId}")
    public List<Permission> listResource(@PathVariable("roleId") Long roleId) {
        return roleService.listResource(roleId);
    }

    @Operation(summary = "Assign menus to role")
    @PostMapping("/allocMenu")
    public Integer allocMenu(
            @RequestParam("roleId") Long roleId, @RequestParam("menuIds") List<Long> menuIds) {
        return roleService.allocMenu(roleId, menuIds);
    }

    @Operation(summary = "Assign permissions to role")
    @PostMapping("/allocResource")
    public Integer allocResource(
            @RequestParam("roleId") Long roleId,
            @RequestParam("resourceIds") List<Long> resourceIds) {
        return roleService.allocResource(roleId, resourceIds);
    }
}
