package com.example.snapcampus.dto.response.event;

import com.example.snapcampus.dto.response.map.MapDtoResponse;
import com.example.snapcampus.dto.response.member.MemberDtoResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventAggregateDtoResponse extends EventDtoResponse {
    private MapDtoResponse map;

    public EventAggregateDtoResponse(Long id, String name, String description, String startDate, String stopDate, String organizer, MemberDtoResponse member, MapDtoResponse map) {
        super(id, name, description, startDate, stopDate, organizer, member);
        this.map = map;
    }
}
