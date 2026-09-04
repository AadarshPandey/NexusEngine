package com.nexusengine.core.controller;

import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.model.OmsCompanyAddress;
import com.nexusengine.core.service.OmsCompanyAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/10/18.
 */
@Controller
@Tag(name = "OmsCompanyAddressController", description = "Oms company address controller APIs")
@RequestMapping("/companyAddress")
public class OmsCompanyAddressController {
    @Autowired
    private OmsCompanyAddressService companyAddressService;

    @Operation(summary = "List Operation")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<OmsCompanyAddress>> list() {
        List<OmsCompanyAddress> companyAddressList = companyAddressService.list();
        return CommonResult.success(companyAddressList);
    }
}
