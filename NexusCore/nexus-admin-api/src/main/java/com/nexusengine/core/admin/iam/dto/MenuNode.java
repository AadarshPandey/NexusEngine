package com.nexusengine.core.admin.iam.dto;

import com.nexusengine.core.domain.iam.entity.ConsoleMenu;


import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonUnwrapped;


/**
 * Represents the MenuNode component.
 * Provides core functionality and operations for MenuNode.
 */
public record MenuNode(
    @JsonUnwrapped ConsoleMenu consoleMenu,
    @Schema(title =  "Children") List<MenuNode> children
) {
}
