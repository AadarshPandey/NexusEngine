package com.nexusengine.core.controller;

import org.springframework.web.bind.annotation.RestController;
import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.dto.S3CallbackResult;
import com.nexusengine.core.dto.S3PolicyResult;
import com.nexusengine.core.service.S3Service;
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
@lombok.RequiredArgsConstructor
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
