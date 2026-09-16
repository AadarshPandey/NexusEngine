package com.nexusengine.core.portal.service;

import com.nexusengine.core.model.UmsMember;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service interface for managing user members in the portal.
 * Handles member registration, authentication, profile retrieval, and security operations.
 */
public interface UmsMemberService {
        /**
     * Retrieves a member by their username.
     *
     * @param username the username of the member
     * @return the associated UmsMember
     */
    UmsMember getByUsername(String username);

        /**
     * Retrieves a member by their unique ID.
     *
     * @param id the unique identifier of the member
     * @return the associated UmsMember
     */
    UmsMember getById(Long id);

        /**
     * Registers a new member in the portal.
     *
     * @param username the desired username
     * @param password the desired password
     * @param email the member's email address
     * @param authCode the verification code sent to the email
     */
    @Transactional
    void register(String username, String password, String email, String authCode);

        /**
     * Generates and sends an authentication code to the specified email.
     *
     * @param email the email address to send the code to
     */
    void generateAuthCode(String email);

        /**
     * Updates a member's password using an authentication code.
     *
     * @param email the member's email address
     * @param password the new password
     * @param authCode the verification code sent to the email
     */
    @Transactional
    void updatePassword(String email, String password, String authCode);

        /**
     * Retrieves the currently authenticated member.
     *
     * @return the current UmsMember
     */
    UmsMember getCurrentMember();
    void updateMember(UmsMember member);

        /**
     * Updates the integration (points) for a specific member.
     *
     * @param id the unique identifier of the member
     * @param integration the integration points to set or add
     */
    void updateIntegration(Long id,Integer integration);


        /**
     * Loads user details for Spring Security authentication.
     *
     * @param username the username to load details for
     * @return the UserDetails required by Spring Security
     */
    UserDetails loadUserByUsername(String username);

        /**
     * Authenticates a member and generates a JWT token.
     *
     * @param username the member username
     * @param password the member password
     * @return the generated JWT token
     */
    String login(String username, String password);

        /**
     * Refreshes an existing valid or recently expired JWT token.
     *
     * @param token the old JWT token
     * @return a new JWT token
     */
    String refreshToken(String token);
}
