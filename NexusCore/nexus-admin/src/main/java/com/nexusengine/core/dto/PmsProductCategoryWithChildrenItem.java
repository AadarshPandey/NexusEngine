package com.nexusengine.core.dto;

import com.nexusengine.core.model.PmsProductCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/5/25.
 */
public class PmsProductCategoryWithChildrenItem extends PmsProductCategory {
    @Getter
    @Setter
    @Schema(title = "Children")
    private List<PmsProductCategory> children;
}
