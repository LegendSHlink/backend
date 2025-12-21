package org.example.web_service_v2.global.storage;

import lombok.RequiredArgsConstructor;
import org.example.web_service_v2.global.config.AwsProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Uploader {

    private final S3Client s3Client;
    private final AwsProperties awsProperties;

    public String upload(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(awsProperties.getS3().getBucket())
                .key(fileName)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(
                request,
                RequestBody.fromInputStream(
                        file.getInputStream(),
                        file.getSize()

                )
        );

        return "https://" + awsProperties.getS3().getBucket()
                +".s3.amazonaws.com/" + fileName;
    }

    public String extractKeyFromUrl(String url) {
        if (url == null || url.isBlank()) return null;
        try {
            URI uri = URI.create(url);
            String path = uri.getPath(); // "/profiles/1/xxx.png"
            if (path == null || path.isBlank()) return null;
            return path.startsWith("/") ? path.substring(1) : path;
        } catch (Exception e) {
            return null;
        }
    }

    public void deleteByUrl(String url) {
        String key = extractKeyFromUrl(url);
        if (key == null) return;

        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(awsProperties.getS3().getBucket())
                .key(key)
                .build());
    }

    public void deleteByKey(String key) {
        if (key == null || key.isBlank()) return;
        s3Client.deleteObject(b -> b.bucket(awsProperties.getS3().getBucket()).key(key));
    }

    private String buildKey(String prefix, String ext) {
        String safePrefix = (prefix == null) ? "" : prefix.trim();
        if (!safePrefix.isEmpty() && !safePrefix.endsWith("/")) safePrefix += "/";
        // key는 URL-safe하게 가는 게 운영에서 덜 골치
        return safePrefix + Instant.now().toEpochMilli() + "_" + UUID.randomUUID() + "." + ext;
    }

    private String buildPublicUrl(String key) {
        return "https://" + awsProperties.getS3().getBucket()
                + ".s3." + awsProperties.getRegion()
                + ".amazonaws.com/" + encodePath(key);
    }

    private String encodePath(String key) {
        // 슬래시 구분 유지하면서 인코딩
        return String.join("/",
                java.util.Arrays.stream(key.split("/"))
                        .map(s -> URLEncoder.encode(s, StandardCharsets.UTF_8))
                        .toList()
        );
    }

    private String getExt(String filename) {
        int idx = filename.lastIndexOf('.');
        return filename.substring(idx + 1).toLowerCase();
    }
}
