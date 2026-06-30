package com.jobportal.jobconnect.exception;

import com.jobportal.jobconnect.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(ResourceNotFoundException ex)
    {
        log.error("Resource not found :{}", ex.getMessage());
        return ResponseEntity.status(404).body(ApiResponse.error(ex.getMessage(),404));
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicate(DuplicateEmailException ex)
    {
        log.warn("Duplicate email: {}",ex.getMessage());
        return ResponseEntity.status(400).body(ApiResponse.error(ex.getMessage(), 400));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadRequest(BadRequestException ex)
    {
        log.warn("BAD REQUEST: {}" , ex.getMessage());
        return ResponseEntity.status(400).body(ApiResponse.error(ex.getMessage(), 400));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnauth(UnauthorizedException ex)
    {
        return ResponseEntity.status(401).body(ApiResponse.error(ex.getMessage(), 401));

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex)
    {
        String msg= ex.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();
        log.warn("Validation error: {}", msg);
        return ResponseEntity.status(400).body(ApiResponse.error(ex.getMessage(),400));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleAll(Exception ex)
    {
        log.error("Unexpected error: {}", ex.getMessage());
        return ResponseEntity.status(500).body(ApiResponse.error("Server mein kuch gadbad ho gayi!", 500));
    }

}
