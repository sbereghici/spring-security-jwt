package com.example.springsecurityjwt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/resources")
public class ResourcesController {

    @GetMapping
    @ResponseBody
    public List<String> getResources() {
        return List.of("This", "is", "a", "test", "resource", "{}");
    }
}
