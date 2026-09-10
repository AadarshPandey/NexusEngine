package com.nexusengine.core.common.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Redis service for managing cache and distributed data structures.
 */
public interface RedisService {

/**
     * Sets a value in Redis with an expiration time.
     *
     * @param key the Redis key
     * @param value the value to store
     * @param time the expiration time in seconds
     */
    void set(String key, Object value, long time);

/**
     * Sets a value in Redis without an expiration time.
     *
     * @param key the Redis key
     * @param value the value to store
     */
    void set(String key, Object value);

/**
     * Gets a value from Redis by key.
     *
     * @param key the Redis key
     * @return the stored value, or null if not found
     */
    Object get(String key);

/**
     * Deletes a key from Redis.
     *
     * @param key the Redis key
     * @return true if successfully deleted, false otherwise
     */
    Boolean del(String key);

/**
     * Deletes multiple keys from Redis.
     *
     * @param keys the list of Redis keys to delete
     * @return the number of keys successfully deleted
     */
    Long del(List<String> keys);

/**
     * Sets an expiration time for an existing Redis key.
     *
     * @param key the Redis key
     * @param time the expiration time in seconds
     * @return true if successful, false otherwise
     */
    Boolean expire(String key, long time);

/**
     * Gets the remaining expiration time for a Redis key.
     *
     * @param key the Redis key
     * @return the remaining time in seconds
     */
    Long getExpire(String key);

/**
     * Checks if a key exists in Redis.
     *
     * @param key the Redis key
     * @return true if the key exists, false otherwise
     */
    Boolean hasKey(String key);

/**
     * Increments a numeric value stored in Redis.
     *
     * @param key the Redis key
     * @param delta the amount to increment by
     * @return the new value
     */
    Long incr(String key, long delta);

/**
     * Decrements a numeric value stored in Redis.
     *
     * @param key the Redis key
     * @param delta the amount to decrement by
     * @return the new value
     */
    Long decr(String key, long delta);

/**
     * Gets a value from a Redis hash.
     *
     * @param key the Redis key for the hash
     * @param hashKey the hash field key
     * @return the hash field value
     */
    Object hGet(String key, String hashKey);

/**
     * Sets a value in a Redis hash with an expiration time for the entire hash.
     *
     * @param key the Redis key for the hash
     * @param hashKey the hash field key
     * @param value the hash field value
     * @param time the expiration time in seconds
     * @return true if successful
     */
    Boolean hSet(String key, String hashKey, Object value, long time);

/**
     * Sets a value in a Redis hash.
     *
     * @param key the Redis key for the hash
     * @param hashKey the hash field key
     * @param value the hash field value
     */
    void hSet(String key, String hashKey, Object value);

/**
     * Gets all field-value pairs from a Redis hash.
     *
     * @param key the Redis key for the hash
     * @return a map of all hash fields and values
     */
    Map<Object, Object> hGetAll(String key);

/**
     * Sets multiple fields in a Redis hash with an expiration time.
     *
     * @param key the Redis key for the hash
     * @param map a map of field-value pairs to set
     * @param time the expiration time in seconds
     * @return true if successful
     */
    Boolean hSetAll(String key, Map<String, Object> map, long time);

/**
     * Sets multiple fields in a Redis hash.
     *
     * @param key the Redis key for the hash
     * @param map a map of field-value pairs to set
     */
    void hSetAll(String key, Map<String, ?> map);

/**
     * Deletes one or more fields from a Redis hash.
     *
     * @param key the Redis key for the hash
     * @param hashKey the hash field keys to delete
     */
    void hDel(String key, Object... hashKey);

/**
     * Checks if a field exists in a Redis hash.
     *
     * @param key the Redis key for the hash
     * @param hashKey the hash field key
     * @return true if the field exists, false otherwise
     */
    Boolean hHasKey(String key, String hashKey);

/**
     * Increments a numeric value in a Redis hash.
     *
     * @param key the Redis key for the hash
     * @param hashKey the hash field key
     * @param delta the amount to increment by
     * @return the new value
     */
    Long hIncr(String key, String hashKey, Long delta);

/**
     * Decrements a numeric value in a Redis hash.
     *
     * @param key the Redis key for the hash
     * @param hashKey the hash field key
     * @param delta the amount to decrement by
     * @return the new value
     */
    Long hDecr(String key, String hashKey, Long delta);

/**
     * Gets all members of a Redis set.
     *
     * @param key the Redis key for the set
     * @return a set of all members
     */
    Set<Object> sMembers(String key);

/**
     * Adds one or more members to a Redis set.
     *
     * @param key the Redis key for the set
     * @param values the members to add
     * @return the number of members successfully added
     */
    Long sAdd(String key, Object... values);

/**
     * Adds one or more members to a Redis set with an expiration time.
     *
     * @param key the Redis key for the set
     * @param time the expiration time in seconds
     * @param values the members to add
     * @return the number of members successfully added
     */
    Long sAdd(String key, long time, Object... values);

/**
     * Checks if a value is a member of a Redis set.
     *
     * @param key the Redis key for the set
     * @param value the value to check
     * @return true if the value is a member, false otherwise
     */
    Boolean sIsMember(String key, Object value);

/**
     * Gets the number of members in a Redis set.
     *
     * @param key the Redis key for the set
     * @return the size of the set
     */
    Long sSize(String key);

/**
     * Removes one or more members from a Redis set.
     *
     * @param key the Redis key for the set
     * @param values the members to remove
     * @return the number of members successfully removed
     */
    Long sRemove(String key, Object... values);

/**
     * Gets a range of elements from a Redis list.
     *
     * @param key the Redis key for the list
     * @param start the starting index
     * @param end the ending index
     * @return a list of elements in the specified range
     */
    List<Object> lRange(String key, long start, long end);

/**
     * Gets the size of a Redis list.
     *
     * @param key the Redis key for the list
     * @return the size of the list
     */
    Long lSize(String key);

/**
     * Gets an element from a Redis list by its index.
     *
     * @param key the Redis key for the list
     * @param index the index of the element
     * @return the element at the specified index
     */
    Object lIndex(String key, long index);

/**
     * Pushes a value onto a Redis list.
     *
     * @param key the Redis key for the list
     * @param value the value to push
     * @return the size of the list after the push operation
     */
    Long lPush(String key, Object value);

/**
     * Pushes a value onto a Redis list with an expiration time.
     *
     * @param key the Redis key for the list
     * @param value the value to push
     * @param time the expiration time in seconds
     * @return the size of the list after the push operation
     */
    Long lPush(String key, Object value, long time);

/**
     * Pushes multiple values onto a Redis list.
     *
     * @param key the Redis key for the list
     * @param values the values to push
     * @return the size of the list after the push operation
     */
    Long lPushAll(String key, Object... values);

/**
     * Pushes multiple values onto a Redis list with an expiration time.
     *
     * @param key the Redis key for the list
     * @param time the expiration time in seconds
     * @param values the values to push
     * @return the size of the list after the push operation
     */
    Long lPushAll(String key, Long time, Object... values);

/**
     * Removes instances of a value from a Redis list.
     *
     * @param key the Redis key for the list
     * @param count the number of occurrences to remove (0 for all)
     * @param value the value to remove
     * @return the number of elements successfully removed
     */
    Long lRemove(String key, long count, Object value);
}