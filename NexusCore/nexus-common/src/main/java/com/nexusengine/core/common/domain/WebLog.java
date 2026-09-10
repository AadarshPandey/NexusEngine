package com.nexusengine.core.common.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Auto-generated documentation
 * Created by macro on 2018/4/26.
 */
@Data
@EqualsAndHashCode
public class WebLog {
        /**
     * The description property.
     */
    private String description;

        /**
     * The username property.
     */
    private String username;

        /**
     * The startTime property.
     */
    private Long startTime;

        /**
     * The spendTime property.
     */
    private Integer spendTime;

        /**
     * The basePath property.
     */
    private String basePath;

    /**
     * URI
     */
    private String uri;

    /**
     * URL
     */
    private String url;

        /**
     * The method property.
     */
    private String method;

        /**
     * The ip property.
     */
    private String ip;

        /**
     * The parameter property.
     */
    private Object parameter;

        /**
     * The result property.
     */
    private Object result;

}
