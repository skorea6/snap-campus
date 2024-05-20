package com.example.snapcampus.controller;

import com.example.snapcampus.dto.response.map.MapDtoResponse;
import com.example.snapcampus.service.MapService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class IndexController {
    private final MapService mapService;

    public IndexController(MapService mapService) {
        this.mapService = mapService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<MapDtoResponse> places = mapService.getAllPlace();
        model.addAttribute("places", places);
        return "/index";
    }

    @GetMapping("/search-result")
    public String searchResult() {
        return "search-result/index";
    }
}
