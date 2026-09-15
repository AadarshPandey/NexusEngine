package com.nexusengine.core.controller;

import org.springframework.web.bind.annotation.RestController;
import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.model.CmsPreferenceArea;
import com.nexusengine.core.service.CmsPreferenceAreaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Represents the CmsPreferenceAreaController component.
 * Provides core functionality and operations for CmsPreferenceAreaController.
 */
@RestController
@Tag(name = "CmsPreferenceAreaController", description = "Cms preference area controller APIs")
@RequestMapping("/preferenceArea")
public class CmsPreferenceAreaController {
    @Autowired
    private CmsPreferenceAreaService preferenceAreaService;

    @Operation(summary = "List all Operation")
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)

    public CommonResult<List<CmsPreferenceArea>> listAll() {
        List<CmsPreferenceArea> preferenceAreaList = preferenceAreaService.listAll();
        return CommonResult.success(preferenceAreaList);
    }
}
