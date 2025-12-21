package org.example.web_service_v2.domain.job_post.repository;

import org.example.web_service_v2.domain.job_post.entity.JobPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface JobPostRepository extends JpaRepository<JobPost, Long> {
    
    // 기본 조회
    Page<JobPost> findAll(Pageable pageable);
    
    // 마감되지 않은 공고만 조회
    @Query("SELECT j FROM JobPost j WHERE j.endDate > :now ORDER BY j.createdAt DESC")
    Page<JobPost> findActiveJobPosts(@Param("now") LocalDateTime now, Pageable pageable);
    
    // 회사별 공고 조회
    Page<JobPost> findByCompanyId(Long companyId, Pageable pageable);
    
    // 직무별 공고 조회
    Page<JobPost> findByFieldId(Long fieldId, Pageable pageable);
    
    // 작성자별 공고 조회
    Page<JobPost> findByProfileId(Long profileId, Pageable pageable);
    
    // 검색 (회사명 또는 설명)
    @Query("SELECT j FROM JobPost j " +
           "WHERE j.company.cpName LIKE %:keyword% " +
           "OR j.description LIKE %:keyword%")
    Page<JobPost> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    // 복합 필터링
    @Query("SELECT j FROM JobPost j " +
           "WHERE (:fieldId IS NULL OR j.field.id = :fieldId) " +
           "AND (:location IS NULL OR j.company.location LIKE %:location%) " +
           "AND j.endDate > :now")
    Page<JobPost> findWithFilters(
        @Param("fieldId") Long fieldId,
        @Param("location") String location,
        @Param("now") LocalDateTime now,
        Pageable pageable
    );
}
