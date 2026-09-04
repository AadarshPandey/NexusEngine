package com.nexusengine.core.portal.service;

import com.nexusengine.core.model.UmsMember;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

/**
 * Auto-generated documentation
 * Created by macro on 2018/8/3.
 */
public interface UmsMemberService {
    /**
     * Auto-generated documentation
     */
    UmsMember getByUsername(String username);

    /**
     * Auto-generated documentation
     */
    UmsMember getById(Long id);

    /**
     * Auto-generated documentation
     */
    @Transactional
    void register(String username, String password, String telephone, String authCode);

    /**
     * Auto-generated documentation
     */
    String generateAuthCode(String telephone);

    /**
     * Auto-generated documentation
     */
    @Transactional
    void updatePassword(String telephone, String password, String authCode);

    /**
     * Auto-generated documentation
     */
    UmsMember getCurrentMember();

    /**
     * Auto-generated documentation
     */
    void updateIntegration(Long id,Integer integration);


    /**
     * Auto-generated documentation
     */
    UserDetails loadUserByUsername(String username);

    /**
     * Auto-generated documentation
     */
    String login(String username, String password);

    /**
     * Auto-generated documentation
     */
    String refreshToken(String token);
}
