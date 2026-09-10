package com.nexusengine.core.controller;

import org.springframework.web.bind.annotation.RestController;
import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.model.CmsPrefrenceArea;
import com.nexusengine.core.service.CmsPrefrenceAreaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Represents the CmsPrefrenceAreaController component.
 * Provides core functionality and operations for CmsPrefrenceAreaController.
 */
@RestController
@Tag(name = "CmsPrefrenceAreaController", description = "Cms prefrence area controller APIs")
@RequestMapping("/prefrenceArea")
public class CmsPrefrenceAreaController {
    @Autowired
    private CmsPrefrenceAreaService prefrenceAreaService;

    @Operation(summary = "List all Operation")
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)

    public CommonResult<List<CmsPrefrenceArea>> listAll() {
        List<CmsPrefrenceArea> prefrenceAreaList = prefrenceAreaService.listAll();
        return CommonResult.success(prefrenceAreaList);
    }
}
