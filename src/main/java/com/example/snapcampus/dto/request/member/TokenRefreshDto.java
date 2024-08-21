package com.example.snapcampus.dto.request.member;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRefreshDto {
    @NotBlank(message = "refreshToken은 필수 입력 값입니다.")
    private String refreshToken;
}
