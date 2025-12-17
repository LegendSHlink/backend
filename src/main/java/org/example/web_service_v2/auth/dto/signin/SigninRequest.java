package org.example.web_service_v2.auth.dto.signin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SigninRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

}
