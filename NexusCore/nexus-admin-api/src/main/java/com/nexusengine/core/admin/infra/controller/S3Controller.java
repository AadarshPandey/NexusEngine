package com.nexusengine.core.admin.infra.controller;

import com.nexusengine.core.admin.infra.dto.S3CallbackResult;
import com.nexusengine.core.admin.infra.dto.S3PolicyResult;
import com.nexusengine.core.admin.infra.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Infrastructure - Storage", description = "S3 object storage management APIs")
@RequestMapping("/api/admin/infra/storage")
@RequiredArgsConstructor
public class S3Controller {
    private final S3Service s3Service;

    @Operation(summary = "Generate S3 presigned upload URL")
    @GetMapping("/presign")
    public S3PolicyResult presign() {
        return s3Service.presign();
    }

    @Operation(summary = "S3 upload callback")
    @PostMapping("/callback")
    public S3CallbackResult callback(HttpServletRequest request) {
        return s3Service.callback(request);
    }
}
