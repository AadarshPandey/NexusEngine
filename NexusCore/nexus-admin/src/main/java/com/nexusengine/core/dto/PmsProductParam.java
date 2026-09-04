package com.nexusengine.core.dto;

import com.nexusengine.core.model.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/4/26.
 */
@Data
@EqualsAndHashCode
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
    @Schema(title = "Subject product relation list")
    private List<CmsSubjectProductRelation> subjectProductRelationList;
    @Schema(title = "Prefrence area product relation list")
    private List<CmsPrefrenceAreaProductRelation> prefrenceAreaProductRelationList;
}
