package com.nexusengine.core.admin.ordering.service;

import com.nexusengine.core.domain.ordering.entity.OrderPolicy;




/**
 * Represents the OrderPolicyService component.
 * Provides core functionality and operations for OrderPolicyService.
 */
public interface OrderPolicyService {
        /**
     * Executes the operation.
     * @param id the id
     * @return the result of the operation
     */
    OrderPolicy getItem(Long id);

        /**
     * Executes the operation.
     * @param id the id
     * @param orderSetting the orderSetting
     * @return the result of the operation
     */
    int update(Long id, OrderPolicy orderSetting);
}
