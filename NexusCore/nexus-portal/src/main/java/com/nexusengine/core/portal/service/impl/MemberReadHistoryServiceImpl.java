package com.nexusengine.core.portal.service.impl;

import com.nexusengine.core.model.PmsProduct;
import com.nexusengine.core.model.UmsMember;
import com.nexusengine.core.portal.domain.MemberReadHistory;
import com.nexusengine.core.portal.repository.MemberReadHistoryRepository;
import com.nexusengine.core.portal.service.MemberReadHistoryService;
import com.nexusengine.core.portal.service.UmsMemberService;
import com.nexusengine.core.repository.PmsProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Member browse history Service implementation
 */
@Service
public class MemberReadHistoryServiceImpl implements MemberReadHistoryService {
    @Autowired
    private PmsProductRepository productRepository;
    @Autowired
    private MemberReadHistoryRepository memberReadHistoryRepository;
    @Autowired
    private UmsMemberService memberService;

    @Override
    public int create(MemberReadHistory memberReadHistory) {
        if (memberReadHistory.getProductId() == null) {
            return 0;
        }
        UmsMember member = memberService.getCurrentMember();
        memberReadHistory.setMemberId(member.getId());
        memberReadHistory.setMemberNickname(member.getNickname());
        memberReadHistory.setMemberIcon(member.getIcon());
        memberReadHistory.setId(null);
        memberReadHistory.setCreateTime(new Date());
        // Enrich with product info from PostgreSQL
        PmsProduct product = productRepository.findById(memberReadHistory.getProductId()).orElse(null);
        if (product != null && product.getDeleteStatus() != 1) {
            memberReadHistory.setProductName(product.getName());
            memberReadHistory.setProductSubTitle(product.getSubTitle());
            memberReadHistory.setProductPrice(product.getPrice() != null ? product.getPrice().toString() : "0");
            memberReadHistory.setProductPic(product.getPic());
        }
        memberReadHistoryRepository.save(memberReadHistory);
        return 1;
    }

    @Override
    public int delete(List<String> ids) {
        List<MemberReadHistory> deleteList = new ArrayList<>();
        for (String id : ids) {
            MemberReadHistory memberReadHistory = new MemberReadHistory();
            memberReadHistory.setId(id);
            deleteList.add(memberReadHistory);
        }
        memberReadHistoryRepository.deleteAll(deleteList);
        return ids.size();
    }

    @Override
    public Page<MemberReadHistory> list(Integer pageNum, Integer pageSize) {
        UmsMember member = memberService.getCurrentMember();
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        return memberReadHistoryRepository.findByMemberIdOrderByCreateTimeDesc(member.getId(), pageable);
    }

    @Override
    public void clear() {
        UmsMember member = memberService.getCurrentMember();
        memberReadHistoryRepository.deleteAllByMemberId(member.getId());
    }
}
