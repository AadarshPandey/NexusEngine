package com.nexusengine.core.portal.service.impl;

import com.nexusengine.core.model.PmsBrand;
import com.nexusengine.core.model.UmsMember;
import com.nexusengine.core.portal.domain.MemberBrandAttention;
import com.nexusengine.core.portal.repository.MemberBrandAttentionRepository;
import com.nexusengine.core.portal.service.MemberAttentionService;
import com.nexusengine.core.portal.service.UmsMemberService;
import com.nexusengine.core.repository.PmsBrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Member brand attention/follow Service implementation
 */
@Service
public class MemberAttentionServiceImpl implements MemberAttentionService {
    @Autowired
    private PmsBrandRepository brandRepository;
    @Autowired
    private MemberBrandAttentionRepository memberBrandAttentionRepository;
    @Autowired
    private UmsMemberService memberService;

    @Override
    public int add(MemberBrandAttention memberBrandAttention) {
        int count = 0;
        if (memberBrandAttention.getBrandId() == null) {
            return 0;
        }
        UmsMember member = memberService.getCurrentMember();
        memberBrandAttention.setMemberId(member.getId());
        memberBrandAttention.setMemberNickname(member.getNickname());
        memberBrandAttention.setMemberIcon(member.getIcon());
        memberBrandAttention.setCreateTime(new Date());
        MemberBrandAttention findAttention = memberBrandAttentionRepository.findByMemberIdAndBrandId(
                memberBrandAttention.getMemberId(), memberBrandAttention.getBrandId());
        if (findAttention == null) {
            // Enrich with brand info from PostgreSQL
            PmsBrand brand = brandRepository.findById(memberBrandAttention.getBrandId()).orElse(null);
            if (brand != null) {
                memberBrandAttention.setBrandName(brand.getName());
                memberBrandAttention.setBrandLogo(brand.getLogo());
            }
            memberBrandAttentionRepository.save(memberBrandAttention);
            count = 1;
        }
        return count;
    }

    @Override
    public int delete(Long brandId) {
        UmsMember member = memberService.getCurrentMember();
        return memberBrandAttentionRepository.deleteByMemberIdAndBrandId(member.getId(), brandId);
    }

    @Override
    public Page<MemberBrandAttention> list(Integer pageNum, Integer pageSize) {
        UmsMember member = memberService.getCurrentMember();
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        return memberBrandAttentionRepository.findByMemberId(member.getId(), pageable);
    }

    @Override
    public MemberBrandAttention detail(Long brandId) {
        UmsMember member = memberService.getCurrentMember();
        return memberBrandAttentionRepository.findByMemberIdAndBrandId(member.getId(), brandId);
    }

    @Override
    public void clear() {
        UmsMember member = memberService.getCurrentMember();
        memberBrandAttentionRepository.deleteAllByMemberId(member.getId());
    }
}
