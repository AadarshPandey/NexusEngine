package com.nexusengine.core.dto;

import com.nexusengine.core.model.PmsProductAttribute;
import com.nexusengine.core.model.PmsProductAttributeCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * Represents the PmsProductAttributeCategoryItem component.
 * Provides core functionality and operations for PmsProductAttributeCategoryItem.
 */
public record PmsProductAttributeCategoryItem(
    @JsonUnwrapped PmsProductAttributeCategory pmsProductAttributeCategory,
    @Schema(title =  "Product attribute list") List<PmsProductAttribute> productAttributeList
) {
}
