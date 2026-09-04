package com.nexusengine.core.controller;

import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.model.UmsMemberLevel;
import com.nexusengine.core.service.UmsMemberLevelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/4/26.
 */
@Controller
@Tag(name = "UmsMemberLevelController", description = "Ums member level controller APIs")
@RequestMapping("/memberLevel")
public class UmsMemberLevelController {
    @Autowired
    private UmsMemberLevelService memberLevelService;

    @Operation(summary = "List Operation")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<UmsMemberLevel>> list(@RequestParam("defaultStatus") Integer defaultStatus) {
        List<UmsMemberLevel> memberLevelList = memberLevelService.list(defaultStatus);
        return CommonResult.success(memberLevelList);
    }
}
