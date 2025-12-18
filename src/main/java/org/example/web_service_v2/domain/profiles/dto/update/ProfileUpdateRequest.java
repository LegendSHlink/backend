package org.example.web_service_v2.domain.profiles.dto.update;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.web_service_v2.domain.job_post.dto.JobPostUpdateRequest;
import org.example.web_service_v2.domain.profiles.dto.portfolio.PortfolioLinkUpdateRequest;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUpdateRequest {

    private String headline;
    private String userImage;
    private Long fieldId;

    private List<PortfolioLinkUpdateRequest> portfolioLinks;
    private List<JobPostUpdateRequest> jobPosts;

}
