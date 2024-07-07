package com.example.snapcampus.dto.request.post;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostLikeDtoRequest {
    @NotNull(message = "postId는 필수 입력 값입니다.")
    private Long postId;
}
