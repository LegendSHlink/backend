package org.example.web_service_v2.domain.home;

import lombok.RequiredArgsConstructor;
import org.example.web_service_v2.domain.article.ArticleRepository;
import org.example.web_service_v2.domain.article.dto.card.ArticleCardResponse;
import org.example.web_service_v2.domain.follow.FollowRepository;
import org.example.web_service_v2.domain.follow.dto.FollowingCardResponse;
import org.example.web_service_v2.domain.home.dto.HomeResponse;
import org.example.web_service_v2.domain.profiles.entity.Profile;
import org.example.web_service_v2.domain.profiles.repositories.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final ProfileRepository profileRepository;
    private final ArticleRepository articleRepository;
    private final FollowRepository followRepository;

    @Transactional(readOnly = true)
    public HomeResponse getHome(Long userId) {

        Profile me = profileRepository.findByUserId(userId);

        List<Profile> followings = followRepository.findFollowingsFollowerProfileId(me.getId());

        List<ArticleCardResponse> articles = articleRepository
                .findHomeArticles()
                .stream()
                .map(ArticleCardResponse::from)
                .toList();

        List<FollowingCardResponse> followingCards = followings.stream()
                .map(FollowingCardResponse::from)
                .toList();

        return HomeResponse.builder()
                .articles(articles)
                .followings(followingCards)
                .build();

    }
}
