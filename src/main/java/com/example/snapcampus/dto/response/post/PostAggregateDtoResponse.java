package com.example.snapcampus.dto.response.post;


import com.example.snapcampus.dto.response.event.EventAggregateDtoResponse;
import com.example.snapcampus.dto.response.member.MemberDtoResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostAggregateDtoResponse extends PostDtoResponse {
    private EventAggregateDtoResponse event;

    public PostAggregateDtoResponse(Long id, String title, String description, Long likeCount, String department, String createdAt, String thumbImage, List<String> images, MemberDtoResponse member, EventAggregateDtoResponse event) {
        super(id, title, description, likeCount, department, createdAt, thumbImage, images, member);
        this.event = event;
    }
}
