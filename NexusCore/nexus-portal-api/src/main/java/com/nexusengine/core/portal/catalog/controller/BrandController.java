package com.nexusengine.core.portal.catalog.controller;

import com.nexusengine.core.common.exception.ResourceNotFoundException;
import com.nexusengine.core.domain.catalog.entity.Brand;
import com.nexusengine.core.portal.catalog.dto.ProductSummaryResponse;
import com.nexusengine.core.portal.catalog.mapper.PortalProductMapper;
import com.nexusengine.core.portal.catalog.service.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("portalBrandController")
@Tag(name = "Storefront - Brands", description = "Storefront brand catalog APIs")
@RequestMapping("/portal/brand")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService portalBrandService;
    private final PortalProductMapper portalProductMapper;

    @Operation(summary = "List recommended brands")
    @GetMapping("/recommendList")
    public List<Brand> recommendList(
            @RequestParam(value = "pageSize", defaultValue = "6") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        return portalBrandService.recommendList(pageNum, pageSize);
    }

    @Operation(summary = "Get brand detail")
    @GetMapping("/detail/{brandId}")
    public Brand detail(@PathVariable("brandId") Long brandId) {
        Brand brand = portalBrandService.detail(brandId);
        if (brand == null) {
            throw new ResourceNotFoundException("Brand", brandId);
        }
        return brand;
    }

    @Operation(summary = "List products by brand")
    @GetMapping("/productList")
    public Page<ProductSummaryResponse> productList(
            @RequestParam("brandId") Long brandId,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "6") Integer pageSize) {
        return portalBrandService
                .productList(brandId, pageNum, pageSize)
                .map(portalProductMapper::toSummary);
    }
}
