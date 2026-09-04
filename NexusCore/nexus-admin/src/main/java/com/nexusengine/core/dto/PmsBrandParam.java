package com.nexusengine.core.dto;

import com.nexusengine.core.validator.FlagValidator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

/**
 * Auto-generated documentation
 * Created by macro on 2018/4/26.
 */
@Data
@EqualsAndHashCode
public class PmsBrandParam {
    @NotEmpty
    @Schema(title =  "Name",requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    @Schema(title =  "First letter")
    private String firstLetter;
    @Min(value = 0)
    @Schema(title =  "Sort")
    private Integer sort;
    @FlagValidator(value = {"0","1"}, message = "Message")
    @Schema(title =  "Factory status")
    private Integer factoryStatus;
    @FlagValidator(value = {"0","1"}, message = "Message")
    @Schema(title =  "Show status")
    private Integer showStatus;
    @NotEmpty
    @Schema(title =  "Logo",requiredMode = Schema.RequiredMode.REQUIRED)
    private String logo;
    @Schema(title =  "Big pic")
    private String bigPic;
    @Schema(title =  "Brand story")
    private String brandStory;
}
