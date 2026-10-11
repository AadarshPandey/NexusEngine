package com.nexusengine.core.admin.analytics.controller;

import com.nexusengine.core.admin.analytics.dto.DashboardInfo;
import com.nexusengine.core.admin.analytics.service.DashboardService;
import com.nexusengine.core.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Tag(name = "DashboardController", description = "Admin Dashboard Statistics Management")
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @Operation(summary = "Get real-time dashboard statistics from database")
    @GetMapping("/info")
    public CommonResult<DashboardInfo> getDashboardInfo() {
        DashboardInfo info = dashboardService.getDashboardInfo();
        return CommonResult.success(info);
    }
}
