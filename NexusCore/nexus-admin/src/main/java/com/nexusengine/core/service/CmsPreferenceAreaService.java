package com.nexusengine.core.service;

import com.nexusengine.core.model.CmsPreferenceArea;

import java.util.List;

/**
 * Represents the CmsPreferenceAreaService component.
 * Provides core functionality and operations for CmsPreferenceAreaService.
 */
public interface CmsPreferenceAreaService {
        /**
     * Executes the operation.
     * @return the result of the operation
     */
    List<CmsPreferenceArea> listAll();
}
