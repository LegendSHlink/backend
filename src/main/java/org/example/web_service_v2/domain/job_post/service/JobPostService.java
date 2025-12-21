package org.example.web_service_v2.domain.job_post.service;

import lombok.RequiredArgsConstructor;
import org.example.web_service_v2.domain.companies.entity.Company;
import org.example.web_service_v2.domain.companies.repository.CompanyRepository;
import org.example.web_service_v2.domain.field.entity.Field;
import org.example.web_service_v2.domain.field.repository.FieldRepository;
import org.example.web_service_v2.domain.job_post.converter.JobPostConverter;
import org.example.web_service_v2.domain.job_post.dto.JobPostRequestDTO;
import org.example.web_service_v2.domain.job_post.dto.JobPostResponseDTO;
import org.example.web_service_v2.domain.job_post.entity.JobPost;
import org.example.web_service_v2.domain.job_post.exception.JobPostForbiddenException;
import org.example.web_service_v2.domain.job_post.exception.JobPostNotFoundException;
import org.example.web_service_v2.domain.job_post.repository.JobPostRepository;
import org.example.web_service_v2.domain.job_post_detail.entity.JobPostDetail;
import org.example.web_service_v2.domain.job_post_detail.repository.JobPostDetailRepository;
import org.example.web_service_v2.domain.profiles.entity.Profile;
import org.example.web_service_v2.domain.profiles.repository.ProfileRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobPostService {

    private final JobPostRepository jobPostRepository;
    private final JobPostDetailRepository jobPostDetailRepository;
    private final ProfileRepository profileRepository;
    private final CompanyRepository companyRepository;
    private final FieldRepository fieldRepository;

    /**
     * 채용 공고 생성
     */
    @Transactional
    public JobPostResponseDTO.JobPostCreatedDTO createJobPost(
            Long userId,
            JobPostRequestDTO.CreateJobPostDTO request
    ) {
        // 1. Profile 조회
        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("프로필을 찾을 수 없습니다."));

        // 2. Company, Field 조회
        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new RuntimeException("회사를 찾을 수 없습니다."));
        
        Field field = fieldRepository.findById(request.getFieldId())
                .orElseThrow(() -> new RuntimeException("직무 분야를 찾을 수 없습니다."));

        // 3. JobPost 생성
        JobPost jobPost = JobPostConverter.toJobPost(request, profile, company, field);
        JobPost savedJobPost = jobPostRepository.save(jobPost);

        // 4. JobPostDetail 생성
        JobPostDetail jobPostDetail = JobPostConverter.toJobPostDetail(request, savedJobPost);
        jobPostDetailRepository.save(jobPostDetail);

        return JobPostResponseDTO.JobPostCreatedDTO.builder()
                .jobPostId(savedJobPost.getId())
                .message("채용 공고가 등록되었습니다.")
                .build();
    }

    /**
     * 채용 공고 목록 조회 (필터링, 검색)
     */
    public Page<JobPostResponseDTO.JobPostListItemDTO> getJobPosts(
            JobPostRequestDTO.JobPostFilterDTO filter
    ) {
        Pageable pageable = createPageable(filter);
        
        Page<JobPost> jobPosts;
        
        if (filter.getKeyword() != null && !filter.getKeyword().isBlank()) {
            // 키워드 검색
            jobPosts = jobPostRepository.searchByKeyword(filter.getKeyword(), pageable);
        } else if (filter.getFieldId() != null || filter.getLocation() != null) {
            // 필터링
            jobPosts = jobPostRepository.findWithFilters(
                    filter.getFieldId(),
                    filter.getLocation(),
                    LocalDateTime.now(),
                    pageable
            );
        } else {
            // 전체 조회 (활성 공고만)
            jobPosts = jobPostRepository.findActiveJobPosts(LocalDateTime.now(), pageable);
        }
        
        return JobPostConverter.toJobPostListPage(jobPosts);
    }

    /**
     * 채용 공고 상세 조회
     */
    public JobPostResponseDTO.JobPostDetailDTO getJobPostDetail(Long jobPostId) {
        JobPost jobPost = jobPostRepository.findById(jobPostId)
                .orElseThrow(() -> new JobPostNotFoundException("채용 공고를 찾을 수 없습니다."));
        
        return JobPostConverter.toJobPostDetailDTO(jobPost);
    }

    /**
     * 채용 공고 수정
     */
    @Transactional
    public void updateJobPost(
            Long userId,
            Long jobPostId,
            JobPostRequestDTO.UpdateJobPostDTO request
    ) {
        JobPost jobPost = jobPostRepository.findById(jobPostId)
                .orElseThrow(() -> new JobPostNotFoundException("채용 공고를 찾을 수 없습니다."));
        
        // 권한 확인
        if (!jobPost.getProfile().getUser().getId().equals(userId)) {
            throw new JobPostForbiddenException("수정 권한이 없습니다.");
        }

        // JobPost 업데이트
        jobPost.updateJobPost(
            request.getDescription(), 
            request.getEndDate(), 
            request.getSize(), 
            request.getRequiredExperience()  // 요구 경력 추가
        );

        // JobPostDetail 업데이트
        JobPostDetail detail = jobPost.getJobPostDetail();
        if (detail != null) {
            detail.updateJobPostDetail(
                request.getOverview(),
                request.getQualifications(),
                request.getConditions(),
                request.getInformation(),
                request.getProcedureInfo()
            );
        }
    }

    /**
     * 채용 공고 삭제
     */
    @Transactional
    public void deleteJobPost(Long userId, Long jobPostId) {
        JobPost jobPost = jobPostRepository.findById(jobPostId)
                .orElseThrow(() -> new JobPostNotFoundException("채용 공고를 찾을 수 없습니다."));
        
        // 권한 확인
        if (!jobPost.getProfile().getUser().getId().equals(userId)) {
            throw new JobPostForbiddenException("삭제 권한이 없습니다.");
        }

        jobPostRepository.delete(jobPost);
    }

    /**
     * 회사별 채용 공고 조회
     */
    public Page<JobPostResponseDTO.JobPostListItemDTO> getJobPostsByCompany(
            Long companyId,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<JobPost> jobPosts = jobPostRepository.findByCompanyId(companyId, pageable);
        return JobPostConverter.toJobPostListPage(jobPosts);
    }

    /**
     * Pageable 생성 헬퍼 메서드
     */
    private Pageable createPageable(JobPostRequestDTO.JobPostFilterDTO filter) {
        Sort.Direction direction = filter.getSortDirection().equalsIgnoreCase("ASC") 
                ? Sort.Direction.ASC 
                : Sort.Direction.DESC;
        
        return PageRequest.of(
                filter.getPage(),
                filter.getSize(),
                Sort.by(direction, filter.getSortBy())
        );
    }
}
