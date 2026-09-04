package com.nexusengine.core.common.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Auto-generated documentation
 * Created by macro on 2020/3/3.
 */
public interface RedisService {

    /**
     * Auto-generated documentation
     */
    void set(String key, Object value, long time);

    /**
     * Auto-generated documentation
     */
    void set(String key, Object value);

    /**
     * Auto-generated documentation
     */
    Object get(String key);

    /**
     * Auto-generated documentation
     */
    Boolean del(String key);

    /**
     * Auto-generated documentation
     */
    Long del(List<String> keys);

    /**
     * Auto-generated documentation
     */
    Boolean expire(String key, long time);

    /**
     * Auto-generated documentation
     */
    Long getExpire(String key);

    /**
     * Auto-generated documentation
     */
    Boolean hasKey(String key);

    /**
     * Auto-generated documentation
     */
    Long incr(String key, long delta);

    /**
     * Auto-generated documentation
     */
    Long decr(String key, long delta);

    /**
     * Auto-generated documentation
     */
    Object hGet(String key, String hashKey);

    /**
     * Auto-generated documentation
     */
    Boolean hSet(String key, String hashKey, Object value, long time);

    /**
     * Auto-generated documentation
     */
    void hSet(String key, String hashKey, Object value);

    /**
     * Auto-generated documentation
     */
    Map<Object, Object> hGetAll(String key);

    /**
     * Auto-generated documentation
     */
    Boolean hSetAll(String key, Map<String, Object> map, long time);

    /**
     * Auto-generated documentation
     */
    void hSetAll(String key, Map<String, ?> map);

    /**
     * Auto-generated documentation
     */
    void hDel(String key, Object... hashKey);

    /**
     * Auto-generated documentation
     */
    Boolean hHasKey(String key, String hashKey);

    /**
     * Auto-generated documentation
     */
    Long hIncr(String key, String hashKey, Long delta);

    /**
     * Auto-generated documentation
     */
    Long hDecr(String key, String hashKey, Long delta);

    /**
     * Auto-generated documentation
     */
    Set<Object> sMembers(String key);

    /**
     * Auto-generated documentation
     */
    Long sAdd(String key, Object... values);

    /**
     * Auto-generated documentation
     */
    Long sAdd(String key, long time, Object... values);

    /**
     * Auto-generated documentation
     */
    Boolean sIsMember(String key, Object value);

    /**
     * Auto-generated documentation
     */
    Long sSize(String key);

    /**
     * Auto-generated documentation
     */
    Long sRemove(String key, Object... values);

    /**
     * Auto-generated documentation
     */
    List<Object> lRange(String key, long start, long end);

    /**
     * Auto-generated documentation
     */
    Long lSize(String key);

    /**
     * Auto-generated documentation
     */
    Object lIndex(String key, long index);

    /**
     * Auto-generated documentation
     */
    Long lPush(String key, Object value);

    /**
     * Auto-generated documentation
     */
    Long lPush(String key, Object value, long time);

    /**
     * Auto-generated documentation
     */
    Long lPushAll(String key, Object... values);

    /**
     * Auto-generated documentation
     */
    Long lPushAll(String key, Long time, Object... values);

    /**
     * Auto-generated documentation
     */
    Long lRemove(String key, long count, Object value);
}