package com.nexusengine.core.portal.service.impl;

import com.nexusengine.core.model.PmsProduct;
import com.nexusengine.core.model.UmsMember;
import com.nexusengine.core.portal.domain.MemberProductCollection;
import com.nexusengine.core.portal.repository.MemberProductCollectionRepository;
import com.nexusengine.core.portal.service.MemberCollectionService;
import com.nexusengine.core.portal.service.UmsMemberService;
import com.nexusengine.core.repository.PmsProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Member product collection/favorites Service implementation
 */
@Service
public class MemberCollectionServiceImpl implements MemberCollectionService {
    @Autowired
    private PmsProductRepository productRepository;
    @Autowired
    private MemberProductCollectionRepository productCollectionRepository;
    @Autowired
    private UmsMemberService memberService;

    @Override
    public int add(MemberProductCollection productCollection) {
        int count = 0;
        if (productCollection.getProductId() == null) {
            return 0;
        }
        UmsMember member = memberService.getCurrentMember();
        productCollection.setMemberId(member.getId());
        productCollection.setMemberNickname(member.getNickname());
        productCollection.setMemberIcon(member.getIcon());
        productCollection.setCreateTime(new Date());
        MemberProductCollection findCollection = productCollectionRepository.findByMemberIdAndProductId(
                productCollection.getMemberId(), productCollection.getProductId());
        if (findCollection == null) {
            // Enrich with product info from PostgreSQL
            PmsProduct product = productRepository.findById(productCollection.getProductId()).orElse(null);
            if (product != null && product.getDeleteStatus() != 1) {
                productCollection.setProductName(product.getName());
                productCollection.setProductSubTitle(product.getSubTitle());
                productCollection.setProductPrice(product.getPrice() != null ? product.getPrice().toString() : "0");
                productCollection.setProductPic(product.getPic());
            } else {
                return 0;
            }
            productCollectionRepository.save(productCollection);
            count = 1;
        }
        return count;
    }

    @Override
    public int delete(Long productId) {
        UmsMember member = memberService.getCurrentMember();
        return productCollectionRepository.deleteByMemberIdAndProductId(member.getId(), productId);
    }

    @Override
    public Page<MemberProductCollection> list(Integer pageNum, Integer pageSize) {
        UmsMember member = memberService.getCurrentMember();
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        return productCollectionRepository.findByMemberId(member.getId(), pageable);
    }

    @Override
    public MemberProductCollection detail(Long productId) {
        UmsMember member = memberService.getCurrentMember();
        return productCollectionRepository.findByMemberIdAndProductId(member.getId(), productId);
    }

    @Override
    public void clear() {
        UmsMember member = memberService.getCurrentMember();
        productCollectionRepository.deleteAllByMemberId(member.getId());
    }
}
