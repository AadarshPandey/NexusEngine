package com.nexusengine.core.security.unit;

import com.nexusengine.core.security.component.DynamicAuthorizationManager;
import com.nexusengine.core.security.component.DynamicSecurityMetadataSource;
import com.nexusengine.core.security.config.IgnoreUrlsConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class DynamicAuthorizationManagerTest {

    @Mock
    private DynamicSecurityMetadataSource securityDataSource;
    @Mock
    private IgnoreUrlsConfig ignoreUrlsConfig;
    @Mock
    private RequestAuthorizationContext context;
    @Mock
    private HttpServletRequest request;
    @Mock
    private Authentication authentication;

    @InjectMocks
    private DynamicAuthorizationManager manager;

    @BeforeEach
    void setUp() {
        when(context.getRequest()).thenReturn(request);
    }

    @Test
    void check_IgnoredUrl_Grants() {
        when(request.getRequestURI()).thenReturn("/swagger-ui.html");
        when(ignoreUrlsConfig.getUrls()).thenReturn(Collections.singletonList("/swagger-ui.html"));

        AuthorizationDecision decision = manager.check(() -> authentication, context);

        assertTrue(decision.isGranted());
    }

    @Test
    void check_OptionsRequest_Grants() {
        when(request.getRequestURI()).thenReturn("/api/test");
        when(ignoreUrlsConfig.getUrls()).thenReturn(Collections.emptyList());
        when(request.getMethod()).thenReturn(HttpMethod.OPTIONS.name());

        AuthorizationDecision decision = manager.check(() -> authentication, context);

        assertTrue(decision.isGranted());
    }

    @Test
    void check_PortalPath_AuthenticatedUser_Grants() {
        when(request.getRequestURI()).thenReturn("/portal/order/generate");
        when(ignoreUrlsConfig.getUrls()).thenReturn(Collections.emptyList());
        when(request.getMethod()).thenReturn("POST");
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("testuser");

        AuthorizationDecision decision = manager.check(() -> authentication, context);

        assertTrue(decision.isGranted());
    }

    @Test
    void check_PortalPath_AnonymousUser_Denies() {
        when(request.getRequestURI()).thenReturn("/portal/order/generate");
        when(ignoreUrlsConfig.getUrls()).thenReturn(Collections.emptyList());
        when(request.getMethod()).thenReturn("POST");
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("anonymousUser");

        AuthorizationDecision decision = manager.check(() -> authentication, context);

        assertFalse(decision.isGranted());
    }

    @Test
    void check_AdminPath_UnmappedResource_FailClosed() {
        when(request.getRequestURI()).thenReturn("/admin/secret");
        when(ignoreUrlsConfig.getUrls()).thenReturn(Collections.emptyList());
        when(request.getMethod()).thenReturn("GET");
        when(securityDataSource.getConfigAttributesWithPath("/admin/secret")).thenReturn(Collections.emptyList());

        AuthorizationDecision decision = manager.check(() -> authentication, context);

        assertFalse(decision.isGranted());
    }

    @Test
    void check_AdminPath_CorrectRole_Grants() {
        when(request.getRequestURI()).thenReturn("/admin/list");
        when(ignoreUrlsConfig.getUrls()).thenReturn(Collections.emptyList());
        when(request.getMethod()).thenReturn("GET");
        when(securityDataSource.getConfigAttributesWithPath("/admin/list"))
                .thenReturn(Collections.singletonList(new SecurityConfig("ROLE_ADMIN")));
        
        when(authentication.isAuthenticated()).thenReturn(true);
        doReturn(Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))).when(authentication).getAuthorities();

        AuthorizationDecision decision = manager.check(() -> authentication, context);

        assertTrue(decision.isGranted());
    }

    @Test
    void check_AdminPath_WrongRole_Denies() {
        when(request.getRequestURI()).thenReturn("/admin/list");
        when(ignoreUrlsConfig.getUrls()).thenReturn(Collections.emptyList());
        when(request.getMethod()).thenReturn("GET");
        when(securityDataSource.getConfigAttributesWithPath("/admin/list"))
                .thenReturn(Collections.singletonList(new SecurityConfig("ROLE_SUPERADMIN")));
        
        when(authentication.isAuthenticated()).thenReturn(true);
        doReturn(Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))).when(authentication).getAuthorities();

        AuthorizationDecision decision = manager.check(() -> authentication, context);

        assertFalse(decision.isGranted());
    }
}
