package com.nexusengine.core.admin.infra.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RestController;

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
import com.nexusengine.core.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpServletRequest;


/**
 * S3 Object Storage Controller
 */
@RestController
@Tag(name = "S3Controller", description = "S3 Object Storage Management")
@RequestMapping("/s3")
@RequiredArgsConstructor
public class S3Controller {
    private final S3Service s3Service;

    @Operation(summary = "Generate S3 presigned upload URL")
    @GetMapping("/presign")

    public CommonResult<S3PolicyResult> presign() {
        S3PolicyResult result = s3Service.presign();
        return CommonResult.success(result);
    }

    @Operation(summary = "S3 upload callback")
    @PostMapping("callback")

    public CommonResult<S3CallbackResult> callback(HttpServletRequest request) {
        S3CallbackResult result = s3Service.callback(request);
        return CommonResult.success(result);
    }
}
