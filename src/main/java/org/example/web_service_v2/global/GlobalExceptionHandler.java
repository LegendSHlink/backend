package org.example.web_service_v2.global;

import io.jsonwebtoken.JwtException;
import org.example.web_service_v2.global.error.ErrorCode;
import org.example.web_service_v2.global.error.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.security.sasl.AuthenticationException;
import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            AuthenticationException.class,
            BadCredentialsException.class,
            UsernameNotFoundException.class,
            JwtException.class
    })
    public ResponseEntity<ErrorResponse> handleAuth(Exception e){
        ErrorCode ec = ErrorCode.UNAUTHORIZED;
        return ResponseEntity.status(ec.getStatus())
                .body(ErrorResponse.of(ec, e.getMessage(), null));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException e){
        ErrorCode ec = ErrorCode.FORBIDDEN;
        return ResponseEntity.status(ec.getStatus())
                .body(ErrorResponse.of(ec, "접근 권한이 없습니다.", null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgNotValid(MethodArgumentNotValidException e){
        ErrorCode ec = ErrorCode.INVALID_REQUEST;

        var errors = e.getBindingResult().getAllErrors().stream()
                .map(err -> {
                    if (err instanceof FieldError fe) {
                        return ErrorResponse.FieldError.builder()
                                .field(fe.getField())
                                .message(fe.getDefaultMessage())
                                .build();
                    }
                    return ErrorResponse.FieldError.builder()
                            .field("_global")
                            .message(err.getDefaultMessage())
                            .build();
                })
                .toList();

        String message = errors.isEmpty() ? ec.getMessage() : errors.get(0).getMessage();

        return ResponseEntity.status(ec.getStatus())
                .body(ErrorResponse.of(ec, message, errors));
    }

    @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(jakarta.validation.ConstraintViolationException e) {

        ErrorCode ec = ErrorCode.INVALID_REQUEST;

        var errors = e.getConstraintViolations().stream()
                .map(v -> ErrorResponse.FieldError.builder()
                        .field(v.getPropertyPath().toString())
                        .message(v.getMessage())
                        .build())
                .toList();

        String message = errors.isEmpty() ? ec.getMessage() : errors.get(0).getMessage();

        return ResponseEntity.status(ec.getStatus())
                .body(ErrorResponse.of(ec, message, errors));
    }

    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleNotReadable(Exception e) {
        ErrorCode ec = ErrorCode.INVALID_REQUEST;
        return ResponseEntity.status(ec.getStatus())
                .body(ErrorResponse.of(ec, "요청 바디(JSON)가 올바르지 않습니다.", null));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException e) {
        ErrorCode ec = e.getErrorCode();
        return ResponseEntity.status(ec.getStatus())
                .body(ErrorResponse.of(ec, e.getMessage(), null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknown(Exception e) {
        e.printStackTrace();
        ErrorCode ec = ErrorCode.INTERNAL_ERROR;
        return ResponseEntity.status(ec.getStatus())
                .body(ErrorResponse.of(ec));
    }


}
