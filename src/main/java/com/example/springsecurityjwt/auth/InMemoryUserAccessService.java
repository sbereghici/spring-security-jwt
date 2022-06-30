package com.example.springsecurityjwt.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Repository("in-memory")
public class InMemoryUserAccessService implements ApplicationUserAccess {
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final List<UserDetails> usersList = new ArrayList<>();

    @PostConstruct
    private void init() {
        usersList.add(new ApplicationUser(
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
        return usersList.stream()
                .filter(it -> it
                        .getUsername()
                        .equals(username))
                .findFirst()
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format("Username %s not found", username))
                );
    }
}
