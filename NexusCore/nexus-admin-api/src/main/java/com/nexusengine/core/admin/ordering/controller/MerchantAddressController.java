package com.nexusengine.core.admin.ordering.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RestController;

import com.nexusengine.core.admin.ordering.service.MerchantAddressService;
import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.domain.ordering.entity.MerchantAddress;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


/**
 * Represents the MerchantAddressController component.
 * Provides core functionality and operations for MerchantAddressController.
 */
@RestController
@Tag(name = "MerchantAddressController", description = "Oms company address controller APIs")
@RequestMapping("/companyAddress")
@RequiredArgsConstructor
public class MerchantAddressController {
    private final MerchantAddressService companyAddressService;

    @Operation(summary = "List Operation")
    @GetMapping("/list")

    public CommonResult<List<MerchantAddress>> list() {
        List<MerchantAddress> companyAddressList = companyAddressService.list();
        return CommonResult.success(companyAddressList);
    }
}
