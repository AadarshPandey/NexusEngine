package com.nexusengine.core.security.component;

import cn.hutool.json.JSONUtil;
import com.nexusengine.core.common.api.CommonResult;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Represents the RestfulAccessDeniedHandler component.
 * Provides core functionality and operations for RestfulAccessDeniedHandler.
 */
public class RestfulAccessDeniedHandler implements AccessDeniedHandler{
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException e) throws IOException {
        response.setHeader("Cache-Control","no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setStatus(403);
        response.getWriter().println(JSONUtil.toJsonStr(CommonResult.forbidden(e.getMessage())));
        response.getWriter().flush();
    }
}
