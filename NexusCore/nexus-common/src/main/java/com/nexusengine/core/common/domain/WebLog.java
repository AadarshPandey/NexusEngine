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
     * Auto-generated documentation
     */
    private String description;

    /**
     * Auto-generated documentation
     */
    private String username;

    /**
     * Auto-generated documentation
     */
    private Long startTime;

    /**
     * Auto-generated documentation
     */
    private Integer spendTime;

    /**
     * Auto-generated documentation
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
     * Auto-generated documentation
     */
    private String method;

    /**
     * Auto-generated documentation
     */
    private String ip;

    /**
     * Auto-generated documentation
     */
    private Object parameter;

    /**
     * Auto-generated documentation
     */
    private Object result;

}
