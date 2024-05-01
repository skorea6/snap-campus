package com.example.snapcampus.controller;

import com.example.snapcampus.service.SecurityService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping("/")
    public String index() {
        return "/index";
    }

    @GetMapping("/search-result")
    public String searchResult() {
        return "search-result/index";
    }
}
