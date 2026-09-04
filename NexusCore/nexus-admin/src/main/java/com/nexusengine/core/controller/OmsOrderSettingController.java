package com.nexusengine.core.controller;

import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.model.OmsOrderSetting;
import com.nexusengine.core.service.OmsOrderSettingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Auto-generated documentation
 * Created by macro on 2018/10/16.
 */
@Controller
@Tag(name = "OmsOrderSettingController", description = "Oms order setting controller APIs")
@RequestMapping("/orderSetting")
public class OmsOrderSettingController {
    @Autowired
    private OmsOrderSettingService orderSettingService;

    @Operation(summary = "Get item Operation")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<OmsOrderSetting> getItem(@PathVariable Long id) {
        OmsOrderSetting orderSetting = orderSettingService.getItem(id);
        return CommonResult.success(orderSetting);
    }

    @Operation(summary = "Update Operation")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable Long id, @RequestBody OmsOrderSetting orderSetting) {
        int count = orderSettingService.update(id,orderSetting);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}
