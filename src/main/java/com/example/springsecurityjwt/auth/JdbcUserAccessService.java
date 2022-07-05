package com.example.springsecurityjwt.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Repository("jdbc-auth")
public class JdbcUserAccessService implements ApplicationUserAccess {
    @Autowired
    private DataSource dataSource;

    private JdbcUserDetailsManager jdbcUserDetailsManager;

    @PostConstruct
    private void init() {
        jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return jdbcUserDetailsManager.loadUserByUsername(username);
    }
}

