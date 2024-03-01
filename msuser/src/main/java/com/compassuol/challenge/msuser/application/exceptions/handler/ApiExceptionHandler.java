package com.compassuol.challenge.msuser.application.exceptions.handler;

import com.compassuol.challenge.msuser.application.exceptions.EmailNotFoundException;
import com.compassuol.challenge.msuser.application.exceptions.ErrorNotificationException;
import com.compassuol.challenge.msuser.application.exceptions.IncorrectPasswordExcpetion;
import com.compassuol.challenge.msuser.application.exceptions.UniqueUserViolationException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleGenericException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }
    @ExceptionHandler(IncorrectPasswordExcpetion.class)
    public ResponseEntity<ErrorMessage> handIncorrectPasswordExcpetion(IncorrectPasswordExcpetion ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(HttpStatus.NOT_FOUND, ex.getMessage()));
    }
    @ExceptionHandler(UniqueUserViolationException.class)
    public ResponseEntity<ErrorMessage> handleUniqueUserViolationException(RuntimeException ex,
                                                                              HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(HttpStatus.CONFLICT, ex.getMessage()));
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleEmailNotFoundException(EmailNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(ErrorNotificationException.class)
    public ResponseEntity<ErrorMessage> handleErrorNotificationException(ErrorNotificationException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR) // ou outro status de erro apropriado
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()));
    }

}
