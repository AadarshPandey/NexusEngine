package com.nexusengine.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * Auto-generated documentation
 * Created by macro on 2018/4/26.
 */
public class PmsProductResult extends PmsProductParam {
    @Getter
    @Setter
    @Schema(title = "Cate parent id")
    private Long cateParentId;
}
