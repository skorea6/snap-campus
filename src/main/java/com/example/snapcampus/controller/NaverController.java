package com.example.snapcampus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NaverController {
    //첫 페이지 호출 시, index 뷰를 호출
    @GetMapping("/naver")
    public String naverMap() {
        return "naver/index";
    }

    @GetMapping("/")
    public String index() {
        return "/index";
    }

    @GetMapping("/search-result")
    public String searchResult() {
        return "search-result/index";
    }
}
