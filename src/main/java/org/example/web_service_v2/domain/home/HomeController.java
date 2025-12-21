package org.example.web_service_v2.domain.home;

import lombok.RequiredArgsConstructor;
import org.example.web_service_v2.domain.article.ArticleService;
import org.example.web_service_v2.domain.home.dto.HomeResponse;
import org.example.web_service_v2.global.auth.dto.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/home")
@RequiredArgsConstructor
public class HomeController {
    private final HomeService homeService;

    @GetMapping
    public ResponseEntity<HomeResponse> home(
            @AuthenticationPrincipal CustomUserDetails principal
            ){
        HomeResponse body = homeService.getHome(principal.getId());
        return ResponseEntity.ok(body);
    }

}
