package com.nexusengine.core.portal.service;

import com.nexusengine.core.model.UmsMember;

/**
 * Represents the UmsMemberCacheService component.
 * Provides core functionality and operations for UmsMemberCacheService.
 */
public interface UmsMemberCacheService {
        /**
     * Executes the operation.
     * @param memberId the memberId
     */
    void delMember(Long memberId);

        /**
     * Executes the operation.
     * @param username the username
     * @return the result of the operation
     */
    UmsMember getMember(String username);

        /**
     * Executes the operation.
     * @param member the member
     */
    void setMember(UmsMember member);

        /**
     * Executes the operation.
     * @param telephone the telephone
     * @param authCode the authCode
     */
    void setAuthCode(String telephone, String authCode);

        /**
     * Executes the operation.
     * @param telephone the telephone
     * @return the result of the operation
     */
    String getAuthCode(String telephone);

        /**
     * Executes the operation.
     * @param telephone the telephone
     */
    void delAuthCode(String telephone);
}
