package com.nexusengine.core.admin.iam.controller;

import com.nexusengine.core.admin.iam.dto.MenuNode;
import com.nexusengine.core.admin.iam.service.ConsoleMenuService;
import com.nexusengine.core.common.exception.Asserts;
import com.nexusengine.core.common.exception.ResourceNotFoundException;
import com.nexusengine.core.domain.iam.entity.ConsoleMenu;
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
@Tag(name = "IAM - Console Menus", description = "Admin console menu management APIs")
@RequestMapping("/api/admin/iam/menus")
@RequiredArgsConstructor
public class ConsoleMenuController {
    private final ConsoleMenuService menuService;

    @Operation(summary = "Create console menu")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Integer create(@Valid @RequestBody ConsoleMenu consoleMenu) {
        int count = menuService.create(consoleMenu);
        if (count <= 0) {
            Asserts.fail("Failed to create console menu");
        }
        return count;
    }

    @Operation(summary = "Update console menu")
    @PutMapping("/{id}")
    public Integer update(
            @PathVariable("id") Long id, @Valid @RequestBody ConsoleMenu consoleMenu) {
        int count = menuService.update(id, consoleMenu);
        if (count <= 0) {
            Asserts.fail("Failed to update console menu");
        }
        return count;
    }

    @Operation(summary = "Get console menu by ID")
    @GetMapping("/{id}")
    public ConsoleMenu getItem(@PathVariable("id") Long id) {
        ConsoleMenu consoleMenu = menuService.getItem(id);
        if (consoleMenu == null) {
            throw new ResourceNotFoundException("ConsoleMenu", id);
        }
        return consoleMenu;
    }

    @Operation(summary = "Delete console menu")
    @DeleteMapping("/{id}")
    public Integer delete(@PathVariable("id") Long id) {
        int count = menuService.delete(id);
        if (count <= 0) {
            Asserts.fail("Failed to delete console menu");
        }
        return count;
    }

    @Operation(summary = "List console menus by parent")
    @GetMapping("/parent/{parentId}")
    public Page<ConsoleMenu> list(
            @PathVariable("parentId") Long parentId,
            @PageableDefault(size = 5) Pageable pageable) {
        return menuService.list(
                parentId, pageable.getPageSize(), pageable.getPageNumber() + 1);
    }

    @Operation(summary = "Get console menu tree")
    @GetMapping("/tree")
    public List<MenuNode> treeList() {
        return menuService.treeList();
    }

    @Operation(summary = "Update console menu visibility")
    @PatchMapping("/{id}/visibility")
    public Integer updateHidden(
            @PathVariable("id") Long id, @RequestParam("hidden") Integer hidden) {
        int count = menuService.updateHidden(id, hidden);
        if (count <= 0) {
            Asserts.fail("Failed to update menu visibility");
        }
        return count;
    }
}
