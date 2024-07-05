package com.example.snapcampus.controller.api;


import com.example.snapcampus.common.dto.BaseResponse;
import com.example.snapcampus.dto.request.event.EventAddRequest;
import com.example.snapcampus.service.EventService;
import com.example.snapcampus.service.SecurityService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/event")
public class EventApiController {
    private final EventService eventService;
    private final SecurityService securityService;

    public EventApiController(EventService eventService, SecurityService securityService) {
        this.eventService = eventService;
        this.securityService = securityService;
    }

    @PostMapping("/add")
    public BaseResponse<Object> addEvent(@RequestBody @Valid EventAddRequest eventAddRequest) {
        securityService.checkIsLogined();

        String memberUserId = securityService.getMemberUserId();
        String resultMsg = eventService.addEvent(memberUserId, eventAddRequest);
        return new BaseResponse<>(resultMsg);
    }
}
