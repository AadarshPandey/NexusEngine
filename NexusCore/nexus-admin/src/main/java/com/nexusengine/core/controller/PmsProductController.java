package com.nexusengine.core.controller;

import org.springframework.web.bind.annotation.RestController;
import com.nexusengine.core.common.api.CommonPage;
import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.dto.PmsProductParam;
import com.nexusengine.core.dto.PmsProductQueryParam;
import com.nexusengine.core.dto.PmsProductResult;
import com.nexusengine.core.model.PmsProduct;
import com.nexusengine.core.service.PmsProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Represents the PmsProductController component.
 * Provides core functionality and operations for PmsProductController.
 */
@RestController
@Tag(name = "PmsProductController", description = "Pms product controller APIs")
@RequestMapping("/product")
@lombok.RequiredArgsConstructor
public class PmsProductController {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PmsProductController.class);
    private final PmsProductService productService;

    @Operation(summary = "Create Operation")
    @PostMapping("/create")

    public CommonResult create(@RequestBody PmsProductParam productParam) {
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

    public CommonResult<PmsProductResult> getUpdateInfo(@PathVariable Long id) {
        PmsProductResult productResult = productService.getUpdateInfo(id);
        return CommonResult.success(productResult);
    }

    @Operation(summary = "Update Operation")
    @PostMapping("/update/{id}")

    public CommonResult update(@PathVariable Long id, @RequestBody PmsProductParam productParam) {
        int count = productService.update(id, productParam);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @Operation(summary = "Get list Operation")
    @GetMapping("/list")

    public CommonResult<CommonPage<PmsProduct>> getList(PmsProductQueryParam productQueryParam,
                                                        @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        org.springframework.data.domain.Page<PmsProduct> productList = productService.list(productQueryParam, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(productList));
    }

    @Operation(summary = "Get list Operation")
    @GetMapping("/simpleList")

    public CommonResult<List<PmsProduct>> getList(String keyword) {
        List<PmsProduct> productList = productService.list(keyword);
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
    @GetMapping("/operateLog/{id}")
    public CommonResult<List<com.nexusengine.core.model.PmsProductOperateLog>> getOperateLog(@PathVariable Long id) {
        return CommonResult.success(productService.getOperateLog(id));
    }

    @Operation(summary = "Get verify records")
    @GetMapping("/verifyRecord/{id}")
    public CommonResult<List<com.nexusengine.core.model.PmsProductVerifyRecord>> getVerifyRecord(@PathVariable Long id) {
        return CommonResult.success(productService.getVerifyRecord(id));
    }

}
