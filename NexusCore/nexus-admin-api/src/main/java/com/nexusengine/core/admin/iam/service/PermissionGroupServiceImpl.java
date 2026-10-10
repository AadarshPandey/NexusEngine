package com.nexusengine.core.admin.iam.service;

import lombok.RequiredArgsConstructor;

import com.nexusengine.core.domain.iam.repository.PermissionGroupRepository;

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
import com.nexusengine.core.admin.catalog.dto.*;
import com.nexusengine.core.admin.catalog.service.*;
import com.nexusengine.core.admin.analytics.service.*;
import com.nexusengine.core.admin.ordering.dto.*;
import com.nexusengine.core.admin.ordering.service.*;
import com.nexusengine.core.admin.promotion.dto.*;
import com.nexusengine.core.admin.promotion.service.*;
import com.nexusengine.core.admin.ai.service.*;
import com.nexusengine.core.admin.infra.dto.*;
import com.nexusengine.core.admin.infra.service.*;
import com.nexusengine.core.admin.iam.dto.*;
import com.nexusengine.core.admin.iam.service.*;
import com.nexusengine.core.domain.iam.entity.PermissionGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PermissionGroupServiceImpl implements PermissionGroupService {
    private final PermissionGroupRepository resourceCategoryRepository;

    @Override
    public List<PermissionGroup> listAll() {
        return resourceCategoryRepository.findAll(Sort.by(Sort.Direction.DESC, "sort"));
    }

    @Override
    public int create(PermissionGroup permissionGroup) {
        permissionGroup.setCreateTime(java.time.OffsetDateTime.now());
        resourceCategoryRepository.save(permissionGroup);
        return 1;
    }

    @Override
    public int update(Long id, PermissionGroup permissionGroup) {
        permissionGroup.setId(id);
        resourceCategoryRepository.save(permissionGroup);
        return 1;
    }

    @Override
    public int delete(Long id) {
        resourceCategoryRepository.deleteById(id);
        return 1;
    }
}
