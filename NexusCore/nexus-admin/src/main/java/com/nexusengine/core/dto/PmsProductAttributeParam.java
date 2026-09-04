package com.nexusengine.core.dto;

import com.nexusengine.core.validator.FlagValidator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotEmpty;

/**
 * Auto-generated documentation
 * Created by macro on 2018/4/26.
 */
@Data
@EqualsAndHashCode
public class PmsProductAttributeParam {
    @NotEmpty
    @Schema(title = "Product attribute category id")
    private Long productAttributeCategoryId;
    @NotEmpty
    @Schema(title = "Name")
    private String name;
    @FlagValidator({"0","1","2"})
    @Schema(title = "Select type")
    private Integer selectType;
    @FlagValidator({"0","1"})
    @Schema(title = "Input type")
    private Integer inputType;
    @Schema(title = "Input list")
    private String inputList;
    private Integer sort;
    @Schema(title = "Filter type")
    @FlagValidator({"0","1"})
    private Integer filterType;
    @Schema(title = "Search type")
    @FlagValidator({"0","1","2"})
    private Integer searchType;
    @Schema(title = "Related status")
    @FlagValidator({"0","1"})
    private Integer relatedStatus;
    @Schema(title = "Hand add status")
    @FlagValidator({"0","1"})
    private Integer handAddStatus;
    @Schema(title = "Type")
    @FlagValidator({"0","1"})
    private Integer type;
}
