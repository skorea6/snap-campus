package com.example.snapcampus.controller;

import com.example.snapcampus.dto.response.map.MapDetailDtoResponse;
import com.example.snapcampus.service.MapService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class MapController {
    private final MapService mapService;

    public MapController(MapService mapService) {
        this.mapService = mapService;
    }

    @GetMapping("/place/{id}")
    public String getPlace(@PathVariable String id, Model model) {
        MapDetailDtoResponse place = mapService.getPlace(Long.valueOf(id));

        model.addAttribute("place", place);
        return "/place/index"; // Mustache 파일 경로
    }
}
