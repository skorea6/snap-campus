package com.example.snapcampus.dto.response.map;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MapAddDtoResponse {
    private Long id;

    public MapAddDtoResponse(Long id) {
        this.id = id;
    }
}
