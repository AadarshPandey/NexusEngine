package com.nexusengine.core.controller;

import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.dto.DashboardInfo;
import com.nexusengine.core.service.DashboardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "DashboardController", description = "Admin Dashboard Statistics Management")
@RequestMapping("/dashboard")
@lombok.RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @Operation(summary = "Get real-time dashboard statistics from database")
    @GetMapping("/info")
    public CommonResult<DashboardInfo> getDashboardInfo() {
        DashboardInfo info = dashboardService.getDashboardInfo();
        return CommonResult.success(info);
    }
}
