package org.example.web_service_v2.domain.profiles.dto.portfolio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioLinkUpdateRequest {
    private Long id;
    private String url;
    private String title;
}
