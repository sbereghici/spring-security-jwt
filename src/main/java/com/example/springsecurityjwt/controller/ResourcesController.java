package com.example.springsecurityjwt.controller;

import com.example.springsecurityjwt.jwt.JwtConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/resources")
public class ResourcesController {
    @Autowired
    private JwtConfiguration jwtConfiguration;

    @GetMapping
    @ResponseBody
    public String getResources() {
        return jwtConfiguration.toString();
    }
}
