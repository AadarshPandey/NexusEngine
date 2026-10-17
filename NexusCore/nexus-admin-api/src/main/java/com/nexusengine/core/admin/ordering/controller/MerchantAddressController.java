package com.nexusengine.core.admin.ordering.controller;

import com.nexusengine.core.admin.ordering.service.MerchantAddressService;
import com.nexusengine.core.domain.ordering.entity.MerchantAddress;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Ordering - Merchant Addresses", description = "Admin merchant address management APIs")
@RequestMapping("/api/admin/ordering/merchant-addresses")
@RequiredArgsConstructor
public class MerchantAddressController {
    private final MerchantAddressService companyAddressService;

    @Operation(summary = "List merchant addresses")
    @GetMapping
    public List<MerchantAddress> list() {
        return companyAddressService.list();
    }

    @Operation(summary = "Get merchant address by ID")
    @GetMapping("/{id}")
    public MerchantAddress getById(@PathVariable("id") Long id) {
        return companyAddressService.getById(id);
    }

    @Operation(summary = "Create merchant address")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MerchantAddress create(@RequestBody MerchantAddress address) {
        return companyAddressService.create(address);
    }

    @Operation(summary = "Update merchant address")
    @PutMapping("/{id}")
    public MerchantAddress update(@PathVariable("id") Long id, @RequestBody MerchantAddress address) {
        return companyAddressService.update(id, address);
    }

    @Operation(summary = "Delete merchant address")
    @DeleteMapping("/{id}")
    public int delete(@PathVariable("id") Long id) {
        return companyAddressService.delete(id);
    }
}

