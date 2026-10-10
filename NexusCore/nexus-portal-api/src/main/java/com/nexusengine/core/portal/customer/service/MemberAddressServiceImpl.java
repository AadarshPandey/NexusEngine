package com.nexusengine.core.portal.customer.service;

import lombok.RequiredArgsConstructor;
import com.nexusengine.core.portal.catalog.dto.*;
import com.nexusengine.core.portal.catalog.service.*;
import com.nexusengine.core.portal.catalog.dao.*;
import com.nexusengine.core.portal.payment.service.*;
import com.nexusengine.core.portal.ordering.dto.*;
import com.nexusengine.core.portal.ordering.service.*;
import com.nexusengine.core.portal.ordering.dao.*;
import com.nexusengine.core.portal.customer.dto.*;
import com.nexusengine.core.portal.customer.service.*;
import com.nexusengine.core.portal.promotion.dto.*;
import com.nexusengine.core.portal.promotion.service.*;
import com.nexusengine.core.portal.promotion.strategy.*;
import com.nexusengine.core.portal.ai.service.*;
import com.nexusengine.core.portal.home.dto.*;
import com.nexusengine.core.portal.home.service.*;
import com.nexusengine.core.portal.engagement.dto.*;
import com.nexusengine.core.portal.engagement.service.*;

import com.nexusengine.core.domain.customer.entity.Member;

import com.nexusengine.core.domain.catalog.entity.*;
import com.nexusengine.core.domain.ordering.entity.*;
import com.nexusengine.core.domain.customer.entity.*;
import com.nexusengine.core.domain.promotion.entity.*;
import com.nexusengine.core.domain.iam.entity.*;
import com.nexusengine.core.domain.catalog.repository.*;
import com.nexusengine.core.domain.ordering.repository.*;
import com.nexusengine.core.domain.customer.repository.*;
import com.nexusengine.core.domain.promotion.repository.*;
import com.nexusengine.core.domain.iam.repository.*;
import com.nexusengine.core.domain.customer.entity.MemberAddress;
import com.nexusengine.core.domain.customer.repository.MemberAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
            address.setDefaultStatus(0);
        }
        if (address.getDefaultStatus() == 1) {
            // Clear other default addresses
            List<MemberAddress> existingList = addressRepository.findByMemberId(currentMember.getId());
            for (MemberAddress existing : existingList) {
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
