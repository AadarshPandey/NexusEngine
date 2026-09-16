package com.nexusengine.core.controller;

import org.springframework.web.bind.annotation.RestController;
import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.model.UmsMemberLevel;
import com.nexusengine.core.service.UmsMemberLevelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Represents the UmsMemberLevelController component.
 * Provides core functionality and operations for UmsMemberLevelController.
 */
@RestController
@Tag(name = "UmsMemberLevelController", description = "Ums member level controller APIs")
@RequestMapping("/memberLevel")
@lombok.RequiredArgsConstructor
public class UmsMemberLevelController {
    private final UmsMemberLevelService memberLevelService;

    @Operation(summary = "List Operation")
    @GetMapping("/list")

    public CommonResult<List<UmsMemberLevel>> list(@RequestParam("defaultStatus") Integer defaultStatus) {
        List<UmsMemberLevel> memberLevelList = memberLevelService.list(defaultStatus);
        return CommonResult.success(memberLevelList);
    }
}
