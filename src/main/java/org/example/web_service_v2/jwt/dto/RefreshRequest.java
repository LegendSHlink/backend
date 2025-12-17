package org.example.web_service_v2.jwt.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RefreshRequest {

    @NotBlank
    private String refreshToken;
}
