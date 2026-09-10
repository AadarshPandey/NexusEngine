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
     * The apiBasePackage property.
     */
    private String apiBasePackage;
        /**
     * The enableSecurity property.
     */
    private boolean enableSecurity;
        /**
     * The title property.
     */
    private String title;
        /**
     * The description property.
     */
    private String description;
        /**
     * The version property.
     */
    private String version;
        /**
     * The contactName property.
     */
    private String contactName;
        /**
     * The contactUrl property.
     */
    private String contactUrl;
        /**
     * The contactEmail property.
     */
    private String contactEmail;
}
