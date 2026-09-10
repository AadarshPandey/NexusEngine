package com.nexusengine.core.security.config;

import com.nexusengine.core.common.config.BaseRedisConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * Represents the RedisConfig component.
 * Provides core functionality and operations for RedisConfig.
 */
@EnableCaching
@Configuration
public class RedisConfig extends BaseRedisConfig {

}
