package org.example.web_service_v2.global.error.dto;

import lombok.*;
import org.example.web_service_v2.global.error.ErrorCode;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String code;
    private String message;
    private List<FieldError> errors;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FieldError{
        private String field;
        private String message;
    }

    public static ErrorResponse of(ErrorCode ec, String message, List<FieldError> errors){
        return ErrorResponse.builder()
                .status(ec.getStatus().value())
                .code(ec.getCode())
                .message(message != null ? message:ec.getMessage())
                .errors(errors)
                .build();
    }

    public static ErrorResponse of(ErrorCode ec){
        return of(ec, null, null);
    }
}
