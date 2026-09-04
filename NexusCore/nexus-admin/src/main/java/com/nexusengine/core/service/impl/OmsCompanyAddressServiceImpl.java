package com.nexusengine.core.service.impl;

import com.nexusengine.core.repository.OmsCompanyAddressRepository;
import com.nexusengine.core.model.OmsCompanyAddress;
import com.nexusengine.core.service.OmsCompanyAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OmsCompanyAddressServiceImpl implements OmsCompanyAddressService {
    @Autowired
    private OmsCompanyAddressRepository companyAddressRepository;

    @Override
    public List<OmsCompanyAddress> list() {
        return companyAddressRepository.findAll();
    }
}
