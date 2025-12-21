package org.example.web_service_v2.domain.job_post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.web_service_v2.domain.job_post.dto.JobPostRequestDTO;
import org.example.web_service_v2.domain.job_post.dto.JobPostResponseDTO;
import org.example.web_service_v2.domain.job_post.service.JobPostService;
import org.example.web_service_v2.global.apiPayload.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Tag(name = "채용공고", description = "채용공고 CRUD 및 검색 API")
@RestController
@RequestMapping("/api/v1/job-posts")
@RequiredArgsConstructor
public class JobPostController {

    private final JobPostService jobPostService;

    @Operation(summary = "채용 공고 생성", description = "새로운 채용 공고를 등록합니다.")
    @PostMapping
    public ApiResponse<JobPostResponseDTO.JobPostCreatedDTO> createJobPost(
            @Parameter(description = "사용자 ID") @RequestParam Long userId,
            @RequestBody JobPostRequestDTO.CreateJobPostDTO request
    ) {
        JobPostResponseDTO.JobPostCreatedDTO response = jobPostService.createJobPost(userId, request);
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "채용 공고 목록 조회", description = "필터링, 검색, 정렬이 가능한 채용 공고 목록을 조회합니다.")
    @GetMapping
    public ApiResponse<Page<JobPostResponseDTO.JobPostListItemDTO>> getJobPosts(
            @Parameter(description = "직무 분야 ID") @RequestParam(required = false) Long fieldId,
            @Parameter(description = "지역") @RequestParam(required = false) String location,
            @Parameter(description = "검색 키워드") @RequestParam(required = false) String keyword,
            @Parameter(description = "페이지 번호 (0부터 시작)") @RequestParam(defaultValue = "0") Integer page,
            @Parameter(description = "페이지 크기") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "정렬 기준 (createdAt, endDate)") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "정렬 방향 (ASC, DESC)") @RequestParam(defaultValue = "DESC") String sortDirection
    ) {
        JobPostRequestDTO.JobPostFilterDTO filter = new JobPostRequestDTO.JobPostFilterDTO();
        filter.setFieldId(fieldId);
        filter.setLocation(location);
        filter.setKeyword(keyword);
        filter.setPage(page);
        filter.setSize(size);
        filter.setSortBy(sortBy);
        filter.setSortDirection(sortDirection);

        Page<JobPostResponseDTO.JobPostListItemDTO> response = jobPostService.getJobPosts(filter);
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "채용 공고 상세 조회", description = "특정 채용 공고의 상세 정보를 조회합니다.")
    @GetMapping("/{jobPostId}")
    public ApiResponse<JobPostResponseDTO.JobPostDetailDTO> getJobPostDetail(
            @Parameter(description = "채용 공고 ID") @PathVariable Long jobPostId
    ) {
        JobPostResponseDTO.JobPostDetailDTO response = jobPostService.getJobPostDetail(jobPostId);
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "채용 공고 수정", description = "채용 공고를 수정합니다. (작성자만 가능)")
    @PutMapping("/{jobPostId}")
    public ApiResponse<String> updateJobPost(
            @Parameter(description = "사용자 ID") @RequestParam Long userId,
            @Parameter(description = "채용 공고 ID") @PathVariable Long jobPostId,
            @RequestBody JobPostRequestDTO.UpdateJobPostDTO request
    ) {
        jobPostService.updateJobPost(userId, jobPostId, request);
        return ApiResponse.onSuccess("채용 공고가 수정되었습니다.");
    }

    @Operation(summary = "채용 공고 삭제", description = "채용 공고를 삭제합니다. (작성자만 가능)")
    @DeleteMapping("/{jobPostId}")
    public ApiResponse<String> deleteJobPost(
            @Parameter(description = "사용자 ID") @RequestParam Long userId,
            @Parameter(description = "채용 공고 ID") @PathVariable Long jobPostId
    ) {
        jobPostService.deleteJobPost(userId, jobPostId);
        return ApiResponse.onSuccess("채용 공고가 삭제되었습니다.");
    }

    @Operation(summary = "회사별 채용 공고 조회", description = "특정 회사의 채용 공고 목록을 조회합니다.")
    @GetMapping("/company/{companyId}")
    public ApiResponse<Page<JobPostResponseDTO.JobPostListItemDTO>> getJobPostsByCompany(
            @Parameter(description = "회사 ID") @PathVariable Long companyId,
            @Parameter(description = "페이지 번호") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기") @RequestParam(defaultValue = "10") int size
    ) {
        Page<JobPostResponseDTO.JobPostListItemDTO> response = 
                jobPostService.getJobPostsByCompany(companyId, page, size);
        return ApiResponse.onSuccess(response);
    }
}
