package org.example.web_service_v2.domain.profiles.dto.basic;

import lombok.*;
import org.example.web_service_v2.domain.field.dto.FieldResponse;
import org.example.web_service_v2.domain.field.entity.Field;
import org.example.web_service_v2.domain.job_post.dto.JobPostSummaryResponse;
import org.example.web_service_v2.domain.profiles.dto.portfolio.PortfolioLinkResponse;
import org.example.web_service_v2.domain.profiles.entity.Profile;

import java.util.List;

@Getter@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileBasicResponse {

    /*
    * 사용자 이름
    * 분야/직군
    * 한줄 소개
    * 팔로잉 팔로우 수
    * 활동 내역
    * 링크
    * */

    private String username;
    private String headline;

    private FieldResponse field;
    private String userImage;

    private List<PortfolioLinkResponse> portfolioUrl;
    private List<JobPostSummaryResponse> jobPosts;

    private Integer followingsCounts;
    private Integer followersCounts;

    public static ProfileBasicResponse from(Profile profile, long followingsCount, long followersCount){
        return ProfileBasicResponse.builder()
                .username(profile.getUser().getName())
                .headline(profile.getHeadline())
                .field(profile.getField() != null
                ? FieldResponse.builder()
                        .id(profile.getField().getId())
                        .name(profile.getField().getName())
                        .build()
                        : null
                )
                .userImage(profile.getUserImage())
                .portfolioUrl(
                        profile.getPortfolioLinks().stream()
                                .map(PortfolioLinkResponse::from)
                                .toList()
                )
                .jobPosts(
                        profile.getJobPosts().stream()
                                .map(JobPostSummaryResponse::from)
                                .toList()
                ).followersCounts((int) followersCount)
                .followingsCounts((int) followingsCount)
                .build();
    }
}
