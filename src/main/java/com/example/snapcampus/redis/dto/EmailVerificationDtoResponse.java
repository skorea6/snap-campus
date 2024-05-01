package com.example.snapcampus.redis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerificationDtoResponse implements Serializable {
    private String token = "";
}
