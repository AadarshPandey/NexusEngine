package com.nexusengine.core.dto;

import com.nexusengine.core.model.PmsProductCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Represents the PmsProductCategoryWithChildrenItem component.
 * Provides core functionality and operations for PmsProductCategoryWithChildrenItem.
 */
public class PmsProductCategoryWithChildrenItem extends PmsProductCategory {
    @Getter
    @Setter
    @Schema(title = "Children")
    private List<PmsProductCategory> children;
}
