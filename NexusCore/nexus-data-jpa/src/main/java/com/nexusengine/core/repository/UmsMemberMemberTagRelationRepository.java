package com.nexusengine.core.repository;

import com.nexusengine.core.model.UmsMemberMemberTagRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UmsMemberMemberTagRelationRepository extends JpaRepository<UmsMemberMemberTagRelation, Long>, JpaSpecificationExecutor<UmsMemberMemberTagRelation> {
}
