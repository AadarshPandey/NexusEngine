package com.nexusengine.core.admin.ordering.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RestController;

import com.nexusengine.core.admin.ordering.dto.OrderStatusUpdateParam;
import com.nexusengine.core.admin.ordering.dto.ReturnRequestQueryParam;
import com.nexusengine.core.admin.ordering.dto.ReturnRequestResult;
import com.nexusengine.core.admin.ordering.service.ReturnRequestService;
import com.nexusengine.core.common.api.CommonPage;
import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.domain.ordering.entity.ReturnRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Represents the ReturnRequestController component.
 * Provides core functionality and operations for ReturnRequestController.
 */
@RestController("adminReturnRequestController")
@Tag(name = "ReturnRequestController", description = "Oms order return apply controller APIs")
@RequestMapping("/returnApply")
@RequiredArgsConstructor
public class ReturnRequestController {
    private final ReturnRequestService returnApplyService;

    @Operation(summary = "List Operation")
    @GetMapping("/list")

    public CommonResult<CommonPage<ReturnRequest>> list(ReturnRequestQueryParam queryParam,
                                                              @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                              @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<ReturnRequest> returnApplyList = returnApplyService.list(queryParam, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(returnApplyList));
    }

    @Operation(summary = "Delete Operation")
    @PostMapping("/delete")

    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        int count = returnApplyService.delete(ids);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "Get item Operation")
    @GetMapping("/{id}")

    public CommonResult getItem(@PathVariable("id") Long id) {
        ReturnRequestResult result = returnApplyService.getItem(id);
        return CommonResult.success(result);
    }

    @Operation(summary = "Update status Operation")
    @PostMapping("/update/status/{id}")

    public CommonResult updateStatus(@PathVariable("id") Long id, @RequestBody OrderStatusUpdateParam statusParam) {
        int count = returnApplyService.updateStatus(id, statusParam);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

}
