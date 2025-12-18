package org.example.web_service_v2.domain.job_post;

import org.example.web_service_v2.domain.job_post.entity.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPostRepository extends JpaRepository<JobPost, Long> {
}
