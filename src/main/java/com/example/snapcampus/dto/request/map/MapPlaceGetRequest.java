package com.example.snapcampus.dto.request.map;

import com.example.snapcampus.entity.Map;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MapPlaceGetRequest {
    @NotNull(message = "id는 필수값입니다.")
    private Long id;
}
