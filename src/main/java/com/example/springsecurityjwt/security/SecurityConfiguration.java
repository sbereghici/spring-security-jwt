package com.example.springsecurityjwt.security;

import com.example.springsecurityjwt.auth.ApplicationUserDetailsService;
import com.example.springsecurityjwt.jwt.JwtAuthenticationFilter;
import com.example.springsecurityjwt.jwt.JwtConfiguration;
import com.example.springsecurityjwt.jwt.JwtTokenVerifierFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ApplicationUserDetailsService applicationUserDetailsService;
    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;
    @Autowired
    private JwtConfiguration jwtConfiguration;
    @Autowired
    private SecretKey jwtSecretKey;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .addFilter(new JwtAuthenticationFilter(authenticationConfiguration.getAuthenticationManager(), jwtConfiguration, jwtSecretKey))
                .addFilterAfter(new JwtTokenVerifierFilter(jwtConfiguration, jwtSecretKey), JwtAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
                .anyRequest().authenticated();
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(applicationUserDetailsService);
        return provider;
    }
}
