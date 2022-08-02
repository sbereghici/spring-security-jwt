package com.example.springsecurityjwt.security;

import com.example.springsecurityjwt.jwt.JwtConfiguration;
import com.example.springsecurityjwt.jwt.JwtTokenVerifierFilter;
import com.example.springsecurityjwt.jwt.KeystoreConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfiguration {
    @Autowired
    private JwtConfiguration jwtConfiguration;
    @Autowired
    private KeystoreConfiguration keystoreConfiguration;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterAfter(new JwtTokenVerifierFilter(jwtConfiguration, keystoreConfiguration), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/auth/jwt-pk").permitAll()
                .anyRequest().authenticated();
        return http.build();
    }
}
