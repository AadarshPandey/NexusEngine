package com.nexusengine.core.admin.analytics.controller;

import com.nexusengine.core.admin.analytics.dto.DashboardInfo;
import com.nexusengine.core.admin.analytics.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Analytics - Dashboard", description = "Admin dashboard statistics APIs")
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @Operation(summary = "Get real-time dashboard statistics")
    @GetMapping("/info")
    public DashboardInfo getDashboardInfo() {
        return dashboardService.getDashboardInfo();
    }
}
