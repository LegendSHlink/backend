package org.example.web_service_v2.domain.job_post.converter;

import org.example.web_service_v2.domain.companies.entity.Company;
import org.example.web_service_v2.domain.field.entity.Field;
import org.example.web_service_v2.domain.job_post.dto.JobPostRequestDTO;
import org.example.web_service_v2.domain.job_post.dto.JobPostResponseDTO;
import org.example.web_service_v2.domain.job_post.entity.JobPost;
import org.example.web_service_v2.domain.job_post_detail.entity.JobPostDetail;
import org.example.web_service_v2.domain.job_post_detail.enums.JobStatus;
import org.example.web_service_v2.domain.profiles.entity.Profile;
import org.springframework.data.domain.Page;

public class JobPostConverter {

    // Entity -> Response DTO
    public static JobPostResponseDTO.JobPostListItemDTO toJobPostListItemDTO(JobPost jobPost) {
        return JobPostResponseDTO.JobPostListItemDTO.builder()
                .jobPostId(jobPost.getId())
                .companyName(jobPost.getCompany().getCpName())
                .companyLogoUrl(jobPost.getCompany().getLogoUrl())
                .companyLocation(jobPost.getCompany().getLocation())
                .fieldName(jobPost.getField().getName())
                .description(jobPost.getDescription())
                .size(jobPost.getSize())
                .createdAt(jobPost.getCreatedAt())
                .endDate(jobPost.getEndDate())
                .jobStatus(jobPost.getJobPostDetail() != null ? 
                          jobPost.getJobPostDetail().getJobStatus() : JobStatus.RECRUITING)
                .isBookmarked(false) // Phase 2에서 구현
                .build();
    }

    public static JobPostResponseDTO.JobPostDetailDTO toJobPostDetailDTO(JobPost jobPost) {
        JobPostDetail detail = jobPost.getJobPostDetail();
        
        return JobPostResponseDTO.JobPostDetailDTO.builder()
                .jobPostId(jobPost.getId())
                .company(toCompanyInfoDTO(jobPost.getCompany()))
                .fieldName(jobPost.getField().getName())
                .description(jobPost.getDescription())
                .size(jobPost.getSize())
                .createdAt(jobPost.getCreatedAt())
                .endDate(jobPost.getEndDate())
                .overview(detail != null ? detail.getOverview() : null)
                .qualifications(detail != null ? detail.getQualifications() : null)
                .conditions(detail != null ? detail.getConditions() : null)
                .information(detail != null ? detail.getInformation() : null)
                .procedureInfo(detail != null ? detail.getProcedureInfo() : null)
                .jobStatus(detail != null ? detail.getJobStatus() : JobStatus.RECRUITING)
                .author(toAuthorInfoDTO(jobPost.getProfile()))
                .isBookmarked(false)
                .build();
    }

    public static JobPostResponseDTO.CompanyInfoDTO toCompanyInfoDTO(Company company) {
        return JobPostResponseDTO.CompanyInfoDTO.builder()
                .companyId(company.getId())
                .companyName(company.getCpName())
                .location(company.getLocation())
                .website(company.getWebsite())
                .logoUrl(company.getLogoUrl())
                .memberSize(company.getMemberSize())
                .companySize(company.getCpSize())
                .build();
    }

    public static JobPostResponseDTO.AuthorInfoDTO toAuthorInfoDTO(Profile profile) {
        return JobPostResponseDTO.AuthorInfoDTO.builder()
                .profileId(profile.getId())
                .userId(profile.getUser().getId())
                .name(profile.getUser().getName())
                .profileImageUrl(profile.getUserimage())
                .headline(profile.getHeadline())
                .build();
    }

    // Request DTO -> Entity
    public static JobPost toJobPost(
            JobPostRequestDTO.CreateJobPostDTO request,
            Profile profile,
            Company company,
            Field field
    ) {
        return JobPost.builder()
                .profile(profile)
                .company(company)
                .field(field)
                .description(request.getDescription())
                .endDate(request.getEndDate())
                .size(request.getSize())
                .build();
    }

    public static JobPostDetail toJobPostDetail(
            JobPostRequestDTO.CreateJobPostDTO request,
            JobPost jobPost
    ) {
        return JobPostDetail.builder()
                .jobPost(jobPost)
                .overview(request.getOverview())
                .qualifications(request.getQualifications())
                .conditions(request.getConditions())
                .information(request.getInformation())
                .procedureInfo(request.getProcedureInfo())
                .jobStatus(JobStatus.RECRUITING)
                .build();
    }

    // Page -> Response
    public static Page<JobPostResponseDTO.JobPostListItemDTO> toJobPostListPage(Page<JobPost> jobPosts) {
        return jobPosts.map(JobPostConverter::toJobPostListItemDTO);
    }
}
