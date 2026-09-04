package com.nexusengine.core.portal.domain;

import com.nexusengine.core.model.PmsProduct;
import com.nexusengine.core.model.PmsProductFullReduction;
import com.nexusengine.core.model.PmsProductLadder;
import com.nexusengine.core.model.PmsSkuStock;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/8/27.
 */
@Getter
@Setter
public class PromotionProduct extends PmsProduct {
    // Auto-generated documentation
    private List<PmsSkuStock> skuStockList;
    // Auto-generated documentation
    private List<PmsProductLadder> productLadderList;
    // Auto-generated documentation
    private List<PmsProductFullReduction> productFullReductionList;
}
