package com.nexusengine.core.controller;

import org.springframework.web.bind.annotation.RestController;
import com.nexusengine.core.common.api.CommonPage;
import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.dto.OmsOrderReturnApplyResult;
import com.nexusengine.core.dto.OmsReturnApplyQueryParam;
import com.nexusengine.core.dto.OmsUpdateStatusParam;
import com.nexusengine.core.model.OmsOrderReturnApply;
import com.nexusengine.core.service.OmsOrderReturnApplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Represents the OmsOrderReturnApplyController component.
 * Provides core functionality and operations for OmsOrderReturnApplyController.
 */
@RestController
@Tag(name = "OmsOrderReturnApplyController", description = "Oms order return apply controller APIs")
@RequestMapping("/returnApply")
@lombok.RequiredArgsConstructor
public class OmsOrderReturnApplyController {
    private final OmsOrderReturnApplyService returnApplyService;

    @Operation(summary = "List Operation")
    @GetMapping("/list")

    public CommonResult<CommonPage<OmsOrderReturnApply>> list(OmsReturnApplyQueryParam queryParam,
                                                              @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                              @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<OmsOrderReturnApply> returnApplyList = returnApplyService.list(queryParam, pageSize, pageNum);
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

    public CommonResult getItem(@PathVariable Long id) {
        OmsOrderReturnApplyResult result = returnApplyService.getItem(id);
        return CommonResult.success(result);
    }

    @Operation(summary = "Update status Operation")
    @PostMapping("/update/status/{id}")

    public CommonResult updateStatus(@PathVariable Long id, @RequestBody OmsUpdateStatusParam statusParam) {
        int count = returnApplyService.updateStatus(id, statusParam);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

}
