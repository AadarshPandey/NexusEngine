package com.nexusengine.core.portal.repository;

import com.nexusengine.core.portal.domain.MemberBrandAttention;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Member brand attention/follow repository - MongoDB
 */
public interface MemberBrandAttentionRepository extends MongoRepository<MemberBrandAttention, String> {
    Page<MemberBrandAttention> findByMemberId(Long memberId, Pageable pageable);
    MemberBrandAttention findByMemberIdAndBrandId(Long memberId, Long brandId);
    int deleteByMemberIdAndBrandId(Long memberId, Long brandId);
    void deleteAllByMemberId(Long memberId);
}
