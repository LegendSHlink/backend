package org.example.web_service_v2.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.web_service_v2.auth.CustomUserDetails;
import org.example.web_service_v2.user.profile.ProfileService;
import org.example.web_service_v2.user.profile.dto.ProfileUpdateRequest;
import org.example.web_service_v2.user.profile.dto.ProfileUpdateResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final ProfileService profileService;

    @PatchMapping("/me/profile")
    public ResponseEntity<ProfileUpdateResponse> patch(@AuthenticationPrincipal CustomUserDetails me,
                                                       @RequestBody Map<String, Object> body){
        return ResponseEntity.ok(profileService.patchMyProfile(me.getId(), body));
    }
}
