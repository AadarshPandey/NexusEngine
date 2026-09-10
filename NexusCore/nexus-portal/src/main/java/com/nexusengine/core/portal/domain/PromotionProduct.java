package com.nexusengine.core.portal.domain;

import com.nexusengine.core.model.PmsProduct;
import com.nexusengine.core.model.PmsProductFullReduction;
import com.nexusengine.core.model.PmsProductLadder;
import com.nexusengine.core.model.PmsSkuStock;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Represents the PromotionProduct component.
 * Provides core functionality and operations for PromotionProduct.
 */
@Getter
@Setter
public class PromotionProduct extends PmsProduct {
    private List<PmsSkuStock> skuStockList;
    private List<PmsProductLadder> productLadderList;
    private List<PmsProductFullReduction> productFullReductionList;
}
