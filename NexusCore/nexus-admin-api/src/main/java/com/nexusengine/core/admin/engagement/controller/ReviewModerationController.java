package com.nexusengine.core.admin.engagement.controller;

import com.nexusengine.core.admin.engagement.dto.ReviewModerationDTO;
import com.nexusengine.core.admin.engagement.service.ReviewModerationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Engagement - Review Moderation", description = "Admin UGC product review moderation APIs")
@RequestMapping("/api/admin/engagement/reviews")
@RequiredArgsConstructor
public class ReviewModerationController {

    private final ReviewModerationService reviewModerationService;

    @Operation(summary = "List product reviews with filtering")
    @GetMapping
    public Page<ReviewModerationDTO> list(
            @RequestParam(value = "productId", required = false) Long productId,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "keyword", required = false) String keyword,
            @PageableDefault(size = 10) Pageable pageable) {
        return reviewModerationService.list(productId, status, keyword, pageable);
    }

    @Operation(summary = "Update review moderation status")
    @PutMapping("/{id}/moderation")
    @ResponseStatus(HttpStatus.OK)
    public void updateModerationStatus(
            @PathVariable("id") Long id,
            @RequestParam("status") Integer status) {
        reviewModerationService.updateModerationStatus(id, status);
    }

    @Operation(summary = "Delete inappropriate review")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        reviewModerationService.delete(id);
    }
}
