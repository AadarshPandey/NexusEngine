package com.nexusengine.core.search.controller;

import com.nexusengine.core.common.exception.Asserts;
import com.nexusengine.core.search.domain.ProductDocument;
import com.nexusengine.core.search.domain.ProductRelatedInfo;
import com.nexusengine.core.search.service.ProductSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Elasticsearch Product Management Controller
 */
@RestController
@Tag(name = "Search - Products", description = "Elasticsearch product search APIs")
@RequestMapping("/esProduct")
@RequiredArgsConstructor
public class ProductSearchController {
    private final ProductSearchService esProductService;

    @Operation(summary = "Import all products into Elasticsearch")
    @PostMapping("/importAll")
    public Integer importAllList() {
        return esProductService.importAll();
    }

    @Operation(summary = "Delete product from Elasticsearch")
    @GetMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        esProductService.delete(id);
    }

    @Operation(summary = "Delete products in batch from Elasticsearch")
    @PostMapping("/delete/batch")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestParam("ids") List<Long> ids) {
        esProductService.delete(ids);
    }

    @Operation(summary = "Index product by ID in Elasticsearch")
    @PostMapping("/create/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDocument create(@PathVariable Long id) {
        ProductDocument esProduct = esProductService.create(id);
        if (esProduct == null) {
            Asserts.fail("Failed to index product in Elasticsearch");
        }
        return esProduct;
    }

    @Operation(summary = "Simple product search")
    @GetMapping("/search/simple")
    public Page<ProductDocument> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "0") Integer pageNum,
            @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        return esProductService.search(keyword, pageNum, pageSize);
    }

    @Operation(summary = "Comprehensive product search")
    @Parameter(
            name = "sort",
            description =
                    "Sort field: 0->Relevance; 1->Newest; 2->Sales; 3->Price low to high; 4->Price high to low",
            in = ParameterIn.QUERY,
            schema =
                    @Schema(
                            type = "integer",
                            defaultValue = "0",
                            allowableValues = {"0", "1", "2", "3", "4"}))
    @GetMapping("/search")
    public Page<ProductDocument> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) Long productCategoryId,
            @RequestParam(required = false, defaultValue = "0") Integer pageNum,
            @RequestParam(required = false, defaultValue = "5") Integer pageSize,
            @RequestParam(required = false, defaultValue = "0") Integer sort) {
        return esProductService.search(
                keyword, brandId, productCategoryId, pageNum, pageSize, sort);
    }

    @Operation(summary = "Recommend products by ID")
    @GetMapping("/recommend/{id}")
    public Page<ProductDocument> recommend(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "0") Integer pageNum,
            @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        return esProductService.recommend(id, pageNum, pageSize);
    }

    @Operation(summary = "Search related product facets")
    @GetMapping("/search/relate")
    public ProductRelatedInfo searchRelatedInfo(@RequestParam(required = false) String keyword) {
        return esProductService.searchRelatedInfo(keyword);
    }
}
