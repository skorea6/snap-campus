package com.example.snapcampus.controller;

import com.example.snapcampus.dto.response.post.PostAggregateDtoResponse;
import com.example.snapcampus.service.PostService;
import com.example.snapcampus.service.SecurityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class PostController {
    private final PostService postService;
    private final SecurityService securityService;

    public PostController(PostService postService, SecurityService securityService) {
        this.postService = postService;
        this.securityService = securityService;
    }

    @GetMapping("/post/{id}")
    public String getPost(@PathVariable String id, Model model) {
        PostAggregateDtoResponse post = postService.getPost(securityService.getMemberUserId(), Long.valueOf(id));

        model.addAttribute("post", post);
        return "/post/index"; // Mustache 파일 경로
    }
}
