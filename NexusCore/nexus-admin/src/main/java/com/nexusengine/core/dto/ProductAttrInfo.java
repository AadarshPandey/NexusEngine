package com.nexusengine.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents the ProductAttrInfo component.
 * Provides core functionality and operations for ProductAttrInfo.
 */
public record ProductAttrInfo(
    @Schema(title = "Attribute id") Long attributeId,
    @Schema(title = "Attribute category id") Long attributeCategoryId
) {
}
