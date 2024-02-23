package com.compassuol.challenge.msuser.application.exceptions.handler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

@Getter

public class ErrorMessage {
    private String path;
    private String method;
    private final int code;
    private final String status;
    private final String message;
    private final List<FieldErrorDetails> details = new ArrayList<>();




    public ErrorMessage(int code, String status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public ErrorMessage(HttpStatus status, String message) {
        this.code = status.value();
        this.status = status.getReasonPhrase();
        this.message = message;
    }

    public ErrorMessage(HttpServletRequest request,HttpStatus status, String message) {
        this.path = request.getRequestURI();
        this.method = request.getMethod();
        this.code = status.value();
        this.status = status.getReasonPhrase();
        this.message = message;
    }

    @Getter
    @AllArgsConstructor
    private static class FieldErrorDetails {
        private String field;
        private String message;
    }

    private void addDetails(BindingResult result) {
        for (FieldError error : result.getFieldErrors()) {
            this.details.add(new FieldErrorDetails(error.getField(), error.getDefaultMessage()));
        }

    }
}
