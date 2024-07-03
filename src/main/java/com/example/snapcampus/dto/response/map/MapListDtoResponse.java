package com.example.snapcampus.dto.response.map;


import com.example.snapcampus.dto.response.event.EventDtoResponse;
import com.example.snapcampus.dto.response.member.MemberDtoResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MapListDtoResponse {
    private Long id;
    private Double coordinate_x;
    private Double coordinate_y;
    private String placeName;
    private String placeType;
    private String placeDescription;

    private MemberDtoResponse member;

    public MapListDtoResponse(Long id, Double coordinate_x, Double coordinate_y, String placeName, String placeType, String placeDescription, MemberDtoResponse member) {
        this.id = id;
        this.coordinate_x = coordinate_x;
        this.coordinate_y = coordinate_y;
        this.placeName = placeName;
        this.placeType = placeType;
        this.placeDescription = placeDescription;
        this.member = member;
    }
}
