package com.nexusengine.core.admin.ordering.dto;

import com.nexusengine.core.domain.ordering.entity.MerchantAddress;


import com.nexusengine.core.domain.ordering.entity.ReturnRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonUnwrapped;


/**
 * Represents the ReturnRequestResult component.
 * Provides core functionality and operations for ReturnRequestResult.
 */
public record ReturnRequestResult(
    @JsonUnwrapped ReturnRequest returnRequest,
    @Schema(title =  "Company address") MerchantAddress companyAddress
) {
}
