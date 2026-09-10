import re
import os

files_data = {
    '/home/aadarsh/Documents/NexusEngine/NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/UmsMemberService.java': [
        (r'/\*\*\s*\*\s*Represents the UmsMemberService component.*?\*/', '/**\n * Service interface for managing user members in the portal.\n * Handles member registration, authentication, profile retrieval, and security operations.\n */'),
        (r'/\*\*\s*\*\s*Executes the operation\.\s*\*\s*@param username the username\s*\*\s*@return the result of the operation\s*\*/\s*UmsMember getByUsername\(String username\);', '/**\n     * Retrieves a member by their username.\n     *\n     * @param username the username of the member\n     * @return the associated UmsMember\n     */\n    UmsMember getByUsername(String username);'),
        (r'/\*\*\s*\*\s*Executes the operation\.\s*\*\s*@param id the id\s*\*\s*@return the result of the operation\s*\*/\s*UmsMember getById\(Long id\);', '/**\n     * Retrieves a member by their unique ID.\n     *\n     * @param id the unique identifier of the member\n     * @return the associated UmsMember\n     */\n    UmsMember getById(Long id);'),
        (r'/\*\*\s*\*\s*Executes the operation\.\s*\*\s*@param username the username\s*\*\s*@param password the password\s*\*\s*@param email the email\s*\*\s*@param authCode the authCode\s*\*/\s*@Transactional\s*void register\(String username, String password, String email, String authCode\);', '/**\n     * Registers a new member in the portal.\n     *\n     * @param username the desired username\n     * @param password the desired password\n     * @param email the member\'s email address\n     * @param authCode the verification code sent to the email\n     */\n    @Transactional\n    void register(String username, String password, String email, String authCode);'),
        (r'/\*\*\s*\*\s*Executes the operation\.\s*\*\s*@param email the email\s*\*/\s*void generateAuthCode\(String email\);', '/**\n     * Generates and sends an authentication code to the specified email.\n     *\n     * @param email the email address to send the code to\n     */\n    void generateAuthCode(String email);'),
        (r'/\*\*\s*\*\s*Executes the operation\.\s*\*\s*@param email the email\s*\*\s*@param password the password\s*\*\s*@param authCode the authCode\s*\*/\s*@Transactional\s*void updatePassword\(String email, String password, String authCode\);', '/**\n     * Updates a member\'s password using an authentication code.\n     *\n     * @param email the member\'s email address\n     * @param password the new password\n     * @param authCode the verification code sent to the email\n     */\n    @Transactional\n    void updatePassword(String email, String password, String authCode);'),
        (r'/\*\*\s*\*\s*Executes the operation\.\s*\*\s*@return the result of the operation\s*\*/\s*UmsMember getCurrentMember\(\);', '/**\n     * Retrieves the currently authenticated member.\n     *\n     * @return the current UmsMember\n     */\n    UmsMember getCurrentMember();'),
        (r'/\*\*\s*\*\s*Executes the operation\.\s*\*\s*@param id the id\s*\*\s*@param integration the integration\s*\*/\s*void updateIntegration\(Long id,Integer integration\);', '/**\n     * Updates the integration (points) for a specific member.\n     *\n     * @param id the unique identifier of the member\n     * @param integration the integration points to set or add\n     */\n    void updateIntegration(Long id,Integer integration);'),
        (r'/\*\*\s*\*\s*Executes the operation\.\s*\*\s*@param username the username\s*\*\s*@return the result of the operation\s*\*/\s*UserDetails loadUserByUsername\(String username\);', '/**\n     * Loads user details for Spring Security authentication.\n     *\n     * @param username the username to load details for\n     * @return the UserDetails required by Spring Security\n     */\n    UserDetails loadUserByUsername(String username);'),
        (r'/\*\*\s*\*\s*Executes the operation\.\s*\*\s*@param username the username\s*\*\s*@param password the password\s*\*\s*@return the result of the operation\s*\*/\s*String login\(String username, String password\);', '/**\n     * Authenticates a member and generates a JWT token.\n     *\n     * @param username the member username\n     * @param password the member password\n     * @return the generated JWT token\n     */\n    String login(String username, String password);'),
        (r'/\*\*\s*\*\s*Executes the operation\.\s*\*\s*@param token the token\s*\*\s*@return the result of the operation\s*\*/\s*String refreshToken\(String token\);', '/**\n     * Refreshes an existing valid or recently expired JWT token.\n     *\n     * @param token the old JWT token\n     * @return a new JWT token\n     */\n    String refreshToken(String token);')
    ]
}

for fp, replacements in files_data.items():
    if os.path.exists(fp):
        with open(fp, 'r') as f:
            content = f.read()
        for old_regex, new_val in replacements:
            content = re.sub(old_regex, new_val, content, flags=re.DOTALL)
        with open(fp, 'w') as f:
            f.write(content)

