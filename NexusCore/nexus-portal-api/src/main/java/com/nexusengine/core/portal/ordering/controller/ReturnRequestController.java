package com.nexusengine.core.portal.ordering.controller;

import lombok.RequiredArgsConstructor;

import com.nexusengine.core.domain.ordering.repository.ReturnReasonRepository;

import com.nexusengine.core.domain.ordering.entity.ReturnReason;

import org.springframework.web.bind.annotation.RestController;
import com.nexusengine.core.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.nexusengine.core.portal.ordering.dto.ReturnApplyParam;
import com.nexusengine.core.portal.ordering.service.ReturnRequestService;


/**
 * Represents the ReturnRequestController component.
 * Provides core functionality and operations for ReturnRequestController.
 */
@RestController("portalReturnRequestController")
@Tag(name = "ReturnRequestController",description = "Oms portal order return apply controller APIs")
@RequestMapping("/portal/returnApply")
@RequiredArgsConstructor
public class ReturnRequestController {
    private final ReturnRequestService returnApplyService;
    private final ReturnReasonRepository returnReasonRepository;

    @Operation(summary = "Create Operation")
    @PostMapping("/create")

    public CommonResult create(@RequestBody ReturnApplyParam returnApply) {
        int count = returnApplyService.create(returnApply);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "List return reasons")
    @org.springframework.web.bind.annotation.GetMapping("/reasons")
    public CommonResult<java.util.List<ReturnReason>> listReasons() {
        java.util.List<ReturnReason> activeReasons = returnReasonRepository.findAll()
            .stream().filter(r -> r.getStatus() != null && r.getStatus() == 1)
            .collect(java.util.stream.Collectors.toList());
        return CommonResult.success(activeReasons);
    }
}
