package com.example.springsecurityjwt.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.HashSet;

@Repository("in-memory-auth")
public class InMemoryUserAccessService implements ApplicationUserAccess {
    private InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    private void init() {
        inMemoryUserDetailsManager.createUser(new ApplicationUser(
                "bereghicidev",
                passwordEncoder.encode("test1234"),
                new HashSet<>(),
                true,
                true,
                true,
                true
        ));
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return inMemoryUserDetailsManager.loadUserByUsername(username);
    }
}
