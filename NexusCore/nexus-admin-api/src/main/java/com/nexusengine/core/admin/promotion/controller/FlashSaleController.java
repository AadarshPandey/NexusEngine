package com.nexusengine.core.admin.promotion.controller;

import com.nexusengine.core.admin.promotion.service.FlashSaleService;
import com.nexusengine.core.common.exception.Asserts;
import com.nexusengine.core.common.exception.ResourceNotFoundException;
import com.nexusengine.core.domain.promotion.entity.FlashSale;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

@RestController
@Tag(name = "Promotion - Flash Sales", description = "Admin flash sale management APIs")
@RequestMapping("/flash")
@RequiredArgsConstructor
public class FlashSaleController {
    private final FlashSaleService flashPromotionService;

    @Operation(summary = "Create flash sale")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Integer create(@Valid @RequestBody FlashSale flashPromotion) {
        int count = flashPromotionService.create(flashPromotion);
        if (count <= 0) {
            Asserts.fail("Failed to create flash sale");
        }
        return count;
    }

    @Operation(summary = "Update flash sale")
    @PostMapping("/update/{id}")
    public Integer update(
            @PathVariable("id") Long id, @Valid @RequestBody FlashSale flashPromotion) {
        int count = flashPromotionService.update(id, flashPromotion);
        if (count <= 0) {
            Asserts.fail("Failed to update flash sale");
        }
        return count;
    }

    @Operation(summary = "Delete flash sale")
    @PostMapping("/delete/{id}")
    public Integer delete(@PathVariable("id") Long id) {
        int count = flashPromotionService.delete(id);
        if (count <= 0) {
            Asserts.fail("Failed to delete flash sale");
        }
        return count;
    }

    @Operation(summary = "Update flash sale status")
    @PostMapping("/update/status/{id}")
    public Integer updateStatus(
            @PathVariable("id") Long id, @RequestParam("status") Integer status) {
        int count = flashPromotionService.updateStatus(id, status);
        if (count <= 0) {
            Asserts.fail("Failed to update flash sale status");
        }
        return count;
    }

    @Operation(summary = "Get flash sale by ID")
    @GetMapping("/{id}")
    public FlashSale getItem(@PathVariable("id") Long id) {
        FlashSale flashPromotion = flashPromotionService.getItem(id);
        if (flashPromotion == null) {
            throw new ResourceNotFoundException("FlashSale", id);
        }
        return flashPromotion;
    }

    @Operation(summary = "List flash sales with pagination")
    @GetMapping("/list")
    public Page<FlashSale> list(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        return flashPromotionService.list(keyword, pageNum, pageSize);
    }
}
