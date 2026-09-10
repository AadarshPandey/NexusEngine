import re

file_path = '/home/aadarsh/Documents/NexusEngine/NexusCore/nexus-admin/src/main/java/com/nexusengine/core/service/UmsAdminService.java'

with open(file_path, 'r') as f:
    content = f.read()

content = content.replace("/**\n */\npublic interface UmsAdminService", "/**\n * Service interface for managing user management system (UMS) administrators.\n * Handles admin registration, login, role assignment, and profile updates.\n */\npublic interface UmsAdminService", 1)

replacements = [
    ("UmsAdmin getAdminByUsername(String username);", "/**\n     * Retrieves an administrator by their unique username.\n     *\n     * @param username the username of the administrator\n     * @return the associated UmsAdmin, or null if not found\n     */"),
    ("UmsAdmin register(UmsAdminParam umsAdminParam);", "/**\n     * Registers a new administrator in the system.\n     *\n     * @param umsAdminParam the registration parameters containing admin details\n     * @return the newly registered UmsAdmin\n     */"),
    ("String login(String username,String password);", "/**\n     * Authenticates an administrator and generates a JWT token.\n     *\n     * @param username the admin username\n     * @param password the admin password\n     * @return the generated JWT token\n     */"),
    ("String refreshToken(String oldToken);", "/**\n     * Refreshes an existing valid or recently expired JWT token.\n     *\n     * @param oldToken the old JWT token\n     * @return a new JWT token\n     */"),
    ("UmsAdmin getItem(Long id);", "/**\n     * Retrieves an administrator by their unique ID.\n     *\n     * @param id the unique identifier of the administrator\n     * @return the associated UmsAdmin\n     */"),
    ("List<UmsAdmin> list(String keyword, Integer pageSize, Integer pageNum);", "/**\n     * Retrieves a paginated list of administrators, optionally filtered by a keyword.\n     *\n     * @param keyword the search keyword (e.g., username or nickname)\n     * @param pageSize the number of items per page\n     * @param pageNum the current page number\n     * @return a list of administrators matching the criteria\n     */"),
    ("int update(Long id, UmsAdmin admin);", "/**\n     * Updates the profile information of an existing administrator.\n     *\n     * @param id the unique identifier of the administrator to update\n     * @param admin the updated administrator data\n     * @return the number of updated records\n     */"),
    ("int delete(Long id);", "/**\n     * Deletes an administrator by their unique ID.\n     *\n     * @param id the unique identifier of the administrator to delete\n     * @return the number of deleted records\n     */"),
    ("@Transactional\n    int updateRole(Long adminId, List<Long> roleIds);", "/**\n     * Updates the roles assigned to an administrator.\n     *\n     * @param adminId the unique identifier of the administrator\n     * @param roleIds the list of role IDs to assign\n     * @return the number of updated roles\n     */"),
    ("List<UmsRole> getRoleList(Long adminId);", "/**\n     * Retrieves all roles assigned to a specific administrator.\n     *\n     * @param adminId the unique identifier of the administrator\n     * @return a list of assigned roles\n     */"),
    ("List<UmsResource> getResourceList(Long adminId);", "/**\n     * Retrieves all accessible resources for a specific administrator based on their roles.\n     *\n     * @param adminId the unique identifier of the administrator\n     * @return a list of accessible resources\n     */"),
    ("int updatePassword(UpdateAdminPasswordParam updatePasswordParam);", "/**\n     * Updates the password for an administrator.\n     *\n     * @param updatePasswordParam the parameters containing the username, old password, and new password\n     * @return the status of the update operation (e.g., 1 for success)\n     */"),
    ("UserDetails loadUserByUsername(String username);", "/**\n     * Loads user details for authentication based on the username.\n     *\n     * @param username the username to load details for\n     * @return the UserDetails required by Spring Security\n     */"),
    ("UmsAdminCacheService getCacheService();", "/**\n     * Retrieves the caching service associated with administrator operations.\n     *\n     * @return the UmsAdminCacheService instance\n     */"),
    ("void logout(String username);", "/**\n     * Logs out an administrator by invalidating their current session or token.\n     *\n     * @param username the username of the administrator to log out\n     */")
]

for old, new in replacements:
    content = content.replace(f"    /**\n     */\n    {old}", f"{new}\n    {old}")
    
with open(file_path, 'w') as f:
    f.write(content)

