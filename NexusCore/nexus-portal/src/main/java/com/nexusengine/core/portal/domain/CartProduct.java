package com.nexusengine.core.portal.domain;

import com.nexusengine.core.model.PmsProduct;
import com.nexusengine.core.model.PmsProductAttribute;
import com.nexusengine.core.model.PmsSkuStock;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Represents the CartProduct component.
 * Provides core functionality and operations for CartProduct.
 */
@Getter
@Setter
public class CartProduct extends PmsProduct {
    @Schema(title = "Product attribute list")
    private List<PmsProductAttribute> productAttributeList;
    @Schema(title = "Sku stock list")
    private List<PmsSkuStock> skuStockList;
}
