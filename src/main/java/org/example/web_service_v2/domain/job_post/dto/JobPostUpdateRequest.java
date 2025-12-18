package org.example.web_service_v2.domain.job_post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPostUpdateRequest {
    private Long id;
    private Long companyId;
    private Long fieldId;

    private String description;
    private LocalDateTime endDate;
    private Integer size;
}
