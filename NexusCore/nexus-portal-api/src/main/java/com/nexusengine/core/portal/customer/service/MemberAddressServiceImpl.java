package com.nexusengine.core.portal.customer.service;

import lombok.RequiredArgsConstructor;

import com.nexusengine.core.domain.customer.entity.Member;

import com.nexusengine.core.domain.customer.entity.MemberAddress;
import com.nexusengine.core.domain.customer.repository.MemberAddressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Member receive address Service implementation
 */
@Service
@Transactional
@RequiredArgsConstructor
public class MemberAddressServiceImpl implements MemberAddressService {
    private final MemberService memberService;
    private final MemberAddressRepository addressRepository;

    @Override
    public int add(MemberAddress address) {
        Member currentMember = memberService.getCurrentMember();
        address.setMemberId(currentMember.getId());
        addressRepository.save(address);
        return 1;
    }

    @Override
    public int delete(Long id) {
        Member currentMember = memberService.getCurrentMember();
        MemberAddress address = addressRepository.findById(id).orElse(null);
        if (address != null && address.getMemberId().equals(currentMember.getId())) {
            addressRepository.deleteById(id);
            return 1;
        }
        return 0;
    }

    @Override
    public int update(Long id, MemberAddress address) {
        Member currentMember = memberService.getCurrentMember();
        MemberAddress existingAddress = addressRepository.findById(id).orElse(null);
        if (existingAddress == null || !existingAddress.getMemberId().equals(currentMember.getId())) {
             throw new RuntimeException("Unauthorized to update this address");
        }
        address.setId(id);
        address.setMemberId(currentMember.getId());
        if (address.getDefaultStatus() == null) {
            address.setDefaultStatus(false);
        }
        if (Boolean.TRUE.equals(address.getDefaultStatus())) {
            // Clear other default addresses
            List<MemberAddress> existingList = addressRepository.findByMemberId(currentMember.getId());
            for (MemberAddress existing : existingList) {
                if (Boolean.TRUE.equals(existing.getDefaultStatus())) {
                    existing.setDefaultStatus(false);
                    addressRepository.save(existing);
                }
            }
        }
        addressRepository.save(address);
        return 1;
    }

    @Override
    public List<MemberAddress> list() {
        Member currentMember = memberService.getCurrentMember();
        return addressRepository.findByMemberId(currentMember.getId());
    }

    @Override
    public MemberAddress getItem(Long id) {
        Member currentMember = memberService.getCurrentMember();
        MemberAddress address = addressRepository.findById(id).orElse(null);
        if (address != null && address.getMemberId().equals(currentMember.getId())) {
            return address;
        }
        return null;
    }
}
