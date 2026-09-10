package com.nexusengine.core.service;

import com.nexusengine.core.model.OmsOrderSetting;

/**
 * Represents the OmsOrderSettingService component.
 * Provides core functionality and operations for OmsOrderSettingService.
 */
public interface OmsOrderSettingService {
        /**
     * Executes the operation.
     * @param id the id
     * @return the result of the operation
     */
    OmsOrderSetting getItem(Long id);

        /**
     * Executes the operation.
     * @param id the id
     * @param orderSetting the orderSetting
     * @return the result of the operation
     */
    int update(Long id, OmsOrderSetting orderSetting);
}
