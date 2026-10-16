package com.nexusengine.core.admin.catalog.controller;

import com.nexusengine.core.admin.catalog.dto.AttributeInfo;
import com.nexusengine.core.admin.catalog.dto.AttributeParam;
import com.nexusengine.core.admin.catalog.service.AttributeService;
import com.nexusengine.core.common.exception.Asserts;
import com.nexusengine.core.common.exception.ResourceNotFoundException;
import com.nexusengine.core.domain.catalog.entity.AttributeDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Tag(name = "Catalog - Attributes", description = "Admin product attribute management APIs")
@RequestMapping("/api/admin/catalog/attributes")
@RequiredArgsConstructor
public class AttributeController {
    private final AttributeService productAttributeService;

    @Operation(summary = "List attributes by product type and category")
    @Parameters({
        @Parameter(
                name = "type",
                description = "0 for attribute, 1 for parameter",
                required = true,
                in = ParameterIn.QUERY,
                schema = @Schema(type = "integer"))
    })
    @GetMapping("/product-type/{cid}")
    public Page<AttributeDefinition> getList(
            @PathVariable("cid") Long cid,
            @RequestParam(value = "type") Integer type,
            @PageableDefault(size = 5) Pageable pageable) {
        return productAttributeService.getList(
                cid, type, pageable.getPageSize(), pageable.getPageNumber() + 1);
    }

    @Operation(summary = "Create attribute")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Integer create(@Valid @RequestBody AttributeParam productAttributeParam) {
        int count = productAttributeService.create(productAttributeParam);
        if (count <= 0) {
            Asserts.fail("Failed to create attribute");
        }
        return count;
    }

    @Operation(summary = "Update attribute")
    @PutMapping("/{id}")
    public Integer update(
            @PathVariable("id") Long id,
            @Valid @RequestBody AttributeParam productAttributeParam) {
        int count = productAttributeService.update(id, productAttributeParam);
        if (count <= 0) {
            Asserts.fail("Failed to update attribute");
        }
        return count;
    }

    @Operation(summary = "Get attribute by ID")
    @GetMapping("/{id}")
    public AttributeDefinition getItem(@PathVariable("id") Long id) {
        AttributeDefinition productAttribute = productAttributeService.getItem(id);
        if (productAttribute == null) {
            throw new ResourceNotFoundException("AttributeDefinition", id);
        }
        return productAttribute;
    }

    @Operation(summary = "Delete attributes")
    @DeleteMapping
    public Integer delete(@RequestParam("ids") List<Long> ids) {
        int count = productAttributeService.delete(ids);
        if (count <= 0) {
            Asserts.fail("Failed to delete attributes");
        }
        return count;
    }

    @Operation(summary = "Get attribute info by category ID")
    @GetMapping("/category-info/{productCategoryId}")
    public List<AttributeInfo> getAttrInfo(
            @PathVariable("productCategoryId") Long productCategoryId) {
        return productAttributeService.getProductAttrInfo(productCategoryId);
    }
}
