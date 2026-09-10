import re

file_path = '/home/aadarsh/Documents/NexusEngine/NexusCore/nexus-common/src/main/java/com/nexusengine/core/common/api/CommonResult.java'

with open(file_path, 'r') as f:
    content = f.read()

content = content.replace("/**\n */\npublic class CommonResult<T>", "/**\n * A generic wrapper class for API responses.\n * Standardizes the structure of responses with code, message, and data fields.\n *\n * @param <T> the type of the payload data\n */\npublic class CommonResult<T>", 1)

content = content.replace("    /**\n     */\n    private long code;", "    /**\n     * The status code of the response.\n     */\n    private long code;")
content = content.replace("    /**\n     */\n    private String message;", "    /**\n     * The descriptive message associated with the response status.\n     */\n    private String message;")
content = content.replace("    /**\n     */\n    private T data;", "    /**\n     * The payload data of the response.\n     */\n    private T data;")

content = content.replace("    /**\n     *\n     */\n    public static <T> CommonResult<T> success(T data)", "    /**\n     * Returns a successful response with the given data.\n     *\n     * @param data the payload data\n     * @param <T> the type of the payload\n     * @return a successful CommonResult\n     */\n    public static <T> CommonResult<T> success(T data)")

content = content.replace("    /**\n     *\n     */\n    public static <T> CommonResult<T> success(T data, String message)", "    /**\n     * Returns a successful response with the given data and a custom message.\n     *\n     * @param data the payload data\n     * @param message the custom success message\n     * @param <T> the type of the payload\n     * @return a successful CommonResult\n     */\n    public static <T> CommonResult<T> success(T data, String message)")

replacements = [
    ("public static <T> CommonResult<T> failed(IErrorCode errorCode)", "/**\n     * Returns a failed response based on the provided error code.\n     *\n     * @param errorCode the error code interface implementation\n     * @param <T> the type of the payload\n     * @return a failed CommonResult\n     */"),
    ("public static <T> CommonResult<T> failed(IErrorCode errorCode,String message)", "/**\n     * Returns a failed response based on the provided error code and a custom message.\n     *\n     * @param errorCode the error code interface implementation\n     * @param message the custom failure message\n     * @param <T> the type of the payload\n     * @return a failed CommonResult\n     */"),
    ("public static <T> CommonResult<T> failed(String message)", "/**\n     * Returns a failed response with a custom failure message.\n     *\n     * @param message the custom failure message\n     * @param <T> the type of the payload\n     * @return a failed CommonResult\n     */"),
    ("public static <T> CommonResult<T> failed()", "/**\n     * Returns a generic failed response with default failure code and message.\n     *\n     * @param <T> the type of the payload\n     * @return a failed CommonResult\n     */"),
    ("public static <T> CommonResult<T> validateFailed()", "/**\n     * Returns a failed response indicating validation failure with default settings.\n     *\n     * @param <T> the type of the payload\n     * @return a validation-failed CommonResult\n     */"),
    ("public static <T> CommonResult<T> validateFailed(String message)", "/**\n     * Returns a failed response indicating validation failure with a custom message.\n     *\n     * @param message the custom validation failure message\n     * @param <T> the type of the payload\n     * @return a validation-failed CommonResult\n     */"),
    ("public static <T> CommonResult<T> unauthorized(T data)", "/**\n     * Returns an unauthorized response, typically when authentication fails.\n     *\n     * @param data the payload data or context for the unauthorized access\n     * @param <T> the type of the payload\n     * @return an unauthorized CommonResult\n     */"),
    ("public static <T> CommonResult<T> forbidden(T data)", "/**\n     * Returns a forbidden response, typically when access is denied due to permissions.\n     *\n     * @param data the payload data or context for the forbidden access\n     * @param <T> the type of the payload\n     * @return a forbidden CommonResult\n     */")
]

for old, new in replacements:
    content = content.replace(f"    /**\n     */\n    {old}", f"{new}\n    {old}")
    
with open(file_path, 'w') as f:
    f.write(content)

