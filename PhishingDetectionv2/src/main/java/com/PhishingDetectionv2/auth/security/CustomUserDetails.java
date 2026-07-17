package com.PhishingDetectionv2.auth.security;

import com.PhishingDetectionv2.auth.entity.Permission;
import com.PhishingDetectionv2.auth.entity.RolePermission;
import com.PhishingDetectionv2.auth.entity.User;
import com.PhishingDetectionv2.auth.entity.UserRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class CustomUserDetails implements UserDetails {

    private final UUID id;

    private final String email;

    private final String password;

    private final boolean enabled;

    private final boolean accountLocked;

    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(User user) {

        this.id = user.getId();

        this.email = user.getEmail();

        this.password = user.getPassword();

        this.enabled = user.isEnabled();

        this.accountLocked = user.isAccountLocked();

        this.authorities = buildAuthorities(user);

    }

    private Collection<? extends GrantedAuthority> buildAuthorities(User user) {

        Set<GrantedAuthority> authorities = new HashSet<>();

        for (UserRole userRole : user.getUserRoles()) {

            authorities.add(
                    new SimpleGrantedAuthority(
                            "ROLE_" + userRole.getRole().getName()
                    )
            );

            for (RolePermission rolePermission : userRole.getRole().getRolePermissions()) {

                Permission permission = rolePermission.getPermission();

                authorities.add(
                        new SimpleGrantedAuthority(
                                permission.getName()
                        )
                );

            }

        }

        return authorities;

    }
    public static CustomUserDetails from(User user) {
        return new CustomUserDetails(user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
