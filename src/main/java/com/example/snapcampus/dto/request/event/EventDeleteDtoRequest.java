package com.example.snapcampus.dto.request.event;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventDeleteDtoRequest {
    @NotNull(message = "id는 필수 입력 값입니다.")
    private Long id;
}
