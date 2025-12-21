package org.example.web_service_v2.domain.profiles.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter@Setter
public class ProfileImageUploadRequest {

    @Schema(
            description = "프로필 이미지 파일",
            type = "string",
            format = "binary"
    )
    private MultipartFile image;
}
