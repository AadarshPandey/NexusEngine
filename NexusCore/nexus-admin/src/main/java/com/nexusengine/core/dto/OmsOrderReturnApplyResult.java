package com.nexusengine.core.dto;

import com.nexusengine.core.model.OmsCompanyAddress;
import com.nexusengine.core.model.OmsOrderReturnApply;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the OmsOrderReturnApplyResult component.
 * Provides core functionality and operations for OmsOrderReturnApplyResult.
 */
public class OmsOrderReturnApplyResult extends OmsOrderReturnApply {
    @Getter
    @Setter
    @Schema(title =  "Company address")
    private OmsCompanyAddress companyAddress;
}
