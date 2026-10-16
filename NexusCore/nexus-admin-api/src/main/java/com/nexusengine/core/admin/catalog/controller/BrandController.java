package com.nexusengine.core.admin.catalog.controller;

import com.nexusengine.core.admin.catalog.dto.BrandParam;
import com.nexusengine.core.admin.catalog.dto.BrandResponse;
import com.nexusengine.core.admin.catalog.mapper.BrandMapper;
import com.nexusengine.core.admin.catalog.service.BrandService;
import com.nexusengine.core.common.exception.Asserts;
import com.nexusengine.core.common.exception.ResourceNotFoundException;
import com.nexusengine.core.domain.catalog.entity.Brand;
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

@RestController("adminBrandController")
@Tag(name = "Catalog - Brands", description = "Admin brand management APIs")
@RequestMapping("/brand")
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;
    private final BrandMapper brandMapper;

    @Operation(summary = "List all brands")
    @GetMapping("/listAll")
    public Page<BrandResponse> getList() {
        return brandService.listAllBrand().map(brandMapper::toResponse);
    }

    @Operation(summary = "Create brand")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Integer create(@Valid @RequestBody BrandParam brand) {
        int count = brandService.createBrand(brand);
        if (count != 1) {
            Asserts.fail("Failed to create brand");
        }
        return count;
    }

    @Operation(summary = "Update brand")
    @PostMapping("/update/{id}")
    public Integer update(
            @PathVariable("id") Long id, @Valid @RequestBody BrandParam brandParam) {
        int count = brandService.updateBrand(id, brandParam);
        if (count != 1) {
            Asserts.fail("Failed to update brand");
        }
        return count;
    }

    @Operation(summary = "Delete brand")
    @GetMapping("/delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        int count = brandService.deleteBrand(id);
        if (count != 1) {
            Asserts.fail("Failed to delete brand");
        }
    }

    @Operation(summary = "List brands with pagination")
    @GetMapping("/list")
    public Page<BrandResponse> getList(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "isVisible", required = false) Integer isVisible,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        Page<Brand> brandList = brandService.listBrand(keyword, isVisible, pageNum, pageSize);
        return brandList.map(brandMapper::toResponse);
    }

    @Operation(summary = "Get brand by ID")
    @GetMapping("/{id}")
    public BrandResponse getItem(@PathVariable("id") Long id) {
        Brand brand = brandService.getBrand(id);
        if (brand == null) {
            throw new ResourceNotFoundException("Brand", id);
        }
        return brandMapper.toResponse(brand);
    }

    @Operation(summary = "Delete brands in batch")
    @PostMapping("/delete/batch")
    public Integer deleteBatch(@RequestParam("ids") List<Long> ids) {
        int count = brandService.deleteBrand(ids);
        if (count <= 0) {
            Asserts.fail("Failed to delete brands");
        }
        return count;
    }

    @Operation(summary = "Update brand visibility")
    @PostMapping("/update/isVisible")
    public Integer updateIsVisible(
            @RequestParam("ids") List<Long> ids, @RequestParam("isVisible") Integer isVisible) {
        int count = brandService.updateIsVisible(ids, isVisible);
        if (count <= 0) {
            Asserts.fail("Failed to update brand visibility");
        }
        return count;
    }
}
