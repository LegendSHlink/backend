package org.example.web_service_v2.domain.users.dto.signup;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignupResponse {
    private String email;
    private String name;
}
