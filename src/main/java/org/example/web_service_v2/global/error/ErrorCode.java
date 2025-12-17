package org.example.web_service_v2.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "PASSWORD_MISMATCH", "비밀번호가 일치하지 않습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", "올바르지 않습니다."),
    TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "TYPE_MISMATCH", "요청 값의 타입이 올바르지 않습니다."),
    INVALID_JSON(HttpStatus.BAD_REQUEST, "INVALID_JSON", "요청 바디가 올바르지 않습니다."),

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "FORBIDDEN", "권한이 없습니다."),

    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "서버 오류가 발생했습니다.");



    private final HttpStatus status;
    private final String code;
    private final String message;
}
