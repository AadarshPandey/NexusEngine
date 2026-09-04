package com.nexusengine.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Auto-generated documentation
 * Created by macro on 2018/4/27.
 */
@Data
@EqualsAndHashCode
public class PmsProductQueryParam {
    @Schema(title = "Publish status")
    private Integer publishStatus;
    @Schema(title = "Verify status")
    private Integer verifyStatus;
    @Schema(title = "Keyword")
    private String keyword;
    @Schema(title = "Product sn")
    private String productSn;
    @Schema(title = "Product category id")
    private Long productCategoryId;
    @Schema(title = "Brand id")
    private Long brandId;
}
