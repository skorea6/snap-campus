package com.example.snapcampus.dto.response.event;

import com.example.snapcampus.dto.response.member.MemberDtoResponse;
import com.example.snapcampus.dto.response.post.PostDetailDtoResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EventDtoResponse {
    private Long id;
    private String name;
    private String description;
    private String startDate;
    private String stopDate;
    private String organizer;

    private MemberDtoResponse member;

    public EventDtoResponse(Long id, String name, String description, String startDate, String stopDate, String organizer, MemberDtoResponse member) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.stopDate = stopDate;
        this.organizer = organizer;
        this.member = member;
    }
}
