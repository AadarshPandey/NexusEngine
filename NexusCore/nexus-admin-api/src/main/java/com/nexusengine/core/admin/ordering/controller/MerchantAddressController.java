package com.nexusengine.core.admin.ordering.controller;

import com.nexusengine.core.admin.ordering.service.MerchantAddressService;
import com.nexusengine.core.domain.ordering.entity.MerchantAddress;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
