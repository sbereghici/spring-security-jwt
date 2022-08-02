package com.example.springsecurityjwt.controller;

import com.example.springsecurityjwt.misc.Application;
import com.example.springsecurityjwt.utils.JwtBuilderService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.UUID;

@Controller
@RequestMapping("/auth")
public class AuthenticatorController {
    public static class CreateUserJwtBody {
        public long applicationId;
        public UUID sessionId;
    }

    @Autowired
    private JwtBuilderService jwtBuilderService;

    @PostMapping("/create-user-jwt")
    @ResponseBody
    public void createUserJwt(@RequestBody CreateUserJwtBody params, HttpServletResponse response) {
        // todo check if applicationId from request is -1
        Application[] applicationsList = Application.values();
        boolean applicationExists = Arrays.stream(applicationsList).anyMatch(it -> it.getId() == params.applicationId);
        if (params.applicationId != Application.AUTHENTICATOR.getId() || !applicationExists) {
            throw new IllegalStateException("Invalid applicationId");
        }
        String jwtToken = jwtBuilderService.build(params.applicationId, params.sessionId);
        // todo p[2] header vs body
        response.addHeader(HttpHeaders.AUTHORIZATION, jwtToken);
    }

    @PostMapping("/refresh-token")
    @ResponseBody
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        Claims claims = (Claims) request.getAttribute("jwtClaims");
        Integer applicationId;
        try {
            applicationId = (Integer) claims.get("applicationId");
            if (applicationId == Application.AUTHENTICATOR.getId()) {
                throw new IllegalStateException("Invalid applicationId");
            }
        } catch (Exception e) {
            throw new IllegalStateException("Invalid applicationId");
        }
        Application[] applicationsList = Application.values();
        boolean applicationExists = Arrays.stream(applicationsList).anyMatch(it -> it.getId() == applicationId);
        if (!applicationExists) {
            throw new IllegalStateException("Invalid applicationId");
        }
        String jwtToken = jwtBuilderService.build(claims);
        response.addHeader(HttpHeaders.AUTHORIZATION, jwtToken);
    }

    // todo switch to json
    @GetMapping("/jwt-pk")
    @ResponseBody
    public byte[] getPublicJwtKey() {
        return jwtBuilderService.getPublicKey();
    }
}
