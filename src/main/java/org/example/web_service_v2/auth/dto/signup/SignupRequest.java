package org.example.web_service_v2.auth.dto.signup;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.example.web_service_v2.auth.validator.PasswordMatch;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@PasswordMatch
public class SignupRequest {

    @Email(message = "형식이 맞지 않습니다.")
    @NotBlank(message = "필수 입력사항입니다.")
    private String email;

    @NotBlank(message = "필수 입력사항입니다.")
    private String nickname;

    @NotBlank(message = "필수 입력사항입니다.")
    private String password;

    @NotBlank(message = "필수 입력사항입니다.")
    private String passwordMatch;
}
