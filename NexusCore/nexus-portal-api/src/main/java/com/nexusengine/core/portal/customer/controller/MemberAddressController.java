package com.nexusengine.core.portal.customer.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RestController;

import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.domain.customer.entity.MemberAddress;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.nexusengine.core.portal.customer.service.MemberAddressService;


/**
 * Represents the MemberAddressController component.
 * Provides core functionality and operations for MemberAddressController.
 */
@RestController
@Tag(name = "MemberAddressController", description = "Ums member receive address controller APIs")
@RequestMapping("/portal/member/address")
@RequiredArgsConstructor
public class MemberAddressController {
    private final MemberAddressService memberReceiveAddressService;

    @Operation(summary = "Add Operation")
    @PostMapping("/add")

    public CommonResult add(@RequestBody MemberAddress address) {
        int count = memberReceiveAddressService.add(address);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "Delete Operation")
    @PostMapping("/delete/{id}")

    public CommonResult delete(@PathVariable("id") Long id) {
        int count = memberReceiveAddressService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "Update Operation")
    @PostMapping("/update/{id}")

    public CommonResult update(@PathVariable("id") Long id, @RequestBody MemberAddress address) {
        int count = memberReceiveAddressService.update(id, address);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "List Operation")
    @GetMapping("/list")

    public CommonResult<List<MemberAddress>> list() {
        List<MemberAddress> addressList = memberReceiveAddressService.list();
        return CommonResult.success(addressList);
    }

    @Operation(summary = "Get item Operation")
    @GetMapping("/{id}")

    public CommonResult<MemberAddress> getItem(@PathVariable("id") Long id) {
        MemberAddress address = memberReceiveAddressService.getItem(id);
        return CommonResult.success(address);
    }
}
