package com.nexusengine.core.admin.catalog.controller;

import lombok.RequiredArgsConstructor;

import com.nexusengine.core.domain.catalog.entity.ProductPriceChange;

import com.nexusengine.core.domain.catalog.entity.*;
import com.nexusengine.core.domain.ordering.entity.*;
import com.nexusengine.core.domain.customer.entity.*;
import com.nexusengine.core.domain.promotion.entity.*;
import com.nexusengine.core.domain.iam.entity.*;
import com.nexusengine.core.domain.catalog.repository.*;
import com.nexusengine.core.domain.ordering.repository.*;
import com.nexusengine.core.domain.customer.repository.*;
import com.nexusengine.core.domain.promotion.repository.*;
import com.nexusengine.core.domain.iam.repository.*;
import com.nexusengine.core.admin.catalog.dto.*;
import com.nexusengine.core.admin.catalog.service.*;
import com.nexusengine.core.admin.analytics.service.*;
import com.nexusengine.core.admin.ordering.dto.*;
import com.nexusengine.core.admin.ordering.service.*;
import com.nexusengine.core.admin.promotion.dto.*;
import com.nexusengine.core.admin.promotion.service.*;
import com.nexusengine.core.admin.ai.service.*;
import com.nexusengine.core.admin.infra.dto.*;
import com.nexusengine.core.admin.infra.service.*;
import com.nexusengine.core.admin.iam.dto.*;
import com.nexusengine.core.admin.iam.service.*;
import com.nexusengine.core.domain.catalog.entity.ProductApprovalEvent;

import org.springframework.web.bind.annotation.RestController;
import com.nexusengine.core.common.api.CommonPage;
import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.domain.catalog.entity.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Represents the ProductController component.
 * Provides core functionality and operations for ProductController.
 */
@RestController
@Tag(name = "ProductController", description = "Pms product controller APIs")
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;

    @Operation(summary = "Create Operation")
    @PostMapping("/create")

    public CommonResult create(@RequestBody ProductParam productParam) {
        try {
            int count = productService.create(productParam);
            if (count > 0) {
                return CommonResult.success(count);
            } else {
                return CommonResult.failed();
            }
        } catch (IllegalArgumentException e) {
            LOGGER.error("Invalid product data:", e);
            return CommonResult.failed("Invalid product data: " + e.getMessage());
        } catch (org.springframework.dao.DataAccessException e) {
            LOGGER.error("Database error during product creation:", e);
            return CommonResult.failed("Database error during product creation");
        }
    }

    @Operation(summary = "Get update info Operation")
    @GetMapping("/updateInfo/{id}")

    public CommonResult<ProductResult> getUpdateInfo(@PathVariable("id") Long id) {
        ProductResult productResult = productService.getUpdateInfo(id);
        return CommonResult.success(productResult);
    }

    @Operation(summary = "Update Operation")
    @PostMapping("/update/{id}")

    public CommonResult update(@PathVariable("id") Long id, @RequestBody ProductParam productParam) {
        int count = productService.update(id, productParam);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @Operation(summary = "Get list Operation")
    @GetMapping("/list")

    public CommonResult<CommonPage<Product>> getList(ProductQueryParam productQueryParam,
                                                        @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        org.springframework.data.domain.Page<Product> productList = productService.list(productQueryParam, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(productList));
    }

    @Operation(summary = "Get list Operation")
    @GetMapping("/simpleList")

    public CommonResult<List<Product>> getList(String keyword) {
        List<Product> productList = productService.list(keyword);
        return CommonResult.success(productList);
    }

    @Operation(summary = "Update verify status Operation")
    @PostMapping("/update/verifyStatus")

        public CommonResult updateVerifyStatus(@RequestParam("ids") List<Long> ids,
                                           @RequestParam("verifyStatus") Integer verifyStatus,
                                           @RequestParam("detail") String detail) {
        int count = productService.updateVerifyStatus(ids, verifyStatus, detail);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @Operation(summary = "Update publish status Operation")
    @PostMapping("/update/publishStatus")

    public CommonResult updatePublishStatus(@RequestParam("ids") List<Long> ids,
                                            @RequestParam("publishStatus") Integer publishStatus) {
        int count = productService.updatePublishStatus(ids, publishStatus);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @Operation(summary = "Update recommend status Operation")
    @PostMapping("/update/recommendStatus")

    public CommonResult updateRecommendStatus(@RequestParam("ids") List<Long> ids,
                                              @RequestParam("recommendStatus") Integer recommendStatus) {
        int count = productService.updateRecommendStatus(ids, recommendStatus);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @Operation(summary = "Update new status Operation")
    @PostMapping("/update/newStatus")

    public CommonResult updateNewStatus(@RequestParam("ids") List<Long> ids,
                                        @RequestParam("newStatus") Integer newStatus) {
        int count = productService.updateNewStatus(ids, newStatus);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @Operation(summary = "Update delete status Operation")
    @PostMapping("/update/deleteStatus")

    public CommonResult updateDeleteStatus(@RequestParam("ids") List<Long> ids,
                                           @RequestParam("deleteStatus") Integer deleteStatus) {
        int count = productService.updateDeleteStatus(ids, deleteStatus);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @Operation(summary = "Get operate logs")
    @GetMapping("/auditLog/{id}")
    public CommonResult<List<ProductPriceChange>> getAuditLog(@PathVariable("id") Long id) {
        return CommonResult.success(productService.getAuditLog(id));
    }

    @Operation(summary = "Get verify records")
    @GetMapping("/approvalHistory/{id}")
    public CommonResult<List<ProductApprovalEvent>> getApprovalHistory(@PathVariable("id") Long id) {
        return CommonResult.success(productService.getApprovalHistory(id));
    }

}
