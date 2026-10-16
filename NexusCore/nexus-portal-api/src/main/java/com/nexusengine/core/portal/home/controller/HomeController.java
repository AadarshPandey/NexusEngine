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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Storefront - Home", description = "Storefront home page APIs")
@RequestMapping("/portal/home")
@RequiredArgsConstructor
public class HomeController {
    private final HomeService homeService;
    private final PortalProductMapper portalProductMapper;

    @Operation(summary = "Get home page content")
    @GetMapping("/body")
    public HomeContentResult content() {
        return homeService.content();
    }

    @Operation(summary = "List recommended products")
    @GetMapping("/recommendProductList")
    public List<ProductSummaryResponse> recommendProductList(
            @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        return portalProductMapper.toSummaryList(
                homeService.recommendProductList(pageSize, pageNum));
    }

    @Operation(summary = "List product categories by parent")
    @GetMapping("/productCateList/{parentId}")
    public List<Category> getProductCateList(@PathVariable("parentId") Long parentId) {
        return homeService.getProductCateList(parentId);
    }

    @Operation(summary = "List hot products")
    @GetMapping("/hotProductList")
    public List<ProductSummaryResponse> hotProductList(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "6") Integer pageSize) {
        return portalProductMapper.toSummaryList(homeService.hotProductList(pageNum, pageSize));
    }

    @Operation(summary = "List new arrival products")
    @GetMapping("/newProductList")
    public List<ProductSummaryResponse> newProductList(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "6") Integer pageSize) {
        return portalProductMapper.toSummaryList(homeService.newProductList(pageNum, pageSize));
    }
}
