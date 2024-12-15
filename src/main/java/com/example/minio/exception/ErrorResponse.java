package com.example.minio.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ErrorResponse {

    private int status;
    private String message;
    private String action;
    private boolean success;
    private String time;

    public ErrorResponse(int status, String message, String action, boolean success, String time) {
        this.status = status;
        this.message = message;
        this.action = action;
        this.success = success;
        this.time = time;
    }
}
