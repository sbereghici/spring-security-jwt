package com.example.springsecurityjwt.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application.keystore")
public class KeystoreConfiguration {
    private String password;
    private String alias;
    private String resourcePath;

    public char[] getPassword() {
        return password.toCharArray();
    }

    public String getAlias() {
        return alias;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }
}
