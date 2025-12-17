package org.example.web_service_v2.user.profile.dto;

import lombok.*;
import org.example.web_service_v2.user.profile.entity.Profile;

@Getter@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileUpdateResponse {
    private Long userId;
    private String field;
    private String headline;
    private String portfolioLink;
    private String userImageLink;

    public static ProfileUpdateResponse from(Profile profile){
        return ProfileUpdateResponse.builder()
                .userId(profile.getUser().getId())
                .field(profile.getField())
                .headline(profile.getHeadline())
                .portfolioLink(profile.getPortfolioLink())
                .userImageLink(profile.getUserImageLink())
                .build();
    }
}
