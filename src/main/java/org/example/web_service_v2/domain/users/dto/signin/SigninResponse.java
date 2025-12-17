package org.example.web_service_v2.domain.users.dto.signin;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SigninResponse {
    private Long userId;
    private String email;
    private String accessToken;
    private String refreshToken;
}
