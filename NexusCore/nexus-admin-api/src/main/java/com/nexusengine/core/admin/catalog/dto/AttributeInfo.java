package com.nexusengine.core.admin.catalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;




/**
 * Represents the AttributeInfo component.
 * Provides core functionality and operations for AttributeInfo.
 */
public record AttributeInfo(
    @Schema(title = "Attribute id") Long attributeId,
    @Schema(title = "Attribute category id") Long attributeCategoryId
) {
}
