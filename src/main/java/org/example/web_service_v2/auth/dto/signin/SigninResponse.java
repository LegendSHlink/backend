package org.example.web_service_v2.auth.dto.signin;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SigninResponse {
    private Long userId;
    private String email;

    private String accessToken;
    private String refreshToken;
}
