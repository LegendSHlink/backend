package org.example.web_service_v2.domain.follow.dto;

import lombok.Builder;
import lombok.Getter;
import org.example.web_service_v2.domain.follow.entity.Follow;
import org.example.web_service_v2.domain.profiles.entity.Profile;

@Getter
@Builder
public class FollowingCardResponse {
    private Long profileId;
    private String name;

    private Long fieldId;
    private String fieldName;
    private String userImage;


    public static FollowingCardResponse from(Profile profile){
        return FollowingCardResponse.builder()
                .profileId(profile.getId())
                .name(profile.getUser().getName())
                .userImage(profile.getUserImage())
                .fieldId(
                        profile.getField() != null
                        ? profile.getField().getId()
                        : null
                )
                .fieldName(
                        profile.getField() != null
                        ? profile.getField().getName()
                        : null
                )
                .build();
    }
}
