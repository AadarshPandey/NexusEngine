package com.nexusengine.core.dto;

import com.nexusengine.core.model.OmsCompanyAddress;
import com.nexusengine.core.model.OmsOrderReturnApply;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * Represents the OmsOrderReturnApplyResult component.
 * Provides core functionality and operations for OmsOrderReturnApplyResult.
 */
public record OmsOrderReturnApplyResult(
    @JsonUnwrapped OmsOrderReturnApply omsOrderReturnApply,
    @Schema(title =  "Company address") OmsCompanyAddress companyAddress
) {
}
