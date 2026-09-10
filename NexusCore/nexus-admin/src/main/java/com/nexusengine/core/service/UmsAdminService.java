package com.nexusengine.core.service;

import com.nexusengine.core.dto.UmsAdminParam;
import com.nexusengine.core.dto.UpdateAdminPasswordParam;
import com.nexusengine.core.model.UmsAdmin;
import com.nexusengine.core.model.UmsResource;
import com.nexusengine.core.model.UmsRole;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service interface for managing user management system (UMS) administrators.
 * Handles admin registration, login, role assignment, and profile updates.
 */
public interface UmsAdminService {
/**
     * Retrieves an administrator by their unique username.
     *
     * @param username the username of the administrator
     * @return the associated UmsAdmin, or null if not found
     */
    UmsAdmin getAdminByUsername(String username);

/**
     * Registers a new administrator in the system.
     *
     * @param umsAdminParam the registration parameters containing admin details
     * @return the newly registered UmsAdmin
     */
    UmsAdmin register(UmsAdminParam umsAdminParam);

/**
     * Authenticates an administrator and generates a JWT token.
     *
     * @param username the admin username
     * @param password the admin password
     * @return the generated JWT token
     */
    String login(String username,String password);

/**
     * Refreshes an existing valid or recently expired JWT token.
     *
     * @param oldToken the old JWT token
     * @return a new JWT token
     */
    String refreshToken(String oldToken);

/**
     * Retrieves an administrator by their unique ID.
     *
     * @param id the unique identifier of the administrator
     * @return the associated UmsAdmin
     */
    UmsAdmin getItem(Long id);

/**
     * Retrieves a paginated list of administrators, optionally filtered by a keyword.
     *
     * @param keyword the search keyword (e.g., username or nickname)
     * @param pageSize the number of items per page
     * @param pageNum the current page number
     * @return a list of administrators matching the criteria
     */
    List<UmsAdmin> list(String keyword, Integer pageSize, Integer pageNum);

/**
     * Updates the profile information of an existing administrator.
     *
     * @param id the unique identifier of the administrator to update
     * @param admin the updated administrator data
     * @return the number of updated records
     */
    int update(Long id, UmsAdmin admin);

/**
     * Deletes an administrator by their unique ID.
     *
     * @param id the unique identifier of the administrator to delete
     * @return the number of deleted records
     */
    int delete(Long id);

/**
     * Updates the roles assigned to an administrator.
     *
     * @param adminId the unique identifier of the administrator
     * @param roleIds the list of role IDs to assign
     * @return the number of updated roles
     */
    @Transactional
    int updateRole(Long adminId, List<Long> roleIds);

/**
     * Retrieves all roles assigned to a specific administrator.
     *
     * @param adminId the unique identifier of the administrator
     * @return a list of assigned roles
     */
    List<UmsRole> getRoleList(Long adminId);

/**
     * Retrieves all accessible resources for a specific administrator based on their roles.
     *
     * @param adminId the unique identifier of the administrator
     * @return a list of accessible resources
     */
    List<UmsResource> getResourceList(Long adminId);

/**
     * Updates the password for an administrator.
     *
     * @param updatePasswordParam the parameters containing the username, old password, and new password
     * @return the status of the update operation (e.g., 1 for success)
     */
    int updatePassword(UpdateAdminPasswordParam updatePasswordParam);

/**
     * Loads user details for authentication based on the username.
     *
     * @param username the username to load details for
     * @return the UserDetails required by Spring Security
     */
    UserDetails loadUserByUsername(String username);

/**
     * Retrieves the caching service associated with administrator operations.
     *
     * @return the UmsAdminCacheService instance
     */
    UmsAdminCacheService getCacheService();

/**
     * Logs out an administrator by invalidating their current session or token.
     *
     * @param username the username of the administrator to log out
     */
    void logout(String username);
}
