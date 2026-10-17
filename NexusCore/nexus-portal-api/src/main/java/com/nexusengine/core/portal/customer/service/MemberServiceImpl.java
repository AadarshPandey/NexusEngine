package com.nexusengine.core.portal.customer.service;

import lombok.RequiredArgsConstructor;

import org.springframework.util.StringUtils;

import com.nexusengine.core.common.exception.Asserts;
import com.nexusengine.core.domain.customer.entity.Member;
import com.nexusengine.core.domain.customer.repository.MemberRepository;
import com.nexusengine.core.security.service.JwtTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.util.List;
import java.security.SecureRandom;
import com.nexusengine.core.portal.customer.dto.MemberDetails;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;


/**
 * Member management Service implementation
 */
@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberServiceImpl.class);
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final MemberRepository memberRepository;
    private final MemberCacheService memberCacheService;
    private final com.nexusengine.core.domain.customer.repository.MemberLoginEventRepository memberLoginEventRepository;
    private final S3Client s3Client;
    @Value("${redis.key.authCode}")
    private String REDIS_KEY_PREFIX_AUTH_CODE;
    @Value("${redis.expire.authCode}")
    private Long AUTH_CODE_EXPIRE_SECONDS;
    @Value("${spring.cloud.aws.s3.bucket-name:nexus-media}")
    private String bucketName;
    @Value("${spring.cloud.aws.s3.endpoint:http://localhost:9000}")
    private String s3Endpoint;

    @Override
    public Member getByUsername(String username) {
        Member member = memberCacheService.getMember(username);
        if (member != null) return member;
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
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }
        if (auth.getPrincipal() instanceof MemberDetails memberDetails) {
            return memberDetails.member();
        }
        if (auth.getPrincipal() instanceof org.springframework.security.oauth2.jwt.Jwt jwt) {
            String username = jwt.getSubject();
            return getByUsername(username);
        }
        String name = auth.getName();
        if (name != null && !name.isBlank()) {
            return getByUsername(name);
        }
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Member member = getByUsername(username);
        if (member != null && member.getPassword() == null) {
            member = memberRepository.findByUsername(username);
        }
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
            token = jwtTokenService.generateToken(userDetails);

            if (userDetails instanceof MemberDetails memberDetails && memberDetails.member() != null) {
                recordLoginEvent(memberDetails.member().getId());
            }
        } catch (AuthenticationException e) {
            LOGGER.warn("Login exception: {}", e.getMessage());
        }
        return token;
    }

    private void recordLoginEvent(Long memberId) {
        try {
            com.nexusengine.core.domain.customer.entity.MemberLoginEvent event = new com.nexusengine.core.domain.customer.entity.MemberLoginEvent();
            event.setMemberId(memberId);
            event.setCreateTime(java.time.OffsetDateTime.now());
            event.setLoginType(1); // 1 = Web / API login
            org.springframework.web.context.request.ServletRequestAttributes attrs =
                    (org.springframework.web.context.request.ServletRequestAttributes) org.springframework.web.context.request.RequestContextHolder.getRequestAttributes();
            if (attrs != null && attrs.getRequest() != null) {
                event.setIp(com.nexusengine.core.common.util.RequestUtil.getRequestIp(attrs.getRequest()));
            }
            memberLoginEventRepository.save(event);
        } catch (Exception ex) {
            LOGGER.debug("Could not record login event: {}", ex.getMessage());
        }
    }

    @Override
    public String refreshToken(String token) {
        return jwtTokenService.refreshHeadToken(token);
    }

    @Override
    public String uploadAvatar(MultipartFile file) {
        try {
            String filename = "avatar_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String objectKey = "public/users/" + filename;
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(objectKey)
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            return s3Endpoint + "/" + bucketName + "/" + objectKey;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to upload avatar", e);
        }
    }

    private boolean verifyAuthCode(String authCode, String email) {
        if (!StringUtils.hasText(authCode)) {
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
