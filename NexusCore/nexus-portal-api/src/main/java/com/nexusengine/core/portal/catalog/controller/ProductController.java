package com.nexusengine.core.portal.catalog.controller;

import com.nexusengine.core.common.exception.ResourceNotFoundException;
import com.nexusengine.core.domain.catalog.entity.Product;
import com.nexusengine.core.portal.catalog.dto.CategoryNode;
import com.nexusengine.core.portal.catalog.dto.ProductDetail;
import com.nexusengine.core.portal.catalog.dto.ProductSummaryResponse;
import com.nexusengine.core.portal.catalog.mapper.PortalProductMapper;
import com.nexusengine.core.portal.catalog.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("portalProductController")
@Tag(name = "Storefront - Products", description = "Storefront product catalog APIs")
@RequestMapping("/portal/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService portalProductService;
    private final PortalProductMapper portalProductMapper;

    @Operation(summary = "Search products")
    @Parameter(
            name = "sort",
            description = "Sort order",
            in = ParameterIn.QUERY,
            schema =
                    @Schema(
                            type = "integer",
                            defaultValue = "0",
                            allowableValues = {"0", "1", "2", "3", "4"}))
    @GetMapping("/search")
    public List<ProductSummaryResponse> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) Long productCategoryId,
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "5") Integer pageSize,
            @RequestParam(required = false, defaultValue = "0") Integer sort) {
        List<Product> productList =
                portalProductService.search(
                        keyword, brandId, productCategoryId, pageNum, pageSize, sort);
        return portalProductMapper.toSummaryList(productList);
    }

    @Operation(summary = "Get category tree")
    @GetMapping("/categoryTreeList")
    public List<CategoryNode> categoryTreeList() {
        return portalProductService.categoryTreeList();
    }

    @Operation(summary = "Get product detail")
    @GetMapping("/detail/{id}")
    public ProductDetail detail(@PathVariable("id") Long id) {
        ProductDetail productDetail = portalProductService.detail(id);
        if (productDetail == null) {
            throw new ResourceNotFoundException("Product", id);
        }
        return productDetail;
    }
}
