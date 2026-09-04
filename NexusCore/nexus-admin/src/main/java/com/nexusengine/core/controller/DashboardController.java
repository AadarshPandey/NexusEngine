package com.nexusengine.core.controller;

import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.dto.DashboardInfo;
import com.nexusengine.core.service.DashboardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "DashboardController", description = "Admin Dashboard Statistics Management")
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @Operation(summary = "Get real-time dashboard statistics from database")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public CommonResult<DashboardInfo> getDashboardInfo() {
        DashboardInfo info = dashboardService.getDashboardInfo();
        return CommonResult.success(info);
    }
}
