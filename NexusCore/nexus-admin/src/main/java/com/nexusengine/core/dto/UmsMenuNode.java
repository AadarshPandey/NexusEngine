package com.nexusengine.core.dto;

import com.nexusengine.core.model.UmsMenu;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2020/2/4.
 */
@Getter
@Setter
public class UmsMenuNode extends UmsMenu {
    @Schema(title =  "Children")
    private List<UmsMenuNode> children;
}
