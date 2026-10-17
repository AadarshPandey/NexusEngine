package com.nexusengine.core.portal.home.controller;

import com.nexusengine.core.domain.catalog.entity.Category;
import com.nexusengine.core.portal.catalog.dto.ProductSummaryResponse;
import com.nexusengine.core.portal.catalog.mapper.PortalProductMapper;
import com.nexusengine.core.portal.home.dto.HomeContentResult;
import com.nexusengine.core.portal.home.service.HomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Storefront - Home", description = "Storefront home page APIs")
@RequestMapping("/api/store/home")
@RequiredArgsConstructor
public class HomeController {
    private final HomeService homeService;
    private final PortalProductMapper portalProductMapper;

    @Operation(summary = "Get home page content")
    @GetMapping
    public HomeContentResult content() {
        return homeService.content();
    }

    @Operation(summary = "List recommended products")
    @GetMapping("/recommended-products")
    public List<ProductSummaryResponse> recommendProductList(
            @PageableDefault(size = 4) Pageable pageable) {
        return portalProductMapper.toSummaryList(
                homeService.recommendProductList(
                        pageable.getPageSize(), pageable.getPageNumber() + 1));
    }

    @Operation(summary = "List product categories by parent")
    @GetMapping("/categories/{parentId}")
    public List<Category> getProductCateList(@PathVariable("parentId") Long parentId) {
        return homeService.getProductCateList(parentId);
    }

    @Operation(summary = "List hot products")
    @GetMapping("/hot-products")
    public List<ProductSummaryResponse> hotProductList(
            @PageableDefault(size = 6) Pageable pageable) {
        return portalProductMapper.toSummaryList(
                homeService.hotProductList(
                        pageable.getPageNumber() + 1, pageable.getPageSize()));
    }

    @Operation(summary = "List new arrival products")
    @GetMapping("/new-products")
    public List<ProductSummaryResponse> newProductList(
            @PageableDefault(size = 6) Pageable pageable) {
        return portalProductMapper.toSummaryList(
                homeService.newProductList(
                        pageable.getPageNumber() + 1, pageable.getPageSize()));
    }

    @Operation(summary = "Record banner click")
    @PostMapping("/banners/{id}/click")
    public void recordBannerClick(@PathVariable("id") Long id) {
        homeService.recordBannerClick(id);
    }
}
