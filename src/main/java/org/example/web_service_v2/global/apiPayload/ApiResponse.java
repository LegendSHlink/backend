package org.example.web_service_v2.global.apiPayload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    
    private final boolean success;
    private final String code;
    private final String message;
    private final T data;

    // 성공 응답
    public static <T> ApiResponse<T> onSuccess(T data) {
        return new ApiResponse<>(true, "SUCCESS", "요청이 성공했습니다.", data);
    }

    public static <T> ApiResponse<T> onSuccess(T data, String message) {
        return new ApiResponse<>(true, "SUCCESS", message, data);
    }

    // 실패 응답
    public static <T> ApiResponse<T> onFailure(String code, String message) {
        return new ApiResponse<>(false, code, message, null);
    }
}
