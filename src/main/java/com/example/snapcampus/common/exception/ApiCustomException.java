package com.example.snapcampus.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ApiCustomException extends RuntimeException {
    private int statusCode;
    private String statusMessage;

    public ApiCustomException(String statusMessage, int statusCode) {
        super(statusMessage); // RuntimeException의 생성자에 메시지를 전달
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }
}
