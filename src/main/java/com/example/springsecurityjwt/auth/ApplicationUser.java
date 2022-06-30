package com.example.springsecurityjwt.auth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

@Getter
@Setter
public class ApplicationUser implements UserDetails {
    private final String username;
    private final String password;
    private final Set<? extends GrantedAuthority> authorities;
    private final boolean isAccountNonExpired;
    private final boolean isAccountNonLocked;
    private final boolean isCredentialsNonExpired;
    private final boolean isEnabled;

    public ApplicationUser(String username,
                           String password,
                           Set<? extends GrantedAuthority> authorities,
                           boolean isAccountNonExpired,
                           boolean isAccountNonLocked,
                           boolean isCredentialsNonExpired,
                           boolean isEnabled) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.isEnabled = isEnabled;
    }
}
