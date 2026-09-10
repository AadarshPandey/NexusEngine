package com.nexusengine.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the PmsProductResult component.
 * Provides core functionality and operations for PmsProductResult.
 */
public class PmsProductResult extends PmsProductParam {
    @Getter
    @Setter
    @Schema(title = "Cate parent id")
    private Long cateParentId;
}
