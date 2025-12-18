package org.example.web_service_v2.domain.users.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.web_service_v2.domain.profiles.ProfileService;
import org.example.web_service_v2.domain.profiles.dto.basic.ProfileBasicResponse;
import org.example.web_service_v2.domain.profiles.dto.update.ProfileUpdateRequest;
import org.example.web_service_v2.global.auth.dto.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Users", description = "내 프로필 조회/수정")
@RestController
@RequestMapping("/api/v2/users")
@RequiredArgsConstructor
public class UserController {

    private final ProfileService profileService;

    /**
     * 사용자 이름
     * 한줄 소개
     * 분야/직군
     * 활동 내역
     * 링크
     * 팔로잉, 팔로우 수
     * */

    @Operation(summary = "내 프로필 조회", description = "JWT 인증된 사용자 자신의 프로필 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "code": "SUCCESS",
                      "message": "요청이 성공했습니다.",
                      "data": {
                        "username": "정민",
                        "headline": "백엔드 개발자",
                        "field": { "id": 1, "name": "Backend" },
                        "userImage": "https://image.url/profile.png",
                        "portfolioUrl": [],
                        "jobPosts": [],
                        "followingsCounts": 3,
                        "followersCounts": 10
                      }
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "인증 실패(토큰 없음/만료/위조)",
                    content = @Content(examples = @ExampleObject(value = """
                    { "success": false, "code": "UNAUTHORIZED", "message": "인증이 필요합니다." }
                    """))),
            @ApiResponse(responseCode = "404", description = "프로필 없음",
                    content = @Content(examples = @ExampleObject(value = """
                    { "success": false, "code": "PROFILE_NOT_FOUND", "message": "프로필을 찾을 수 없습니다." }
                    """)))
    })
    @GetMapping("/me")
    public ResponseEntity<ProfileBasicResponse> getProfile(
            @AuthenticationPrincipal CustomUserDetails principal
    ){
        Long id = principal.getId();

        ProfileBasicResponse body = profileService.getUserProfile(id);
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "내 프로필 수정", description = "headline, fieldId, userImage, portfolioLinks, jobPosts 등을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공",
                    content = @Content(examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "code": "SUCCESS",
                      "message": "요청이 성공했습니다.",
                      "data": {
                        "username": "정민",
                        "headline": "수정된 한줄소개",
                        "field": { "id": 2, "name": "AI" },
                        "userImage": "https://image.url/new.png",
                        "portfolioUrl": [],
                        "jobPosts": [],
                        "followingsCounts": 3,
                        "followersCounts": 10
                      }
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "요청값 검증 실패",
                    content = @Content(examples = @ExampleObject(value = """
                    { "success": false, "code": "VALIDATION_ERROR", "message": "요청값이 올바르지 않습니다." }
                    """))),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(examples = @ExampleObject(value = """
                    { "success": false, "code": "UNAUTHORIZED", "message": "인증이 필요합니다." }
                    """))),
            @ApiResponse(responseCode = "404", description = "프로필 없음",
                    content = @Content(examples = @ExampleObject(value = """
                    { "success": false, "code": "PROFILE_NOT_FOUND", "message": "프로필을 찾을 수 없습니다." }
                    """)))
    })
    @PatchMapping("/me")
    public ResponseEntity<ProfileBasicResponse> updateProfile(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestBody ProfileUpdateRequest request
    ){
        ProfileBasicResponse body = profileService.updateUserProfile(principal.getId(), request);
        return ResponseEntity.ok(body);
    }
}
