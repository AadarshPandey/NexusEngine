package com.nexusengine.core.admin.ordering.service;

import com.nexusengine.core.common.exception.ResourceNotFoundException;
import com.nexusengine.core.domain.ordering.entity.MerchantAddress;
import com.nexusengine.core.domain.ordering.repository.MerchantAddressRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MerchantAddressServiceImpl implements MerchantAddressService {
    private final MerchantAddressRepository companyAddressRepository;

    @Override
    public List<MerchantAddress> list() {
        return companyAddressRepository.findAll();
    }

    @Override
    public MerchantAddress getById(Long id) {
        return companyAddressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MerchantAddress", id));
    }

    @Override
    @Transactional
    public MerchantAddress create(MerchantAddress address) {
        address.setId(null);
        return companyAddressRepository.save(address);
    }

    @Override
    @Transactional
    public MerchantAddress update(Long id, MerchantAddress address) {
        MerchantAddress existing = getById(id);
        existing.setAddressName(address.getAddressName());
        existing.setSendStatus(address.getSendStatus());
        existing.setReceiveStatus(address.getReceiveStatus());
        existing.setName(address.getName());
        existing.setPhone(address.getPhone());
        existing.setState(address.getState());
        existing.setCity(address.getCity());
        existing.setDetailAddress(address.getDetailAddress());
        return companyAddressRepository.save(existing);
    }

    @Override
    @Transactional
    public int delete(Long id) {
        companyAddressRepository.deleteById(id);
        return 1;
    }
}
