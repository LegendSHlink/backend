package org.example.web_service_v2.user.profile;

import lombok.RequiredArgsConstructor;
import org.example.web_service_v2.auth.AuthRepository;
import org.example.web_service_v2.global.BusinessException;
import org.example.web_service_v2.global.error.ErrorCode;
import org.example.web_service_v2.user.entity.User;
import org.example.web_service_v2.user.profile.dto.ProfileUpdateRequest;
import org.example.web_service_v2.user.profile.dto.ProfileUpdateResponse;
import org.example.web_service_v2.user.profile.entity.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final AuthRepository authRepository;

    public ProfileUpdateResponse patchMyProfile(Long userId, Map<String, Object> body){
        User user = authRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REQUEST));

        Profile profile = user.ensureProfile();

        if (body.containsKey("field")) {
            profile.setField((String) body.get("field")); // null이면 삭제
        }
        if (body.containsKey("headline")) {
            profile.setHeadline((String) body.get("headline"));
        }
        if (body.containsKey("portfolioLink")) {
            profile.setPortfolioLink((String) body.get("portfolioLink"));
        }
        if (body.containsKey("userImageLink")) {
            profile.setUserImageLink((String) body.get("userImageLink"));
        }

        return ProfileUpdateResponse.from(profile);
    }
}
