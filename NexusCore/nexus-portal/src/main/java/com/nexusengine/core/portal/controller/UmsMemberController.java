package com.nexusengine.core.portal.controller;

import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.model.UmsMember;
import com.nexusengine.core.portal.service.UmsMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * Auto-generated documentation
 * Created by macro on 2018/8/3.
 */
@Controller
@Tag(name = "UmsMemberController", description = "Ums member controller APIs")
@RequestMapping("/portal/sso")
public class UmsMemberController {
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private UmsMemberService memberService;

    @Operation(summary = "Register Operation")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult register(@RequestParam("username") String username,
                                 @RequestParam("password") String password,
                                 @RequestParam("email") String email,
                                 @RequestParam("authCode") String authCode) {
        memberService.register(username, password, email, authCode);
        return CommonResult.success(null,"Success");
    }

    @Operation(summary = "Login Operation")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult login(@RequestParam String username,
                              @RequestParam String password) {
        String token = memberService.login(username, password);
        if (token == null) {
            return CommonResult.validateFailed("Invalid username or password");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return CommonResult.success(tokenMap);
    }

    @Operation(summary = "Info Operation")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult info(Principal principal) {
        if(principal==null){
            return CommonResult.unauthorized(null);
        }
        UmsMember member = memberService.getCurrentMember();
        return CommonResult.success(member);
    }

    @Operation(summary = "Get auth code Operation")
    @RequestMapping(value = "/getAuthCode", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getAuthCode(@RequestParam("email") String email) {
        memberService.generateAuthCode(email);
        return CommonResult.success(null, "Auth code sent successfully");
    }

    @Operation(summary = "Update password Operation")
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updatePassword(@RequestParam String email,
                                 @RequestParam String password,
                                 @RequestParam String authCode) {
        memberService.updatePassword(email,password,authCode);
        return CommonResult.success(null,"Success");
    }


    @Operation(summary = "Refresh token Operation")
    @RequestMapping(value = "/refreshToken", method = RequestMethod.GET)
    @ResponseBody
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
