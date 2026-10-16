package com.nexusengine.core.portal.ordering.controller;

import com.nexusengine.core.common.exception.Asserts;
import com.nexusengine.core.domain.ordering.entity.ReturnReason;
import com.nexusengine.core.domain.ordering.repository.ReturnReasonRepository;
import com.nexusengine.core.portal.ordering.dto.ReturnApplyParam;
import com.nexusengine.core.portal.ordering.service.ReturnRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController("portalReturnRequestController")
@Tag(name = "Storefront - Return Requests", description = "Storefront order return request APIs")
@RequestMapping("/portal/returnApply")
@RequiredArgsConstructor
public class ReturnRequestController {
    private final ReturnRequestService returnApplyService;
    private final ReturnReasonRepository returnReasonRepository;

    @Operation(summary = "Create return request")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Integer create(@Valid @RequestBody ReturnApplyParam returnApply) {
        int count = returnApplyService.create(returnApply);
        if (count <= 0) {
            Asserts.fail("Failed to create return request");
        }
        return count;
    }

    @Operation(summary = "List active return reasons")
    @GetMapping("/reasons")
    public List<ReturnReason> listReasons() {
        return returnReasonRepository.findAll().stream()
                .filter(r -> Boolean.TRUE.equals(r.getStatus()))
                .collect(Collectors.toList());
    }
}
