package com.nexusengine.core.admin.catalog.controller;

import com.nexusengine.core.admin.catalog.dto.ProductParam;
import com.nexusengine.core.admin.catalog.dto.ProductQueryParam;
import com.nexusengine.core.admin.catalog.dto.ProductResult;
import com.nexusengine.core.admin.catalog.dto.ProductSummaryResponse;
import com.nexusengine.core.admin.catalog.mapper.ProductMapper;
import com.nexusengine.core.admin.catalog.service.ProductService;
import com.nexusengine.core.common.exception.Asserts;
import com.nexusengine.core.domain.catalog.entity.Product;
import com.nexusengine.core.domain.catalog.entity.ProductApprovalEvent;
import com.nexusengine.core.domain.catalog.entity.ProductPriceChange;
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

@RestController("adminProductController")
@Tag(name = "Catalog - Products", description = "Admin product management APIs")
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductMapper productMapper;

    @Operation(summary = "Create product")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Integer create(@Valid @RequestBody ProductParam productParam) {
        int count = productService.create(productParam);
        if (count <= 0) {
            Asserts.fail("Failed to create product");
        }
        return count;
    }

    @Operation(summary = "Get product update info")
    @GetMapping("/updateInfo/{id}")
    public ProductResult getUpdateInfo(@PathVariable("id") Long id) {
        return productService.getUpdateInfo(id);
    }

    @Operation(summary = "Update product")
    @PostMapping("/update/{id}")
    public Integer update(
            @PathVariable("id") Long id, @Valid @RequestBody ProductParam productParam) {
        int count = productService.update(id, productParam);
        if (count <= 0) {
            Asserts.fail("Failed to update product");
        }
        return count;
    }

    @Operation(summary = "List products")
    @GetMapping("/list")
    public Page<ProductSummaryResponse> getList(
            ProductQueryParam productQueryParam,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        Page<Product> productList = productService.list(productQueryParam, pageSize, pageNum);
        return productList.map(productMapper::toSummary);
    }

    @Operation(summary = "Simple list products")
    @GetMapping("/simpleList")
    public List<ProductSummaryResponse> getList(
            @RequestParam(required = false) String keyword) {
        return productMapper.toSummaryList(productService.list(keyword));
    }

    @Operation(summary = "Update approval status")
    @PostMapping("/update/verifyStatus")
    public Integer updateVerifyStatus(
            @RequestParam("ids") List<Long> ids,
            @RequestParam("verifyStatus") Integer verifyStatus,
            @RequestParam("detail") String detail) {
        int count = productService.updateVerifyStatus(ids, verifyStatus, detail);
        if (count <= 0) {
            Asserts.fail("Failed to update approval status");
        }
        return count;
    }

    @Operation(summary = "Update publish status")
    @PostMapping("/update/publishStatus")
    public Integer updatePublishStatus(
            @RequestParam("ids") List<Long> ids,
            @RequestParam("publishStatus") Integer publishStatus) {
        int count = productService.updatePublishStatus(ids, publishStatus);
        if (count <= 0) {
            Asserts.fail("Failed to update publish status");
        }
        return count;
    }

    @Operation(summary = "Update featured status")
    @PostMapping("/update/recommendStatus")
    public Integer updateRecommendStatus(
            @RequestParam("ids") List<Long> ids,
            @RequestParam("recommendStatus") Integer recommendStatus) {
        int count = productService.updateRecommendStatus(ids, recommendStatus);
        if (count <= 0) {
            Asserts.fail("Failed to update featured status");
        }
        return count;
    }

    @Operation(summary = "Update new arrival status")
    @PostMapping("/update/newStatus")
    public Integer updateNewStatus(
            @RequestParam("ids") List<Long> ids, @RequestParam("newStatus") Integer newStatus) {
        int count = productService.updateNewStatus(ids, newStatus);
        if (count <= 0) {
            Asserts.fail("Failed to update new arrival status");
        }
        return count;
    }

    @Operation(summary = "Update delete status")
    @PostMapping("/update/deleteStatus")
    public Integer updateDeleteStatus(
            @RequestParam("ids") List<Long> ids,
            @RequestParam("deleteStatus") Integer deleteStatus) {
        int count = productService.updateDeleteStatus(ids, deleteStatus);
        if (count <= 0) {
            Asserts.fail("Failed to update delete status");
        }
        return count;
    }

    @Operation(summary = "Get price audit logs")
    @GetMapping("/auditLog/{id}")
    public List<ProductPriceChange> getAuditLog(@PathVariable("id") Long id) {
        return productService.getAuditLog(id);
    }

    @Operation(summary = "Get approval history")
    @GetMapping("/approvalHistory/{id}")
    public List<ProductApprovalEvent> getApprovalHistory(@PathVariable("id") Long id) {
        return productService.getApprovalHistory(id);
    }
}
