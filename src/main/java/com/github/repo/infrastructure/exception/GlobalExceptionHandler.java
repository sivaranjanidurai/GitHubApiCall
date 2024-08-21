package com.github.repo.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
    @ExceptionHandler(GitRepoNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(GitRepoNotFoundException ex, HandlerMethod handlerMethod, HttpServletRequest request) {
        Class ControllerName = handlerMethod.getMethod().getDeclaringClass();
        String MethodName = handlerMethod.getMethod().getName();
        ErrorResponse error=new ErrorResponse();

        error.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setMessage(ex.getMessage());
        error.setController(ControllerName.toString());
        error.setService(MethodName.toString());
        error.setTimestamp(sdf.format(System.currentTimeMillis()));

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
    }
}
