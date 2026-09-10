import re

file_path = '/home/aadarsh/Documents/NexusEngine/NexusCore/nexus-common/src/main/java/com/nexusengine/core/common/service/RedisService.java'

with open(file_path, 'r') as f:
    content = f.read()

# I will replace each match manually in the python script or just generate the file.
new_content = content.replace("/**\n */", "/**\n * Redis service for managing cache and distributed data structures.\n */", 1)

replacements = [
    ("void set(String key, Object value, long time);", "/**\n     * Sets a value in Redis with an expiration time.\n     *\n     * @param key the Redis key\n     * @param value the value to store\n     * @param time the expiration time in seconds\n     */"),
    ("void set(String key, Object value);", "/**\n     * Sets a value in Redis without an expiration time.\n     *\n     * @param key the Redis key\n     * @param value the value to store\n     */"),
    ("Object get(String key);", "/**\n     * Gets a value from Redis by key.\n     *\n     * @param key the Redis key\n     * @return the stored value, or null if not found\n     */"),
    ("Boolean del(String key);", "/**\n     * Deletes a key from Redis.\n     *\n     * @param key the Redis key\n     * @return true if successfully deleted, false otherwise\n     */"),
    ("Long del(List<String> keys);", "/**\n     * Deletes multiple keys from Redis.\n     *\n     * @param keys the list of Redis keys to delete\n     * @return the number of keys successfully deleted\n     */"),
    ("Boolean expire(String key, long time);", "/**\n     * Sets an expiration time for an existing Redis key.\n     *\n     * @param key the Redis key\n     * @param time the expiration time in seconds\n     * @return true if successful, false otherwise\n     */"),
    ("Long getExpire(String key);", "/**\n     * Gets the remaining expiration time for a Redis key.\n     *\n     * @param key the Redis key\n     * @return the remaining time in seconds\n     */"),
    ("Boolean hasKey(String key);", "/**\n     * Checks if a key exists in Redis.\n     *\n     * @param key the Redis key\n     * @return true if the key exists, false otherwise\n     */"),
    ("Long incr(String key, long delta);", "/**\n     * Increments a numeric value stored in Redis.\n     *\n     * @param key the Redis key\n     * @param delta the amount to increment by\n     * @return the new value\n     */"),
    ("Long decr(String key, long delta);", "/**\n     * Decrements a numeric value stored in Redis.\n     *\n     * @param key the Redis key\n     * @param delta the amount to decrement by\n     * @return the new value\n     */"),
    ("Object hGet(String key, String hashKey);", "/**\n     * Gets a value from a Redis hash.\n     *\n     * @param key the Redis key for the hash\n     * @param hashKey the hash field key\n     * @return the hash field value\n     */"),
    ("Boolean hSet(String key, String hashKey, Object value, long time);", "/**\n     * Sets a value in a Redis hash with an expiration time for the entire hash.\n     *\n     * @param key the Redis key for the hash\n     * @param hashKey the hash field key\n     * @param value the hash field value\n     * @param time the expiration time in seconds\n     * @return true if successful\n     */"),
    ("void hSet(String key, String hashKey, Object value);", "/**\n     * Sets a value in a Redis hash.\n     *\n     * @param key the Redis key for the hash\n     * @param hashKey the hash field key\n     * @param value the hash field value\n     */"),
    ("Map<Object, Object> hGetAll(String key);", "/**\n     * Gets all field-value pairs from a Redis hash.\n     *\n     * @param key the Redis key for the hash\n     * @return a map of all hash fields and values\n     */"),
    ("Boolean hSetAll(String key, Map<String, Object> map, long time);", "/**\n     * Sets multiple fields in a Redis hash with an expiration time.\n     *\n     * @param key the Redis key for the hash\n     * @param map a map of field-value pairs to set\n     * @param time the expiration time in seconds\n     * @return true if successful\n     */"),
    ("void hSetAll(String key, Map<String, ?> map);", "/**\n     * Sets multiple fields in a Redis hash.\n     *\n     * @param key the Redis key for the hash\n     * @param map a map of field-value pairs to set\n     */"),
    ("void hDel(String key, Object... hashKey);", "/**\n     * Deletes one or more fields from a Redis hash.\n     *\n     * @param key the Redis key for the hash\n     * @param hashKey the hash field keys to delete\n     */"),
    ("Boolean hHasKey(String key, String hashKey);", "/**\n     * Checks if a field exists in a Redis hash.\n     *\n     * @param key the Redis key for the hash\n     * @param hashKey the hash field key\n     * @return true if the field exists, false otherwise\n     */"),
    ("Long hIncr(String key, String hashKey, Long delta);", "/**\n     * Increments a numeric value in a Redis hash.\n     *\n     * @param key the Redis key for the hash\n     * @param hashKey the hash field key\n     * @param delta the amount to increment by\n     * @return the new value\n     */"),
    ("Long hDecr(String key, String hashKey, Long delta);", "/**\n     * Decrements a numeric value in a Redis hash.\n     *\n     * @param key the Redis key for the hash\n     * @param hashKey the hash field key\n     * @param delta the amount to decrement by\n     * @return the new value\n     */"),
    ("Set<Object> sMembers(String key);", "/**\n     * Gets all members of a Redis set.\n     *\n     * @param key the Redis key for the set\n     * @return a set of all members\n     */"),
    ("Long sAdd(String key, Object... values);", "/**\n     * Adds one or more members to a Redis set.\n     *\n     * @param key the Redis key for the set\n     * @param values the members to add\n     * @return the number of members successfully added\n     */"),
    ("Long sAdd(String key, long time, Object... values);", "/**\n     * Adds one or more members to a Redis set with an expiration time.\n     *\n     * @param key the Redis key for the set\n     * @param time the expiration time in seconds\n     * @param values the members to add\n     * @return the number of members successfully added\n     */"),
    ("Boolean sIsMember(String key, Object value);", "/**\n     * Checks if a value is a member of a Redis set.\n     *\n     * @param key the Redis key for the set\n     * @param value the value to check\n     * @return true if the value is a member, false otherwise\n     */"),
    ("Long sSize(String key);", "/**\n     * Gets the number of members in a Redis set.\n     *\n     * @param key the Redis key for the set\n     * @return the size of the set\n     */"),
    ("Long sRemove(String key, Object... values);", "/**\n     * Removes one or more members from a Redis set.\n     *\n     * @param key the Redis key for the set\n     * @param values the members to remove\n     * @return the number of members successfully removed\n     */"),
    ("List<Object> lRange(String key, long start, long end);", "/**\n     * Gets a range of elements from a Redis list.\n     *\n     * @param key the Redis key for the list\n     * @param start the starting index\n     * @param end the ending index\n     * @return a list of elements in the specified range\n     */"),
    ("Long lSize(String key);", "/**\n     * Gets the size of a Redis list.\n     *\n     * @param key the Redis key for the list\n     * @return the size of the list\n     */"),
    ("Object lIndex(String key, long index);", "/**\n     * Gets an element from a Redis list by its index.\n     *\n     * @param key the Redis key for the list\n     * @param index the index of the element\n     * @return the element at the specified index\n     */"),
    ("Long lPush(String key, Object value);", "/**\n     * Pushes a value onto a Redis list.\n     *\n     * @param key the Redis key for the list\n     * @param value the value to push\n     * @return the size of the list after the push operation\n     */"),
    ("Long lPush(String key, Object value, long time);", "/**\n     * Pushes a value onto a Redis list with an expiration time.\n     *\n     * @param key the Redis key for the list\n     * @param value the value to push\n     * @param time the expiration time in seconds\n     * @return the size of the list after the push operation\n     */"),
    ("Long lPushAll(String key, Object... values);", "/**\n     * Pushes multiple values onto a Redis list.\n     *\n     * @param key the Redis key for the list\n     * @param values the values to push\n     * @return the size of the list after the push operation\n     */"),
    ("Long lPushAll(String key, Long time, Object... values);", "/**\n     * Pushes multiple values onto a Redis list with an expiration time.\n     *\n     * @param key the Redis key for the list\n     * @param time the expiration time in seconds\n     * @param values the values to push\n     * @return the size of the list after the push operation\n     */"),
    ("Long lRemove(String key, long count, Object value);", "/**\n     * Removes instances of a value from a Redis list.\n     *\n     * @param key the Redis key for the list\n     * @param count the number of occurrences to remove (0 for all)\n     * @param value the value to remove\n     * @return the number of elements successfully removed\n     */")
]

for old, new in replacements:
    new_content = new_content.replace(f"    /**\n     */\n    {old}", f"{new}\n    {old}")

with open(file_path, 'w') as f:
    f.write(new_content)
