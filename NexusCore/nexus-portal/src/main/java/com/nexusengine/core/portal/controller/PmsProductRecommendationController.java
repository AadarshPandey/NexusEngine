package com.nexusengine.core.portal.controller;

import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.model.PmsProduct;
import com.nexusengine.core.portal.service.PmsProductSemanticSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Tag(name = "PmsProductRecommendationController", description = "AI Product Recommendation APIs")
@RequestMapping("/portal/recommendation")
public class PmsProductRecommendationController {

    @Autowired
    private PmsProductSemanticSearchService semanticSearchService;

    @Operation(summary = "Generate product embeddings (Admin/Cron trigger)")
    @RequestMapping(value = "/generateEmbeddings", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Integer> generateEmbeddings() {
        int count = semanticSearchService.generateAllProductEmbeddings();
        return CommonResult.success(count, "Successfully generated embeddings for " + count + " products.");
    }

    @Operation(summary = "Get AI recommendations based on context")
    @RequestMapping(value = "/products", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<PmsProduct>> semanticSearch(
            @RequestParam(required = false) Long memberId,
            @RequestParam String searchContext) {
        List<PmsProduct> productList = semanticSearchService.semanticSearch(memberId, searchContext);
        return CommonResult.success(productList);
    }
}
