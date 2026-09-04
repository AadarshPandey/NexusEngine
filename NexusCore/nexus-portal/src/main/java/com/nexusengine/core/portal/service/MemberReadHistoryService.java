package com.nexusengine.core.portal.service;

import com.nexusengine.core.portal.domain.MemberReadHistory;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/8/3.
 */
public interface MemberReadHistoryService {
    /**
     * Auto-generated documentation
     */
    int create(MemberReadHistory memberReadHistory);

    /**
     * Auto-generated documentation
     */
    int delete(List<String> ids);

    /**
     * Auto-generated documentation
     */
    Page<MemberReadHistory> list(Integer pageNum, Integer pageSize);

    /**
     * Auto-generated documentation
     */
    void clear();
}
