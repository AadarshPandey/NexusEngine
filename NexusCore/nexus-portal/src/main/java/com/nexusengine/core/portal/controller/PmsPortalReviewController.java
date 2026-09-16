package com.nexusengine.core.portal.controller;

import com.nexusengine.core.common.api.CommonPage;
import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.model.PmsReview;
import com.nexusengine.core.portal.domain.ReviewParam;
import com.nexusengine.core.portal.service.PmsPortalReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "PmsPortalReviewController", description = "Product Review Management")
@RequestMapping("/portal/review")
@lombok.RequiredArgsConstructor
public class PmsPortalReviewController {

    private final PmsPortalReviewService reviewService;

    @Operation(summary = "Get reviews for product")
    @GetMapping("/product/{productId}")
    public CommonResult<CommonPage<PmsReview>> list(
            @PathVariable Long productId,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return CommonResult.success(reviewService.list(productId, pageNum, pageSize));
    }

    @Operation(summary = "Get replies to a review")
    @GetMapping("/{parentId}/replies")
    public CommonResult<List<PmsReview>> listReplies(@PathVariable Long parentId) {
        return CommonResult.success(reviewService.listReplies(parentId));
    }

    @Operation(summary = "Create review or reply")
    @PostMapping("/create")
    public CommonResult<PmsReview> create(@RequestBody ReviewParam param) {
        return CommonResult.success(reviewService.create(param));
    }

    @Operation(summary = "Upload media for review")
    @PostMapping("/upload")
    public CommonResult<Map<String, String>> upload(@RequestParam("file") MultipartFile file) {
        return CommonResult.success(reviewService.uploadMedia(file));
    }
}
