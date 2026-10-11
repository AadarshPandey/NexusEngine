package com.nexusengine.core.admin.catalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;


import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonUnwrapped;


/**
 * Represents the ProductResult component.
 * Provides core functionality and operations for ProductResult.
 */
public record ProductResult(
    @JsonUnwrapped ProductParam productParam,
    @Schema(title = "Cate parent id") Long cateParentId
) {
}
