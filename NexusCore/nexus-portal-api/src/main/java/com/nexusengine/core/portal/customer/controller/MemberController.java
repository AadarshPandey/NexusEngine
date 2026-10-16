package com.nexusengine.core.portal.customer.controller;

import com.nexusengine.core.common.api.ResultCode;
import com.nexusengine.core.common.exception.Asserts;
import com.nexusengine.core.domain.customer.entity.Member;
import com.nexusengine.core.portal.customer.dto.TokenResponse;
import com.nexusengine.core.portal.customer.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Tag(name = "Storefront - Auth & Member", description = "Storefront member authentication and profile APIs")
@RequestMapping("/portal/sso")
@RequiredArgsConstructor
public class MemberController {
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    private final MemberService memberService;

    @Operation(summary = "Register member")
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(
            @RequestParam("username") String username,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "passwordHash", required = false) String passwordHash,
            @RequestParam("email") String email,
            @RequestParam("authCode") String authCode) {
        String finalPassword = password != null ? password : passwordHash;
        memberService.register(username, finalPassword, email, authCode);
    }

    @Operation(summary = "Login member")
    @PostMapping("/login")
    public TokenResponse login(
            @RequestParam("username") String username,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "passwordHash", required = false) String passwordHash) {
        String finalPassword = password != null ? password : passwordHash;
        String token = memberService.login(username, finalPassword);
        if (token == null) {
            Asserts.fail("Invalid username or password");
        }
        return new TokenResponse(token, tokenHead);
    }

    @Operation(summary = "Upload avatar")
    @PostMapping("/uploadAvatar")
    public String uploadAvatar(@RequestParam("file") MultipartFile file) {
        return memberService.uploadAvatar(file);
    }

    @Operation(summary = "Update member profile")
    @PostMapping("/updateProfile")
    public Member updateProfile(@Valid @RequestBody Member member) {
        Member current = memberService.getCurrentMember();
        if (member.getDisplayName() != null) {
            current.setDisplayName(member.getDisplayName());
        }
        if (member.getContactPhone() != null) {
            current.setContactPhone(member.getContactPhone());
        }
        if (member.getIconUrl() != null) {
            current.setIconUrl(member.getIconUrl());
        }
        memberService.updateMember(current);
        return current;
    }

    @Operation(summary = "Get current member info")
    @GetMapping("/info")
    public Member info(Principal principal) {
        if (principal == null) {
            Asserts.fail(ResultCode.UNAUTHORIZED);
        }
        return memberService.getCurrentMember();
    }

    @Operation(summary = "Request OTP verification code via email")
    @PostMapping("/getAuthCode")
    public void getAuthCode(@RequestParam("email") String email) {
        memberService.generateAuthCode(email);
    }

    @Operation(summary = "Update member password")
    @PostMapping("/updatePassword")
    public void updatePassword(
            @RequestParam("email") String email,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "passwordHash", required = false) String passwordHash,
            @RequestParam("authCode") String authCode) {
        String finalPassword = password != null ? password : passwordHash;
        memberService.updatePassword(email, finalPassword, authCode);
    }

    @Operation(summary = "Refresh JWT token")
    @GetMapping("/refreshToken")
    public TokenResponse refreshToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String refreshToken = memberService.refreshToken(token);
        if (refreshToken == null) {
            Asserts.fail("Token refresh failed");
        }
        return new TokenResponse(refreshToken, tokenHead);
    }
}
