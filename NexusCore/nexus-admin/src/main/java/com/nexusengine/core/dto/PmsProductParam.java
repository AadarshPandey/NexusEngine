package com.nexusengine.core.dto;

import com.nexusengine.core.model.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Represents the PmsProductParam component.
 * Provides core functionality and operations for PmsProductParam.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PmsProductParam extends PmsProduct{
    @Schema(title = "Product ladder list")
    private List<PmsProductLadder> productLadderList;
    @Schema(title = "Product full reduction list")
    private List<PmsProductFullReduction> productFullReductionList;
    @Schema(title = "Member price list")
    private List<PmsMemberPrice> memberPriceList;
    @Schema(title = "Sku stock list")
    private List<PmsSkuStock> skuStockList;
    @Schema(title = "Product attribute value list")
    private List<PmsProductAttributeValue> productAttributeValueList;
}
