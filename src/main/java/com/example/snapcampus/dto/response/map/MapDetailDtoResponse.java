package com.example.snapcampus.dto.response.map;


import com.example.snapcampus.dto.response.event.EventDetailDtoResponse;
import com.example.snapcampus.dto.response.member.MemberDtoResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MapDetailDtoResponse extends MapDtoResponse {
    private List<EventDetailDtoResponse> events;

    public MapDetailDtoResponse(Long id, Double coordinate_x, Double coordinate_y, String placeName, String placeType, String placeDescription, MemberDtoResponse member, List<EventDetailDtoResponse> events) {
        super(id, coordinate_x, coordinate_y, placeName, placeType, placeDescription, member);
        this.events = events;
    }
}
