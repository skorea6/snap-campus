package com.example.snapcampus.controller;

import com.example.snapcampus.dto.response.event.EventDtoResponse;
import com.example.snapcampus.dto.response.map.MapListDtoResponse;
import com.example.snapcampus.dto.response.post.PostDtoResponse;
import com.example.snapcampus.service.MapService;
import com.example.snapcampus.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {
    private final MapService mapService;
    private final PostService postService;

    public IndexController(MapService mapService, PostService postService) {
        this.mapService = mapService;
        this.postService = postService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<MapListDtoResponse> places = mapService.getAllPlace();
        List<PostDtoResponse> allPosts = postService.getAllPosts();

        List<List<PostDtoResponse>> columnPosts = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            columnPosts.add(new ArrayList<>());
        }

        EventDtoResponse.processEventPosts(allPosts, columnPosts);

        model.addAttribute("places", places);
        model.addAttribute("allColumnPosts", columnPosts);
        return "/index";
    }

    @GetMapping("/search-result")
    public String searchResult() {
        return "search-result/index";
    }
}
