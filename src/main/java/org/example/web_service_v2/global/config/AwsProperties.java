package org.example.web_service_v2.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter@Getter
@Component
@ConfigurationProperties(prefix = "cloud.aws")
public class AwsProperties {
    private String region;
    private S3 s3;
    private Credentials credentials;

    @Getter@Setter
    public static class S3{
        private String bucket;
    }

    @Getter@Setter
    public static class Credentials{
        private String accessKey;
        private String secretKey;
    }
}
