package com.nexusengine.core.portal.service;

import com.nexusengine.core.model.UmsMember;

/**
 * Auto-generated documentation
 * Created by macro on 2020/3/14.
 */
public interface UmsMemberCacheService {
    /**
     * Auto-generated documentation
     */
    void delMember(Long memberId);

    /**
     * Auto-generated documentation
     */
    UmsMember getMember(String username);

    /**
     * Auto-generated documentation
     */
    void setMember(UmsMember member);

    /**
     * Auto-generated documentation
     */
    void setAuthCode(String telephone, String authCode);

    /**
     * Auto-generated documentation
     */
    String getAuthCode(String telephone);
}
