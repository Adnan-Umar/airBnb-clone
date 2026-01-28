package com.adnanumar.projects.airBnbApp.advices;

import com.adnanumar.projects.airBnbApp.exception.ResourceNotFoundException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ApiError error = ApiError.builder()
                .status(HttpStatus.NOT_FOUND)
                .message(ex.getMessage())
                .build();

        return buildErrorResponseEntity(error);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<?>> handleAuthenticationException(AuthenticationException ex) {
        ApiError error = ApiError.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .message(ex.getMessage())
                .build();
        return buildErrorResponseEntity(error);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse<?>> handleJwtException(JwtException ex) {
        ApiError error = ApiError.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .message(ex.getMessage())
                .build();
        return buildErrorResponseEntity(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAccessDeniedException(AccessDeniedException ex) {
        ApiError error = ApiError.builder()
                .status(HttpStatus.FORBIDDEN)
                .message(ex.getMessage())
                .build();
        return buildErrorResponseEntity(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleInternalServerError(Exception ex) {
        ApiError error = ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(ex.getMessage())
                .build();
        return buildErrorResponseEntity(error);
    }

    private ResponseEntity<ApiResponse<?>> buildErrorResponseEntity(ApiError error) {
        return new ResponseEntity<>(new ApiResponse<>(error), error.getStatus());
    }

}
