package org.example.web_service_v2.domain.users.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.web_service_v2.domain.users.dto.signin.SigninRequest;
import org.example.web_service_v2.domain.users.dto.signin.SigninResponse;
import org.example.web_service_v2.domain.users.dto.signup.SignupRequest;
import org.example.web_service_v2.domain.users.service.AuthService;
import org.example.web_service_v2.global.apiPayload.ApiResponse;
import org.example.web_service_v2.global.auth.dto.RefreshRequest;
import org.springframework.web.bind.annotation.*;

@Tag(name = "인증", description = "회원가입, 로그인, 토큰 갱신 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    
    private final AuthService authService;

    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @PostMapping("/signup")
    public ApiResponse<Void> signup(@RequestBody @Valid SignupRequest request) {
        authService.signup(request);
        return ApiResponse.onSuccess(null);
    }

    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인하여 액세스 토큰과 리프레시 토큰을 발급받습니다.")
    @PostMapping("/signin")
    public ApiResponse<SigninResponse> signin(@RequestBody @Valid SigninRequest request) {
        SigninResponse response = authService.login(request);
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "토큰 갱신", description = "리프레시 토큰으로 새로운 액세스 토큰을 발급받습니다.")
    @PostMapping("/refresh")
    public ApiResponse<SigninResponse> refresh(@RequestBody @Valid RefreshRequest request) {
        SigninResponse response = authService.refresh(request.getRefreshToken());
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "로그아웃", description = "사용자의 리프레시 토큰을 무효화합니다.")
    @PostMapping("/logout")
    public ApiResponse<Void> logout(
            @Parameter(description = "사용자 ID") @RequestParam Long userId
    ) {
        authService.logout(userId);
        return ApiResponse.onSuccess(null);
    }
}
