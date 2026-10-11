package com.nexusengine.core.portal.engagement.controller;

import lombok.RequiredArgsConstructor;

import com.nexusengine.core.domain.catalog.entity.Product;


import com.nexusengine.core.common.api.CommonPage;
import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.domain.engagement.entity.ProductReview;
import com.nexusengine.core.portal.engagement.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;
import com.nexusengine.core.portal.engagement.dto.ReviewParam;


@RestController
@Tag(name = "ReviewController", description = "Product Review Management")
@RequestMapping("/portal/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "Get reviews for product")
    @GetMapping("/product/{productId}")
    public CommonResult<CommonPage<ProductReview>> list(
            @PathVariable("productId") Long productId,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return CommonResult.success(reviewService.list(productId, pageNum, pageSize));
    }

    @Operation(summary = "Get replies to a review")
    @GetMapping("/{parentId}/replies")
    public CommonResult<List<ProductReview>> listReplies(@PathVariable("parentId") Long parentId) {
        return CommonResult.success(reviewService.listReplies(parentId));
    }

    @Operation(summary = "Create review or reply")
    @PostMapping("/create")
    public CommonResult<ProductReview> create(@RequestBody ReviewParam param) {
        return CommonResult.success(reviewService.create(param));
    }

    @Operation(summary = "Upload media for review")
    @PostMapping("/upload")
    public CommonResult<Map<String, String>> upload(@RequestParam("file") MultipartFile file) {
        return CommonResult.success(reviewService.uploadMedia(file));
    }

    @Operation(summary = "Like a review or reply")
    @PostMapping("/{id}/like")
    public CommonResult<Integer> like(@PathVariable("id") Long id) {
        return CommonResult.success(reviewService.like(id));
    }

    @PostMapping("/{id}/dislike")
    public CommonResult<Integer> dislike(@PathVariable("id") Long id) {
        return CommonResult.success(reviewService.dislike(id));
    }
}
