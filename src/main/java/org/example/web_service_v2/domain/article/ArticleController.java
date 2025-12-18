package org.example.web_service_v2.domain.article;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.web_service_v2.domain.article.dto.create.ArticleCreateRequest;
import org.example.web_service_v2.domain.article.dto.create.ArticleCreateResponse;
import org.example.web_service_v2.global.auth.dto.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Articles", description = "아티클 생성 API")
@RestController
@RequestMapping("/api/v2/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    // 아티클 생성하기
    @Operation(summary = "아티클 생성", description = "로그인한 사용자가 아티클을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "생성 성공",
                    content = @Content(examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "code": "SUCCESS",
                      "message": "요청이 성공했습니다.",
                      "data": {
                        "articleId": 10,
                        "content": "안녕하세요",
                        "createdAt": "2025-12-19T16:00:00"
                      }
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "요청값 검증 실패",
                    content = @Content(examples = @ExampleObject(value = """
                    { "success": false, "code": "VALIDATION_ERROR", "message": "요청값이 올바르지 않습니다." }
                    """))),
            @ApiResponse(responseCode = "404", description = "사용자 없음",
                    content = @Content(examples = @ExampleObject(value = """
                    { "success": false, "code": "USER_NOT_FOUND", "message": "사용자를 찾을 수 없습니다." }
                    """)))
    })
    @PostMapping("/create")
    public ResponseEntity<?> createArticle(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestBody ArticleCreateRequest request
            ){

        ArticleCreateResponse body = articleService.create(principal.getId(), request);
        return ResponseEntity.ok(body);
    }


}
