package com.nexusengine.core.portal.catalog.dto;

import com.nexusengine.core.domain.catalog.entity.Category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonUnwrapped;


/**
 * Represents the CategoryNode component.
 * Provides core functionality and operations for CategoryNode.
 */
public record CategoryNode(
    @JsonUnwrapped Category category,
    @Schema(title = "Children") List<CategoryNode> children
) {
}
