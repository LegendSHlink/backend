package org.example.web_service_v2.domain.job_post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

public class JobPostRequestDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class CreateJobPostDTO {
        private Long companyId;
        private Long fieldId;
        private String description;
        private LocalDateTime endDate;
        private Integer size;
        private String requiredExperience;  // 요구 경력
        
        // 상세 정보
        private String overview;
        private String qualifications;
        private String conditions;
        private String information;
        private String procedureInfo;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class UpdateJobPostDTO {
        private String description;
        private LocalDateTime endDate;
        private Integer size;
        private String requiredExperience;  // 요구 경력
        
        // 상세 정보
        private String overview;
        private String qualifications;
        private String conditions;
        private String information;
        private String procedureInfo;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class JobPostFilterDTO {
        private Long fieldId;
        private String location;
        private String keyword;
        private Integer page = 0;
        private Integer size = 10;
        private String sortBy = "createdAt"; // createdAt, endDate
        private String sortDirection = "DESC";
    }
}
