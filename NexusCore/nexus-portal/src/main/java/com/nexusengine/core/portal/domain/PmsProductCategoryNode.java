package com.nexusengine.core.portal.domain;

import com.nexusengine.core.model.PmsProductCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Represents the PmsProductCategoryNode component.
 * Provides core functionality and operations for PmsProductCategoryNode.
 */
@Getter
@Setter
public class PmsProductCategoryNode extends PmsProductCategory {
    @Schema(title = "Children")
    private List<PmsProductCategoryNode> children;
}
