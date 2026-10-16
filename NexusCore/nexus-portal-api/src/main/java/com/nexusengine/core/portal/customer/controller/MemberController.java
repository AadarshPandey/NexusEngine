package com.nexusengine.core.portal.customer.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RestController;

import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.domain.customer.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import com.nexusengine.core.portal.customer.service.MemberService;


/**
 * Customer-facing authentication and member management controller.
 * Handles registration, login, OTP verification, and token refresh for storefront users.
 */
@RestController
@Tag(name = "MemberController", description = "Ums member controller APIs")
@RequestMapping("/portal/sso")
@RequiredArgsConstructor
public class MemberController {
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    private final MemberService memberService;

    @Operation(summary = "Register Operation")
    @PostMapping("/register")

    public CommonResult register(@RequestParam("username") String username,
                                 @RequestParam(value = "password", required = false) String password,
                                 @RequestParam(value = "passwordHash", required = false) String passwordHash,
                                 @RequestParam("email") String email,
                                 @RequestParam("authCode") String authCode) {
        String finalPassword = password != null ? password : passwordHash;
        memberService.register(username, finalPassword, email, authCode);
        return CommonResult.success(null,"Success");
    }

    @Operation(summary = "Login Operation")
    @PostMapping("/login")
    public CommonResult login(@RequestParam("username") String username,
                              @RequestParam(value = "password", required = false) String password,
                              @RequestParam(value = "passwordHash", required = false) String passwordHash) {
        String finalPassword = password != null ? password : passwordHash;
        String token = memberService.login(username, finalPassword);
        if (token == null) {
            return CommonResult.validateFailed("Invalid username or password");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return CommonResult.success(tokenMap);
    }

    @Operation(summary = "Upload Avatar")
    @PostMapping("/uploadAvatar")
    public CommonResult uploadAvatar(@org.springframework.web.bind.annotation.RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        return CommonResult.success(memberService.uploadAvatar(file));
    }

    @Operation(summary = "Update Profile Operation")
    @PostMapping("/updateProfile")
    public CommonResult updateProfile(@org.springframework.web.bind.annotation.RequestBody Member member) {
        Member current = memberService.getCurrentMember();
        if (member.getDisplayName() != null) current.setDisplayName(member.getDisplayName());
        if (member.getContactPhone() != null) current.setContactPhone(member.getContactPhone());
        if (member.getIconUrl() != null) current.setIconUrl(member.getIconUrl());
        memberService.updateMember(current);
        return CommonResult.success(current);
    }

    @Operation(summary = "Info Operation")
    @GetMapping("/info")
    public CommonResult info(Principal principal) {
        if(principal==null){
            return CommonResult.unauthorized(null);
        }
        Member member = memberService.getCurrentMember();
        return CommonResult.success(member);
    }

    @Operation(summary = "Request OTP verification code via email")
    @PostMapping("/getAuthCode")

    public CommonResult getAuthCode(@RequestParam("email") String email) {
        memberService.generateAuthCode(email);
        return CommonResult.success(null, "Auth code sent successfully");
    }

    @Operation(summary = "Update password Operation")
    @PostMapping("/updatePassword")

    public CommonResult updatePassword(@RequestParam("email") String email,
                                 @RequestParam(value = "password", required = false) String password,
                                 @RequestParam(value = "passwordHash", required = false) String passwordHash,
                                 @RequestParam("authCode") String authCode) {
        String finalPassword = password != null ? password : passwordHash;
        memberService.updatePassword(email, finalPassword, authCode);
        return CommonResult.success(null,"Success");
    }


    @Operation(summary = "Refresh token Operation")
    @GetMapping("/refreshToken")

    public CommonResult refreshToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String refreshToken = memberService.refreshToken(token);
        if (refreshToken == null) {
            return CommonResult.failed("Token refresh failed");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", refreshToken);
        tokenMap.put("tokenHead", tokenHead);
        return CommonResult.success(tokenMap);
    }
}
