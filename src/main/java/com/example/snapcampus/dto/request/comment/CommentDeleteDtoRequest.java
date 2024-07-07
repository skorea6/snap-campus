package com.example.snapcampus.dto.request.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDeleteDtoRequest {
    @NotNull(message = "id는 필수 입력 값입니다.")
    private Long id;
}
