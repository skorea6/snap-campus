package com.example.snapcampus.controller;

import com.example.snapcampus.common.dto.PageWrapper;
import com.example.snapcampus.dto.request.comment.CommentListDtoRequest;
import com.example.snapcampus.dto.response.comment.CommentDtoResponse;
import com.example.snapcampus.dto.response.post.PostAggregateDtoResponse;
import com.example.snapcampus.service.CommentService;
import com.example.snapcampus.service.PostService;
import com.example.snapcampus.service.SecurityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class PostController {
    private final PostService postService;
    private final CommentService commentService;
    private final SecurityService securityService;

    public PostController(PostService postService, CommentService commentService, SecurityService securityService) {
        this.postService = postService;
        this.commentService = commentService;
        this.securityService = securityService;
    }

    @GetMapping("/post/{id}")
    public String getPost(@PathVariable String id, Model model) {
        PostAggregateDtoResponse post = postService.getPost(securityService.getMemberUserId(), Long.valueOf(id));
        PageWrapper<CommentDtoResponse> comments = commentService.list(new CommentListDtoRequest(post.getId(), 1L, 40L, "asc"));

        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        return "post/index"; // Mustache 파일 경로
    }
}
