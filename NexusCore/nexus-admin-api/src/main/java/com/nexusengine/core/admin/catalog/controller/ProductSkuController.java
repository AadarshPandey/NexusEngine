package com.nexusengine.core.admin.catalog.controller;

import com.nexusengine.core.admin.catalog.service.ProductSkuService;
import com.nexusengine.core.common.exception.Asserts;
import com.nexusengine.core.domain.catalog.entity.ProductSku;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Catalog - Product SKUs", description = "Admin SKU inventory management APIs")
@RequestMapping("/sku")
@RequiredArgsConstructor
public class ProductSkuController {
    private final ProductSkuService skuStockService;

    @Operation(summary = "List SKUs for product")
    @GetMapping("/{pid}")
    public List<ProductSku> getList(
            @PathVariable("pid") Long pid,
            @RequestParam(value = "keyword", required = false) String keyword) {
        return skuStockService.getList(pid, keyword);
    }

    @Operation(summary = "Update SKUs for product")
    @PostMapping("/update/{pid}")
    public Integer update(
            @PathVariable("pid") Long pid, @Valid @RequestBody List<ProductSku> skuStockList) {
        int count = skuStockService.update(pid, skuStockList);
        if (count <= 0) {
            Asserts.fail("Failed to update product SKUs");
        }
        return count;
    }
}
