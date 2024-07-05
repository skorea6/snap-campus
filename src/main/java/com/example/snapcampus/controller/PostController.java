package com.example.snapcampus.controller;

import com.example.snapcampus.dto.response.post.PostAggregateDtoResponse;
import com.example.snapcampus.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/post/{id}")
    public String getPost(@PathVariable String id, Model model) {
        PostAggregateDtoResponse post = postService.getPost(Long.valueOf(id));

        model.addAttribute("post", post);
        return "/post/index"; // Mustache 파일 경로
    }
}
