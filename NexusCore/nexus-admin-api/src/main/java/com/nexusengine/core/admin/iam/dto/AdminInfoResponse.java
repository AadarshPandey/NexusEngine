package com.nexusengine.core.admin.iam.dto;

import java.util.List;

public record AdminInfoResponse(
        String username, String icon, List<String> roles, Long brandId, String brandName) {}
