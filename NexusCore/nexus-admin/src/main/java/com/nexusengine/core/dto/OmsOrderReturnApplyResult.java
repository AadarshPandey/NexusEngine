package com.nexusengine.core.dto;

import com.nexusengine.core.model.OmsCompanyAddress;
import com.nexusengine.core.model.OmsOrderReturnApply;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * Auto-generated documentation
 * Created by macro on 2018/10/18.
 */
public class OmsOrderReturnApplyResult extends OmsOrderReturnApply {
    @Getter
    @Setter
    @Schema(title =  "Company address")
    private OmsCompanyAddress companyAddress;
}
