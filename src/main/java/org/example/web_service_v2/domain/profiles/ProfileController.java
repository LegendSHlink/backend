package org.example.web_service_v2.domain.profiles;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.web_service_v2.domain.profiles.dto.basic.ProfileBasicResponse;
import org.example.web_service_v2.global.auth.dto.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/profiles")
@RequiredArgsConstructor
@Tag(name = "Profiles", description = "프로필 조회 API")
public class ProfileController {
    private final ProfileService profileService;

    @Operation(
            summary = "프로필 조회",
            description = "profileId로 특정 사용자의 프로필 기본 정보를 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(
                    schema = @Schema(implementation = ProfileBasicResponse.class),
                    examples = @ExampleObject(
                            name = "성공 응답",
                            value = """
                            {
                              "success": true,
                              "code": "SUCCESS",
                              "message": "요청이 성공했습니다.",
                              "data": {
                                "username": "정민",
                                "headline": "백엔드 개발자",
                                "field": {
                                  "id": 1,
                                  "name": "Backend"
                                },
                                "userImage": "https://image.url/profile.png",
                                "portfolioUrl": [],
                                "jobPosts": [],
                                "followingsCounts": 3,
                                "followersCounts": 10
                              }
                            }
                            """
                    )
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "프로필을 찾을 수 없음",
            content = @Content(
                    examples = @ExampleObject(
                            name = "실패 응답",
                            value = """
                            {
                              "success": false,
                              "code": "PROFILE_NOT_FOUND",
                              "message": "프로필을 찾을 수 없습니다."
                            }
                            """
                    )
            )
    )
    @GetMapping("/{profileId}")
    public ResponseEntity<ProfileBasicResponse> getProfile(
            @PathVariable Long profileId
    ){
        ProfileBasicResponse body = profileService.getUserProfile(profileId);
        return ResponseEntity.ok(body);
    }

}
