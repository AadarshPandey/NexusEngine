package com.nexusengine.core.repository;

import com.nexusengine.core.model.UmsMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface UmsMemberRepository extends JpaRepository<UmsMember, Long>, JpaSpecificationExecutor<UmsMember> {
    UmsMember findByUsername(String username);
    UmsMember findByEmail(String email);
    List<UmsMember> findByUsernameOrEmail(String username, String email);
}
