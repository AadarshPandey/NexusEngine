package com.nexusengine.core.security.component;

import cn.hutool.core.collection.CollUtil;
import com.nexusengine.core.security.config.IgnoreUrlsConfig;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.ConfigAttribute;
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

    @Autowired
    private DynamicSecurityMetadataSource securityDataSource;
    @Autowired
    private IgnoreUrlsConfig ignoreUrlsConfig;

    @Override
    public void verify(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        AuthorizationManager.super.verify(authentication, object);
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext requestAuthorizationContext) {
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

        List<ConfigAttribute> configAttributeList = securityDataSource.getConfigAttributesWithPath(path);
        List<String> needAuthorities = configAttributeList.stream()
                .map(ConfigAttribute::getAttribute)
                .collect(Collectors.toList());
        if (CollUtil.isEmpty(needAuthorities)) {
            // Fix 5: Fail-closed instead of fail-open.
            // If the endpoint is not explicitly mapped in UmsResource, deny access
            // instead of blindly allowing any authenticated user (e.g. member) to access it.
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
