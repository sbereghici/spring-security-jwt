package com.example.springsecurityjwt.auth;

import com.example.springsecurityjwt.security.ApplicationUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Service("jdbc-auth")
public class JdbcUserAccessService implements ApplicationUserAccess {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private JdbcUserDetailsManager jdbcUserDetailsManager;

    @PostConstruct
    private void init() {
        jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
//        jdbcUserDetailsManager.createUser(new ApplicationUser(
//                "bereghicidev", passwordEncoder.encode("test1234"),
//                true,
//                ApplicationUserRole.ADMIN.getGrantedAuthorities()
//        ));
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return jdbcUserDetailsManager.loadUserByUsername(username);
    }
}

