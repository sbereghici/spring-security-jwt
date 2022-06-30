package com.example.springsecurityjwt.auth;

import org.springframework.security.core.userdetails.UserDetails;

public interface ApplicationUserAccess {
    UserDetails loadUserByUsername(String username);
}
