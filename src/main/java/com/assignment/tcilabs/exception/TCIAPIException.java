package com.assignment.tcilabs.exception;

import org.springframework.http.HttpStatus;

public class TCIAPIException extends RuntimeException {

    private HttpStatus status;
    private String message;

    public TCIAPIException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public TCIAPIException(String message, HttpStatus status, String message1) {
        super(message);
        this.status = status;
        this.message = message1;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
