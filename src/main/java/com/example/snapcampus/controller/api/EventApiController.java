package com.example.snapcampus.controller.api;


import com.example.snapcampus.common.dto.BaseResponse;
import com.example.snapcampus.dto.request.event.EventAddDtoRequest;
import com.example.snapcampus.dto.request.event.EventDeleteDtoRequest;
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
    public BaseResponse<Object> addEvent(@RequestBody @Valid EventAddDtoRequest eventAddDtoRequest) {
        securityService.checkIsLogined();

        String memberUserId = securityService.getMemberUserId();
        String resultMsg = eventService.addEvent(memberUserId, eventAddDtoRequest);
        return new BaseResponse<>(resultMsg);
    }

    /**
     * 관리자만 이용 가능
     */
    @PostMapping("/delete")
    public BaseResponse<Object> deleteEvent(@RequestBody @Valid EventDeleteDtoRequest eventDeleteDtoRequest) {
        String resultMsg = eventService.deleteEvent(eventDeleteDtoRequest);
        return new BaseResponse<>(resultMsg);
    }
}
