package com.example.snapcampus.dto.response.map;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MapDtoResponse {
    private Long id;
    private Double coordinate_x;
    private Double coordinate_y;
    private String placeName;
    private String placeType;
    private String placeDescription;

    public MapDtoResponse(Long id, Double coordinate_x, Double coordinate_y, String placeName, String placeType, String placeDescription) {
        this.id = id;
        this.coordinate_x = coordinate_x;
        this.coordinate_y = coordinate_y;
        this.placeName = placeName;
        this.placeType = placeType;
        this.placeDescription = placeDescription;
    }
}
