package com.nexusengine.core.security.component;

import cn.hutool.json.JSONUtil;
import com.nexusengine.core.common.api.CommonResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Represents the RestAuthenticationEntryPoint component.
 * Provides core functionality and operations for RestAuthenticationEntryPoint.
 */
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setHeader("Cache-Control","no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setStatus(401);
        response.getWriter().println(JSONUtil.toJsonStr(CommonResult.unauthorized(authException.getMessage())));
        response.getWriter().flush();
    }
}
