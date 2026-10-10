package com.nexusengine.core.security.component;



import cn.hutool.core.collection.CollUtil;
import com.nexusengine.core.security.config.IgnoreUrlsConfig;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Represents the DynamicAuthorizationManager component.
 * Provides core functionality and operations for DynamicAuthorizationManager.
 */
public class DynamicAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final DynamicSecurityMetadataSource securityDataSource;
    private final IgnoreUrlsConfig ignoreUrlsConfig;

    public DynamicAuthorizationManager(DynamicSecurityMetadataSource securityDataSource, IgnoreUrlsConfig ignoreUrlsConfig) {
        this.securityDataSource = securityDataSource;
        this.ignoreUrlsConfig = ignoreUrlsConfig;
    }

    @Override
    public void verify(Supplier<? extends Authentication> authentication, RequestAuthorizationContext object) {
        AuthorizationManager.super.verify(authentication, object);
    }

    @Override
    public AuthorizationDecision authorize(Supplier<? extends Authentication> authentication, RequestAuthorizationContext requestAuthorizationContext) {
        HttpServletRequest request = requestAuthorizationContext.getRequest();
        String path = request.getRequestURI();
        PathMatcher pathMatcher = new AntPathMatcher();
        List<String> ignoreUrls = ignoreUrlsConfig.getUrls();
        for (String ignoreUrl : ignoreUrls) {
            if (pathMatcher.match(ignoreUrl, path)) {
                return new AuthorizationDecision(true);
            }
        }
        if(request.getMethod().equals(HttpMethod.OPTIONS.name())){
            return new AuthorizationDecision(true);
        }
        
        // Portal APIs are customer-facing and do not require admin backend permissions
        if(pathMatcher.match("/portal/**", path)) {
            Authentication currentAuth = authentication.get();
            if (currentAuth != null && currentAuth.isAuthenticated() && !currentAuth.getPrincipal().equals("anonymousUser")) {
                return new AuthorizationDecision(true);
            } else {
                return new AuthorizationDecision(false); // Let access denied handler handle it
            }
        }

        List<String> needAuthorities = securityDataSource.getConfigAttributesWithPath(path);
        if (CollUtil.isEmpty(needAuthorities)) {
            // If the endpoint is not explicitly mapped in Permission, allow access
            // ONLY if the user is an admin.
            Authentication currentAuth = authentication.get();
            if (currentAuth != null && currentAuth.isAuthenticated() &&
                currentAuth.getPrincipal().getClass().getName().endsWith("AdminUserDetails")) {
                return new AuthorizationDecision(true);
            }
            return new AuthorizationDecision(false);
        }

        Authentication currentAuth = authentication.get();
        if(currentAuth.isAuthenticated()){
            Collection<? extends GrantedAuthority> grantedAuthorities = currentAuth.getAuthorities();
            List<? extends GrantedAuthority> hasAuth = grantedAuthorities.stream()
                    .filter(item -> needAuthorities.contains(item.getAuthority()))
                    .collect(Collectors.toList());
            if(CollUtil.isNotEmpty(hasAuth)){
                return new AuthorizationDecision(true);
            }else{
                return new AuthorizationDecision(false);
            }
        }else{
            return new AuthorizationDecision(false);
        }
    }
}
