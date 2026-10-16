package com.nexusengine.core.admin.catalog.controller;

import com.nexusengine.core.admin.catalog.dto.ProductTypeWithAttributes;
import com.nexusengine.core.admin.catalog.service.ProductTypeService;
import com.nexusengine.core.common.exception.Asserts;
import com.nexusengine.core.common.exception.ResourceNotFoundException;
import com.nexusengine.core.domain.catalog.entity.ProductType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Catalog - Product Types", description = "Admin product type management APIs")
@RequestMapping("/productAttribute/category")
@RequiredArgsConstructor
public class ProductTypeController {
    private final ProductTypeService productAttributeCategoryService;

    @Operation(summary = "Create product type")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Integer create(@RequestParam("name") String name) {
        int count = productAttributeCategoryService.create(name);
        if (count <= 0) {
            Asserts.fail("Failed to create product type");
        }
        return count;
    }

    @Operation(summary = "Update product type")
    @PostMapping("/update/{id}")
    public Integer update(@PathVariable("id") Long id, @RequestParam("name") String name) {
        int count = productAttributeCategoryService.update(id, name);
        if (count <= 0) {
            Asserts.fail("Failed to update product type");
        }
        return count;
    }

    @Operation(summary = "Delete product type")
    @GetMapping("/delete/{id}")
    public Integer delete(@PathVariable("id") Long id) {
        int count = productAttributeCategoryService.delete(id);
        if (count <= 0) {
            Asserts.fail("Failed to delete product type");
        }
        return count;
    }

    @Operation(summary = "Get product type by ID")
    @GetMapping("/{id}")
    public ProductType getItem(@PathVariable("id") Long id) {
        ProductType productAttributeCategory = productAttributeCategoryService.getItem(id);
        if (productAttributeCategory == null) {
            throw new ResourceNotFoundException("ProductType", id);
        }
        return productAttributeCategory;
    }

    @Operation(summary = "List product types")
    @GetMapping("/list")
    public Page<ProductType> getList(
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "1") Integer pageNum) {
        return productAttributeCategoryService.getList(pageSize, pageNum);
    }

    @Operation(summary = "List product types with attributes")
    @GetMapping("/list/withAttr")
    public List<ProductTypeWithAttributes> getListWithAttr() {
        return productAttributeCategoryService.getListWithAttr();
    }
}
