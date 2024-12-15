package com.example.minio.utils;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ApiResponse {

    private String message;
    private boolean success;

    public ApiResponse(){

    }
    public ApiResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

}
