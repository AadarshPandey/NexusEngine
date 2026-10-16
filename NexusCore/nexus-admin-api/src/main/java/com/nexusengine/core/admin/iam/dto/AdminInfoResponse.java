package com.nexusengine.core.admin.iam.dto;

import com.nexusengine.core.domain.iam.entity.ConsoleMenu;
import java.util.List;

public record AdminInfoResponse(
        String username, List<ConsoleMenu> menus, String icon, List<String> roles) {}
