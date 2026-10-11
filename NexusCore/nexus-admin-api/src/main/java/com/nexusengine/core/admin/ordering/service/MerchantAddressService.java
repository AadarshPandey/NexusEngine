package com.nexusengine.core.admin.ordering.service;

import com.nexusengine.core.domain.ordering.entity.MerchantAddress;



import java.util.List;


/**
 * Represents the MerchantAddressService component.
 * Provides core functionality and operations for MerchantAddressService.
 */
public interface MerchantAddressService {
        /**
     * Executes the operation.
     * @return the result of the operation
     */
    List<MerchantAddress> list();
}
