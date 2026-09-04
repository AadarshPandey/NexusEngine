package com.nexusengine.core.portal.service;

import com.nexusengine.core.portal.domain.MemberProductCollection;
import org.springframework.data.domain.Page;

/**
 * Auto-generated documentation
 * Created by macro on 2018/8/2.
 */
public interface MemberCollectionService {
    /**
     * Auto-generated documentation
     */
    int add(MemberProductCollection productCollection);

    /**
     * Auto-generated documentation
     */
    int delete(Long productId);

    /**
     * Auto-generated documentation
     */
    Page<MemberProductCollection> list(Integer pageNum, Integer pageSize);

    /**
     * Auto-generated documentation
     */
    MemberProductCollection detail(Long productId);

    /**
     * Auto-generated documentation
     */
    void clear();
}
