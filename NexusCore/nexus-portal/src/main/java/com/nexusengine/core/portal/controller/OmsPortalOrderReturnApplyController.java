package com.nexusengine.core.portal.controller;

import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.portal.domain.OmsOrderReturnApplyParam;
import com.nexusengine.core.portal.service.OmsPortalOrderReturnApplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Auto-generated documentation
 * Created by macro on 2018/10/17.
 */
@Controller
@Tag(name = "OmsPortalOrderReturnApplyController",description = "Oms portal order return apply controller APIs")
@RequestMapping("/portal/returnApply")
public class OmsPortalOrderReturnApplyController {
    @Autowired
    private OmsPortalOrderReturnApplyService returnApplyService;

    @Operation(summary = "Create Operation")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@RequestBody OmsOrderReturnApplyParam returnApply) {
        int count = returnApplyService.create(returnApply);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}
