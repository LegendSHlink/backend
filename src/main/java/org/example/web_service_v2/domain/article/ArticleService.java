package org.example.web_service_v2.domain.article;

import lombok.RequiredArgsConstructor;
import org.example.web_service_v2.domain.article.dto.create.ArticleCreateRequest;
import org.example.web_service_v2.domain.article.dto.create.ArticleCreateResponse;
import org.example.web_service_v2.domain.article.entity.Article;
import org.example.web_service_v2.domain.profiles.repositories.ProfileRepository;
import org.example.web_service_v2.domain.users.entity.User;
import org.example.web_service_v2.domain.users.repository.UserRepository;
import org.example.web_service_v2.global.apiPayload.exception.BusinessException;
import org.example.web_service_v2.global.apiPayload.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    @Transactional
    public ArticleCreateResponse create(Long userId, ArticleCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        Article article = Article.to(user, request.getContent());

        user.addArticle(article);
        Article saved = articleRepository.save(article);
        return ArticleCreateResponse.from(saved);
    }
}
