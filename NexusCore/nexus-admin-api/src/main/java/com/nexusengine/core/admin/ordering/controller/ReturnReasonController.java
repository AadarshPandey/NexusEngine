package com.nexusengine.core.admin.ordering.controller;

import com.nexusengine.core.admin.ordering.service.ReturnReasonService;
import com.nexusengine.core.common.exception.Asserts;
import com.nexusengine.core.common.exception.ResourceNotFoundException;
import com.nexusengine.core.domain.ordering.entity.ReturnReason;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Ordering - Return Reasons", description = "Admin order return reason management APIs")
@RequestMapping("/returnReason")
@RequiredArgsConstructor
public class ReturnReasonController {
    private final ReturnReasonService orderReturnReasonService;

    @Operation(summary = "Create return reason")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Integer create(@Valid @RequestBody ReturnReason returnReason) {
        int count = orderReturnReasonService.create(returnReason);
        if (count <= 0) {
            Asserts.fail("Failed to create return reason");
        }
        return count;
    }

    @Operation(summary = "Update return reason")
    @PostMapping("/update/{id}")
    public Integer update(
            @PathVariable("id") Long id, @Valid @RequestBody ReturnReason returnReason) {
        int count = orderReturnReasonService.update(id, returnReason);
        if (count <= 0) {
            Asserts.fail("Failed to update return reason");
        }
        return count;
    }

    @Operation(summary = "Delete return reasons")
    @PostMapping("/delete")
    public Integer delete(@RequestParam("ids") List<Long> ids) {
        int count = orderReturnReasonService.delete(ids);
        if (count <= 0) {
            Asserts.fail("Failed to delete return reasons");
        }
        return count;
    }

    @Operation(summary = "List return reasons")
    @GetMapping("/list")
    public List<ReturnReason> list(
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        return orderReturnReasonService.list(pageSize, pageNum);
    }

    @Operation(summary = "Get return reason by ID")
    @GetMapping("/{id}")
    public ReturnReason getItem(@PathVariable("id") Long id) {
        ReturnReason reason = orderReturnReasonService.getItem(id);
        if (reason == null) {
            throw new ResourceNotFoundException("ReturnReason", id);
        }
        return reason;
    }

    @Operation(summary = "Update return reason status")
    @PostMapping("/update/status")
    public Integer updateStatus(
            @RequestParam(value = "status") Integer status, @RequestParam("ids") List<Long> ids) {
        int count = orderReturnReasonService.updateStatus(ids, status);
        if (count <= 0) {
            Asserts.fail("Failed to update return reason status");
        }
        return count;
    }
}
