package com.nexusengine.core.admin.ordering.service;

import lombok.RequiredArgsConstructor;

import com.nexusengine.core.domain.ordering.repository.MerchantAddressRepository;


import com.nexusengine.core.domain.ordering.entity.MerchantAddress;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@RequiredArgsConstructor
public class MerchantAddressServiceImpl implements MerchantAddressService {
    private final MerchantAddressRepository companyAddressRepository;

    @Override
    public List<MerchantAddress> list() {
        return /* findAll() is acceptable for small reference tables */
        companyAddressRepository.findAll();
    }
}
