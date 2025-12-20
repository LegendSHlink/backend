package org.example.web_service_v2.domain.job_post.dto;

import lombok.Builder;
import lombok.Getter;
import org.example.web_service_v2.domain.job_post_detail.enums.JobStatus;

import java.time.LocalDateTime;

public class JobPostResponseDTO {

    @Getter
    @Builder
    public static class JobPostListItemDTO {
        private Long jobPostId;
        private String companyName;
        private String companyLogoUrl;
        private String companyLocation;
        private String fieldName;
        private String description;
        private Integer size;
        private LocalDateTime createdAt;
        private LocalDateTime endDate;
        private JobStatus jobStatus;
        private Boolean isBookmarked; // 북마크 여부 (Phase 2에서 추가)
    }

    @Getter
    @Builder
    public static class JobPostDetailDTO {
        private Long jobPostId;
        
        // 기본 정보
        private CompanyInfoDTO company;
        private String fieldName;
        private String description;
        private Integer size;
        private LocalDateTime createdAt;
        private LocalDateTime endDate;
        
        // 상세 정보
        private String overview;
        private String qualifications;
        private String conditions;
        private String information;
        private String procedureInfo;
        private JobStatus jobStatus;
        
        // 작성자 정보
        private AuthorInfoDTO author;
        
        private Boolean isBookmarked;
    }

    @Getter
    @Builder
    public static class CompanyInfoDTO {
        private Long companyId;
        private String companyName;
        private String location;
        private String website;
        private String logoUrl;
        private Integer memberSize;
        private String companySize;
    }

    @Getter
    @Builder
    public static class AuthorInfoDTO {
        private Long profileId;
        private Long userId;
        private String name;
        private String profileImageUrl;
        private String headline;
    }

    @Getter
    @Builder
    public static class JobPostCreatedDTO {
        private Long jobPostId;
        private String message;
    }
}
