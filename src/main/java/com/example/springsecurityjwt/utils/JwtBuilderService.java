package com.example.springsecurityjwt.utils;

import com.example.springsecurityjwt.jwt.JwtConfiguration;
import com.example.springsecurityjwt.jwt.KeystoreConfiguration;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.*;

@Service
public class JwtBuilderService {
    @Autowired
    private KeystoreConfiguration keystoreConfiguration;
    @Autowired
    private JwtConfiguration jwtConfiguration;

    public String build(Map<String, Object> claims) {
        try {
            char[] jksPassword = keystoreConfiguration.getPassword();
            String alias = keystoreConfiguration.getAlias();
            String resourcePath = keystoreConfiguration.getResourcePath();

            ClassPathResource resource = new ClassPathResource(resourcePath);
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(resource.getInputStream(), jksPassword);

            Key key = keystore.getKey(alias, jksPassword);
            Calendar expires = Calendar.getInstance();
            expires.roll(Calendar.MINUTE, jwtConfiguration.getTokenExpirationAfterMinutes());
            return jwtConfiguration.getTokenPrefix() + Jwts.builder()
                    .setClaims(claims)
                    .setIssuedAt(new Date())
                    .setExpiration(expires.getTime())
                    .signWith(key, SignatureAlgorithm.RS256)
                    .compact();
        } catch (Exception e) {
            return null;
        }
    }

    public String build(long applicationId, UUID sessionId) {
        Map<String, Object> claims = new HashMap<>() {{
            put("applicationId", applicationId);
            put("sessionId", sessionId);
        }};
        return  build(claims);
    }

    public byte[] getPublicKey() {
        try {
            ClassPathResource resource = new ClassPathResource(keystoreConfiguration.getResourcePath());
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(resource.getInputStream(), keystoreConfiguration.getPassword());
            Certificate cert = keystore.getCertificate(keystoreConfiguration.getAlias());
            PublicKey publicKey = cert.getPublicKey();
            return publicKey.getEncoded();
        } catch (Exception e) {
            return null;
        }
    }
}
