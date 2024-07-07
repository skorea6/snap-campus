package com.example.snapcampus.dto.request.comment;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentAddDtoRequest {
    @NotNull(message = "postId는 필수 입력 값입니다.")
    private Long postId;

    @NotBlank(message = "content은 필수 입력 값입니다.")
    private String content;
}
