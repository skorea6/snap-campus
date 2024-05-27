package com.example.snapcampus.controller.api;


import com.example.snapcampus.common.dto.BaseResponse;
import com.example.snapcampus.dto.request.map.MapPlaceAddRequest;
import com.example.snapcampus.service.MapService;
import com.example.snapcampus.service.SecurityService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/map")
public class MapApiController {
    private final MapService mapService;
    private final SecurityService securityService;

    public MapApiController(MapService mapService, SecurityService securityService) {
        this.mapService = mapService;
        this.securityService = securityService;
    }

    @PostMapping("/place/add")
    public BaseResponse<Object> addPlace(@RequestBody @Valid MapPlaceAddRequest mapPlaceAddRequest) {
        securityService.checkIsLogined();

        String memberUserId = securityService.getMemberUserId();
        String resultMsg = mapService.addPlace(memberUserId, mapPlaceAddRequest);
        return new BaseResponse<>(resultMsg);
    }
}
