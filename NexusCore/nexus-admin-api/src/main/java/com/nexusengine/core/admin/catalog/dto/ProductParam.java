package com.nexusengine.core.admin.catalog.dto;

import com.nexusengine.core.domain.promotion.entity.SpendThresholdDiscount;
import com.nexusengine.core.admin.catalog.dto.*;
import com.nexusengine.core.admin.catalog.service.*;
import com.nexusengine.core.admin.analytics.service.*;
import com.nexusengine.core.admin.ordering.dto.*;
import com.nexusengine.core.admin.ordering.service.*;
import com.nexusengine.core.admin.promotion.dto.*;
import com.nexusengine.core.admin.promotion.service.*;
import com.nexusengine.core.admin.ai.service.*;
import com.nexusengine.core.admin.infra.dto.*;
import com.nexusengine.core.admin.infra.service.*;
import com.nexusengine.core.admin.iam.dto.*;
import com.nexusengine.core.admin.iam.service.*;
import com.nexusengine.core.domain.catalog.entity.ProductAttributeValue;
import com.nexusengine.core.domain.catalog.entity.ProductSku;
import com.nexusengine.core.domain.promotion.entity.QuantityTierDiscount;
import com.nexusengine.core.domain.catalog.entity.Product;


import com.nexusengine.core.domain.catalog.entity.*;
import com.nexusengine.core.domain.ordering.entity.*;
import com.nexusengine.core.domain.customer.entity.*;
import com.nexusengine.core.domain.promotion.entity.*;
import com.nexusengine.core.domain.iam.entity.*;
import com.nexusengine.core.domain.catalog.repository.*;
import com.nexusengine.core.domain.ordering.repository.*;
import com.nexusengine.core.domain.customer.repository.*;
import com.nexusengine.core.domain.promotion.repository.*;
import com.nexusengine.core.domain.iam.repository.*;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonUnwrapped;


/**
 * Represents the ProductParam component.
 * Provides core functionality and operations for ProductParam.
 */

public record ProductParam(
    @JsonUnwrapped Product product,
    @Schema(title = "Product ladder list") List<QuantityTierDiscount> productVolumeDiscountList,
    @Schema(title = "Product full reduction list") List<SpendThresholdDiscount> productSpendDiscountList,
    @Schema(title = "Sku stock list") List<ProductSku> skuStockList,
    @Schema(title = "Product attribute value list") List<ProductAttributeValue> productAttributeValueList
) {
}
