package com.nexusengine.core.portal.service.impl;

import com.nexusengine.core.model.UmsMember;
import com.nexusengine.core.model.UmsMemberReceiveAddress;
import com.nexusengine.core.portal.service.UmsMemberReceiveAddressService;
import com.nexusengine.core.portal.service.UmsMemberService;
import com.nexusengine.core.repository.UmsMemberReceiveAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Member receive address Service implementation
 */
@Service
public class UmsMemberReceiveAddressServiceImpl implements UmsMemberReceiveAddressService {
    @Autowired
    private UmsMemberService memberService;
    @Autowired
    private UmsMemberReceiveAddressRepository addressRepository;

    @Override
    public int add(UmsMemberReceiveAddress address) {
        UmsMember currentMember = memberService.getCurrentMember();
        address.setMemberId(currentMember.getId());
        addressRepository.save(address);
        return 1;
    }

    @Override
    public int delete(Long id) {
        UmsMember currentMember = memberService.getCurrentMember();
        UmsMemberReceiveAddress address = addressRepository.findById(id).orElse(null);
        if (address != null && address.getMemberId().equals(currentMember.getId())) {
            addressRepository.deleteById(id);
            return 1;
        }
        return 0;
    }

    @Override
    public int update(Long id, UmsMemberReceiveAddress address) {
        UmsMember currentMember = memberService.getCurrentMember();
        address.setId(id);
        address.setMemberId(currentMember.getId());
        if (address.getDefaultStatus() == null) {
            address.setDefaultStatus(0);
        }
        if (address.getDefaultStatus() == 1) {
            // Clear other default addresses
            List<UmsMemberReceiveAddress> existingList = addressRepository.findByMemberId(currentMember.getId());
            for (UmsMemberReceiveAddress existing : existingList) {
                if (existing.getDefaultStatus() != null && existing.getDefaultStatus() == 1) {
                    existing.setDefaultStatus(0);
                    addressRepository.save(existing);
                }
            }
        }
        addressRepository.save(address);
        return 1;
    }

    @Override
    public List<UmsMemberReceiveAddress> list() {
        UmsMember currentMember = memberService.getCurrentMember();
        return addressRepository.findByMemberId(currentMember.getId());
    }

    @Override
    public UmsMemberReceiveAddress getItem(Long id) {
        UmsMember currentMember = memberService.getCurrentMember();
        UmsMemberReceiveAddress address = addressRepository.findById(id).orElse(null);
        if (address != null && address.getMemberId().equals(currentMember.getId())) {
            return address;
        }
        return null;
    }
}
