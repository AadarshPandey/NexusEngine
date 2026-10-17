package com.nexusengine.core.admin.ordering.service;

import com.nexusengine.core.domain.ordering.entity.MerchantAddress;
import java.util.List;

/**
 * Service interface for managing merchant fulfillment and return warehouse addresses.
 */
public interface MerchantAddressService {
    List<MerchantAddress> list();
    MerchantAddress getById(Long id);
    MerchantAddress create(MerchantAddress address);
    MerchantAddress update(Long id, MerchantAddress address);
    int delete(Long id);
}

