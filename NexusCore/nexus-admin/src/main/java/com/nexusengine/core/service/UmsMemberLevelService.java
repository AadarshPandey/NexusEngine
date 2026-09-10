package com.nexusengine.core.service;

import com.nexusengine.core.model.UmsMemberLevel;

import java.util.List;

/**
 * Represents the UmsMemberLevelService component.
 * Provides core functionality and operations for UmsMemberLevelService.
 */
public interface UmsMemberLevelService {
        /**
     * Executes the operation.
     * @param defaultStatus the defaultStatus
     * @return the result of the operation
     */
    List<UmsMemberLevel> list(Integer defaultStatus);
}
