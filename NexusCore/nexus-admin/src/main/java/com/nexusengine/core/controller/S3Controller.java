package com.nexusengine.core.controller;

import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.dto.S3CallbackResult;
import com.nexusengine.core.dto.S3PolicyResult;
import com.nexusengine.core.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;

/**
 * S3 Object Storage Controller
 */
@Controller
@Tag(name = "S3Controller", description = "S3 Object Storage Management")
@RequestMapping("/s3")
public class S3Controller {
    @Autowired
    private S3Service s3Service;

    @Operation(summary = "Generate S3 presigned upload URL")
    @RequestMapping(value = "/presign", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<S3PolicyResult> presign() {
        S3PolicyResult result = s3Service.presign();
        return CommonResult.success(result);
    }

    @Operation(summary = "S3 upload callback")
    @RequestMapping(value = "callback", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<S3CallbackResult> callback(HttpServletRequest request) {
        S3CallbackResult result = s3Service.callback(request);
        return CommonResult.success(result);
    }
}
