package com.nexusengine.core.admin.iam.dto;

import com.nexusengine.core.domain.iam.entity.AdminUser;
import com.nexusengine.core.domain.iam.entity.Permission;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AdminUserDetails implements UserDetails {
    private final AdminUser adminUser;
    private final List<Permission> resourceList;

    public AdminUserDetails(AdminUser adminUser, List<Permission> resourceList) {
        this.adminUser = adminUser;
        this.resourceList = resourceList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return resourceList.stream()
                .map(resource -> new SimpleGrantedAuthority(resource.getId() + ":" + resource.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return adminUser.getPassword();
    }

    @Override
    public String getUsername() {
        return adminUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return adminUser.getStatus() != null && adminUser.getStatus().equals(1);
    }
}
