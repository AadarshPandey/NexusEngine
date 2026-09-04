package com.nexusengine.core.portal.repository;

import com.nexusengine.core.portal.domain.MemberReadHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Member product browse history repository - MongoDB
 */
public interface MemberReadHistoryRepository extends MongoRepository<MemberReadHistory, String> {
    Page<MemberReadHistory> findByMemberIdOrderByCreateTimeDesc(Long memberId, Pageable pageable);
    void deleteAllByMemberId(Long memberId);
}
