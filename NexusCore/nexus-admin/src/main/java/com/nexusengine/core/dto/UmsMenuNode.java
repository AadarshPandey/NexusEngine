package com.nexusengine.core.dto;

import com.nexusengine.core.model.UmsMenu;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Represents the UmsMenuNode component.
 * Provides core functionality and operations for UmsMenuNode.
 */
@Getter
@Setter
public class UmsMenuNode extends UmsMenu {
    @Schema(title =  "Children")
    private List<UmsMenuNode> children;
}
