package com.nexusengine.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * Represents the PmsProductResult component.
 * Provides core functionality and operations for PmsProductResult.
 */
public record PmsProductResult(
    @JsonUnwrapped PmsProductParam pmsProductParam,
    @Schema(title = "Cate parent id") Long cateParentId
) {
}
