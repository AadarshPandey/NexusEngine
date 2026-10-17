package com.nexusengine.core.admin.security;

import com.nexusengine.core.admin.iam.service.AdminUserService;
import com.nexusengine.core.common.api.ResultCode;
import com.nexusengine.core.common.exception.ApiException;
import com.nexusengine.core.domain.iam.entity.AdminUser;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

/**
 * Multi-tenant brand context for vendor administrators (e.g. samsung_admin, nike_admin).
 * Enforces row-level brand scoping across Dashboard, Catalog, Ordering, and Analytics APIs.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AdminTenantContext {

    private final AdminUserService adminUserService;

    /**
     * Resolves the brand ID if the current authenticated administrator is brand-scoped.
     * Returns Optional.empty() if the user is Super Admin (unrestricted) or unauthenticated.
     */
    public Optional<Long> currentBrandId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return Optional.empty();
        }

        // 1. Check brand_id claim in JWT (fast path, zero DB hit)
        if (auth instanceof JwtAuthenticationToken jwtAuth) {
            Jwt jwt = jwtAuth.getToken();
            Object brandClaim = jwt.getClaim("brand_id");
            if (brandClaim instanceof Number num) {
                return Optional.of(num.longValue());
            }
        }

        // 2. Fallback to cached AdminUser lookup
        String username = auth.getName();
        if (username != null && !username.isBlank() && !"admin".equalsIgnoreCase(username)) {
            try {
                AdminUser admin = adminUserService.getAdminByUsername(username);
                if (admin != null && admin.getBrandId() != null) {
                    return Optional.of(admin.getBrandId());
                }
            } catch (Exception e) {
                log.warn("Failed to load admin user for brand context: {}", e.getMessage());
            }
        }

        return Optional.empty();
    }

    /**
     * Asserts that the current administrator is permitted to access/mutate a resource with the given brand ID.
     * Throws 403 Forbidden ApiException if a brand mismatch is detected.
     */
    public void assertBrandAccess(Long resourceBrandId) {
        currentBrandId().ifPresent(adminBrandId -> {
            if (resourceBrandId == null || !adminBrandId.equals(resourceBrandId)) {
                throw new ApiException(ResultCode.FORBIDDEN);
            }
        });
    }

    /**
     * Checks whether the current user is brand-scoped.
     */
    public boolean isBrandScoped() {
        return currentBrandId().isPresent();
    }
}
