package com.umutgldn.tickethub.common.exception;

import com.umutgldn.tickethub.auth.exception.AccountLockedException;
import com.umutgldn.tickethub.auth.exception.InvalidCredentialsException;
import com.umutgldn.tickethub.auth.exception.InvalidTokenException;
import com.umutgldn.tickethub.auth.exception.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiError> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        log.warn("Invalid credentials attempts: {}", ex.getMessage());
        return buildResponse(HttpStatus.UNAUTHORIZED, "Invalid email or password");
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiError> handleInvalidTokenException(InvalidTokenException ex) {
        log.warn("Invalid token : {}", ex.getMessage());
        return buildResponse(HttpStatus.UNAUTHORIZED, "Invalid or expired token");
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ApiError>  handleTokenExpiredException(TokenExpiredException ex) {
        log.warn("Token expired : {}", ex.getMessage());
        return buildResponse(HttpStatus.UNAUTHORIZED, "Token has expired");
    }

    @ExceptionHandler(AccountLockedException.class)
    public ResponseEntity<ApiError>  handleAccountLockedException(AccountLockedException ex) {
        log.warn("Locked account access attempt: {}", ex.getMessage());
        return buildResponse(HttpStatus.LOCKED, ex.getMessage());
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError>  handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.warn("Resource not found : {}", ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError>  handleBusinessException(BusinessException ex) {
        log.warn("Business rule violation : {}", ex.getMessage());
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError>  handleAccessDeniedException(AccessDeniedException ex) {
        log.warn("Access denied : {}", ex.getMessage());
        return buildResponse(HttpStatus.FORBIDDEN, "You do not have permission to perform this action");
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError>  handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String,String> fieldErrors=ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Invalid value",
                        (first,second)-> first
                ));

        log.warn("Validation failed : {}", fieldErrors);

        ApiError error=new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                fieldErrors,
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException ex) {
        log.warn("Spring security access denied : {}", ex.getMessage());
        return buildResponse(HttpStatus.FORBIDDEN, "You do not have permission to perform this action");
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError>  handleException(Exception ex) {
        log.error("Unexpected error ", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,"An unexpected error occurred");
    }

    private ResponseEntity<ApiError> buildResponse(HttpStatus status, String message) {
        ApiError error= new ApiError(
                status.value(),
                message,
                null,
                Instant.now()
        );
        return ResponseEntity.status(status).body(error);
    }

}
