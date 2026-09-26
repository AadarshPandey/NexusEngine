package com.nexusengine.core.portal.domain;

import com.nexusengine.core.model.PmsProductCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * Represents the PmsProductCategoryNode component.
 * Provides core functionality and operations for PmsProductCategoryNode.
 */
public record PmsProductCategoryNode(
    @JsonUnwrapped PmsProductCategory pmsProductCategory,
    @Schema(title = "Children") List<PmsProductCategoryNode> children
) {
}
