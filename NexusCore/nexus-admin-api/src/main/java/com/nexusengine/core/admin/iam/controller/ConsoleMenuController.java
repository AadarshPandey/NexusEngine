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
@Tag(name = "IAM - Console Menus", description = "Admin console menu management APIs")
@RequestMapping("/menu")
@RequiredArgsConstructor
public class ConsoleMenuController {
    private final ConsoleMenuService menuService;

    @Operation(summary = "Create console menu")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Integer create(@Valid @RequestBody ConsoleMenu consoleMenu) {
        int count = menuService.create(consoleMenu);
        if (count <= 0) {
            Asserts.fail("Failed to create console menu");
        }
        return count;
    }

    @Operation(summary = "Update console menu")
    @PostMapping("/update/{id}")
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
    @PostMapping("/delete/{id}")
    public Integer delete(@PathVariable("id") Long id) {
        int count = menuService.delete(id);
        if (count <= 0) {
            Asserts.fail("Failed to delete console menu");
        }
        return count;
    }

    @Operation(summary = "List console menus by parent")
    @GetMapping("/list/{parentId}")
    public Page<ConsoleMenu> list(
            @PathVariable("parentId") Long parentId,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        return menuService.list(parentId, pageSize, pageNum);
    }

    @Operation(summary = "Get console menu tree")
    @GetMapping("/treeList")
    public List<MenuNode> treeList() {
        return menuService.treeList();
    }

    @Operation(summary = "Update console menu visibility")
    @PostMapping("/updateHidden/{id}")
    public Integer updateHidden(
            @PathVariable("id") Long id, @RequestParam("hidden") Integer hidden) {
        int count = menuService.updateHidden(id, hidden);
        if (count <= 0) {
            Asserts.fail("Failed to update menu visibility");
        }
        return count;
    }
}
