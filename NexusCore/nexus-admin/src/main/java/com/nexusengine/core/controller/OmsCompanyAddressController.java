package com.nexusengine.core.controller;

import org.springframework.web.bind.annotation.RestController;
import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.model.OmsCompanyAddress;
import com.nexusengine.core.service.OmsCompanyAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Represents the OmsCompanyAddressController component.
 * Provides core functionality and operations for OmsCompanyAddressController.
 */
@RestController
@Tag(name = "OmsCompanyAddressController", description = "Oms company address controller APIs")
@RequestMapping("/companyAddress")
@lombok.RequiredArgsConstructor
public class OmsCompanyAddressController {
    private final OmsCompanyAddressService companyAddressService;

    @Operation(summary = "List Operation")
    @GetMapping("/list")

    public CommonResult<List<OmsCompanyAddress>> list() {
        List<OmsCompanyAddress> companyAddressList = companyAddressService.list();
        return CommonResult.success(companyAddressList);
    }
}
