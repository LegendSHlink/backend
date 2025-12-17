package org.example.web_service_v2.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.web_service_v2.auth.dto.signin.SigninRequest;
import org.example.web_service_v2.auth.dto.signin.SigninResponse;
import org.example.web_service_v2.auth.dto.signup.SignupRequest;
import org.example.web_service_v2.jwt.dto.RefreshRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody @Valid SignupRequest request){
        authService.signup(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signin")
    public ResponseEntity<SigninResponse> login(@RequestBody@Valid SigninRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<SigninResponse> refresh(@RequestBody @Valid RefreshRequest request){
        return ResponseEntity.ok(authService.refresh(request.getRefreshToken()));
    }

}
