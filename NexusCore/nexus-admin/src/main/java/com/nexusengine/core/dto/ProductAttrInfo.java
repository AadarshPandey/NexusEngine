package com.nexusengine.core.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Auto-generated documentation
 * Created by macro on 2018/5/23.
 */
@Data
@EqualsAndHashCode
public class ProductAttrInfo {
    @Schema(title = "Attribute id")
    private Long attributeId;
    @Schema(title = "Attribute category id")
    private Long attributeCategoryId;
}
