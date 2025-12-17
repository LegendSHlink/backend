package org.example.web_service_v2.auth;

import lombok.RequiredArgsConstructor;
import org.example.web_service_v2.auth.dto.signin.SigninRequest;
import org.example.web_service_v2.auth.dto.signin.SigninResponse;
import org.example.web_service_v2.auth.dto.signup.SignupRequest;
import org.example.web_service_v2.config.JwtProperties;
import org.example.web_service_v2.global.BusinessException;
import org.example.web_service_v2.global.error.ErrorCode;
import org.example.web_service_v2.jwt.JwtProvider;
import org.example.web_service_v2.jwt.utils.TokenHash;
import org.example.web_service_v2.user.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final JwtProperties jwtProperties;

    @Transactional
    public SigninResponse login(SigninRequest request){
        User user = authRepository.findUserByEmail(request.getEmail());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }
        return getSigninResponse(user);
    }

    @Transactional
    public void signup(SignupRequest request){

        String encoded = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .email(request.getEmail())
                .password(encoded)
                .nickname(request.getNickname())
                .createdAt(LocalDate.now())
                .build();

        authRepository.save(user);
    }

    @Transactional
    public SigninResponse refresh(String refreshToken) {

        if (!jwtProvider.validateToken(refreshToken) || !jwtProvider.isRefreshToken(refreshToken)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다.");
        }

        Long userId = jwtProvider.getUserId(refreshToken);

        User user = authRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 만료 시간도 DB 기준으로 한 번 더 체크(방어)
        if (user.getRefreshTokenExpiresAt() == null || user.getRefreshTokenExpiresAt().isBefore(LocalDateTime.now())) {
            user.setRefreshTokenHash(null);
            user.setRefreshTokenExpiresAt(null);
            throw new IllegalArgumentException("리프레시 토큰이 만료되었습니다.");
        }

        String incomingHash = TokenHash.sha256Hex(refreshToken);

        // 🔥 Rotation + Reuse Detection 핵심
        if (user.getRefreshTokenHash() == null || !user.getRefreshTokenHash().equals(incomingHash)) {
            // 재사용/탈취 의심 → 전부 무효화(= 강제 로그아웃)
            user.setRefreshTokenHash(null);
            user.setRefreshTokenExpiresAt(null);
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "리프레시 토큰 재사용이 감지되었습니다. 다시 로그인하세요.");
        }

        // 여기부터 정상 갱신
        return getSigninResponse(user);
    }

    @Transactional
    public void logout(Long userId){
        User user = authRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REQUEST));

        user.setRefreshTokenHash(null);
        user.setRefreshTokenExpiresAt(null);
    }

    private SigninResponse getSigninResponse(User user) {
        String newAccessToken = jwtProvider.createAccessToken(user.getId(), user.getEmail());
        String newRefreshToken = jwtProvider.createRefreshToken(user.getId());

        user.setRefreshTokenHash(TokenHash.sha256Hex(newRefreshToken));
        user.setRefreshTokenExpiresAt(LocalDateTime.now().plusSeconds(jwtProperties.getRefreshTokenExpirationMs() / 1000));

        return SigninResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .email(user.getEmail())
                .userId(user.getId())
                .build();
    }

}
