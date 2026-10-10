package com.nexusengine.core.portal.customer.service;

import com.nexusengine.core.domain.customer.entity.Member;
import com.nexusengine.core.portal.catalog.dto.*;
import com.nexusengine.core.portal.catalog.service.*;
import com.nexusengine.core.portal.catalog.dao.*;
import com.nexusengine.core.portal.payment.service.*;
import com.nexusengine.core.portal.ordering.dto.*;
import com.nexusengine.core.portal.ordering.service.*;
import com.nexusengine.core.portal.ordering.dao.*;
import com.nexusengine.core.portal.customer.dto.*;
import com.nexusengine.core.portal.customer.service.*;
import com.nexusengine.core.portal.promotion.dto.*;
import com.nexusengine.core.portal.promotion.service.*;
import com.nexusengine.core.portal.promotion.strategy.*;
import com.nexusengine.core.portal.ai.service.*;
import com.nexusengine.core.portal.home.dto.*;
import com.nexusengine.core.portal.home.service.*;
import com.nexusengine.core.portal.engagement.dto.*;
import com.nexusengine.core.portal.engagement.service.*;

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


/**
 * Represents the MemberCacheService component.
 * Provides core functionality and operations for MemberCacheService.
 */
public interface MemberCacheService {
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
    Member getMember(String username);

        /**
     * Executes the operation.
     * @param member the member
     */
    void setMember(Member member);

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
