package com.nexusengine.core.admin.iam.dto;

import com.nexusengine.core.domain.iam.entity.AdminUser;
import com.nexusengine.core.domain.iam.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class AdminUserDetails implements UserDetails {
    private final AdminUser adminUser;
    private final List<Role> roleList;

    public AdminUserDetails(AdminUser adminUser, List<Role> roleList) {
        this.adminUser = adminUser;
        this.roleList = roleList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new java.util.ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        if (roleList != null) {
            authorities.addAll(
                    roleList.stream()
                            .filter(r -> r.getName() != null)
                            .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getName().toUpperCase().replace(" ", "_")))
                            .toList());
        }
        return authorities;
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
        return Boolean.TRUE.equals(adminUser.getStatus());
    }

    public Long getBrandId() {
        return adminUser != null ? adminUser.getBrandId() : null;
    }

    public AdminUser getAdminUser() {
        return adminUser;
    }
}
