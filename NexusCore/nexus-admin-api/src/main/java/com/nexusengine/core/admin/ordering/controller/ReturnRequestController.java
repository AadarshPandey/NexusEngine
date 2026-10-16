package com.nexusengine.core.admin.ordering.controller;

import com.nexusengine.core.admin.ordering.dto.OrderStatusUpdateParam;
import com.nexusengine.core.admin.ordering.dto.ReturnRequestQueryParam;
import com.nexusengine.core.admin.ordering.dto.ReturnRequestResult;
import com.nexusengine.core.admin.ordering.service.ReturnRequestService;
import com.nexusengine.core.common.exception.Asserts;
import com.nexusengine.core.common.exception.ResourceNotFoundException;
import com.nexusengine.core.domain.ordering.entity.ReturnRequest;
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

@RestController("adminReturnRequestController")
@Tag(name = "Ordering - Return Requests", description = "Admin order return request management APIs")
@RequestMapping("/returnApply")
@RequiredArgsConstructor
public class ReturnRequestController {
    private final ReturnRequestService returnApplyService;

    @Operation(summary = "List return requests")
    @GetMapping("/list")
    public List<ReturnRequest> list(
            ReturnRequestQueryParam queryParam,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        return returnApplyService.list(queryParam, pageSize, pageNum);
    }

    @Operation(summary = "Delete return requests")
    @PostMapping("/delete")
    public Integer delete(@RequestParam("ids") List<Long> ids) {
        int count = returnApplyService.delete(ids);
        if (count <= 0) {
            Asserts.fail("Failed to delete return requests");
        }
        return count;
    }

    @Operation(summary = "Get return request detail")
    @GetMapping("/{id}")
    public ReturnRequestResult getItem(@PathVariable("id") Long id) {
        ReturnRequestResult result = returnApplyService.getItem(id);
        if (result == null) {
            throw new ResourceNotFoundException("ReturnRequest", id);
        }
        return result;
    }

    @Operation(summary = "Update return request status")
    @PostMapping("/update/status/{id}")
    public Integer updateStatus(
            @PathVariable("id") Long id, @Valid @RequestBody OrderStatusUpdateParam statusParam) {
        int count = returnApplyService.updateStatus(id, statusParam);
        if (count <= 0) {
            Asserts.fail("Failed to update return request status");
        }
        return count;
    }
}
