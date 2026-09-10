package com.nexusengine.core.security.component;

import org.springframework.security.access.ConfigAttribute;

import java.util.Map;

/**
 * Represents the DynamicSecurityService component.
 * Provides core functionality and operations for DynamicSecurityService.
 */
public interface DynamicSecurityService {
        /**
     * Executes the operation.
     * @return the result of the operation
     */
    Map<String, ConfigAttribute> loadDataSource();
}
