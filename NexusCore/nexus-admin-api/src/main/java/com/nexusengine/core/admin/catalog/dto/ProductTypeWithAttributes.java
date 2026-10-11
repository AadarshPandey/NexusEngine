package com.nexusengine.core.admin.catalog.dto;




import com.nexusengine.core.domain.catalog.entity.AttributeDefinition;
import com.nexusengine.core.domain.catalog.entity.ProductType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonUnwrapped;


/**
 * Represents the ProductTypeWithAttributes component.
 * Provides core functionality and operations for ProductTypeWithAttributes.
 */
public record ProductTypeWithAttributes(
    @JsonUnwrapped ProductType productType,
    @Schema(title =  "Product attribute list") List<AttributeDefinition> productAttributeList
) {
}
