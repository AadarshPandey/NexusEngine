package com.nexusengine.core.portal.customer.service;

import lombok.RequiredArgsConstructor;

import com.nexusengine.core.common.service.RedisService;

import com.nexusengine.core.domain.customer.repository.MemberRepository;
import com.nexusengine.core.domain.customer.entity.Member;
import com.nexusengine.core.security.annotation.CacheException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * Represents the MemberCacheServiceImpl component.
 * Provides core functionality and operations for MemberCacheServiceImpl.
 */
@Service
@RequiredArgsConstructor
public class MemberCacheServiceImpl implements MemberCacheService {
    private final RedisService redisService;
    private final MemberRepository memberRepository;
    @Value("${redis.database}")
    private String REDIS_DATABASE;
    @Value("${redis.expire.common}")
    private Long REDIS_EXPIRE;
    @Value("${redis.expire.authCode}")
    private Long REDIS_EXPIRE_AUTH_CODE;
    @Value("${redis.key.member}")
    private String REDIS_KEY_MEMBER;
    @Value("${redis.key.authCode}")
    private String REDIS_KEY_AUTH_CODE;

    @Override
    public void delMember(Long memberId) {
        Member member = memberRepository.findById(memberId).orElse(null);
        if (member != null) {
            String key = REDIS_DATABASE + ":" + REDIS_KEY_MEMBER + ":" + member.getUsername();
            redisService.del(key);
        }
    }

    @Override
    public Member getMember(String username) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_MEMBER + ":" + username;
        return (Member) redisService.get(key);
    }

    @Override
    public void setMember(Member member) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_MEMBER + ":" + member.getUsername();
        redisService.set(key, member, REDIS_EXPIRE);
    }

    @CacheException
    @Override
    public void setAuthCode(String telephone, String authCode) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_AUTH_CODE + ":" + telephone;
        redisService.set(key,authCode,REDIS_EXPIRE_AUTH_CODE);
    }

    @CacheException
    @Override
    public String getAuthCode(String telephone) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_AUTH_CODE + ":" + telephone;
        return (String) redisService.get(key);
    }

    @Override
    public void delAuthCode(String telephone) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_AUTH_CODE + ":" + telephone;
        redisService.del(key);
    }
}
