package com.example.springsecurityjwt.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "application.jwt")
@Component
@Getter
@Setter
public class JwtConfiguration {
    private String secretKey;
    private String tokenPrefix;
    private Integer tokenExpirationAfterDays;

    public JwtConfiguration() {
    }

    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }

    @Override
    public String toString() {
        return "JwtConfiguration{" +
                "secretKey='" + secretKey + '\'' +
                ", tokenPrefix='" + tokenPrefix + '\'' +
                ", tokenExpirationAfterDays=" + tokenExpirationAfterDays +
                '}';
    }
}
