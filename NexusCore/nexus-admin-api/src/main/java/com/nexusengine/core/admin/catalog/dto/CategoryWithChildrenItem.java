package com.nexusengine.core.admin.catalog.dto;

import com.nexusengine.core.domain.catalog.entity.Category;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonUnwrapped;


/**
 * Represents the CategoryWithChildrenItem component.
 * Provides core functionality and operations for CategoryWithChildrenItem.
 */
public record CategoryWithChildrenItem(
    @JsonUnwrapped Category category,
    @Schema(title = "Children") List<Category> children
) {
}
