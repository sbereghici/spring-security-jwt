package com.example.springsecurityjwt.jwt;

import com.example.springsecurityjwt.misc.Application;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

public class JwtTokenVerifierFilter extends OncePerRequestFilter {
    private KeystoreConfiguration keystoreConfiguration;
    private final JwtConfiguration jwtConfiguration;

    public JwtTokenVerifierFilter(JwtConfiguration jwtConfiguration, KeystoreConfiguration keystoreConfiguration) {
        this.jwtConfiguration = jwtConfiguration;
        this.keystoreConfiguration = keystoreConfiguration;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith(jwtConfiguration.getTokenPrefix())) {
            filterChain.doFilter(request, response);
            return;
        }
        String jwtToken = authorizationHeader.replace(jwtConfiguration.getTokenPrefix(), "");
        try {
            ClassPathResource resource = new ClassPathResource(keystoreConfiguration.getResourcePath());
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(resource.getInputStream(), keystoreConfiguration.getPassword());

            Certificate cert = keystore.getCertificate(keystoreConfiguration.getAlias());
            PublicKey publicKey = cert.getPublicKey();
            Jwt parsed = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parse(jwtToken);
            Object applicationId = ((Claims) parsed.getBody()).get("applicationId");
            if (applicationId == null) {
                throw new IllegalStateException("Invalid application id");
            }
            Application[] applicationsList = Application.values();
            boolean applicationExists = Arrays.stream(applicationsList).anyMatch(it -> it.getId() == (int) applicationId);
            if (!applicationExists) {
                throw new IllegalStateException("Invalid applicationId");
            }

            // todo check if it's possible to add to SecurityContext
            request.setAttribute("applicationId", applicationId);
            request.setAttribute("jwtClaims", parsed.getBody());

            // todo check if it's not better to create new SecurityContextHolder
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                    UUID.randomUUID(),
                    null,
                    new HashSet<>()
            ));
        } catch (ExpiredJwtException e) {
            throw new IllegalStateException(String.format("Token %s is expired!", jwtToken));
        } catch (Exception e) {
            throw new IllegalStateException(String.format("Token %s cannot be trusted", jwtToken));
        }
        filterChain.doFilter(request, response);
    }
}
