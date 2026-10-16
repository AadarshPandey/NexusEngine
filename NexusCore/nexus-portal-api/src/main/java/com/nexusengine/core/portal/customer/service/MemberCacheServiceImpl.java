package com.nexusengine.core.portal.customer.service;

import com.nexusengine.core.domain.customer.entity.Member;
import com.nexusengine.core.domain.customer.repository.MemberRepository;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Service managing Redis caching for member profiles and OTP auth codes.
 */
@Service
@RequiredArgsConstructor
public class MemberCacheServiceImpl implements MemberCacheService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final MemberRepository memberRepository;

    @Value("${redis.database:nexus}")
    private String redisDatabase;

    @Value("${redis.expire.common:86400}")
    private Long redisExpire;

    @Value("${redis.expire.authCode:300}")
    private Long redisExpireAuthCode;

    @Value("${redis.key.member:customer:member:}")
    private String redisKeyMember;

    @Value("${redis.key.authCode:customer:authCode:}")
    private String redisKeyAuthCode;

    @Override
    public void delMember(Long memberId) {
        Member member = memberRepository.findById(memberId).orElse(null);
        if (member != null) {
            String key = redisDatabase + ":" + redisKeyMember + ":" + member.getUsername();
            redisTemplate.delete(key);
        }
    }

    @Override
    public Member getMember(String username) {
        String key = redisDatabase + ":" + redisKeyMember + ":" + username;
        return (Member) redisTemplate.opsForValue().get(key);
    }

    @Override
    public void setMember(Member member) {
        String key = redisDatabase + ":" + redisKeyMember + ":" + member.getUsername();
        redisTemplate.opsForValue().set(key, member, Duration.ofSeconds(redisExpire));
    }

    @Override
    public void setAuthCode(String telephone, String authCode) {
        String key = redisDatabase + ":" + redisKeyAuthCode + ":" + telephone;
        redisTemplate.opsForValue().set(key, authCode, Duration.ofSeconds(redisExpireAuthCode));
    }

    @Override
    public String getAuthCode(String telephone) {
        String key = redisDatabase + ":" + redisKeyAuthCode + ":" + telephone;
        Object val = redisTemplate.opsForValue().get(key);
        return val != null ? val.toString() : null;
    }

    @Override
    public void delAuthCode(String telephone) {
        String key = redisDatabase + ":" + redisKeyAuthCode + ":" + telephone;
        redisTemplate.delete(key);
    }
}
