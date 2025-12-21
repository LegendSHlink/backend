package org.example.web_service_v2.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Getter@Setter
@ConfigurationProperties(prefix = "app.upload.image")
public class ImageUploadProperties {
    private long maxSizeBytes = 5 * 1024 * 1024;
    private List<String> allowedContentTypes = new ArrayList<>(List.of(
            "image/jpeg", "image/png", "image/webp"
    ));
    private boolean verifyImageContent = true;
}
