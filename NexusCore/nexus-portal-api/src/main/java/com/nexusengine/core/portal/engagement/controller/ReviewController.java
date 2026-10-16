package com.nexusengine.core.portal.engagement.controller;

import com.nexusengine.core.domain.engagement.entity.ProductReview;
import com.nexusengine.core.portal.engagement.dto.MediaUploadResponse;
import com.nexusengine.core.portal.engagement.dto.ReviewParam;
import com.nexusengine.core.portal.engagement.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Tag(name = "Storefront - Product Reviews", description = "Storefront product review management APIs")
@RequestMapping("/portal/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "Get reviews for product")
    @GetMapping("/product/{productId}")
    public Page<ProductReview> list(
            @PathVariable("productId") Long productId,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return reviewService.list(productId, pageNum, pageSize);
    }

    @Operation(summary = "Get replies to a review")
    @GetMapping("/{parentId}/replies")
    public List<ProductReview> listReplies(@PathVariable("parentId") Long parentId) {
        return reviewService.listReplies(parentId);
    }

    @Operation(summary = "Create review or reply")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductReview create(@Valid @RequestBody ReviewParam param) {
        return reviewService.create(param);
    }

    @Operation(summary = "Upload media for review")
    @PostMapping("/upload")
    public MediaUploadResponse upload(@RequestParam("file") MultipartFile file) {
        return reviewService.uploadMedia(file);
    }

    @Operation(summary = "Like a review or reply")
    @PostMapping("/{id}/like")
    public Integer like(@PathVariable("id") Long id) {
        return reviewService.like(id);
    }

    @Operation(summary = "Dislike a review or reply")
    @PostMapping("/{id}/dislike")
    public Integer dislike(@PathVariable("id") Long id) {
        return reviewService.dislike(id);
    }
}
