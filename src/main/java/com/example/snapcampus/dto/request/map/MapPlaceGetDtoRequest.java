package com.example.snapcampus.dto.request.map;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MapPlaceGetDtoRequest {
    @NotNull(message = "id는 필수값입니다.")
    private Long id;
}
