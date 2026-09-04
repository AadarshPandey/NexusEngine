package com.nexusengine.core.portal.repository;

import com.nexusengine.core.portal.domain.MemberProductCollection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Member product collection/favorites repository - MongoDB
 */
public interface MemberProductCollectionRepository extends MongoRepository<MemberProductCollection, String> {
    Page<MemberProductCollection> findByMemberId(Long memberId, Pageable pageable);
    MemberProductCollection findByMemberIdAndProductId(Long memberId, Long productId);
    int deleteByMemberIdAndProductId(Long memberId, Long productId);
    void deleteAllByMemberId(Long memberId);
}
