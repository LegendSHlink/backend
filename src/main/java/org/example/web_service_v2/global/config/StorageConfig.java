package org.example.web_service_v2.global.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ImageUploadProperties.class)
public class StorageConfig {
}
