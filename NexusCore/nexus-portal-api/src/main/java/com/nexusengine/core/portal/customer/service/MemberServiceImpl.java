package com.nexusengine.core.portal.customer.service;

import lombok.RequiredArgsConstructor;

import cn.hutool.core.util.StrUtil;

import com.nexusengine.core.common.exception.Asserts;
import com.nexusengine.core.domain.customer.entity.Member;
import com.nexusengine.core.domain.customer.repository.MemberRepository;
import com.nexusengine.core.security.util.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.security.SecureRandom;
import com.nexusengine.core.portal.customer.dto.MemberDetails;


/**
 * Member management Service implementation
 */
@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberServiceImpl.class);
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final MemberRepository memberRepository;
    private final MemberCacheService memberCacheService;
    @Value("${redis.key.authCode}")
    private String REDIS_KEY_PREFIX_AUTH_CODE;
    @Value("${redis.expire.authCode}")
    private Long AUTH_CODE_EXPIRE_SECONDS;

    @Override
    public Member getByUsername(String username) {
        Member member = memberCacheService.getMember(username);
        if (member != null && member.getPassword() != null) return member;
        member = memberRepository.findByUsername(username);
        if (member != null) {
            memberCacheService.setMember(member);
            return member;
        }
        return null;
    }

    @Override
    public Member getById(Long id) {
        return memberRepository.findById(id).orElse(null);
    }

    @Override
    public void register(String username, String password, String email, String authCode) {
        // Fix 9: Username validation to prevent prefix injection (like "admin:macro")
        if (username == null || !username.matches("^[a-zA-Z0-9_]+$")) {
            Asserts.fail("Username can only contain alphanumeric characters and underscores");
        }
        
        // Password strength validation
        if (password == null || password.length() < 8) {
            Asserts.fail("Password must be at least 8 characters long");
        }
        if (!password.matches(".*[A-Z].*") || !password.matches(".*[a-z].*") || !password.matches(".*\\d.*")) {
            Asserts.fail("Password must contain at least one uppercase letter, one lowercase letter, and one digit");
        }
        
        if (!verifyAuthCode(authCode, email)) {
            Asserts.fail("Invalid verification code");
        }
        List<Member> existing = memberRepository.findByUsernameOrEmail(username, email);
        if (!CollectionUtils.isEmpty(existing)) {
            Asserts.fail("User already exists");
        }
        Member member = new Member();
        member.setUsername(username);
        member.setEmail(email);
        member.setPassword(passwordEncoder.encode(password));
        member.setCreatedAt(java.time.OffsetDateTime.now());
        member.setStatus(1);
        memberRepository.save(member);
    }

        @Value("${spring.mail.username:noreply@nexusengine.com}")
    private String fromEmail;

    @Override
    public void generateAuthCode(String email) {
        StringBuilder sb = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        memberCacheService.setAuthCode(email, sb.toString());
        
        // Send email
        LOGGER.info("OTP code generated: " + sb.toString());
    }

    @Override
    public void updatePassword(String email, String password, String authCode) {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            Asserts.fail("Account not found");
        }
        if (!verifyAuthCode(authCode, email)) {
            Asserts.fail("Invalid verification code");
        }
        // Password strength validation
        if (password == null || password.length() < 8) {
            Asserts.fail("Password must be at least 8 characters long");
        }
        if (!password.matches(".*[A-Z].*") || !password.matches(".*[a-z].*") || !password.matches(".*\\d.*")) {
            Asserts.fail("Password must contain at least one uppercase letter, one lowercase letter, and one digit");
        }
        member.setPassword(passwordEncoder.encode(password));
        memberRepository.save(member);
        memberCacheService.delMember(member.getId());
    }

    @Override
    public void updateMember(Member member) {
        memberRepository.save(member);
        memberCacheService.setMember(member);
    }

    @Override
    public Member getCurrentMember() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        if (auth == null || !auth.isAuthenticated() || !(auth.getPrincipal() instanceof MemberDetails)) {
            return null;
        }
        MemberDetails memberDetails = (MemberDetails) auth.getPrincipal();
        return memberDetails.member();
    }

    @Override
    public void updatePoints(Long id, Integer integration) {
        Member member = memberRepository.findById(id).orElse(null);
        if (member != null) {
            member.setPointsBalance(integration);
            memberRepository.save(member);
            memberCacheService.delMember(id);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Member member = getByUsername(username);
        if (member != null) {
            return new MemberDetails(member);
        }
        throw new UsernameNotFoundException("Invalid username or password");
    }

    @Override
    public String login(String username, String password) {
        String token = null;
        try {
            UserDetails userDetails = loadUserByUsername(username);
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("Invalid password");
            }
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtTokenUtil.generateToken(userDetails);
        } catch (AuthenticationException e) {
            LOGGER.warn("Login exception: {}", e.getMessage());
        }
        return token;
    }

    @Override
    public String refreshToken(String token) {
        return jwtTokenUtil.refreshHeadToken(token);
    }

    private boolean verifyAuthCode(String authCode, String email) {
        if (StrUtil.isEmpty(authCode)) {
            return false;
        }
        String realAuthCode = memberCacheService.getAuthCode(email);
        if (realAuthCode != null && java.security.MessageDigest.isEqual(authCode.getBytes(), realAuthCode.getBytes())) {
            memberCacheService.delAuthCode(email);
            return true;
        }
        return false;
    }
}
