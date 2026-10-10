package com.nexusengine.core.domain.customer.repository;

import com.nexusengine.core.domain.customer.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long>, JpaSpecificationExecutor<Member> {
    Member findByUsername(String username);
    Member findByEmail(String email);
    List<Member> findByUsernameOrEmail(String username, String email);
}
