package com.nexusengine.core.admin.catalog.controller;

import com.nexusengine.core.admin.catalog.dto.CategoryParam;
import com.nexusengine.core.admin.catalog.dto.CategoryResponse;
import com.nexusengine.core.admin.catalog.dto.CategoryWithChildrenItem;
import com.nexusengine.core.admin.catalog.mapper.CategoryMapper;
import com.nexusengine.core.admin.catalog.service.CategoryService;
import com.nexusengine.core.common.exception.Asserts;
import com.nexusengine.core.common.exception.ResourceNotFoundException;
import com.nexusengine.core.domain.catalog.entity.Category;
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
@Tag(name = "Catalog - Categories", description = "Admin product category management APIs")
@RequestMapping("/api/admin/catalog/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService productCategoryService;
    private final CategoryMapper categoryMapper;

    @Operation(summary = "Create category")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Integer create(@Valid @RequestBody CategoryParam productCategoryParam) {
        int count = productCategoryService.create(productCategoryParam);
        if (count <= 0) {
            Asserts.fail("Failed to create category");
        }
        return count;
    }

    @Operation(summary = "Update category")
    @PutMapping("/{id}")
    public Integer update(
            @PathVariable("id") Long id, @Valid @RequestBody CategoryParam productCategoryParam) {
        int count = productCategoryService.update(id, productCategoryParam);
        if (count <= 0) {
            Asserts.fail("Failed to update category");
        }
        return count;
    }

    @Operation(summary = "List categories by parent")
    @GetMapping("/parent/{parentId}")
    public Page<CategoryResponse> getList(
            @PathVariable("parentId") Long parentId,
            @PageableDefault(size = 5) Pageable pageable) {
        Page<Category> productCategoryList =
                productCategoryService.getList(
                        parentId, pageable.getPageSize(), pageable.getPageNumber() + 1);
        return productCategoryList.map(categoryMapper::toResponse);
    }

    @Operation(summary = "Get category by ID")
    @GetMapping("/{id}")
    public CategoryResponse getItem(@PathVariable("id") Long id) {
        Category productCategory = productCategoryService.getItem(id);
        if (productCategory == null) {
            throw new ResourceNotFoundException("Category", id);
        }
        return categoryMapper.toResponse(productCategory);
    }

    @Operation(summary = "Delete category")
    @DeleteMapping("/{id}")
    public Integer delete(@PathVariable("id") Long id) {
        int count = productCategoryService.delete(id);
        if (count <= 0) {
            Asserts.fail("Failed to delete category");
        }
        return count;
    }

    @Operation(summary = "Update navigation status")
    @PatchMapping("/navigation-status")
    public Integer updateNavStatus(
            @RequestParam("ids") List<Long> ids,
            @RequestParam("isInNavigation") Integer navStatus) {
        int count = productCategoryService.updateNavStatus(ids, navStatus);
        if (count <= 0) {
            Asserts.fail("Failed to update navigation status");
        }
        return count;
    }

    @Operation(summary = "Update visibility status")
    @PatchMapping("/visibility")
    public Integer updateShowStatus(
            @RequestParam("ids") List<Long> ids, @RequestParam("isVisible") Integer showStatus) {
        int count = productCategoryService.updateShowStatus(ids, showStatus);
        if (count <= 0) {
            Asserts.fail("Failed to update visibility status");
        }
        return count;
    }

    @Operation(summary = "List categories with children")
    @GetMapping("/tree")
    public List<CategoryWithChildrenItem> listWithChildren() {
        return productCategoryService.listWithChildren();
    }
}
