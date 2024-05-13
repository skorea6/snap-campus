package com.example.snapcampus.dto.request.map;

import com.example.snapcampus.entity.Map;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MapPlaceAddRequest {
    @NotNull(message = "좌표 x는 필수 입력 값입니다.")
    private Double coordinate_x;

    @NotNull(message = "좌표 y는 필수 입력 값입니다.")
    private Double coordinate_y;

    @NotBlank(message = "장소 이름은 필수 입력 값입니다.")
    @Size(max = 50, message = "장소 이름은 최대 50자여야 합니다.")
    private String placeName;

    @NotBlank(message = "장소 유형은 필수 입력 값입니다.")
    @Size(max = 50, message = "장소 유형은 최대 50자여야 합니다.")
    private String placeType;

    @NotBlank(message = "장소 설명은 필수 입력 값입니다.")
    @Size(max = 200, message = "장소 설명은 최대 200자여야 합니다.")
    private String placeDescription;

    public Map toEntity(){
        return new Map(coordinate_x, coordinate_y, placeName, placeType, placeDescription);
    }
}
