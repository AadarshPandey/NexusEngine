package com.nexusengine.core.dto;

import com.nexusengine.core.validator.FlagValidator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/4/26.
 */
@Data
@EqualsAndHashCode
public class PmsProductCategoryParam {
    @Schema(title = "Parent id")
    private Long parentId;
    @NotEmpty
    @Schema(title = "Name",requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    @Schema(title = "Product unit")
    private String productUnit;
    @FlagValidator(value = {"0","1"},message = "Message")
    @Schema(title = "Nav status")
    private Integer navStatus;
    @FlagValidator(value = {"0","1"},message = "Message")
    @Schema(title = "Show status")
    private Integer showStatus;
    @Min(value = 0)
    @Schema(title = "Sort")
    private Integer sort;
    @Schema(title = "Icon")
    private String icon;
    @Schema(title = "Keywords")
    private String keywords;
    @Schema(title = "Description")
    private String description;
    @Schema(title = "Product attribute id list")
    private List<Long> productAttributeIdList;
}
