package com.nexusengine.core.admin.iam.controller;

import com.nexusengine.core.admin.iam.service.PermissionService;
import com.nexusengine.core.common.exception.Asserts;
import com.nexusengine.core.common.exception.ResourceNotFoundException;
import com.nexusengine.core.domain.iam.entity.Permission;
import com.nexusengine.core.security.component.DynamicSecurityMetadataSource;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "IAM - Permissions", description = "Admin permission management APIs")
@RequestMapping("/api/admin/iam/permissions")
@RequiredArgsConstructor
public class PermissionController {
    private final PermissionService resourceService;
    private final DynamicSecurityMetadataSource dynamicSecurityMetadataSource;

    @Operation(summary = "Create permission")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Integer create(@Valid @RequestBody Permission permission) {
        int count = resourceService.create(permission);
        dynamicSecurityMetadataSource.clearDataSource();
        if (count <= 0) {
            Asserts.fail("Failed to create permission");
        }
        return count;
    }

    @Operation(summary = "Update permission")
    @PutMapping("/{id}")
    public Integer update(
            @PathVariable("id") Long id, @Valid @RequestBody Permission permission) {
        int count = resourceService.update(id, permission);
        dynamicSecurityMetadataSource.clearDataSource();
        if (count <= 0) {
            Asserts.fail("Failed to update permission");
        }
        return count;
    }

    @Operation(summary = "Get permission by ID")
    @GetMapping("/{id}")
    public Permission getItem(@PathVariable("id") Long id) {
        Permission permission = resourceService.getItem(id);
        if (permission == null) {
            throw new ResourceNotFoundException("Permission", id);
        }
        return permission;
    }

    @Operation(summary = "Delete permission")
    @DeleteMapping("/{id}")
    public Integer delete(@PathVariable("id") Long id) {
        int count = resourceService.delete(id);
        dynamicSecurityMetadataSource.clearDataSource();
        if (count <= 0) {
            Asserts.fail("Failed to delete permission");
        }
        return count;
    }

    @Operation(summary = "List permissions with pagination")
    @GetMapping
    public Page<Permission> list(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String nameKeyword,
            @RequestParam(required = false) String urlKeyword,
            @PageableDefault(size = 5) Pageable pageable) {
        return resourceService.list(
                categoryId,
                nameKeyword,
                urlKeyword,
                pageable.getPageSize(),
                pageable.getPageNumber() + 1);
    }

    @Operation(summary = "List all permissions")
    @GetMapping("/all")
    public List<Permission> listAll() {
        return resourceService.listAll();
    }
}
