package com.nexusengine.core.portal.customer.controller;

import com.nexusengine.core.common.exception.Asserts;
import com.nexusengine.core.common.exception.ResourceNotFoundException;
import com.nexusengine.core.domain.customer.entity.MemberAddress;
import com.nexusengine.core.portal.customer.service.MemberAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Storefront - Member Addresses", description = "Storefront member shipping address APIs")
@RequestMapping("/portal/member/address")
@RequiredArgsConstructor
public class MemberAddressController {
    private final MemberAddressService memberReceiveAddressService;

    @Operation(summary = "Add shipping address")
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Integer add(@Valid @RequestBody MemberAddress address) {
        int count = memberReceiveAddressService.add(address);
        if (count <= 0) {
            Asserts.fail("Failed to add shipping address");
        }
        return count;
    }

    @Operation(summary = "Delete shipping address")
    @PostMapping("/delete/{id}")
    public Integer delete(@PathVariable("id") Long id) {
        int count = memberReceiveAddressService.delete(id);
        if (count <= 0) {
            Asserts.fail("Failed to delete shipping address");
        }
        return count;
    }

    @Operation(summary = "Update shipping address")
    @PostMapping("/update/{id}")
    public Integer update(
            @PathVariable("id") Long id, @Valid @RequestBody MemberAddress address) {
        int count = memberReceiveAddressService.update(id, address);
        if (count <= 0) {
            Asserts.fail("Failed to update shipping address");
        }
        return count;
    }

    @Operation(summary = "List member shipping addresses")
    @GetMapping("/list")
    public List<MemberAddress> list() {
        return memberReceiveAddressService.list();
    }

    @Operation(summary = "Get shipping address by ID")
    @GetMapping("/{id}")
    public MemberAddress getItem(@PathVariable("id") Long id) {
        MemberAddress address = memberReceiveAddressService.getItem(id);
        if (address == null) {
            throw new ResourceNotFoundException("MemberAddress", id);
        }
        return address;
    }
}
