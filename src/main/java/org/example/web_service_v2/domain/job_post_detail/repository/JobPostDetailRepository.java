package org.example.web_service_v2.domain.job_post_detail.repository;

import org.example.web_service_v2.domain.job_post_detail.entity.JobPostDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobPostDetailRepository extends JpaRepository<JobPostDetail, Long> {
    Optional<JobPostDetail> findByJobPostId(Long jobPostId);
}
