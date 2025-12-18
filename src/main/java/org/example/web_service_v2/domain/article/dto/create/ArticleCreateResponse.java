package org.example.web_service_v2.domain.article.dto.create;

import lombok.Builder;
import lombok.Getter;
import org.example.web_service_v2.domain.article.entity.Article;

@Getter
@Builder
public class ArticleCreateResponse {
    private Long id;
    private String content;
    private Long userId;
    private String username;

    public static ArticleCreateResponse from(Article article){
        return ArticleCreateResponse.builder()
                .id(article.getId())
                .content(article.getContext())
                .userId(article.getUser().getId())
                .username(article.getUser().getName())
                .build();
    }
}
