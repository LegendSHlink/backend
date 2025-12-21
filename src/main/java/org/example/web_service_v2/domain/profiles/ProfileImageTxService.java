package org.example.web_service_v2.domain.profiles;

import lombok.RequiredArgsConstructor;
import org.example.web_service_v2.domain.follow.FollowRepository;
import org.example.web_service_v2.domain.profiles.dto.basic.ProfileBasicResponse;
import org.example.web_service_v2.domain.profiles.entity.Profile;
import org.example.web_service_v2.domain.profiles.repositories.ProfileRepository;
import org.example.web_service_v2.global.apiPayload.exception.BusinessException;
import org.example.web_service_v2.global.apiPayload.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileImageTxService {

    private final ProfileRepository profileRepository;
    private final FollowRepository followRepository;

    @Transactional
    public ProfileBasicResponse changeImageUrl(Long userId, String newUrl) {
        Profile profile = profileRepository.findByUserId(userId);
        if (profile == null) throw new BusinessException(ErrorCode.PROFILE_NOT_FOUND);

        profile.setUserImage(newUrl);

        long followingsCount = followRepository.countByFollowerId(profile.getId());
        long followersCount  = followRepository.countByFollowingId(profile.getId());
        return ProfileBasicResponse.from(profile, followingsCount, followersCount);
    }

    @Transactional
    public ProfileBasicResponse removeImageUrl(Long userId) {
        Profile profile = profileRepository.findByUserId(userId);
        if (profile == null) throw new BusinessException(ErrorCode.PROFILE_NOT_FOUND);

        profile.setUserImage(null);

        long followingsCount = followingsCount(profile);
        long followersCount  = followersCount(profile);
        return ProfileBasicResponse.from(profile, followingsCount, followersCount);
    }

    private long followersCount(Profile profile) {
        return followRepository.countByFollowingId(profile.getId());
    }

    private long followingsCount(Profile profile) {
        return followRepository.countByFollowerId(profile.getId());
    }

}
