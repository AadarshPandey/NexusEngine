package com.nexusengine.core.admin.iam.controller;

import com.nexusengine.core.admin.iam.service.PermissionGroupService;
import com.nexusengine.core.common.exception.Asserts;
import com.nexusengine.core.domain.iam.entity.PermissionGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "IAM - Permission Groups", description = "Admin permission group management APIs")
@RequestMapping("/resourceCategory")
@RequiredArgsConstructor
public class PermissionGroupController {
    private final PermissionGroupService resourceCategoryService;

    @Operation(summary = "List all permission groups")
    @GetMapping("/listAll")
    public List<PermissionGroup> listAll() {
        return resourceCategoryService.listAll();
    }

    @Operation(summary = "Create permission group")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Integer create(@Valid @RequestBody PermissionGroup permissionGroup) {
        int count = resourceCategoryService.create(permissionGroup);
        if (count <= 0) {
            Asserts.fail("Failed to create permission group");
        }
        return count;
    }

    @Operation(summary = "Update permission group")
    @PostMapping("/update/{id}")
    public Integer update(
            @PathVariable("id") Long id, @Valid @RequestBody PermissionGroup permissionGroup) {
        int count = resourceCategoryService.update(id, permissionGroup);
        if (count <= 0) {
            Asserts.fail("Failed to update permission group");
        }
        return count;
    }

    @Operation(summary = "Delete permission group")
    @PostMapping("/delete/{id}")
    public Integer delete(@PathVariable("id") Long id) {
        int count = resourceCategoryService.delete(id);
        if (count <= 0) {
            Asserts.fail("Failed to delete permission group");
        }
        return count;
    }
}
