package com.nexusengine.core.portal.domain;

import com.nexusengine.core.model.CmsSubject;
import com.nexusengine.core.model.PmsBrand;
import com.nexusengine.core.model.PmsProduct;
import com.nexusengine.core.model.SmsHomeAdvertise;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2019/1/28.
 */
@Getter
@Setter
public class HomeContentResult {
    @Schema(title = "Advertise list")
    private List<SmsHomeAdvertise> advertiseList;
    @Schema(title = "Brand list")
    private List<PmsBrand> brandList;
    @Schema(title = "Home flash promotion")
    private HomeFlashPromotion homeFlashPromotion;
    @Schema(title = "New product list")
    private List<PmsProduct> newProductList;
    @Schema(title = "Hot product list")
    private List<PmsProduct> hotProductList;
    @Schema(title = "Subject list")
    private List<CmsSubject> subjectList;
    @Schema(title = "AI Recommended product list")
    private List<PmsProduct> aiRecommendProductList;
}
