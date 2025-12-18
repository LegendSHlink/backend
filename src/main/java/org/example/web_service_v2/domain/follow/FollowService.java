package org.example.web_service_v2.domain.follow;

import lombok.RequiredArgsConstructor;
import org.example.web_service_v2.domain.follow.dto.FollowingCardResponse;
import org.example.web_service_v2.domain.follow.entity.Follow;
import org.example.web_service_v2.domain.profiles.entity.Profile;
import org.example.web_service_v2.domain.profiles.repositories.ProfileRepository;
import org.example.web_service_v2.global.apiPayload.exception.BusinessException;
import org.example.web_service_v2.global.apiPayload.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class FollowService {
    private final ProfileRepository profileRepository;
    private final FollowRepository followRepository;

    @Transactional
    public void follow(Long userId, Long targetProfileId){
        Profile me = profileRepository.findByUserId(userId);

        Profile target = profileRepository.findById(targetProfileId)
                .orElseThrow(()-> new BusinessException(ErrorCode.PROFILE_NOT_FOUND));

        if (me.getId().equals(target.getId())){
            throw new BusinessException(ErrorCode.CANNOT_FOLLOW_SELF);
        }

        if (followRepository.existsByFollowerIdAndFollowingId(me.getId(), target.getId())){
            throw new BusinessException(ErrorCode.ALREADY_FOLLOWING);
        }

        Follow follow = Follow.builder()
                .follower(me)
                .following(target)
                .build();

        followRepository.save(follow);
    }

    @Transactional
    public void unfollow(Long userId, Long targetProfileId){
        Profile me = profileRepository.findByUserId(userId);

        if (!followRepository.existsByFollowerIdAndFollowingId(me.getId(), targetProfileId)){
            throw new BusinessException(ErrorCode.NOT_FOLLOWING);
        }

        followRepository.deleteByFollowerIdAndFollowingId(me.getId(), targetProfileId);
    }

    public List<FollowingCardResponse> getMyFollowings(Long userId) {
        Profile me = profileRepository.findByUserId(userId);

        return followRepository.findFollowingsFollowerProfileId(me.getId())
                .stream()
                .map(FollowingCardResponse::from)
                .toList();
    }

    public List<FollowingCardResponse> getMyFollowers(Long userId) {
        Profile me = profileRepository.findByUserId(userId);

        return followRepository.findFollowersByFollowingProfileId(me.getId())
                .stream()
                .map(FollowingCardResponse::from)
                .toList();
    }
}
