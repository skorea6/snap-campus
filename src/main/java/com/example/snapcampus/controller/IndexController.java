package com.example.snapcampus.controller;

import com.example.snapcampus.dto.response.event.EventDetailDtoResponse;
import com.example.snapcampus.dto.response.map.MapDtoResponse;
import com.example.snapcampus.dto.response.post.PostDetailDtoResponse;
import com.example.snapcampus.service.MapService;
import com.example.snapcampus.service.PostLikeService;
import com.example.snapcampus.service.PostService;
import com.example.snapcampus.service.SecurityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {
    private final MapService mapService;
    private final PostService postService;
    private final PostLikeService postLikeService;
    private final SecurityService securityService;

    public IndexController(MapService mapService, PostService postService, PostLikeService postLikeService, SecurityService securityService) {
        this.mapService = mapService;
        this.postService = postService;
        this.postLikeService = postLikeService;
        this.securityService = securityService;
    }

    @GetMapping("/")
    public String index(Model model) {
        String memberUserId = securityService.getMemberUserId();

        List<MapDtoResponse> places = mapService.getAllPlace();
        List<PostDetailDtoResponse> allPosts = postService.getAllPosts();

        List<List<PostDetailDtoResponse>> columnPosts = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            columnPosts.add(new ArrayList<>());
        }

        EventDetailDtoResponse.processEventPosts(allPosts, columnPosts);

        model.addAttribute("places", places);
        model.addAttribute("allColumnPosts", columnPosts);

        if(memberUserId != null){
            List<PostDetailDtoResponse> likePosts = postLikeService.list(memberUserId);
            model.addAttribute("likePosts", likePosts);
            model.addAttribute("hasLikePosts", true);
        }

        return "index";
    }
}
