package org.example.web_service_v2.domain.article.dto.card;

import lombok.Builder;
import lombok.Getter;
import org.example.web_service_v2.domain.article.entity.Article;
import org.example.web_service_v2.domain.profiles.entity.Profile;

import java.time.LocalDateTime;

@Getter
@Builder
public class ArticleCardResponse {
    private Long articleId;
    private String content;
    private LocalDateTime createdAt;

    private Long userId;
    private String username;

    private Long profileId;
    private String userImage;

    private Long fieldId;
    private String fieldName;


    public static ArticleCardResponse from(Article article){
        Profile profile = article.getUser().getProfile();

        return ArticleCardResponse.builder()
                .articleId(article.getId())
                .content(article.getContext())
                .createdAt(article.getCreatedAt())
                .userId(article.getUser().getId())
                .username(article.getUser().getName())
                .profileId(profile != null ? profile.getId() : null)
                .userImage(profile != null ? profile.getUserImage() : null)
                .fieldId(profile != null && profile.getField() != null ? profile.getField().getId() : null)
                .fieldName(profile != null && profile.getField() != null ? profile.getField().getName() : null)
                .build();
    }
}
