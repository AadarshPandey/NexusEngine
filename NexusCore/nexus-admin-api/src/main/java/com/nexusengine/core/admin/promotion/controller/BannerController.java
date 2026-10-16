package com.nexusengine.core.admin.promotion.controller;

import com.nexusengine.core.admin.promotion.service.BannerService;
import com.nexusengine.core.common.exception.Asserts;
import com.nexusengine.core.common.exception.ResourceNotFoundException;
import com.nexusengine.core.domain.promotion.entity.Banner;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Promotion - Banners", description = "Admin promotional banner management APIs")
@RequestMapping("/api/admin/promotion/banners")
@RequiredArgsConstructor
public class BannerController {
    private final BannerService advertiseService;

    @Operation(summary = "Create banner")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Integer create(@Valid @RequestBody Banner advertise) {
        int count = advertiseService.create(advertise);
        if (count <= 0) {
            Asserts.fail("Failed to create banner");
        }
        return count;
    }

    @Operation(summary = "Delete banners")
    @DeleteMapping
    public Integer delete(@RequestParam("ids") List<Long> ids) {
        int count = advertiseService.delete(ids);
        if (count <= 0) {
            Asserts.fail("Failed to delete banners");
        }
        return count;
    }

    @Operation(summary = "Update banner status")
    @PatchMapping("/{id}/status")
    public Integer updateStatus(
            @PathVariable("id") Long id, @RequestParam("status") Integer status) {
        int count = advertiseService.updateStatus(id, status);
        if (count <= 0) {
            Asserts.fail("Failed to update banner status");
        }
        return count;
    }

    @Operation(summary = "Get banner by ID")
    @GetMapping("/{id}")
    public Banner getItem(@PathVariable("id") Long id) {
        Banner advertise = advertiseService.getItem(id);
        if (advertise == null) {
            throw new ResourceNotFoundException("Banner", id);
        }
        return advertise;
    }

    @Operation(summary = "Update banner")
    @PutMapping("/{id}")
    public Integer update(@PathVariable("id") Long id, @Valid @RequestBody Banner advertise) {
        int count = advertiseService.update(id, advertise);
        if (count <= 0) {
            Asserts.fail("Failed to update banner");
        }
        return count;
    }

    @Operation(summary = "List banners")
    @GetMapping
    public List<Banner> list(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "type", required = false) Integer type,
            @RequestParam(value = "endTime", required = false) String endTime,
            @PageableDefault(size = 5) Pageable pageable) {
        return advertiseService.list(
                name, type, endTime, pageable.getPageSize(), pageable.getPageNumber() + 1);
    }
}
