package com.example.springsecurityjwt;

import com.example.springsecurityjwt.jwt.JwtConfiguration;
import com.example.springsecurityjwt.jwt.KeystoreConfiguration;
import com.example.springsecurityjwt.misc.Application;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.security.Key;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class SpringSecurityJwtApplicationTests {
    @Autowired
    private KeystoreConfiguration keystoreConfiguration;

    @Test
    void contextLoads() throws Exception {
        char[] jksPassword = keystoreConfiguration.getPassword();
        String alias = keystoreConfiguration.getAlias();
        String resourcePath = keystoreConfiguration.getResourcePath();

        ClassPathResource resource = new ClassPathResource(resourcePath);
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(resource.getInputStream(), jksPassword);

        Key key = keystore.getKey(alias, jksPassword);
        Certificate cert = keystore.getCertificate(alias);
        PublicKey publicKey = cert.getPublicKey();
        Map<String, Object> claims = new HashMap<>();
        claims.put("applicationId", "cope");
        Calendar expires = Calendar.getInstance();
        expires.roll(Calendar.HOUR, 2);
        String s = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(expires.getTime())
                .signWith(key, SignatureAlgorithm.RS256)
                .compact();
        System.out.println(s);
        Jwts.parserBuilder().setSigningKey(publicKey).require("user", "cope").build().parse(s);
    }

    @Test
    void generateClientJWT() throws Exception {
        char[] jksPassword = keystoreConfiguration.getPassword();
        String alias = keystoreConfiguration.getAlias();
        String resourcePath = keystoreConfiguration.getResourcePath();

        ClassPathResource resource = new ClassPathResource(resourcePath);
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(resource.getInputStream(), jksPassword);

        Key key = keystore.getKey(alias, jksPassword);
        Map<String, Object> claims = new HashMap<>();
        claims.put("applicationId", Application.AUTHENTICATOR.getId());
        Calendar expires = Calendar.getInstance();
        expires.roll(Calendar.YEAR, 100);
        String s = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(expires.getTime())
                .signWith(key, SignatureAlgorithm.RS256)
                .compact();
        System.out.println(s);
    }

}
