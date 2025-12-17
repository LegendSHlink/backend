package org.example.web_service_v2.user.profile.dto;

import lombok.*;

@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileUpdateRequest {
    private String field;
    private String headline;
    private String portfolioLink;
    private String userImageLink;
}
