package com.nexusengine.core.portal.service.impl;

import cn.hutool.core.util.StrUtil;
import com.nexusengine.core.common.exception.Asserts;
import com.nexusengine.core.model.UmsMember;
import com.nexusengine.core.model.UmsMemberLevel;
import com.nexusengine.core.portal.domain.MemberDetails;
import com.nexusengine.core.portal.service.UmsMemberCacheService;
import com.nexusengine.core.portal.service.UmsMemberService;
import com.nexusengine.core.repository.UmsMemberLevelRepository;
import com.nexusengine.core.repository.UmsMemberRepository;
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
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.security.SecureRandom;

/**
 * Member management Service implementation
 */
@Service
public class UmsMemberServiceImpl implements UmsMemberService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UmsMemberServiceImpl.class);
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UmsMemberRepository memberRepository;
    @Autowired
    private UmsMemberLevelRepository memberLevelRepository;
    @Autowired
    private UmsMemberCacheService memberCacheService;
    @Value("${redis.key.authCode}")
    private String REDIS_KEY_PREFIX_AUTH_CODE;
    @Value("${redis.expire.authCode}")
    private Long AUTH_CODE_EXPIRE_SECONDS;

    @Override
    public UmsMember getByUsername(String username) {
        UmsMember member = memberCacheService.getMember(username);
        if (member != null && member.getPassword() != null) return member;
        member = memberRepository.findByUsername(username);
        if (member != null) {
            memberCacheService.setMember(member);
            return member;
        }
        return null;
    }

    @Override
    public UmsMember getById(Long id) {
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
        List<UmsMember> existing = memberRepository.findByUsernameOrEmail(username, email);
        if (!CollectionUtils.isEmpty(existing)) {
            Asserts.fail("User already exists");
        }
        UmsMember umsMember = new UmsMember();
        umsMember.setUsername(username);
        umsMember.setEmail(email);
        umsMember.setPassword(passwordEncoder.encode(password));
        umsMember.setCreateTime(new Date());
        umsMember.setStatus(1);
        List<UmsMemberLevel> memberLevelList = memberLevelRepository.findByDefaultStatus(1);
        if (!CollectionUtils.isEmpty(memberLevelList)) {
            umsMember.setMemberLevelId(memberLevelList.get(0).getId());
        }
        memberRepository.save(umsMember);
    }

    @Autowired
    private org.springframework.mail.javamail.JavaMailSender mailSender;
    @Value("${spring.mail.username}")
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
        try {
            org.springframework.mail.SimpleMailMessage message = new org.springframework.mail.SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setSubject("Your Registration OTP");
            message.setText("Your OTP code is: " + sb.toString() + "\nIt is valid for " + (AUTH_CODE_EXPIRE_SECONDS / 60) + " minutes.");
            mailSender.send(message);
        } catch (Exception e) {
            LOGGER.error("Failed to send OTP email", e);
            org.springframework.security.authentication.BadCredentialsException ex = new org.springframework.security.authentication.BadCredentialsException("Failed to send email. Please check your SMTP configuration.");
            ex.initCause(e);
            throw ex;
        }
    }

    @Override
    public void updatePassword(String email, String password, String authCode) {
        UmsMember member = memberRepository.findByEmail(email);
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
    public UmsMember getCurrentMember() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        if (auth == null || !auth.isAuthenticated() || !(auth.getPrincipal() instanceof MemberDetails)) {
            return null;
        }
        MemberDetails memberDetails = (MemberDetails) auth.getPrincipal();
        return memberDetails.getUmsMember();
    }

    @Override
    public void updateIntegration(Long id, Integer integration) {
        UmsMember member = memberRepository.findById(id).orElse(null);
        if (member != null) {
            member.setRewardPoints(integration);
            memberRepository.save(member);
            memberCacheService.delMember(id);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        UmsMember member = getByUsername(username);
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
