package org.example.web_service_v2.domain.users.dto.signup;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignupRequest {

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "이메일은 필수 입력사항입니다.")
    private String email;

    @NotBlank(message = "이름은 필수 입력사항입니다.")
    private String name;

    @NotBlank(message = "비밀번호는 필수 입력사항입니다.")
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수 입력사항입니다.")
    private String passwordConfirm;
}
