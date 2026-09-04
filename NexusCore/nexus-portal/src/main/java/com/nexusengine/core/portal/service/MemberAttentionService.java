package com.nexusengine.core.portal.service;

import com.nexusengine.core.portal.domain.MemberBrandAttention;
import org.springframework.data.domain.Page;

/**
 * Auto-generated documentation
 * Created by macro on 2018/8/2.
 */
public interface MemberAttentionService {
    /**
     * Auto-generated documentation
     */
    int add(MemberBrandAttention memberBrandAttention);

    /**
     * Auto-generated documentation
     */
    int delete(Long brandId);

    /**
     * Auto-generated documentation
     */
    Page<MemberBrandAttention> list(Integer pageNum, Integer pageSize);

    /**
     * Auto-generated documentation
     */
    MemberBrandAttention detail(Long brandId);

    /**
     * Auto-generated documentation
     */
    void clear();
}
