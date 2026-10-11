package com.nexusengine.core.admin.catalog.dto;




import com.nexusengine.core.admin.shared.validator.FlagValidator;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;


/**
 * Represents the CategoryParam component.
 * Provides core functionality and operations for CategoryParam.
 */
public record CategoryParam(
    @Schema(title = "Parent id") Long parentId,
    @NotEmpty @Schema(title = "Name",requiredMode = Schema.RequiredMode.REQUIRED) String name,
    @com.fasterxml.jackson.annotation.JsonProperty("isInNavigation") @FlagValidator(value = {"0","1"},message = "Message") @Schema(title = "Nav status") Integer navStatus,
    @com.fasterxml.jackson.annotation.JsonProperty("isVisible") @FlagValidator(value = {"0","1"},message = "Message") @Schema(title = "Show status") Integer showStatus,
    @com.fasterxml.jackson.annotation.JsonProperty("iconUrl") @Schema(title = "Icon") String icon,
    @Schema(title = "Product attribute id list") List<Long> productAttributeIdList
) {
}
