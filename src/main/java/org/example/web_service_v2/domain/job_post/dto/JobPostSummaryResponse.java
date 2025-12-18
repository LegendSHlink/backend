package org.example.web_service_v2.domain.job_post.dto;

import lombok.Builder;
import lombok.Getter;
import org.example.web_service_v2.domain.job_post.entity.JobPost;

import java.time.LocalDateTime;

@Getter @Builder
public class JobPostSummaryResponse {
    private Long id;
    private Long companyId;
    private String companyName;
    private Long fieldId;
    private String fieldName;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime endDate;

    public static JobPostSummaryResponse from(JobPost jobPost){
        return JobPostSummaryResponse.builder()
                .id(jobPost.getId())
                .companyId(jobPost.getCompany().getId())
                .companyName(jobPost.getCompany().getCpName())
                .fieldId(jobPost.getField().getId())
                .fieldName(jobPost.getField().getName())
                .description(jobPost.getDescription())
                .createdAt(jobPost.getCreatedAt())
                .endDate(jobPost.getEndDate())
                .build();
    }
}
