package org.example.web_service_v2.domain.home.dto;

import lombok.Builder;
import lombok.Getter;
import org.example.web_service_v2.domain.article.dto.card.ArticleCardResponse;
import org.example.web_service_v2.domain.follow.dto.FollowingCardResponse;

import java.util.List;

@Getter
@Builder
public class HomeResponse {
    private List<ArticleCardResponse> articles;
    private List<FollowingCardResponse> followings;
}
