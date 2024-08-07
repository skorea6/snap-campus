package com.example.snapcampus.controller.api;


import com.example.snapcampus.common.dto.BaseResponse;
import com.example.snapcampus.dto.request.map.MapPlaceAddDtoRequest;
import com.example.snapcampus.dto.request.map.MapPlaceDeleteDtoRequest;
import com.example.snapcampus.dto.response.map.MapAddDtoResponse;
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
    public BaseResponse<MapAddDtoResponse> addPlace(@RequestBody @Valid MapPlaceAddDtoRequest mapPlaceAddDtoRequest) {
        securityService.checkIsLogined();

        String memberUserId = securityService.getMemberUserId();
        MapAddDtoResponse response = mapService.addPlace(memberUserId, mapPlaceAddDtoRequest);
        return new BaseResponse<>(response);
    }

    @PostMapping("/place/delete")
    public BaseResponse<Object> deletePlace(@RequestBody @Valid MapPlaceDeleteDtoRequest mapPlaceDeleteDtoRequest) {
        String resultMsg = mapService.deletePlace(mapPlaceDeleteDtoRequest);
        return new BaseResponse<>(resultMsg);
    }
}
