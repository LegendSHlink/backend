package org.example.web_service_v2.global.apiPayload.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 400 BAD REQUEST
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", "잘못된 요청입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "INVALID_PASSWORD", "비밀번호가 일치하지 않습니다."),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "PASSWORD_MISMATCH", "비밀번호 확인이 일치하지 않습니다."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "DUPLICATE_EMAIL", "이미 존재하는 이메일입니다."),
    TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "TYPE_MISMATCH", "요청 값의 타입이 올바르지 않습니다."),
    INVALID_JSON(HttpStatus.BAD_REQUEST, "INVALID_JSON", "요청 바디가 올바르지 않습니다."),

    // 401 UNAUTHORIZED
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "인증이 필요합니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "INVALID_TOKEN", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "EXPIRED_TOKEN", "만료된 토큰입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "INVALID_REFRESH_TOKEN", "유효하지 않은 리프레시 토큰입니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "EXPIRED_REFRESH_TOKEN", "만료된 리프레시 토큰입니다."),
    REUSED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "REUSED_REFRESH_TOKEN", "리프레시 토큰 재사용이 감지되었습니다. 다시 로그인하세요."),

    // 403 FORBIDDEN
    FORBIDDEN(HttpStatus.FORBIDDEN, "FORBIDDEN", "권한이 없습니다."),
    CHAT_SELF_ROOM_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "CHAT_SELF_ROOM_NOT_ALLOWED", "자기 자신과 채팅할 수 없습니다."),
    CHAT_INVALID_MESSAGE(HttpStatus.BAD_REQUEST, "CHAT_INVALID_MESSAGE", "메시지는 비어 있을 수 없습니다."),
    CHAT_INVALID_CURSOR(HttpStatus.BAD_REQUEST, "CHAT_INVALID_CURSOR", "유효하지 않은 커서 값입니다."),
    CHAT_MESSAGE_NOT_IN_ROOM(HttpStatus.BAD_REQUEST, "CHAT_MESSAGE_NOT_IN_ROOM", "해당 채팅방의 메시지가 아닙니다."),
    CHAT_NOT_PARTICIPANT(HttpStatus.FORBIDDEN, "CHAT_NOT_PARTICIPANT", "해당 채팅방 참여자가 아닙니다."),


    // 404 NOT FOUND
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "사용자를 찾을 수 없습니다."),
    PROFILE_NOT_FOUND(HttpStatus.NOT_FOUND, "PROFILE_NOT_FOUND", "프로필을 찾을 수 없습니다."),
    CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "CHAT_ROOM_NOT_FOUND", "채팅방을 찾을 수 없습니다."),

    CHAT_ROOM_CONFLICT(HttpStatus.CONFLICT, "CHAT_ROOM_CONFLICT", "채팅방 생성 충돌이 발생했습니다."),

    // 500 INTERNAL SERVER ERROR
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "서버 오류가 발생했습니다."),

    CANNOT_FOLLOW_SELF(HttpStatus.NOT_ACCEPTABLE, "FOLLOW_400_1", "자기 자신은 팔로우할 수 없습니다."),
    ALREADY_FOLLOWING(HttpStatus.BAD_REQUEST, "FOLLOW_409_1", "이미 팔로우한 사용자입니다."),
    NOT_FOLLOWING(HttpStatus.BAD_REQUEST, "FOLLOW_409_2", "팔로우 상태가 아닙니다.");
    private final HttpStatus status;
    private final String code;
    private final String message;
}
