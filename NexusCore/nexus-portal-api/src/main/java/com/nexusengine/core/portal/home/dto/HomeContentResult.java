package com.nexusengine.core.portal.home.dto;

import com.nexusengine.core.domain.catalog.entity.Brand;
import com.nexusengine.core.portal.catalog.dto.*;
import com.nexusengine.core.portal.catalog.service.*;
import com.nexusengine.core.portal.catalog.dao.*;
import com.nexusengine.core.portal.payment.service.*;
import com.nexusengine.core.portal.ordering.dto.*;
import com.nexusengine.core.portal.ordering.service.*;
import com.nexusengine.core.portal.ordering.dao.*;
import com.nexusengine.core.portal.customer.dto.*;
import com.nexusengine.core.portal.customer.service.*;
import com.nexusengine.core.portal.promotion.dto.*;
import com.nexusengine.core.portal.promotion.service.*;
import com.nexusengine.core.portal.promotion.strategy.*;
import com.nexusengine.core.portal.ai.service.*;
import com.nexusengine.core.portal.home.dto.*;
import com.nexusengine.core.portal.home.service.*;
import com.nexusengine.core.portal.engagement.dto.*;
import com.nexusengine.core.portal.engagement.service.*;

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
import com.nexusengine.core.domain.catalog.entity.Product;
import com.nexusengine.core.domain.promotion.entity.Banner;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.util.List;


/**
 * Represents the HomeContentResult component.
 * Provides core functionality and operations for HomeContentResult.
 */
public record HomeContentResult(
    @Schema(title = "Advertise list") List<Banner> advertiseList,
    @Schema(title = "Brand list") List<Brand> brandList,
    @Schema(title = "Home flash promotion") HomeFlashPromotion homeFlashPromotion,
    @Schema(title = "New product list") List<Product> newProductList,
    @Schema(title = "Hot product list") List<Product> hotProductList,
    @Schema(title = "AI Recommended product list") List<Product> aiRecommendProductList
) {
}
