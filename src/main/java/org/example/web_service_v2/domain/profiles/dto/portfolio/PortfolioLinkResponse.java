package org.example.web_service_v2.domain.profiles.dto.portfolio;

import lombok.Builder;
import lombok.Getter;
import org.example.web_service_v2.domain.profiles.entity.ProfilePortfolioLink;

@Getter
@Builder
public class PortfolioLinkResponse {
    private Long id;
    private String url;
    private String title;
    private boolean primaryLink;

    public static PortfolioLinkResponse from(ProfilePortfolioLink portfolioLink){
        return PortfolioLinkResponse.builder()
                .id(portfolioLink.getId())
                .url(portfolioLink.getUrl())
                .title(portfolioLink.getTitle())
                .primaryLink(portfolioLink.isPrimaryLink())
                .build();
    }
}
