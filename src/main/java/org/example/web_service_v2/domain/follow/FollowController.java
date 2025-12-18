package org.example.web_service_v2.domain.follow;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.web_service_v2.domain.follow.dto.FollowingCardResponse;
import org.example.web_service_v2.global.auth.dto.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Follows", description = "팔로우/언팔로우 및 팔로잉, 팔로워 조회")
@RestController
@RequestMapping("/api/v2/follows")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @Operation(summary = "팔로우", description = "내 프로필이 targetProfileId를 팔로우합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "팔로우 성공",
                    content = @Content(examples = @ExampleObject(value = """
                    { "success": true, "code": "SUCCESS", "message": "요청이 성공했습니다." }
                    """))),
            @ApiResponse(responseCode = "400", description = "자기 자신 팔로우 불가",
                    content = @Content(examples = @ExampleObject(value = """
                    { "success": false, "code": "CANNOT_FOLLOW_SELF", "message": "자기 자신은 팔로우할 수 없습니다." }
                    """))),
            @ApiResponse(responseCode = "409", description = "이미 팔로우 중",
                    content = @Content(examples = @ExampleObject(value = """
                    { "success": false, "code": "ALREADY_FOLLOWING", "message": "이미 팔로우한 사용자입니다." }
                    """))),
            @ApiResponse(responseCode = "404", description = "대상 프로필 없음",
                    content = @Content(examples = @ExampleObject(value = """
                    { "success": false, "code": "PROFILE_NOT_FOUND", "message": "프로필을 찾을 수 없습니다." }
                    """)))
    })
    @PostMapping("/{targetProfileId}")
    public ResponseEntity<Void> follow(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable Long targetProfileId
            ){
        followService.follow(principal.getId(), targetProfileId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "언팔로우", description = "내 프로필이 targetProfileId를 언팔로우합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "언팔로우 성공",
                    content = @Content(examples = @ExampleObject(value = """
                    { "success": true, "code": "SUCCESS", "message": "요청이 성공했습니다." }
                    """))),
            @ApiResponse(responseCode = "409", description = "팔로우 상태가 아님",
                    content = @Content(examples = @ExampleObject(value = """
                    { "success": false, "code": "NOT_FOLLOWING", "message": "팔로우 상태가 아닙니다." }
                    """))),
            @ApiResponse(responseCode = "404", description = "대상 프로필 없음",
                    content = @Content(examples = @ExampleObject(value = """
                    { "success": false, "code": "PROFILE_NOT_FOUND", "message": "프로필을 찾을 수 없습니다." }
                    """)))
    })
    @DeleteMapping("/{targetProfileId}")
    public ResponseEntity<Void> unfollow(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable Long targetProfileId
    ){
        followService.unfollow(principal.getId(), targetProfileId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "내 팔로잉 목록", description = "내가 팔로우하는 프로필 카드 목록을 반환합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(examples = @ExampleObject(value = """
            {
              "success": true,
              "code": "SUCCESS",
              "message": "요청이 성공했습니다.",
              "data": [
                { "profileId": 2, "username": "철수", "userImage": "https://img.url/a.png", "headline": "백엔드" }
              ]
            }
            """)))
    @GetMapping("/me/followings")
    public ResponseEntity<?> myFollowing(
            @AuthenticationPrincipal CustomUserDetails principal
    ){
        List<FollowingCardResponse> body = followService.getMyFollowings(principal.getId());
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "내 팔로워 목록", description = "나를 팔로우하는 프로필 카드 목록을 반환합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(examples = @ExampleObject(value = """
            {
              "success": true,
              "code": "SUCCESS",
              "message": "요청이 성공했습니다.",
              "data": [
                { "profileId": 3, "username": "영희", "userImage": "https://img.url/b.png", "headline": "프론트" }
              ]
            }
            """)))
    @GetMapping("/me/followers")
    public ResponseEntity<?> myFollowers(
            @AuthenticationPrincipal CustomUserDetails principal
    ){
        List<FollowingCardResponse> body = followService.getMyFollowers(principal.getId());
        return ResponseEntity.ok(body);
    }
}
