package com.nexusengine.core.portal.catalog.controller;

import lombok.RequiredArgsConstructor;



import org.springframework.web.bind.annotation.RestController;
import com.nexusengine.core.common.api.CommonPage;
import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.domain.catalog.entity.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.nexusengine.core.portal.catalog.dto.CategoryNode;
import com.nexusengine.core.portal.catalog.dto.ProductDetail;
import com.nexusengine.core.portal.catalog.service.ProductService;


/**
 * Represents the ProductController component.
 * Provides core functionality and operations for ProductController.
 */
@RestController("portalProductController")
@Tag(name = "ProductController", description = "Pms portal product controller APIs")
@RequestMapping("/portal/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService portalProductService;

    @Operation(summary = "API Operation")
    @Parameter(name = "sort", description = "Description",
            in= ParameterIn.QUERY,schema = @Schema(type = "integer",defaultValue = "0",allowableValues = {"0","1","2","3","4"}))
    @GetMapping("/search")

    public CommonResult<CommonPage<Product>> search(@RequestParam(required = false) String keyword,
                                                       @RequestParam(required = false) Long brandId,
                                                       @RequestParam(required = false) Long productCategoryId,
                                                       @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                       @RequestParam(required = false, defaultValue = "5") Integer pageSize,
                                                       @RequestParam(required = false, defaultValue = "0") Integer sort) {
        List<Product> productList = portalProductService.search(keyword, brandId, productCategoryId, pageNum, pageSize, sort);
        return CommonResult.success(CommonPage.restPage(productList, productList.size(), pageNum, pageSize));
    }

    @Operation(summary = "Category tree list Operation")
    @GetMapping("/categoryTreeList")

    public CommonResult<List<CategoryNode>> categoryTreeList() {
        List<CategoryNode> list = portalProductService.categoryTreeList();
        return CommonResult.success(list);
    }

    @Operation(summary = "Detail Operation")
    @GetMapping("/detail/{id}")

    public CommonResult<ProductDetail> detail(@PathVariable("id") Long id) {
        ProductDetail productDetail = portalProductService.detail(id);
        return CommonResult.success(productDetail);
    }
}
