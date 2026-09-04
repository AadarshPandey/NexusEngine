package com.nexusengine.core.common.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Auto-generated documentation
 * Created by macro on 2020/7/16.
 */
@Data
@EqualsAndHashCode
@Builder
public class SwaggerProperties {
    /**
     * Auto-generated documentation
     */
    private String apiBasePackage;
    /**
     * Auto-generated documentation
     */
    private boolean enableSecurity;
    /**
     * Auto-generated documentation
     */
    private String title;
    /**
     * Auto-generated documentation
     */
    private String description;
    /**
     * Auto-generated documentation
     */
    private String version;
    /**
     * Auto-generated documentation
     */
    private String contactName;
    /**
     * Auto-generated documentation
     */
    private String contactUrl;
    /**
     * Auto-generated documentation
     */
    private String contactEmail;
}
