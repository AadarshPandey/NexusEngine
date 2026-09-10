package com.nexusengine.core.service;

import com.nexusengine.core.model.OmsCompanyAddress;

import java.util.List;

/**
 * Represents the OmsCompanyAddressService component.
 * Provides core functionality and operations for OmsCompanyAddressService.
 */
public interface OmsCompanyAddressService {
        /**
     * Executes the operation.
     * @return the result of the operation
     */
    List<OmsCompanyAddress> list();
}
