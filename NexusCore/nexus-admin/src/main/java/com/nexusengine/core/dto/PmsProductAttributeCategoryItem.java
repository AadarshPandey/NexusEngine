package com.nexusengine.core.dto;

import com.nexusengine.core.model.PmsProductAttribute;
import com.nexusengine.core.model.PmsProductAttributeCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/5/24.
 */
public class PmsProductAttributeCategoryItem extends PmsProductAttributeCategory {
    @Getter
    @Setter
    @Schema(title =  "Product attribute list")
    private List<PmsProductAttribute> productAttributeList;
}
