package com.nexusengine.core.controller;

import org.springframework.web.bind.annotation.RestController;
import com.nexusengine.core.common.api.CommonPage;
import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.model.PromotionalBanner;
import com.nexusengine.core.service.PromotionalBannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Represents the PromotionalBannerController component.
 * Provides core functionality and operations for PromotionalBannerController.
 */
@RestController
@Tag(name = "PromotionalBannerController", description = "Sms home advertise controller APIs")
@RequestMapping("/home/advertise")
@lombok.RequiredArgsConstructor
public class PromotionalBannerController {
    private final PromotionalBannerService advertiseService;

    @Operation(summary = "Create Operation")
    @PostMapping("/create")

    public CommonResult create(@RequestBody PromotionalBanner advertise) {
        int count = advertiseService.create(advertise);
        if (count > 0)
            return CommonResult.success(count);
        return CommonResult.failed();
    }

    @Operation(summary = "Delete Operation")
    @PostMapping("/delete")

    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        int count = advertiseService.delete(ids);
        if (count > 0)
            return CommonResult.success(count);
        return CommonResult.failed();
    }

    @Operation(summary = "Update status Operation")
    @PostMapping("/update/status/{id}")

    public CommonResult updateStatus(@PathVariable Long id, Integer status) {
        int count = advertiseService.updateStatus(id, status);
        if (count > 0)
            return CommonResult.success(count);
        return CommonResult.failed();
    }

    @Operation(summary = "Get item Operation")
    @GetMapping("/{id}")

    public CommonResult<PromotionalBanner> getItem(@PathVariable Long id) {
        PromotionalBanner advertise = advertiseService.getItem(id);
        return CommonResult.success(advertise);
    }

    @Operation(summary = "Update Operation")
    @PostMapping("/update/{id}")

    public CommonResult update(@PathVariable Long id, @RequestBody PromotionalBanner advertise) {
        int count = advertiseService.update(id, advertise);
        if (count > 0)
            return CommonResult.success(count);
        return CommonResult.failed();
    }

    @Operation(summary = "List Operation")
    @GetMapping("/list")

    public CommonResult<CommonPage<PromotionalBanner>> list(@RequestParam(value = "name", required = false) String name,
                                                           @RequestParam(value = "type", required = false) Integer type,
                                                           @RequestParam(value = "endTime", required = false) String endTime,
                                                           @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                           @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<PromotionalBanner> advertiseList = advertiseService.list(name, type, endTime, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(advertiseList));
    }
}
