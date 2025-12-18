package org.example.web_service_v2.domain.users.service;

import lombok.RequiredArgsConstructor;
import org.example.web_service_v2.domain.profiles.repositories.ProfileRepository;
import org.example.web_service_v2.domain.profiles.entity.Profile;
import org.example.web_service_v2.domain.users.dto.signin.SigninRequest;
import org.example.web_service_v2.domain.users.dto.signin.SigninResponse;
import org.example.web_service_v2.domain.users.dto.signup.SignupRequest;
import org.example.web_service_v2.domain.users.entity.User;
import org.example.web_service_v2.domain.users.repository.UserRepository;
import org.example.web_service_v2.global.apiPayload.exception.BusinessException;
import org.example.web_service_v2.global.apiPayload.exception.ErrorCode;
import org.example.web_service_v2.global.auth.jwt.JwtProvider;
import org.example.web_service_v2.global.auth.jwt.TokenHash;
import org.example.web_service_v2.global.config.JwtProperties;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final JwtProperties jwtProperties;
    private final ProfileRepository profileRepository;

    @Transactional
    public SigninResponse login(SigninRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }
        return getSigninResponse(user);
    }

    @Transactional
    public void signup(SignupRequest request) {
        // 이메일 중복 체크
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(encodedPassword)
                .name(request.getName())
                .build();

        Profile profile = Profile.builder()
                .headline(null)
                .field(null)
                .userImage(null)
                .build();

        user.attachProfile(profile);
        userRepository.save(user);
    }

    @Transactional
    public SigninResponse refresh(String refreshToken) {
        if (!jwtProvider.validateToken(refreshToken) || !jwtProvider.isRefreshToken(refreshToken)) {
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Long userId = jwtProvider.getUserId(refreshToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 만료 시간 체크
        if (user.getRefreshTokenExpiresAt() == null || user.getRefreshTokenExpiresAt().isBefore(LocalDateTime.now())) {
            user.clearRefreshToken();
            throw new BusinessException(ErrorCode.EXPIRED_REFRESH_TOKEN);
        }

        String incomingHash = TokenHash.sha256Hex(refreshToken);

        // Rotation + Reuse Detection
        if (user.getRefreshTokenHash() == null || !user.getRefreshTokenHash().equals(incomingHash)) {
            user.clearRefreshToken();
            throw new BusinessException(ErrorCode.REUSED_REFRESH_TOKEN);
        }

        return getSigninResponse(user);
    }

    @Transactional
    public void logout(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        user.clearRefreshToken();
    }

    private SigninResponse getSigninResponse(User user) {
        String newAccessToken = jwtProvider.createAccessToken(user.getId(), user.getEmail());
        String newRefreshToken = jwtProvider.createRefreshToken(user.getId());

        user.updateRefreshToken(
                TokenHash.sha256Hex(newRefreshToken),
                LocalDateTime.now().plusSeconds(jwtProperties.getRefreshTokenExpirationMs() / 1000)
        );

        return SigninResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .email(user.getEmail())
                .userId(user.getId())
                .build();
    }
}
