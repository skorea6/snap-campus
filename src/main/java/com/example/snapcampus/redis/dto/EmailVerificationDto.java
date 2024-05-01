package com.example.snapcampus.redis.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerificationDto implements Serializable {
    private String email = "";
    private String verificationToken = "";
    private String verificationNumber = "";
    private int attemptCount = 0;
    private boolean isDone = false;

    public EmailVerificationDto(String email, String verificationToken, String verificationNumber) {
        this.email = email;
        this.verificationToken = verificationToken;
        this.verificationNumber = verificationNumber;
    }

    public EmailVerificationDtoResponse toResponse() {
        return new EmailVerificationDtoResponse(this.verificationToken);
    }
}

