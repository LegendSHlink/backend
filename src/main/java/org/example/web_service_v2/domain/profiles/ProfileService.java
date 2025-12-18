package org.example.web_service_v2.domain.profiles;

import lombok.RequiredArgsConstructor;
import org.example.web_service_v2.domain.companies.CompanyRepository;
import org.example.web_service_v2.domain.companies.entity.Company;
import org.example.web_service_v2.domain.field.FieldRepository;
import org.example.web_service_v2.domain.field.entity.Field;
import org.example.web_service_v2.domain.follow.FollowRepository;
import org.example.web_service_v2.domain.job_post.dto.JobPostUpdateRequest;
import org.example.web_service_v2.domain.job_post.entity.JobPost;
import org.example.web_service_v2.domain.profiles.dto.basic.ProfileBasicResponse;
import org.example.web_service_v2.domain.profiles.dto.portfolio.PortfolioLinkUpdateRequest;
import org.example.web_service_v2.domain.profiles.dto.update.ProfileUpdateRequest;
import org.example.web_service_v2.domain.profiles.entity.Profile;
import org.example.web_service_v2.domain.profiles.entity.ProfilePortfolioLink;
import org.example.web_service_v2.domain.profiles.repositories.ProfileRepository;
import org.example.web_service_v2.domain.users.repository.UserRepository;
import org.example.web_service_v2.global.apiPayload.exception.BusinessException;
import org.example.web_service_v2.global.apiPayload.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final FieldRepository fieldRepository;
    private final CompanyRepository companyRepository;
    private final FollowRepository followRepository;

    public ProfileBasicResponse getUserProfile(Long profileId){

        Profile profile = profileRepository.findProfileById((profileId));
        if (profile == null) throw new BusinessException(ErrorCode.PROFILE_NOT_FOUND);

        long followingsCount = followRepository.countByFollowingId(profile.getId());
        long followersCount = followRepository.countByFollowerId(profile.getId());

        return ProfileBasicResponse.from(profile, followingsCount, followersCount);
    }

    @Transactional
    public ProfileBasicResponse updateUserProfile(Long userId, ProfileUpdateRequest request) {

        Profile profile = profileRepository.findByUserId(userId);

        if (request.getHeadline() != null){
            profile.setHeadline(request.getHeadline());
        }

        if (request.getUserImage() != null){
            profile.setUserImage(request.getUserImage());
        }

        if (request.getFieldId() != null){
            Field field = fieldRepository.findById(request.getFieldId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.INTERNAL_ERROR));
            profile.updateField(field);
        }

        if (request.getPortfolioLinks() != null){
            syncPortfolioLinks(profile, request.getPortfolioLinks());
        }

        if (request.getJobPosts() != null){
            syncJobPosts(profile, request.getJobPosts());
        }

        long followingsCount = followRepository.countByFollowerId(profile.getId());
        long followersCount = followRepository.countByFollowingId(profile.getId());

        return ProfileBasicResponse.from(profile, followingsCount, followersCount);

    }

    private void syncPortfolioLinks(Profile profile, List<PortfolioLinkUpdateRequest> requestLinks){
        Map<Long, ProfilePortfolioLink> currentById = profile.getPortfolioLinks().stream()
                .filter(l -> l.getId() != null)
                .collect(Collectors.toMap(ProfilePortfolioLink::getId, Function.identity()));

        Set<Long> requestIds = requestLinks.stream()
                .map(PortfolioLinkUpdateRequest::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        profile.getPortfolioLinks().removeIf(link ->
                link.getId() != null && !requestIds.contains(link.getId())
        );

        for (PortfolioLinkUpdateRequest request: requestLinks){
            if (request.getId() == null){
                ProfilePortfolioLink newLink = ProfilePortfolioLink.builder()
                        .profile(profile)
                        .url(request.getUrl())
                        .title(request.getTitle())
                        .build();
                profile.getPortfolioLinks().add(newLink);
            } else {
                ProfilePortfolioLink link = currentById.get(request.getId());
                if (link == null){
                    throw new BusinessException(ErrorCode.PROFILE_NOT_FOUND);
                }

                if (request.getUrl() != null) link.setUrl(request.getUrl());
                if (request.getTitle() != null) link.setTitle(request.getTitle());
            }
        }

        ProfilePortfolioLink firstPrimary = null;
        for (ProfilePortfolioLink link : profile.getPortfolioLinks()){
            if (link.isPrimaryLink()){
                if (firstPrimary == null) firstPrimary = link;
                else link.setPrimaryLink(false);
            }
        }
    }

    private void syncJobPosts(Profile profile, List<JobPostUpdateRequest> reqPosts) {

        Map<Long, JobPost> currentById = profile.getJobPosts().stream()
                .filter(j -> j.getId() != null)
                .collect(Collectors.toMap(JobPost::getId, Function.identity()));

        Set<Long> requestedIds = reqPosts.stream()
                .map(JobPostUpdateRequest::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // (A) 삭제
        profile.getJobPosts().removeIf(j ->
                j.getId() != null && !requestedIds.contains(j.getId())
        );

        // (B) upsert
        for (JobPostUpdateRequest r : reqPosts) {

            if (r.getId() == null) {
                // 신규 생성: 회사/분야는 id로 조회해서 세팅
                Company company = companyRepository.findById(r.getCompanyId())
                        .orElseThrow(() -> new BusinessException(ErrorCode.INTERNAL_ERROR));

                Field field = fieldRepository.findById(r.getFieldId())
                        .orElseThrow(() -> new BusinessException(ErrorCode.INTERNAL_ERROR));

                JobPost newPost = JobPost.builder()
                        .profile(profile)
                        .company(company)
                        .field(field)
                        .description(r.getDescription())
                        .endDate(r.getEndDate())
                        .size(r.getSize())
                        .build();

                profile.getJobPosts().add(newPost);

            } else {
                // 수정: 내 jobPost인지 검증
                JobPost post = currentById.get(r.getId());
                if (post == null) {
                    throw new BusinessException(ErrorCode.INTERNAL_ERROR);
                }

                // company 변경
                if (r.getCompanyId() != null) {
                    Company company = companyRepository.findById(r.getCompanyId())
                            .orElseThrow(() -> new BusinessException(ErrorCode.INTERNAL_ERROR));
                    post.setCompany(company);
                }

                // field 변경
                if (r.getFieldId() != null) {
                    Field field = fieldRepository.findById(r.getFieldId())
                            .orElseThrow(() -> new BusinessException(ErrorCode.INTERNAL_ERROR));
                    post.setField(field);
                }

                if (r.getDescription() != null) post.setDescription(r.getDescription());
                if (r.getEndDate() != null) post.setEndDate(r.getEndDate());
                if (r.getSize() != null) post.setSize(r.getSize());
            }
        }
    }

}
